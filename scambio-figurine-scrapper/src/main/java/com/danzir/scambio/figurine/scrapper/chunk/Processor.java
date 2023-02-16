package com.danzir.scambio.figurine.scrapper.chunk;

import com.danzir.scambio.figurine.scrapper.config.ConfigProperties;
import com.danzir.scambio.figurine.data.model.Record;
import com.danzir.scambio.figurine.scrapper.navigation.Navigation;
import com.danzir.scambio.figurine.scrapper.navigation.PlaywrightFactory;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.item.ItemProcessor;

public class Processor implements ItemProcessor<String, Record> {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private ConfigProperties configProperties;
    private Navigation navigation;
    private Page page;
    private String stepName;

    public Processor(String stepName, ConfigProperties configProperties, Navigation navigation){
        this.configProperties = configProperties;
        this.navigation = navigation;
        this.stepName = stepName;
    }

    @BeforeProcess
    public void beforeProcess(){
        if(page == null) {
            page = PlaywrightFactory.getPage(configProperties.isHeadless(), configProperties.getTimeout());
            navigation.login(page);
        }
    }

    @Override
    public Record process(String userId) {
        logger.debug("[{}]Processing userId {}", stepName, userId);
        return navigation.readUserData(page, userId);
    }

}
