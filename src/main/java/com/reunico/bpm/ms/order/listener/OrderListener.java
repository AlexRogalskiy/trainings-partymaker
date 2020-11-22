package com.reunico.bpm.ms.order.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.ExchangeConstants;
import com.reunico.bpm.constants.MessageTypeConstants;
import com.reunico.bpm.constants.RoutingKeyConstants;
import com.reunico.bpm.domain.Payment;
import com.reunico.bpm.domain.PaymentResult;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
public class OrderListener {

  private final RuntimeService runtimeService;
  private final ObjectMapper objectMapper;

  public OrderListener(RuntimeService runtimeService, ObjectMapper objectMapper) {
    this.runtimeService = runtimeService;
    this.objectMapper = objectMapper;
  }



  @RabbitListener(bindings = @QueueBinding(
      value = @Queue(value = RoutingKeyConstants.PAYMENT_ORDER, durable = "true"),
      exchange = @Exchange(value = ExchangeConstants.PAYMENT_BUS, type = "topic", durable = "true"),
      key = RoutingKeyConstants.PAYMENT_ORDER))
  @Transactional  
  public void getPaymentResult(String paymentResultStr) throws JsonProcessingException {
    System.out.println("Новый результат списания средств:" + paymentResultStr);
    PaymentResult paymentResult = mapToPr(paymentResultStr);
    if (paymentResult.getResult()) {
      runtimeService.createMessageCorrelation(MessageTypeConstants.PAYMENT_SUCCESS)
              .processInstanceBusinessKey(paymentResult.getOrderId()).correlateAll();
    } else {
      runtimeService.createMessageCorrelation(MessageTypeConstants.PAYMENT_FAILED)
              .processInstanceBusinessKey(paymentResult.getOrderId()).correlateAll();
    }
  }

  public PaymentResult mapToPr(String paymentResultStr) throws JsonProcessingException {
    return objectMapper.readValue(paymentResultStr, PaymentResult.class);
  }
}
