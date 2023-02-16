package com.danzir.scambio.figurine.scrapper.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkExecutionListener implements ChunkListener {

    private static final Logger logger = LoggerFactory.getLogger(ChunkExecutionListener.class);

    @Override
    public void afterChunk(ChunkContext context) {
        logger.trace("After chunk");
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        logger.trace("Before chunk");
    }

}
