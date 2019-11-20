package com.daprref.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdditionOperandsTest {

    private static final Integer OP2 = 2;
    private static final Integer OP1 = 1;
    private static final String EXPECTED_JSON = "{\"operand1\":1,\"operand2\":2}";

    @Test
    public void canMarshalToJson() throws JsonProcessingException {
        AdditionOperands example = new AdditionOperands(OP1, OP2);
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(example);

        assertThat(str, equalTo(EXPECTED_JSON));
    }

}