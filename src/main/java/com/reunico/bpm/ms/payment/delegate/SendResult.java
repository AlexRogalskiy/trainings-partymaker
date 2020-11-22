package com.reunico.bpm.ms.payment.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reunico.bpm.constants.ExchangeConstants;
import com.reunico.bpm.constants.ProcessVariablesConstants;
import com.reunico.bpm.constants.RoutingKeyConstants;
import com.reunico.bpm.domain.PaymentResult;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendResult implements JavaDelegate {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public SendResult(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        PaymentResult result = (PaymentResult) delegateExecution.getVariable(ProcessVariablesConstants.PAYMENT_RESULT);
        rabbitTemplate.convertAndSend(
                ExchangeConstants.PAYMENT_BUS,
                RoutingKeyConstants.PAYMENT_ORDER,
                objectMapper.writeValueAsString(result)
        );
    }
}
