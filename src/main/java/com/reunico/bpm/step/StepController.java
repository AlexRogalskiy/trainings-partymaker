package com.reunico.bpm.step;

import com.reunico.bpm.domain.Order;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StepController {

    private final StepService stepService;

    public StepController(StepService stepService) {
        this.stepService = stepService;
    }

    @PutMapping("/message/{message}")
    public void receiveOrder(@RequestBody Order order,
                              @PathVariable String message
                              ) {
        stepService.messageSaved(order, message);

    }

}
