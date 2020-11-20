package com.reunico.bpm.domain;

import java.util.Objects;

public class Payment {

    private Long amount;
    private String pan;
    private String orderId;

    public Payment() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Payment(Long amount, String pan, String orderId) {
        this.amount = amount;
        this.pan = pan;
        this.orderId = orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", pan='" + pan + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return orderId.equals(payment.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
