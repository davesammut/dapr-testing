package com.daprref.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DaprHttpPubSubTests extends DaprServerTestingOrchestrator {

    @Test
    public void additionSubscriptionTest() throws IOException, InterruptedException {
        AdditionOperands additionOperands = new AdditionOperands();
        additionOperands.setOperand1(20);
        additionOperands.setOperand2(10);
        ObjectMapper mapper = new ObjectMapper();
        String daprPublishCommand = "dapr publish --topic topic-addition --payload " + mapper.writeValueAsString(additionOperands);
        this.runCommandAndWaitUntil(daprPublishCommand, "Event published successfully");

        String expectedResponse = "30";
        Thread.sleep(2000); // Hack to wait for the message to be consumed and state updated before checking. TODO: Adopt a retry (number of times) until state has changed.
        String calculationResultUrl = "http://localhost:8080/calculation-result";

        HttpGet getRequest = new HttpGet(calculationResultUrl);
        getRequest.addHeader("accept", MediaType.APPLICATION_XML_VALUE);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(getRequest);

        HttpEntity httpEntity = response.getEntity();
        String actualResponse = EntityUtils.toString(httpEntity);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void additionPublishSubscriptionTest() throws IOException, InterruptedException {
        AdditionOperands additionOperands = new AdditionOperands();
        additionOperands.setOperand1(80);
        additionOperands.setOperand2(10);
        ObjectMapper mapper = new ObjectMapper();
        String daprPublishCommand = "dapr publish --topic topic-addition --payload " + mapper.writeValueAsString(additionOperands);
        this.runCommandAndWaitUntil(daprPublishCommand, "Event published successfully");

        //TODO: Trigger this test by Posting AdditionOperands payload to a URL which is responsible for the publishing of an addition calculation message

        String expectedResponse = "90";
        Thread.sleep(2000); // Hack to wait for the message to be consumed and state updated before checking. TODO: Adopt a retry (number of times) until state has changed.
        String calculationResultUrl = "http://localhost:8080/calculation-result";

        HttpGet getRequest = new HttpGet(calculationResultUrl);
        getRequest.addHeader("accept", MediaType.APPLICATION_XML_VALUE);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(getRequest);

        HttpEntity httpEntity = response.getEntity();
        String actualResponse = EntityUtils.toString(httpEntity);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}