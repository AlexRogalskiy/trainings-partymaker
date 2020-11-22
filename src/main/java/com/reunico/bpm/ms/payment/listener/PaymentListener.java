package com.reunico.bpm.ms.payment.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.ExchangeConstants;
import com.reunico.bpm.constants.MessageTypeConstants;
import com.reunico.bpm.constants.ProcessVariablesConstants;
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

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("!test")
public class PaymentListener {

  private final RuntimeService runtimeService;
  private final ObjectMapper objectMapper;

  public PaymentListener(RuntimeService runtimeService, ObjectMapper objectMapper) {
    this.runtimeService = runtimeService;
    this.objectMapper = objectMapper;
  }

  @RabbitListener(bindings = @QueueBinding(
          value = @Queue(value = RoutingKeyConstants.ORDER_PAYMENT, durable = "true"),
          exchange = @Exchange(value = ExchangeConstants.PAYMENT_BUS, type = "topic", durable = "true"),
          key = RoutingKeyConstants.ORDER_PAYMENT))
  @Transactional
  public void getPaymentOrder(String paymentOrderStr) throws JsonProcessingException {
    System.out.println("Новое поручение на списание средств:" + paymentOrderStr);
    Payment payment = mapToPayment(paymentOrderStr);
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(ProcessVariablesConstants.PAYMENT, payment);
    runtimeService.startProcessInstanceByMessage(MessageTypeConstants.RECEIVE_PAYMENT, payment.getOrderId(), variables);
  }

  public Payment mapToPayment(String paymentStr) throws JsonProcessingException {
    return objectMapper.readValue(paymentStr, Payment.class);
  }

}
