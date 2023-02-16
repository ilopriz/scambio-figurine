package com.danzir.scambio.figurine.data.model;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id
    private String userId;

    private String userName;
    private int feedbackPositivi;
    private int feedbackNegativi;
    private String provincia;
    private String comune;
    private boolean scambioPosta;
    private boolean scambioMano;
    private String dataRegistrazione;
    private String dataUltimoAccesso;

    protected  User(){}

    public User(String userId) {
        this.userId = userId;
    }

    public User(String userId, String userName, int feedbackPositivi, int feedbackNegativi, String provincia, String comune, boolean scambioPosta, boolean scambioMano, String dataRegistrazione, String dataUltimoAccesso) {
        this.userId = userId;
        this.userName = userName;
        this.feedbackPositivi = feedbackPositivi;
        this.feedbackNegativi = feedbackNegativi;
        this.provincia = provincia;
        this.comune = comune;
        this.scambioPosta = scambioPosta;
        this.scambioMano = scambioMano;
        this.dataRegistrazione = dataRegistrazione;
        this.dataUltimoAccesso = dataUltimoAccesso;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", feedbackPositivi=" + feedbackPositivi +
                ", feedbackNegativi=" + feedbackNegativi +
                ", provincia='" + provincia + '\'' +
                ", comune='" + comune + '\'' +
                ", scambioPosta=" + scambioPosta +
                ", scambioMano=" + scambioMano +
                ", dataRegistrazione='" + dataRegistrazione + '\'' +
                ", dataUltimoAccesso='" + dataUltimoAccesso + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFeedbackPositivi() {
        return feedbackPositivi;
    }

    public void setFeedbackPositivi(int feedbackPositivi) {
        this.feedbackPositivi = feedbackPositivi;
    }

    public int getFeedbackNegativi() {
        return feedbackNegativi;
    }

    public void setFeedbackNegativi(int feedbackNegativi) {
        this.feedbackNegativi = feedbackNegativi;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public boolean isScambioPosta() {
        return scambioPosta;
    }

    public void setScambioPosta(boolean scambioPosta) {
        this.scambioPosta = scambioPosta;
    }

    public boolean isScambioMano() {
        return scambioMano;
    }

    public void setScambioMano(boolean scambioMano) {
        this.scambioMano = scambioMano;
    }

    public String getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(String dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public String getDataUltimoAccesso() {
        return dataUltimoAccesso;
    }

    public void setDataUltimoAccesso(String dataUltimoAccesso) {
        this.dataUltimoAccesso = dataUltimoAccesso;
    }

}
