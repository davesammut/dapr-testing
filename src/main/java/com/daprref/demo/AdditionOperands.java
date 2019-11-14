package com.daprref.demo;

public class AdditionOperands {
    private Integer operand1;
    private Integer operand2;

    public AdditionOperands() {

    }

    public AdditionOperands(Integer operand1, Integer operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public Integer getOperand1() {
        return operand1;
    }

    public void setOperand1(Integer value) {
        this.operand1 = value;
    }

    public Integer getOperand2() {
        return operand2;
    }

    public void setOperand2(Integer value) {
        this.operand2 = value;
    }

}