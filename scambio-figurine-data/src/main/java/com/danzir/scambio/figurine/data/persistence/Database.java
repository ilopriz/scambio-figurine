package com.danzir.scambio.figurine.data.persistence;

import com.danzir.scambio.figurine.data.model.Album;
import com.danzir.scambio.figurine.data.model.User;
import com.danzir.scambio.figurine.data.repository.AlbumRepository;
import com.danzir.scambio.figurine.data.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Database {

    private AlbumRepository albumRepository;
    private UserRepository userRepository;

    public Database(AlbumRepository albumRepository, UserRepository userRepository){
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
    }

    public synchronized void writeRecord(List<User> users, List<Album> albums){
        userRepository.saveAll(users);
        albumRepository.saveAll(albums);
    }

    public synchronized void writeRecord(User user, Album album){
        userRepository.save(user);
        albumRepository.save(album);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
