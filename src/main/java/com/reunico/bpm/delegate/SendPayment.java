package com.reunico.bpm.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.ProcessVariablesConstants;
import com.reunico.bpm.domain.Payment;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    
    String exchange = "payment";
    String routingKey = "receivePayment";
    
    rabbitTemplate.convertAndSend(exchange, routingKey, objectMapper.writeValueAsString(payment));
  }

}
