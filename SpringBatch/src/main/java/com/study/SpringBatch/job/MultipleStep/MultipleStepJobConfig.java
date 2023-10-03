package com.study.SpringBatch.job.MultipleStep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc : multiStep
 * run : --spring.batch.job.names=MultipleStepJob
 */
@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job MultipleStepJob(Step MultipleStep1 , Step MultipleStep2 , Step MultipleStep3){
        return jobBuilderFactory.get("MultipleStepJob")
                .incrementer(new RunIdIncrementer())
                .start(MultipleStep1)
                .next(MultipleStep2)
                .next(MultipleStep3)
                .build();
    }

    @JobScope
    @Bean
    public Step MultipleStep1(){
        return stepBuilderFactory.get("MultipleStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope
    @Bean
    public Step MultipleStep2(){
        return stepBuilderFactory.get("MultipleStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    executionContext.put("key","hello"); // 다음 step의 값을 전달 할 수 있음
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @JobScope
    @Bean
    public Step MultipleStep3(){
        return stepBuilderFactory.get("MultipleStep3")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    System.out.println(executionContext.get("key")); // 이전 step에서 전달받은 값을 key값으로 가져올 수 있음
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
