package kr.co.selenium.test.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.co.selenium.common.config.TaskJobParameterConfig;
import kr.co.selenium.common.listener.JobListener;
import kr.co.selenium.test.task.TaskSeleniumTest;

@Configuration
@EnableBatchProcessing
public class JobSeleniumTest {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobListener jobListener;

	@Autowired
	private TaskJobParameterConfig taskJobParameterConfig;

	@Autowired
	private TaskSeleniumTest taskSeleniumTest;

	private final String JOB_ID = "JobSeleniumTest";
	private final String STEP_ID_00 = "taskJobParameterConfig";
	private final String STEP_ID_01 = "taskSeleniumTest";

	private Map<String, String> jobparametersMap = new HashMap<>();

	@Bean(JOB_ID)
	public Job doJob() {

		return jobBuilderFactory.get(JOB_ID)
				.listener(jobListener)
				.preventRestart()
				.incrementer(new RunIdIncrementer())
				.start(taskJobParameterConfig(null))
				.next((taskSeleniumTest()))
				.build();
	}

	@Bean(JOB_ID + STEP_ID_00)
	@JobScope
	public Step taskJobParameterConfig(@Value("#{jobParameters[runDt]}") String runDt) {

		jobparametersMap.put("runDt", runDt);

		return stepBuilderFactory.get(STEP_ID_00)
				.tasklet(taskJobParameterConfig.taskJobParameterConfig(jobparametersMap))
				.build();
	}

	@Bean(JOB_ID + STEP_ID_01)
	@JobScope
	public Step taskSeleniumTest() {

		return stepBuilderFactory.get(STEP_ID_01)
				.tasklet(taskSeleniumTest.doTask(jobparametersMap))
				.build();
	}
}