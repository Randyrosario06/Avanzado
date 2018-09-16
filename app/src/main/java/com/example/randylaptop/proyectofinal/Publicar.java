package com.example.randylaptop.proyectofinal;

public class Publicar {
    private String descripcion;
    private String lugar;
    private String imgURL;

    public Publicar(){

    }

    public Publicar(String descripcion, String lugar, String imgURL){
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.imgURL = imgURL;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setImgURL(String imgURL) { this.imgURL = imgURL; }

    public String getImgURL() { return imgURL; }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
