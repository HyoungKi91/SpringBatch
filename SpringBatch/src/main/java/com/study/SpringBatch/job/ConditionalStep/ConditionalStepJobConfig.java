package com.study.SpringBatch.job.ConditionalStep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc : Step 분기 처리
 * run : --spring.batch.job.names=ConditionalStepJob
 */
@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ConditionalStepJob(Step ConditionalStep
                                  ,Step ConditionalFailedStep
                                  ,Step ConditionalCompletedStep
                                  ,Step ConditionalAllStep){
        return jobBuilderFactory.get("ConditionalStepJob")
                .incrementer(new RunIdIncrementer())
                .start(ConditionalStep)
                    .on("FAILED").to(ConditionalFailedStep)
                .from(ConditionalStep)
                    .on("COMPLETED").to(ConditionalCompletedStep)
                .from(ConditionalStep)
                    .on("*").to(ConditionalAllStep)
                .end()
                .build();
    }

    @JobScope
    @Bean
    public Step ConditionalStep(){
        return stepBuilderFactory.get("ConditionalStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("ConditionalStep");
                        throw new Exception("fail");
                        //return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @JobScope
    @Bean
    public Step ConditionalFailedStep(){
        return stepBuilderFactory.get("ConditionalFailedStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("ConditionalFailedStep");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @JobScope
    @Bean
    public Step ConditionalCompletedStep(){
        return stepBuilderFactory.get("ConditionalCompletedStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("ConditionalCompletedStep");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @JobScope
    @Bean
    public Step ConditionalAllStep(){
        return stepBuilderFactory.get("ConditionalAllStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("ConditionalAllStep");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
