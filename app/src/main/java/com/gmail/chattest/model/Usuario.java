package com.gmail.chattest.model;

public class Usuario {
    private String usuario;
    private int edad;

    public Usuario(){}

    public Usuario(String usuario, int edad) {
        this.usuario = usuario;
        this.edad = edad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
