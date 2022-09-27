package com.example.newapp;

public class UserModel {
    private String nombre,fecha;
    private int imagen;


    public UserModel(String nombre, String fecha, int imagen) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.imagen = imagen;
    }

    public String getNombre() {

        return nombre;
    }

    public String getFecha() {

        return fecha;
    }

    public int getImagen() {

        return imagen;
    }

}