package com.study.SpringBatch.job.ValidatedParam.Validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName"); // JobParameters에서 fileName의 값을 가져옴

        if (!StringUtils.endsWithIgnoreCase(fileName , "csv")){
            throw new JobParametersInvalidException("This is not csv file");
        }
    }
}
