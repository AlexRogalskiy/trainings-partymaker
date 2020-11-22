package com.reunico.bpm.ms.order.delegate;

import com.reunico.bpm.constants.ProcessVariablesConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import com.reunico.bpm.domain.*;
import org.springframework.stereotype.Component;
import com.reunico.bpm.domain.*;

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
