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
public class DaprHttpRequestTest extends DaprServerTestingOrchestrator {

    @Test
    public void helloWorldTest() throws IOException {
        String expectedResponse = "Hello Mister Calculator";
        String helloUrl = "http://localhost:" + getDaprPort() + "/v1.0/invoke/addcalculator/method/";

        HttpGet getRequest = new HttpGet(helloUrl);
        getRequest.addHeader("accept", MediaType.APPLICATION_XML_VALUE);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(getRequest);

        HttpEntity httpEntity = response.getEntity();
        String actualResponse = EntityUtils.toString(httpEntity);
//      System.out.println("ACTUAL RESPONSE: " + actualResponse);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void additionTest() throws IOException {
        String expectedAdditionResult = "25";
        String additionDaprUrl = "http://localhost:" + getDaprPort() + "/v1.0/invoke/addcalculator/method/add";
        HttpPost postRequest = new HttpPost(additionDaprUrl);
        postRequest.addHeader("accept", MediaType.APPLICATION_JSON_VALUE);

        AdditionOperands additionOperands = new AdditionOperands();
        additionOperands.setOperand1(20);
        additionOperands.setOperand2(5);

        ObjectMapper mapper = new ObjectMapper();
        postRequest.setEntity(new StringEntity(mapper.writeValueAsString(additionOperands)));

        DefaultHttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = httpClient.execute(postRequest);

        HttpEntity httpEntity = response.getEntity();
        String actualResponse = EntityUtils.toString(httpEntity);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(actualResponse).isEqualTo(expectedAdditionResult);
    }
}
