package com.reunico.bpm.step;

import com.reunico.bpm.domain.Order;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.model.bpmn.instance.Message;
import org.springframework.stereotype.Service;

@Service
public class StepService {

    private final RuntimeService runtimeService;

    public StepService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void messageSaved(Order order, String message) {

        runtimeService.correlateMessage(message);
        System.out.println("New order received" + order);
    }

}
