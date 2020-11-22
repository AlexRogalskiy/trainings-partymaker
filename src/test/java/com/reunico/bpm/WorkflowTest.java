package com.reunico.bpm;

import com.reunico.bpm.constants.ProcessVariablesConstants;
import com.reunico.bpm.domain.Payment;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.RequiredHistoryLevel;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Deployment(resources = "partyMaker_v2.bpmn")
public class WorkflowTest extends AbstractProcessEngineRuleTest {

  @Autowired
  public RuntimeService runtimeService;

  @MockBean
  RabbitTemplate rabbitTemplate;

  @Autowired
  ProcessEngine processEngine;

  @Autowired
  ManagementService managementService;

  @Rule
  @ClassRule
  public static ProcessEngineRule rule;

  @PostConstruct
  void initRule() {
    rule = TestCoverageProcessEngineRuleBuilder.create(processEngine).build();
  }


  Payment payment = new Payment(300L, "55443311226677", "777");


  @Before
  public void setup() {
    Mockito.when(rabbitTemplate.convertSendAndReceive(payment)).thenReturn(null);
  }

  @Test
  @RequiredHistoryLevel("none")
  public void shouldExecuteHappyPath() {
    // given
    String processDefinitionKey = "order";

    // when
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, "777");

    // then
    assertThat(processInstance).isStarted()
        .task()
        .hasDefinitionKey("orderParty")
        .isNotAssigned();
    complete(task(processInstance), withVariables(ProcessVariablesConstants.AMOUNT, 300L,
            ProcessVariablesConstants.PAN, "55443311226677"));

    Job job = managementService.createJobQuery().singleResult();
    managementService.executeJob(job.getId());
    assertThat(processInstance).isWaitingAt("Gateway_0dtmurp");
  }

}
