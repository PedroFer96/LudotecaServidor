/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author pedro
 */
public class Sala {
    
    private int id;
    private String nombre;
    private int aforo_maximo;
    private int id_ludoteca;

    public Sala() {
    }

    public Sala(int id, String nombre, int aforo_maximo, int id_ludoteca) {
        this.id = id;
        this.nombre = nombre;
        this.aforo_maximo = aforo_maximo;
        this.id_ludoteca = id_ludoteca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAforo_maximo() {
        return aforo_maximo;
    }

    public void setAforo_maximo(int aforo_maximo) {
        this.aforo_maximo = aforo_maximo;
    }

    public int getId_ludoteca() {
        return id_ludoteca;
    }

    public void setId_ludoteca(int id_ludoteca) {
        this.id_ludoteca = id_ludoteca;
    }

    @Override
    public String toString() {
        return "Sala{" + "id=" + id + ", nombre=" + nombre + ", aforo_maximo=" + aforo_maximo + ", id_ludoteca=" + id_ludoteca + '}';
    }
    
}
