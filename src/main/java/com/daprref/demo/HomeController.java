package com.daprref.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private Integer calculationResult;

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
    public String createPerson(@RequestBody AdditionOperands additionOperands) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(Calculator.add(additionOperands.getOperand1(), additionOperands.getOperand2()));
    }

    @GetMapping(
            value = "/dapr/subscribe",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody String subscribeToTopics() throws JsonProcessingException {
        System.out.println("SUBSCRIBE DAPR ENDPOINT CALLED!");
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

//        if(additionOperands.getOperand1() == null || additionOperands.getOperand2() == null) {
//            System.out.println("Invalid topic-addition payload");
//            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//        calculationResult = Calculator.add(additionOperands.getOperand1(), additionOperands.getOperand2());
//        System.out.println("Successfully processed topic-addition payload Operand1 [" + additionOperands.getOperand1() + "] Operand2 [" + additionOperands.getOperand2() + "]");
        System.out.println("Topic Subscriber topic-addition CALLED " + message);
        ObjectMapper mapper = new ObjectMapper();
        DaprMessage daprMessage = mapper.readValue(message, DaprMessage.class);
        calculationResult = Calculator.add(daprMessage.getData().getOperand1(), daprMessage.getData().getOperand2());
        return new ResponseEntity(HttpStatus.OK);
    }
}
