package com.danzir.scambio.figurine.scrapper.chunk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import java.util.*;

public class Reader implements ItemReader<String> {

    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    private Queue<String> userIdsQueue;
    private String stepName;

    public Reader(String stepName, Queue<String> userIdsQueue){
        this.stepName = stepName;
        this.userIdsQueue = userIdsQueue;
    }

    @Override
    public String read() {
        String userId = null;
        int recordQueueSize = userIdsQueue.size();
        if (recordQueueSize > 0) {
            userId = userIdsQueue.remove();
        }
        logger.debug("[{}]Reading userId {}", stepName, userId);
        return userId;
    }

}
