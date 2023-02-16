package com.danzir.scambio.figurine.scrapper.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution){
        logger.info("!!! JOB STARTED!");
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("!!! JOB FINISHED!");
        }
    }
}
