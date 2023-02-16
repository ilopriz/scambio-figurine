package com.danzir.scambio.figurine.scrapper.config;

import com.danzir.scambio.figurine.scrapper.chunk.Processor;
import com.danzir.scambio.figurine.scrapper.chunk.Reader;
import com.danzir.scambio.figurine.scrapper.chunk.Writer;
import com.danzir.scambio.figurine.data.model.Record;
import com.danzir.scambio.figurine.scrapper.listener.ChunkExecutionListener;
import com.danzir.scambio.figurine.scrapper.listener.JobCompletionNotificationListener;
import com.danzir.scambio.figurine.scrapper.listener.StepExecutionNotificationListener;
import com.danzir.scambio.figurine.scrapper.navigation.Navigation;
import com.danzir.scambio.figurine.scrapper.navigation.PlaywrightFactory;
import com.danzir.scambio.figurine.data.persistence.Database;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Configuration
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    private ConfigProperties configProperties;
    private JobRepository jobRepository;
    private PlatformTransactionManager transactionManager;
    private Navigation navigation;
    private Database database;

    public BatchConfig(ConfigProperties configProperties,
                       JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       Navigation navigation,
                       Database database){
        this.configProperties = configProperties;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.navigation = navigation;
        this.database = database;
    }

    @Bean
    public Job createJob() {
        logger.trace("createJob");

        int stepsSetSize = configProperties.getThreadSetSize()-1;
        logger.trace("stepsSetSize: {}", stepsSetSize);

        boolean headless = configProperties.isHeadless();
        logger.trace("headless: {}", headless);

        long timeout = configProperties.getTimeout();
        logger.trace("timeout: {}", timeout);

        //start a browser
        Page page = PlaywrightFactory.getPage(headless, timeout);
        navigation.login(page);

        //store current user's album
        Record sessionUserData = navigation.readSessionUserData(page);
        database.writeRecord(sessionUserData.getUser(), sessionUserData.getAlbum());

        //collect all user ids to be processed
        navigation.goToScambi(page);
        List<String> allUsersIds = navigation.getAllUsersIds(page);
        int totalUsers = allUsersIds.size();
        PlaywrightFactory.closePage();

        //Create a list of queues of user ids. One queue for each step
        List<Queue<String>> userIdsQueues = new ArrayList<>();
        for (int i=0; i<stepsSetSize; i++){
            userIdsQueues.add(new LinkedList<String>());
        }

        //Distribute userIds in the queues
        for (int i=0; i<totalUsers; i++){
            String userId = allUsersIds.get(i);
            userIdsQueues.get(i%stepsSetSize).add(userId);
        }

        //create ona flow for each step
        List<Flow> flowList = new ArrayList<>();
        for (int i=0; i<stepsSetSize; i++){
            flowList.add(createFlow(userIdsQueues.get(i), i));
        }

        //create a flow split from list of flows
        Flow splitFlow = new FlowBuilder<SimpleFlow>("splitFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flowList.toArray(new Flow[flowList.size()])).build();

        //create job from the flow split
        return new JobBuilder("figurineScrapperJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionNotificationListener())
                .start(splitFlow)
                .build().build();

/*
        return new JobBuilder("figurineScrapperJob", jobRepository)
                .listener(new JobCompletionNotificationListener())
                .start(flowList.get(0)).split(new SimpleAsyncTaskExecutor()).add(flowList.toArray(new Flow[flowList.size()])).end().build();*/
    }

    @Bean
    public Writer writer(String stepName){
        return new Writer(stepName);
    }

    private Flow createFlow(Queue<String> userIdsQueue, int stepNum) {
        Step step = createStep(userIdsQueue, stepNum);
        return new FlowBuilder<Flow>("flow"+stepNum).start(step).build();
    }

    private Step createStep(Queue<String> userIdsQueue, int stepNum) {
        String stepName = "step"+stepNum;
        logger.debug("{} will process {} user ids: {}", stepName, userIdsQueue.size(), userIdsQueue);
        int chunkSize = configProperties.getChunkSize();
        return new StepBuilder(stepName, jobRepository)
                .<String, Record> chunk(chunkSize, transactionManager)
                .reader(new Reader(stepName, userIdsQueue))
                .processor(new Processor(stepName, configProperties, navigation))
                .writer(writer(stepName))
                .listener(new StepExecutionNotificationListener())
                .listener(new ChunkExecutionListener())
                .build();
    }

}
