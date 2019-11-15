package com.daprref.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpPubSubTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void subscriberTopicAdditionMessageConsumer() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        {"operand1":20,"operand2":15}
        AdditionOperands additionOperands = new AdditionOperands();
        additionOperands.setOperand1(10);
        additionOperands.setOperand2(5);

        DaprMessage daprMessage = new DaprMessage("some id", "source", "type", "specversion", "datacontenttype", additionOperands);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(daprMessage);

        HttpEntity<String> request = new HttpEntity<String>(payload, headers);
        String additionTopicUrl = "http://localhost:" + port + "/topic-addition";

        ResponseEntity responseEntity  = this.restTemplate.postForEntity(additionTopicUrl, request, ResponseEntity.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // TODO: test case using http mock to assert that the result of the addition is sent as a subscriber
    }

//    @Test //TODO: Review invalid payload approach
    public void subscriberTopicAdditionMessageConsumerWithInvalidPayload() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String invalidPayload = "{\"operandX\":20,\"operandY\":15}";

        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request = new HttpEntity<String>(invalidPayload, headers);
        String additionTopicUrl = "http://localhost:" + port + "/topic-addition";

        ResponseEntity responseEntity  = this.restTemplate.postForEntity(additionTopicUrl, request, ResponseEntity.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
