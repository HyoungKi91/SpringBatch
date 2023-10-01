package com.study.SpringBatch.job;

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

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory; // Job을 생성하기위한 객체

    @Autowired
    private StepBuilderFactory stepBuilderFactory; // step을 생성하기위한 객체

    @Bean
    public Job helloWorldJob(){
        return jobBuilderFactory.get("helloWorldJob") //이름 정해주기
                .incrementer(new RunIdIncrementer()) //Job의 이름 지정. -- RunIdIncrementer으로 시퀀스로 순차적 ID 지정
                .start(helloWorldStep()) // step을 지정
                .build();
    }

    @JobScope
    @Bean
    public Step helloWorldStep(){
        return stepBuilderFactory.get("helloWorldStep") // step 이름 지정
                .tasklet(helloWorldTasklet())
                .build();
    }
    @StepScope
    @Bean
    public Tasklet helloWorldTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("helloWorld");
                return RepeatStatus.FINISHED; // 이 Step이 끝났다라는 명시
            }
        };
    }
}
