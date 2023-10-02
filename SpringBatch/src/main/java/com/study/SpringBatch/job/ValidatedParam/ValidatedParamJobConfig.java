package com.study.SpringBatch.job.ValidatedParam;

import com.study.SpringBatch.job.ValidatedParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.Task;

import java.util.Arrays;

/**
 * desc: 파일 이름 파라미터 전달 그리고 검증
 * run: --spring.batch.job.names=ValidatedParamJob -fileName=test.csv
 */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ValidatedParamJob(Step ValidatedParamStep){ // Bean으로 주입된 ValidatedParamStep 를 받아옴
        return jobBuilderFactory.get("ValidatedParamJob")
                .incrementer(new RunIdIncrementer())
                //.validator(new FileParamValidator()) // file형식이 csv인지 체크 -- 단건의 validator
                .validator(multipleValidator()) //여러개의 validator을 사용하고 싶을때
                .start(ValidatedParamStep)
                .build();
    }
    private CompositeJobParametersValidator multipleValidator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator())); // validator을 여러개 사용 가능
        return validator;
    }
    @JobScope
    @Bean
    public Step ValidatedParamStep(Tasklet ValidatedParamTasklet){ // Bean으로 주입된 ValidatedParamTasklet를 받아옴
        return stepBuilderFactory.get("ValidatedParamStep")
                .tasklet(ValidatedParamTasklet)
                .build();
    }
    @StepScope
    @Bean
    public Tasklet ValidatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println(fileName);
                System.out.println("ValidatedParamTasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
