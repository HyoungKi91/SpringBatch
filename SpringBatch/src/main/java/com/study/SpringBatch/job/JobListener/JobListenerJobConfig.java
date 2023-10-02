package com.study.SpringBatch.job.JobListener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: JobListener 구현
 * run: --spring.batch.job.names=JobListenerJob
 */
@Configuration
@RequiredArgsConstructor
public class JobListenerJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job JobListenerJob(Step JobListenerStep){
        return jobBuilderFactory.get("JobListenerJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener()) // Job Listener 등록 (Job 실행전 및 실행후 처리)
                .start(JobListenerStep)
                .build();
    }

    @JobScope
    @Bean
    public Step JobListenerStep(Tasklet JobListenerTasklet){
        return stepBuilderFactory.get("JobListenerStep")
                .tasklet(JobListenerTasklet)
                .build();
    }
    @StepScope
    @Bean
    public Tasklet JobListenerTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("JobListener");
                return RepeatStatus.FINISHED; // 이 Step이 끝났다라는 명시
            }
        };
    }
}
