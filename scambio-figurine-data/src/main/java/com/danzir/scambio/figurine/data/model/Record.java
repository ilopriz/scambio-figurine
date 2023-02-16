package com.danzir.scambio.figurine.data.model;

public class Record {

    private User user;
    private Album album;

    public Record(User user, Album album) {
        this.user = user;
        this.album = album;
    }

    @Override
    public String toString() {
        return "Record{" +
                "user=" + user +
                ", album=" + album +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
