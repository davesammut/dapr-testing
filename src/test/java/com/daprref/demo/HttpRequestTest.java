package com.daprref.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnTextMessage() throws Exception {
        String helloUrl = "http://localhost:" + port + "/";
        String actualResponse = this.restTemplate.getForObject(helloUrl, String.class);
        assertThat(actualResponse).isEqualTo("Hello Mister Calculator");
    }

    @Test
    public void additionShouldReturnCorrectCalculation() throws Exception {
        AdditionOperands additionOperands = new AdditionOperands();
        additionOperands.setOperand1(20);
        additionOperands.setOperand2(15);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(additionOperands), headers);
        String addUrl = "http://localhost:" + port + "/add";
        String actualCalculationResponse = this.restTemplate.postForObject(addUrl, request, String.class);
        String expectedCalculation = "35";
        assertThat(actualCalculationResponse).isEqualTo(expectedCalculation);
    }
}
