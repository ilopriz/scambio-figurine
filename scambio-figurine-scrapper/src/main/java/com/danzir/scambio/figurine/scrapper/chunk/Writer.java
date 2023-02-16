package com.danzir.scambio.figurine.scrapper.chunk;

import com.danzir.scambio.figurine.data.model.Album;
import com.danzir.scambio.figurine.data.model.Record;
import com.danzir.scambio.figurine.data.model.User;
import com.danzir.scambio.figurine.data.persistence.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Writer implements ItemWriter<Record> {

    private static final Logger logger = LoggerFactory.getLogger(Writer.class);

    @Autowired
    private Database database;

    private String stepName;

    public Writer(String stepName){
        this.stepName = stepName;
    }

    @Override
    public void write(Chunk<? extends Record> chunk) {
        logger.debug("[{}]Writing chunk {}", stepName, chunk);
        List<User> users = new ArrayList<>();
        List<Album> albums = new ArrayList<>();
        for (Record record : chunk){
            users.add(record.getUser());
            albums.add(record.getAlbum());
        }
        database.writeRecord(users, albums);
    }

}
