package com.reunico.bpm.ms.payment.delegate;

import com.reunico.bpm.constants.ErrorMessageConstants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(LogDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.error(ErrorMessageConstants.NO_FUNDS);
    }
}
