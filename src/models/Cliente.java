/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author pedro
 */
public class Cliente {
    
    private int id;
    private String nombreC;
    private String apellidosC;
    private String fechaNacimiento;
    private String dniC;
    private String phoneC;
    private String correoC;
    private String userC;
    private String passwordC;
    private int estado;
    private int idTutor;
    private String nombreT;
    private String apellidosT;
    private String fechaNacimientoTutor;
    private String dniT;
    private int phoneT;
    private int codSeguridad;

    public Cliente() {
    }

    public Cliente(int id, String nombreC, String apellidosC, String fechaNacimiento, String dniC, String phoneC, String correoC, String userC, String passwordC, int estado, int idTutor, String nombreT, String apellidosT, String fechaNacimientoTutor, String dniT, int phoneT, int codSeguridad) {
        this.id = id;
        this.nombreC = nombreC;
        this.apellidosC = apellidosC;
        this.fechaNacimiento = fechaNacimiento;
        this.dniC = dniC;
        this.phoneC = phoneC;
        this.correoC = correoC;
        this.userC = userC;
        this.passwordC = passwordC;
        this.estado = estado;
        this.idTutor = idTutor;
        this.nombreT = nombreT;
        this.apellidosT = apellidosT;
        this.fechaNacimientoTutor = fechaNacimientoTutor;
        this.dniT = dniT;
        this.phoneT = phoneT;
        this.codSeguridad = codSeguridad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreC() {
        return nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getApellidosC() {
        return apellidosC;
    }

    public void setApellidosC(String apellidosC) {
        this.apellidosC = apellidosC;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDniC() {
        return dniC;
    }

    public void setDniC(String dniC) {
        this.dniC = dniC;
    }

    public String getPhoneC() {
        return phoneC;
    }

    public void setPhoneC(String phoneC) {
        this.phoneC = phoneC;
    }

    public String getCorreoC() {
        return correoC;
    }

    public void setCorreoC(String correoC) {
        this.correoC = correoC;
    }

    public String getUserC() {
        return userC;
    }

    public void setUserC(String userC) {
        this.userC = userC;
    }

    public String getPasswordC() {
        return passwordC;
    }

    public void setPasswordC(String passwordC) {
        this.passwordC = passwordC;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getNombreT() {
        return nombreT;
    }

    public void setNombreT(String nombreT) {
        this.nombreT = nombreT;
    }

    public String getApellidosT() {
        return apellidosT;
    }

    public void setApellidosT(String apellidosT) {
        this.apellidosT = apellidosT;
    }

    public String getFechaNacimientoTutor() {
        return fechaNacimientoTutor;
    }

    public void setFechaNacimientoTutor(String fechaNacimientoTutor) {
        this.fechaNacimientoTutor = fechaNacimientoTutor;
    }

    public String getDniT() {
        return dniT;
    }

    public void setDniT(String dniT) {
        this.dniT = dniT;
    }

    public int getPhoneT() {
        return phoneT;
    }

    public void setPhoneT(int phoneT) {
        this.phoneT = phoneT;
    }

    public int getCodSeguridad() {
        return codSeguridad;
    }

    public void setCodSeguridad(int codSeguridad) {
        this.codSeguridad = codSeguridad;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", nombreC=" + nombreC + ", apellidosC=" + apellidosC + ", fechaNacimiento=" + fechaNacimiento + ", dniC=" + dniC + ", phoneC=" + phoneC + ", correoC=" + correoC + ", userC=" + userC + ", passwordC=" + passwordC + ", estado=" + estado + ", idTutor=" + idTutor + ", nombreT=" + nombreT + ", apellidosT=" + apellidosT + ", fechaNacimientoTutor=" + fechaNacimientoTutor + ", dniT=" + dniT + ", phoneT=" + phoneT + ", codSeguridad=" + codSeguridad + '}';
    }

    
}
