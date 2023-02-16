package com.danzir.scambio.figurine.scrapper.listener;

import com.danzir.scambio.figurine.scrapper.navigation.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepExecutionNotificationListener implements StepExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(StepExecutionNotificationListener.class);
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.trace("After step");
        PlaywrightFactory.closePage();
        return null;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.trace("Before step");
    }

}
