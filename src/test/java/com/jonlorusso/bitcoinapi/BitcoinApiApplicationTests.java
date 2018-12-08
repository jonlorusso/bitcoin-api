package com.jonlorusso.bitcoinapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BitcoinApiApplicationTests {

    @Autowired
    BitcoinApiApplication.Controller bitcoinApiController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertThat(bitcoinApiController).isNotNull();
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

    @Test
    public void addressRouteShouldReturnOutputsKey() throws Exception {
        String address = "1AJbsFZ64EpEfS5UAjAfcUG8pH8Jn3rn1F";
        Response response = this.restTemplate.getForObject("http://localhost:" + port + "/address/" + address, Response.class);
        assertThat(response.getOutputs()).isNotNull();
    }
}
