package com.jonlorusso.bitcoinapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.empty;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.util.StringUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@SpringBootApplication
public class BitcoinApiApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    public static class Controller {

        @Autowired
        private RestTemplate restTemplate;

        private Stream<TransactionOutput> outputs(Transaction transaction) {
            String address = transaction.getSubjectAddress();
            String hash = transaction.getHash();

            return transaction.getOut().stream()
                    .filter(o -> !isEmpty(o.getAddr()))
                    .filter(o -> o.getAddr().equals(address))
                    .filter(o -> !o.isSpent())
                    .peek(o -> o.setTxHash(hash));
        }

        private Stream<Transaction> transactions(String address) {
            Stream<Transaction> transactions = empty();

            int offset = 0;
            String addressUrl = format("https://blockchain.info/rawaddr/%s?offset=%d", address, offset);
            AddressInfo addressInfo = restTemplate.getForObject(addressUrl, AddressInfo.class);

            while (addressInfo.getTransactions().size() > 0) {
                transactions = concat(transactions, addressInfo.getTransactions().stream().peek(t -> t.setSubjectAddress(address)));

                offset += addressInfo.getTransactions().size();
                addressUrl = format("https://blockchain.info/rawaddr/%s?offset=%d", address, offset);
                addressInfo = restTemplate.getForObject(addressUrl, AddressInfo.class);
            }

            return transactions;
        }

        @RequestMapping(value = "/address/{address}", method = GET)
        public Map<String, List<TransactionOutput>> unspentTransactions(@PathVariable String address) {
            Map<String, List<TransactionOutput>> response = new HashMap<>();
            response.put("outputs", transactions(address).flatMap(this::outputs).collect(toList()));
            return response;
        }
    }

    public static void main(String[] args) {
        run(BitcoinApiApplication.class, args);
    }
}
