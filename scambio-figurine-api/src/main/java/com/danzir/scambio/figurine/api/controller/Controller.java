package com.danzir.scambio.figurine.api.controller;

import com.danzir.scambio.figurine.data.model.Album;
import com.danzir.scambio.figurine.data.model.User;
import com.danzir.scambio.figurine.data.persistence.Database;
import com.danzir.scambio.figurine.data.repository.AlbumRepository;
import com.danzir.scambio.figurine.data.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    private Database database;
    private AlbumRepository albumRepository;
    private UserRepository userRepository;

    public Controller(Database database,
                      AlbumRepository albumRepository,
                      UserRepository userRepository){
        this.database = database;
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers(){
        return database.getUsers();
    }

    @GetMapping(value = "/albums")
    public List<Album> getAlbums(){
        return albumRepository.findAll();
    }

    @GetMapping(value = "/test")
    public String test(){
        return "test";
    }

}
