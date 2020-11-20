package com.reunico.bpm.delegate;

import com.reunico.bpm.constants.ProcessVariablesConstants;
import com.reunico.bpm.domain.Order;
import com.reunico.bpm.domain.Payment;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SaveOrder implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Order order = new Order(
                String.valueOf(delegateExecution.getVariable(ProcessVariablesConstants.CUSTOMER)),
                String.valueOf(delegateExecution.getVariable(ProcessVariablesConstants.PHONE)),
                String.valueOf(delegateExecution.getVariable(ProcessVariablesConstants.DESCRIPTION))
                );

        Payment payment = new Payment(
                (Long) delegateExecution.getVariable(ProcessVariablesConstants.AMOUNT),
                String.valueOf(delegateExecution.getVariable(ProcessVariablesConstants.PAN)),
                delegateExecution.getBusinessKey()
        );

        delegateExecution.setVariable(ProcessVariablesConstants.ORDER, order);
        delegateExecution.setVariable(ProcessVariablesConstants.PAYMENT, payment);
    }
}
