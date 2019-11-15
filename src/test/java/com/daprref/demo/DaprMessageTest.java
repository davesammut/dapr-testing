package com.daprref.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaprMessageTest {

    @Test
    public void jsonToObjectTest() throws JsonProcessingException {
       String jsonMessage = "{\"id\":\"3c10f462-2432-4f19-aaf2-1e99f303f4b1\",\"source\":\"addcalculator\",\"type\":\"com.dapr.event.sent\",\"specversion\":\"0.3\",\"datacontenttype\":\"application/json\",\"data\":{\"operand1\":40,\"operand2\":90}}";
       ObjectMapper mapper = new ObjectMapper();
       DaprMessage daprMessage = mapper.readValue(jsonMessage, DaprMessage.class);
       assertThat(daprMessage.getData().getOperand1()).isEqualTo(40);
       assertThat(daprMessage.getData().getOperand2()).isEqualTo(90);
    }
}
