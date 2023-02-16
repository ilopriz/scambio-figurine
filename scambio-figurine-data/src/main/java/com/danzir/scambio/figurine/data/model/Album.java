package com.danzir.scambio.figurine.data.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="album")
public class Album {

    @Id
    private String userId;

    @ElementCollection
    private List<Figurina> doppie;

    @ElementCollection
    private List<String> mancanti;

    protected Album(){}

    public Album(String userId, List<Figurina> doppie, List<String> mancanti) {
        this.userId = userId;
        this.doppie = doppie;
        this.mancanti = mancanti;
    }

    @Override
    public String toString() {
        return "Album{" +
                "userId='" + userId + '\'' +
                ", doppie=" + doppie +
                ", mancanti=" + mancanti +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Figurina> getDoppie() {
        return doppie;
    }

    public void setDoppie(List<Figurina> doppie) {
        this.doppie = doppie;
    }

    public List<String> getMancanti() {
        return mancanti;
    }

    public void setMancanti(List<String> mancanti) {
        this.mancanti = mancanti;
    }

}
