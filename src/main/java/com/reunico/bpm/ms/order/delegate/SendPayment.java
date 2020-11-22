package com.reunico.bpm.ms.order.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.ExchangeConstants;
import com.reunico.bpm.constants.ProcessVariablesConstants;
import com.reunico.bpm.constants.RoutingKeyConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.reunico.bpm.domain.*;

@Component
public class SendPayment implements JavaDelegate {

  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;

  public SendPayment(ObjectMapper objectMapper, RabbitTemplate rabbitTemplate) {
    this.objectMapper = objectMapper;
    this.rabbitTemplate = rabbitTemplate;
  }
  
  @Override
  public void execute(DelegateExecution ctx) throws Exception {
    Payment payment = (Payment) ctx.getVariable(ProcessVariablesConstants.PAYMENT);

    rabbitTemplate.convertAndSend(
            ExchangeConstants.PAYMENT_BUS,
            RoutingKeyConstants.ORDER_PAYMENT,
            objectMapper.writeValueAsString(payment));

    System.out.println("New payment request: " + payment);
  }

}
