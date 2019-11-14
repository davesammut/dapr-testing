package com.daprref.demo;


import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CalculatorTests {

    @Test
    public void addTest() {
        int operand1 = 10;
        int operand2 = 20;
        int actualResult = Calculator.add(operand1, operand2);
        assertThat(actualResult).isEqualTo(30);
    }
}
