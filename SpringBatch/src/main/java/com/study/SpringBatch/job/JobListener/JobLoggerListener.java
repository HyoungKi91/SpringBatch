package com.study.SpringBatch.job.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MESSAGE = "{} Job is Running";

    private static String AFTER_MESSAGE = "{} Job is Done. (Status: {})";
    @Override
    public void beforeJob(JobExecution jobExecution) { // 잡이 실행하기 전
        log.info(BEFORE_MESSAGE , jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) { // 잡이 실행한 후
        log.info(AFTER_MESSAGE , jobExecution.getJobInstance().getJobName() , jobExecution.getExitStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED){
            //이메일이나 , 문자 처리 가능
            log.info("job is Failed");
        }
    }
}
