package com.jonlorusso.bitcoinapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.random;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BitcoinApiApplicationTests {

    @Mock
    private RestTemplate serverRestTemplate;

    @Autowired
    @InjectMocks
    BitcoinApiApplication.Controller bitcoinApiController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate clientRestTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public static class Response {
        public List<Object> outputs;

        public List<Object> getOutputs() {
            return outputs;
        }

        public void setOutputs(List<Object> outputs) {
            this.outputs = outputs;
        }
    }

    private String createAddressInfoUrl(String address, int offset) {
        return format("https://blockchain.info/rawaddr/%s?offset=%d", address, offset);
    }

    private Transaction createTransaction(String address, int transactionIndex, int unspentOutputs) {
        Transaction transaction = new Transaction();
        transaction.setSubjectAddress(address);
        transaction.setHash(format("tx-%d", transactionIndex));

        int totalOutputs = Math.max((int)(random() * 10), unspentOutputs);

        transaction.setOut(IntStream.range(0, totalOutputs).mapToObj(j -> {
            return new TransactionOutput(format("%d-%d", transactionIndex, j), j >= unspentOutputs , address, transactionIndex * j * 1000, j);
        }).collect(toList()));

        return transaction;
    }

    private AddressInfo createAddressInfo(String address, int count, int unspentOutputsPerTransaction) {
        AddressInfo addressInfo = new AddressInfo();

        addressInfo.setTransactions(IntStream.range(1, count + 1).mapToObj(transactionIndex -> {
            return createTransaction(address, transactionIndex, unspentOutputsPerTransaction);
        }).collect(toList()));

        return addressInfo;
    }

    @Test
    public void testUnspentOutputs() throws Exception {
        String address = "testAddress";

        when(serverRestTemplate.getForObject(createAddressInfoUrl(address, 0), AddressInfo.class)).thenReturn(createAddressInfo(address, 5, 0));
        when(serverRestTemplate.getForObject(createAddressInfoUrl(address, 5), AddressInfo.class)).thenReturn(createAddressInfo(address, 5, 1));
        when(serverRestTemplate.getForObject(createAddressInfoUrl(address, 10), AddressInfo.class)).thenReturn(createAddressInfo(address, 1, 2));
        when(serverRestTemplate.getForObject(createAddressInfoUrl(address, 11), AddressInfo.class)).thenReturn(createAddressInfo(address, 0, 0));

        Response response = clientRestTemplate.getForObject("http://localhost:" + port + "/address/" + address, Response.class);
        assertThat(response.getOutputs()).hasSize(7);
        // TODO test index and value of unspent outputs.
    }
}
