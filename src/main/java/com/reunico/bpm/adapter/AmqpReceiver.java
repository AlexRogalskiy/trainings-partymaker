package com.reunico.bpm.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.MessageTypeConstants;
import com.reunico.bpm.domain.PaymentResult;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
@Profile("!test")
public class AmqpReceiver {

  private final RuntimeService runtimeService;
  private final ObjectMapper objectMapper;

  public AmqpReceiver(RuntimeService runtimeService, ObjectMapper objectMapper) {
    this.runtimeService = runtimeService;
    this.objectMapper = objectMapper;
  }


//  @RabbitListener(bindings = @QueueBinding( //
//      value = @Queue(value = "payment", durable = "true"), //
//      exchange = @Exchange(value = "payment", type = "topic", durable = "true"), //
//      key = "paymentResult"))
  @Transactional  
  public void getPaymentInfo(String paymentResultStr) throws JsonProcessingException {
    System.out.println("New message:" + paymentResultStr);
    PaymentResult paymentResult = mapToPr(paymentResultStr);
    if (paymentResult.getResult()) {
      runtimeService.correlateMessage(paymentResult.getOrderId(), MessageTypeConstants.PAYMENT_SUCCESS);
    } else {
      runtimeService.correlateMessage(paymentResult.getOrderId(), MessageTypeConstants.PAYMENT_FAILED);
    }
  }

  public PaymentResult mapToPr(String paymentResultStr) throws JsonProcessingException {
    return objectMapper.readValue(paymentResultStr, PaymentResult.class);
  }
  
}
