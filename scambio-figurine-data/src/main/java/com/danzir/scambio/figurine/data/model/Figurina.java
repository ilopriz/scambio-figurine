package com.danzir.scambio.figurine.data.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Figurina {

    private String numero;
    private int qta;

    public Figurina(){}

    public Figurina(String numero, int qta) {
        this.numero = numero;
        this.qta = qta;
    }

    @Override
    public String toString() {
        return "Figurina{" +
                "numero='" + numero + '\'' +
                ", qta='" + qta + '\'' +
                '}';
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getQta() {
        return qta;
    }

    public void setQta(int qta) {
        this.qta = qta;
    }
}
