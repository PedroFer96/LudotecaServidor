/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;

/**
 *
 * @author pedro
 */
public class Empleado implements Serializable{
    
    private int id;
    private String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String dni;
    private String user;
    private String password;
    private String phone;
    private String correo;
    private String idLudoteca;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellidos, String dni, String phone, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.phone = phone;
        this.correo = correo;
    }

    public Empleado(int id, String nombre, String apellidos, String fechaNacimiento, String dni, String user, String password, String phone, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.user = user;
        this.password = password;
        this.phone = phone;
        this.correo = correo;
    }
    
    public Empleado(int id, String nombre, String apellidos, String fechaNacimiento, String dni, String user, String password, String phone, String correo, String idLudoteca) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.user = user;
        this.password = password;
        this.phone = phone;
        this.correo = correo;
        this.idLudoteca = idLudoteca;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdLudoteca() {
        return idLudoteca;
    }

    public void setIdLudoteca(String idLudoteca) {
        this.idLudoteca = idLudoteca;
    }

    
    
    @Override
    public String toString() {
        return "Empleado{" + "id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", fechaNacimiento=" + fechaNacimiento + ", dni=" + dni + ", user=" + user + ", password=" + password + ", phone=" + phone + ", correo=" + correo + '}';
    }    
    
}
