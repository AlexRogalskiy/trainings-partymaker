package com.reunico.bpm.domain;

public class PaymentResult {
    private String orderId;
    private Boolean result;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public PaymentResult() {
    }

    public PaymentResult(String orderId, Boolean result) {
        this.orderId = orderId;
        this.result = result;
    }

    @Override
    public String toString() {
        return "PaymentResult{" +
                "orderId='" + orderId + '\'' +
                ", result=" + result +
                '}';
    }
}
