package com.reunico.bpm.ms.payment.delegate;

import com.reunico.bpm.constants.ErrorMessageConstants;
import com.reunico.bpm.constants.ProcessVariablesConstants;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.reunico.bpm.domain.*;

@Component
public class ChargeDelegate implements JavaDelegate {
    private final Long creditLimit = 50000L;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Payment payment = (Payment) delegateExecution.getVariable(ProcessVariablesConstants.PAYMENT);
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setOrderId(payment.getOrderId());
        if (payment.getAmount() < creditLimit) {
            paymentResult.setResult(true);
            delegateExecution.setVariable(ProcessVariablesConstants.PAYMENT_RESULT, paymentResult);
        } else {
            paymentResult.setResult(false);
            delegateExecution.setVariable(ProcessVariablesConstants.PAYMENT_RESULT, paymentResult);
            throw new BpmnError(ErrorMessageConstants.NO_FUNDS);
        }
    }
}
