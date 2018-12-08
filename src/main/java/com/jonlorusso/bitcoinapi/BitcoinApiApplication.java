package com.jonlorusso.bitcoinapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
public class BitcoinApiApplication {


    @RestController
    public static class Controller {

        private Stream<TransactionOutput> getOutputs(String address, Transaction transaction) {
            return transaction.getOut().stream()
                    .filter(o -> !isEmpty(o.getAddr()))
                    .filter(o -> o.getAddr().equals(address))
                    .filter(o -> !o.isSpent())
                    .map(o -> { o.setTxHash(transaction.getHash()); return o; });
        }

        private Stream<Transaction> transactions(String address) {
            Stream<Transaction> transactions = Stream.empty();

            RestTemplate restTemplate = new RestTemplate();

            int offset = 0;
            String addressUrl = format("https://blockchain.info/rawaddr/%s?offset=%d", address, offset);
            AddressInfo addressInfo = restTemplate.getForObject(addressUrl, AddressInfo.class);

            while (addressInfo.getTransactions().size() > 0) {
                transactions = Stream.concat(transactions, addressInfo.getTransactions().stream());

                offset += addressInfo.getTransactions().size();
                addressUrl = format("https://blockchain.info/rawaddr/%s?offset=%d", address, offset);
                addressInfo = restTemplate.getForObject(addressUrl, AddressInfo.class);
            }

            return transactions;
        }

        @RequestMapping(value = "/address/{address}", method = GET)
        public Map<String, List<TransactionOutput>> unspentTransactions(@PathVariable String address) {
            Map<String, List<TransactionOutput>> response = new HashMap<>();
            response.put("outputs", transactions(address).flatMap(t -> getOutputs(address, t)).collect(toList()));
            return response;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BitcoinApiApplication.class, args);
    }
}
