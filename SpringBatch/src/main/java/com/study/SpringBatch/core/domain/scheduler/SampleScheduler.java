package com.study.SpringBatch.core.domain.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SampleScheduler {

    @Autowired
    private JobLauncher launcher;

    @Autowired
    private Job helloWorldJob;

    @Scheduled(cron = "0 */1 * * * *") //1분주기
    public void helloWorldJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParameters(
                Collections.singletonMap("requestTime",new JobParameter(System.currentTimeMillis())) //다른 값으로 넘겨줘야 배치가 계속 실행된다.
        );

        launcher.run(helloWorldJob , jobParameters);
    }
}
