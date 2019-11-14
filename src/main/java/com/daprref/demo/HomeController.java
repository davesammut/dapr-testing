package com.daprref.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping("/")
    public @ResponseBody String greeting() {
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

}