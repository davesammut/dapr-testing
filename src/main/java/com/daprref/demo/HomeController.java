package com.daprref.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private Integer calculationResult;
    private final Integer daprPort = 3500;
    private final String daprPublishUrl = String.format("http://localhost:%d/v1.0/publish/topic-addition", daprPort);

    @RequestMapping("/calculation-result")
    public @ResponseBody ResponseEntity greeting() {

        System.out.println("Calculation Result CALLED! calculationResult [" + calculationResult + "]");
        if(this.calculationResult == null) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
            return new ResponseEntity(Integer.toString(-99), HttpStatus.OK);
        }
        return new ResponseEntity(Integer.toString(calculationResult), HttpStatus.OK);
    }

    @RequestMapping("/")
    public @ResponseBody String calculationResult() {
        return "Hello Mister Calculator";
    }

    @RequestMapping(
            value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String additionCalculation(@RequestBody AdditionOperands additionOperands) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(Calculator.add(additionOperands.getOperand1(), additionOperands.getOperand2()));
    }

    @RequestMapping(
            value = "/topic-addition-publish-trigger",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity additionCalculationPublishTrigger(@RequestBody AdditionOperands additionOperands) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(mapper.writeValueAsString(additionOperands), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity responseEntity = restTemplate.postForEntity(daprPublishUrl, requestEntity, String.class);

        return new ResponseEntity(responseEntity.getStatusCode());
    }

    @GetMapping(
            value = "/dapr/subscribe",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody String subscribeToTopics() throws JsonProcessingException {
        List topics = Collections.singletonList("topic-addition");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(topics);
    }

    @RequestMapping(
            value = "/topic-addition",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity topicAddition(@RequestBody String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DaprMessage daprMessage = mapper.readValue(message, DaprMessage.class);
        calculationResult = Calculator.add(daprMessage.getData().getOperand1(), daprMessage.getData().getOperand2());
        return new ResponseEntity(HttpStatus.OK);
    }
}
