/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.Actividades;
import models.Administrador;
import models.Cliente;
import models.Empleado;
import models.Ludoteca;
import models.Propietario;
import models.Sala;

/**
 *
 * @author pedro
 */
public class ManejoDB {
    
    private String url = "jdbc:mysql://localhost:3306/ludotecabd";
    private String user = "pedro";
    private String passw = "pedro";
    public java.sql.Connection conexion = null;
    public java.sql.Statement sentencia = null;
    public java.sql.ResultSet resultset = null;
    
    public ManejoDB(){
        try { 
            conexion();
        }catch (ClassNotFoundException ex){
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void conexion() throws ClassNotFoundException {
        
        try{
            this.conexion = DriverManager.getConnection(url,user,passw);
        } catch (SQLException ex) {
            System.out.println("Error al conectar con la base de datos");
        }
    }
    
    public int loginTipo(String usuario, String contraseña) {

        int tipo = -1;
        
        try {

            this.sentencia = conexion.createStatement();
            PreparedStatement ps = conexion.prepareStatement("SELECT tipo FROM persona INNER JOIN personal ON persona.id = personal.id_persona WHERE persona.user = ? AND persona.password = ?; ");
            
            ps.setString(1, usuario);
            ps.setString(2, contraseña);
            
            resultset = ps.executeQuery();
            
            
            while(resultset.next()){
                tipo = resultset.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return tipo;
    }


    public Administrador loginAdmin(String usuario, String contraseña) throws SQLException {

        this.sentencia = conexion.createStatement();
        PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre, apellidos, dni, user, password, phone, tipo From persona INNER JOIN personal ON persona.id = personal.id_persona WHERE persona.user = ? AND persona.password = ? AND personal.tipo = 0;" );

        ps.setString(1, usuario);
        ps.setString(2, contraseña);

        resultset = ps.executeQuery();


        Administrador ad = new Administrador();
        
        while(resultset.next()){
            
            ad.setId(resultset.getInt("id"));
            ad.setNombre(resultset.getString("nombre"));
            ad.setApellidos(resultset.getString("apellidos"));
        }
            
        return ad;
        
    }

    public Propietario loginPropietario(String usuario, String contraseña) throws SQLException {
        
        
        PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre, apellidos, fechaNacimiento, dni, user, password, correo, phone, tipo From persona INNER JOIN personal ON persona.id = personal.id_persona WHERE persona.user = ? AND persona.password = ? AND personal.tipo = 1;");
        
        ps.setString(1, usuario);
        ps.setString(2, contraseña);
        
        resultset = ps.executeQuery();
        
        Propietario pr = new Propietario();
        
        while(resultset.next()){
            pr.setId(resultset.getInt("id"));
            pr.setNombre(resultset.getString("nombre"));
            pr.setApellidos(resultset.getString("apellidos"));
            pr.setFechaNacimiento(resultset.getString("fechaNacimiento"));
            pr.setDni(resultset.getString("dni"));
            pr.setPhone(resultset.getString("phone"));
            pr.setTipo(resultset.getInt("tipo"));
            pr.setUser(resultset.getString("user"));
            pr.setPassword(resultset.getString("password"));
            pr.setCorreo(resultset.getString("correo"));
        }
        
        return pr;
    }
    
    public Empleado loginEmpleado(String usuario, String contraseña) throws SQLException {
           
        PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, nombre, apellidos, fechaNacimiento, dni, user, password, correo, phone, empleados.id_ludoteca FROM persona INNER JOIN personal ON persona.id = personal.id_persona INNER JOIN empleados ON empleados.id = personal.id_persona WHERE persona.user = ? AND persona.password = ? AND personal.tipo = 2; ");
        
        ps.setString(1, usuario);
        ps.setString(2, contraseña);
        
        resultset = ps.executeQuery();
        
        Empleado em = new Empleado();
        
        while(resultset.next()){
            em.setId(resultset.getInt("persona.id"));
            em.setNombre(resultset.getString("nombre"));
            em.setApellidos(resultset.getString("apellidos"));
            em.setFechaNacimiento(resultset.getString("fechaNacimiento"));
            em.setDni(resultset.getString("dni"));
            em.setPhone(resultset.getString("phone"));
            em.setUser(resultset.getString("user"));
            em.setPassword(resultset.getString("password"));
            em.setCorreo(resultset.getString("correo"));
            em.setIdLudoteca(resultset.getString("empleados.id_ludoteca"));
        }
        
        return em;
        
    }
    
    
    public Propietario nombrePropietario(String id) {

        Propietario p = new Propietario();
        try{
            PreparedStatement ps = conexion.prepareStatement("SELECT nombre, apellidos FROM persona WHERE id = ?;");
            
            ps.setString(1, id);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                p.setNombre(resultset.getString("nombre"));
                p.setApellidos(resultset.getString("apellidos"));
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
    
    
    public Propietario informacionPropietario(String idPropietario) {
     
        Propietario pr = new Propietario();
        
        try{       
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre, apellidos, fechaNacimiento, dni, user, password, correo, phone, tipo From persona INNER JOIN personal ON persona.id = personal.id_persona WHERE persona.id = ? AND personal.tipo = 1;");
            
            
            ps.setString(1, idPropietario);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                pr.setId(resultset.getInt("id"));
                pr.setNombre(resultset.getString("nombre"));
                pr.setApellidos(resultset.getString("apellidos"));
                pr.setFechaNacimiento(resultset.getString("fechaNacimiento"));
                pr.setDni(resultset.getString("dni"));
                pr.setPhone(resultset.getString("phone"));
                pr.setTipo(resultset.getInt("tipo"));
                pr.setUser(resultset.getString("user"));
                pr.setPassword(resultset.getString("password"));
                pr.setCorreo(resultset.getString("correo"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pr;
        
    }
    
    public Empleado informacionEmpleado(String idEmpleado) {
        Empleado em = new Empleado();
        
        try{           
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, nombre, apellidos, fechaNacimiento, dni, user, password, correo, phone, empleados.id_ludoteca FROM persona INNER JOIN personal ON persona.id = personal.id_persona INNER JOIN empleados ON empleados.id = personal.id_persona WHERE persona.id = ? AND personal.tipo = 2; ");
            
            ps.setString(1, idEmpleado);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                em.setId(resultset.getInt("persona.id"));
                em.setNombre(resultset.getString("nombre"));
                em.setApellidos(resultset.getString("apellidos"));
                em.setFechaNacimiento(resultset.getString("fechaNacimiento"));
                em.setDni(resultset.getString("dni"));
                em.setPhone(resultset.getString("phone"));
                em.setUser(resultset.getString("user"));
                em.setPassword(resultset.getString("password"));
                em.setCorreo(resultset.getString("correo"));
                em.setIdLudoteca(resultset.getString("empleados.id_ludoteca"));                
            }

        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return em;        
    }



    public boolean addPropietario(String nombre, String apellidos, String fechaNacimiento, String dni, String phone, String correo, String user, String password) {

        try{
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, correo, user, password, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaNacimiento);
            ps.setString(4, dni);
            ps.setString(5, correo);
            ps.setString(6, user);
            ps.setString(7, password);
            ps.setString(8, phone);
            
            int resp = ps.executeUpdate();
            
            if(resp > 0 ){
            
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC limit 1;");
                
                int id;
                
                resultset = ps1.executeQuery();
                
                while(resultset.next()){
                    
                    id = resultset.getInt("id");

                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO personal (id_persona, tipo) VALUES (?,?);");
                    PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO propietario (id) VALUES (?);");
                    
                    ps2.setInt(1, id);
                    ps2.setInt(2, 1);
                    ps3.setInt(1, id);
                    
                    int resp1 = ps2.executeUpdate();
                    int resp2 = ps3.executeUpdate();
                    
                    if(resp1 > 0 && resp2 > 0){
                        return true;
                    }
                    
                }

            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public ArrayList<Propietario> obtenerPropietarios() {
   
        ArrayList<Propietario> listaPropietarios = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre, apellidos, fechaNacimiento, dni, correo, phone FROM persona INNER JOIN personal ON persona.id = personal.id_persona WHERE personal.tipo =1;");
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                Propietario p = new Propietario();
                p.setId(resultset.getInt("id"));
                p.setNombre(resultset.getString("nombre"));
                p.setApellidos(resultset.getString("apellidos"));
                p.setFechaNacimiento(resultset.getString("fechaNacimiento"));
                p.setDni(resultset.getString("dni"));
                p.setCorreo(resultset.getString("correo"));
                p.setPhone(resultset.getString("phone"));
                listaPropietarios.add(p);
            }
            
        
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaPropietarios;
        
        
    }
    
    
    public boolean eliminarPropietario(String correo) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM persona WHERE correo = ?;");
            
            ps.setString(1, correo);
            
            int resultado = ps.executeUpdate();            
            
            if(resultado == 1){
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }
    
    public boolean addLudoteca(String nombre, String direccion, String telefono, String latitude, String longitude, String id) {
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO ludotecas (nombre, direccion, latitude, longitude, telefono, fecha_creacion, id_propietario) VALUES (?,?,?,?,?,CURDATE(),?);");
            
            ps.setString(1, nombre);
            ps.setString(2, direccion);
            ps.setString(3, latitude);
            ps.setString(4, longitude);
            ps.setString(5, telefono);

            ps.setInt(6, Integer.parseInt(id));
            
            int resp = ps.executeUpdate();
            
            if(resp > 0){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return false;
    }
    
    
    public boolean eliminarLudoteca(String idLudoteca) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM ludotecas WHERE id = ?;");
            
            ps.setString(1, idLudoteca);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1) {
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return false;
        
    }    
    
    public ArrayList<Ludoteca> obtenerLudotecas() {
        
        ArrayList<Ludoteca> listLudotecas = new ArrayList<>();
        
        try {
            //Opcion pasando por personal
            PreparedStatement ps = conexion.prepareStatement("SELECT ludotecas.id, ludotecas.nombre, direccion, telefono, fecha_creacion, persona.id, persona.nombre FROM ludotecas INNER JOIN propietario ON ludotecas.id_propietario = propietario.id INNER JOIN personal ON propietario.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id;");
            
            //Opcion saltando personal y tomando referencia a persona directamene porque es lo mismo
            //SELECT ludotecas.id, ludotecas.nombre, direccion, telefono, fecha_creacion, persona.nombre FROM ludotecas INNER JOIN propietario ON ludotecas.id_propietario = propietario.id INNER JOIN persona ON propietario.id = persona.id;
                    
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Ludoteca l = new Ludoteca();
                
                l.setId(resultset.getInt("ludotecas.id"));
                l.setNombre(resultset.getString("ludotecas.nombre"));
                l.setDireccion(resultset.getString("direccion"));
                l.setPhone(resultset.getString("telefono"));
                l.setFecha_creacion(resultset.getDate("fecha_creacion"));
                l.setId_propietario(resultset.getInt("persona.id"));
                l.setNombre_propietario(resultset.getString("persona.nombre"));
                listLudotecas.add(l);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return listLudotecas;
    }
    
    public ArrayList<Ludoteca> obtenerLudotecasPropietarios(String idPropietario) {
    
        ArrayList<Ludoteca> listaLudotecaPropietario = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre, direccion, longitude, latitude, telefono, fecha_creacion, id_propietario FROM ludotecas WHERE id_propietario = ?; ");
            
            ps.setString(1, idPropietario);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Ludoteca l = new Ludoteca();
                
                l.setId(resultset.getInt("id"));
                l.setNombre(resultset.getString("nombre"));
                l.setDireccion(resultset.getString("direccion"));
                l.setLatitude(resultset.getDouble("latitude"));
                l.setLongitude(resultset.getDouble("longitude"));
                l.setPhone(resultset.getString("telefono"));
                l.setFecha_creacion(resultset.getDate("fecha_creacion"));
                l.setId_propietario(resultset.getInt("id_propietario"));
                
                listaLudotecaPropietario.add(l);
            
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaLudotecaPropietario;
    }
    
    public ArrayList<Empleado> obtenerEmpleadosLudoteca(String idLudoteca) {
    
        ArrayList<Empleado> listaEmpleadosLudoteca = new ArrayList<>();
        
        try{
        
        
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, nombre, apellidos, fechaNacimiento, dni, phone, correo, user, password from persona INNER JOIN personal ON persona.id = personal.id_persona INNER JOIN empleados on personal.id_persona = empleados.id WHERE personal.tipo = 2 AND empleados.id_ludoteca = ?;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Empleado e = new Empleado();
                
                e.setId(resultset.getInt("persona.id"));
                e.setNombre(resultset.getString("nombre"));
                e.setApellidos(resultset.getString("apellidos"));
                e.setFechaNacimiento(resultset.getString("fechaNacimiento"));
                e.setDni(resultset.getString("dni"));
                e.setPhone(resultset.getString("phone"));
                e.setCorreo(resultset.getString("correo"));
                e.setUser(resultset.getString("user"));
                e.setPassword(resultset.getString("password"));
                
                listaEmpleadosLudoteca.add(e);
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return listaEmpleadosLudoteca;
    }

    
    public ArrayList<Propietario> obtenerPropietario(String argumento) {
        ArrayList<Propietario> listaPropietarios = new ArrayList<>();
        try{
            
            boolean resultado;
            
            try{
                Integer.parseInt(argumento);
                resultado= true;
            }catch (NumberFormatException e){
                resultado=false;
            }
            
            PreparedStatement ps;
            
            if(resultado){
            
                ps = conexion.prepareStatement("SELECT * FROM persona INNER JOIN personal ON persona.id = personal.id_persona WHERE personal.tipo = 1 AND persona.id = ?");
                
            } else {
                
                ps = conexion.prepareStatement("SELECT * FROM persona INNER JOIN personal ON persona.id = personal.id_persona WHERE personal.tipo = 1 AND persona.correo = ?");
            }

            ps.setString(1, argumento);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Propietario p = new Propietario();
                p.setId(resultset.getInt("id"));
                p.setNombre(resultset.getString("nombre"));
                p.setApellidos(resultset.getString("apellidos"));
                p.setFechaNacimiento(resultset.getString("fechaNacimiento"));
                p.setDni(resultset.getString("dni"));
                p.setCorreo(resultset.getString("correo"));
                p.setPhone(resultset.getString("phone"));
                p.setUser(resultset.getString("user"));
                p.setPassword(resultset.getString("password"));
                p.setTipo(resultset.getInt("tipo"));
                listaPropietarios.add(p);
            }
            
            
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPropietarios;
    }

    
    public boolean modificarPropietario(String id, String nombre, String apellidos, String fechaNacimiento, String dni, String phone, String correo, String user, String password) {

        try{
            PreparedStatement ps = conexion.prepareStatement("UPDATE persona SET nombre = ?, apellidos = ?, fechaNacimiento = ?, dni = ?, phone = ?, correo = ?, user = ?, password = ?WHERE id = ?;");
        
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaNacimiento);
            ps.setString(4, dni);
            ps.setString(5, phone);
            ps.setString(6, correo);
            ps.setString(7, user);
            ps.setString(8, password);
            ps.setString(9, id);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }

    public ArrayList<Sala> obtenerSalas(String idLudoteca) {
    
        ArrayList<Sala> listaSalas = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("Select id, nombre, aforo_maximo, id_ludoteca FROM salas WHERE id_ludoteca = ?;");
            
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Sala s = new Sala();
                s.setId(resultset.getInt("id"));
                s.setNombre(resultset.getString("nombre"));
                s.setAforo_maximo(resultset.getInt("aforo_maximo"));
                s.setId_ludoteca(resultset.getInt("id_ludoteca"));
                listaSalas.add(s);
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaSalas;
        
    }
    
    public boolean addSala(String nombreSala, String aforo, String idLudoteca) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO salas (nombre, aforo_maximo, id_ludoteca) VALUES (?, ?, ?)");
            
            ps.setString(1, nombreSala);
            ps.setString(2, aforo);
            ps.setString(3, idLudoteca);
            
            int resp = ps.executeUpdate();
            
            if (resp > 0){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }    
    
    public boolean updateSala(String idSala, String nombre, String aforo) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("UPDATE salas set nombre = ?, aforo_maximo = ? WHERE id = ?;");
            
            ps.setString(1, nombre);
            ps.setString(2, aforo);
            ps.setString(3, idSala);
            
            int resultado = ps.executeUpdate();
            if(resultado == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    
    public boolean eliminarSala(String idSala) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM salas WHERE id = ?;");
            
            ps.setString(1, idSala);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }
    
    public boolean addEmpleado(String nombre, String apellidos, String fechaNacimiento, String dni, String phone, String user, String password, String correo, String idLudoteca) {
    
        try {
        
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, phone, user, password, correo) VALUES (?,?,?,?,?,?,?,?); ");
            
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaNacimiento);
            ps.setString(4, dni);
            ps.setString(5, phone);
            ps.setString(6, user);
            ps.setString(7, password);
            ps.setString(8, correo);
            
            int resp = ps.executeUpdate();
            
            if(resp > 0) {
            
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC Limit 1;");
                
                int id;
                
                resultset = ps1.executeQuery();
                
                while(resultset.next()){
                
                    id = resultset.getInt("id");
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO personal (id_persona, tipo) VALUES (?,?);");
                    PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO empleados (id, id_ludoteca) VALUES (?, ?);");
                    
                    ps2.setInt(1, id);
                    ps2.setInt(2, 2);
                    ps3.setInt(1, id);
                    ps3.setString(2, idLudoteca);
                    
                    
                    int resp1 = ps2.executeUpdate();
                    int resp2 = ps3.executeUpdate();
                    
                    if(resp1 > 0 && resp2 > 0){
                        return true;
                    }
                }
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public boolean updateEmpleado(String nombre, String apellidos, String fechaN, String dni, String phone, String user, String password, String correo, String idEmpleado) {
     
        try{
        
            PreparedStatement ps = conexion.prepareStatement("UPDATE persona set nombre = ?, apellidos = ?, fechaNacimiento = ?, dni = ?, user = ?, password = ?, phone = ?, correo = ? WHERE id = ?;");
            
            
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaN);
            ps.setString(4, dni);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phone);
            ps.setString(8, correo);
            ps.setString(9, idEmpleado);
            
            int resultado = ps.executeUpdate();
            if(resultado == 1){
                return true;
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean eliminarEmpleado(String idEmpleado) {
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM personal WHERE id_persona = ?;");
            
            ps.setString(1, idEmpleado);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return false;
    }

    
    public ArrayList<Cliente> obtenerClientes(String idLudoteca) {
       
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, persona.nombre, persona.apellidos, persona.fechaNacimiento, persona.dni, persona.user, persona.password, persona.phone, persona.correo, pre_inscripcion.estado, tutor.id, tutor.nombre, tutor.apellidos, tutor.fechaNacimiento, tutor.dni, tutor.phone, tutor.codSeguridad FROM persona INNER JOIN cliente ON persona.id = cliente.id_persona INNER JOIN pre_inscripcion ON pre_inscripcion.id_cliente = cliente.id_persona LEFT JOIN tutor ON cliente.id_persona = tutor.id_cliente INNER JOIN ludotecas_clientes ON cliente.id_persona = ludotecas_clientes.idCliente WHERE pre_inscripcion.estado = 1 AND ludotecas_clientes.idLudoteca = ?;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Cliente c= new Cliente();
                c.setId(resultset.getInt("persona.id"));
                c.setNombreC(resultset.getString("persona.nombre"));
                c.setApellidosC(resultset.getString("persona.apellidos"));
                c.setFechaNacimiento(resultset.getString("persona.fechaNacimiento"));
                c.setDniC(resultset.getString("persona.dni"));
                c.setPhoneC(resultset.getString("persona.phone"));
                c.setCorreoC(resultset.getString("persona.correo"));
                c.setUserC(resultset.getString("persona.user"));
                c.setPasswordC(resultset.getString("persona.password"));
                c.setEstado(resultset.getInt("pre_inscripcion.estado"));
                c.setIdTutor(resultset.getInt("tutor.id"));
                c.setNombreT(resultset.getString("tutor.nombre"));
                c.setApellidosT(resultset.getString("tutor.apellidos"));
                c.setFechaNacimientoTutor(resultset.getString("tutor.fechaNacimiento"));
                c.setDniT(resultset.getString("tutor.dni"));
                c.setPhoneT(resultset.getInt("tutor.phone"));
                c.setCodSeguridad(resultset.getInt("tutor.codSeguridad"));
                listaClientes.add(c);                        
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaClientes;
    }
    
    public boolean addClienteMayor(String nombre, String apellidos, String fechaN, String dni, String phone, String user, String password, String correo, String idLudoteca) {
        
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, user, password, phone, correo) VALUES (?,?,?,?,?,?,?,?);");
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaN);
            ps.setString(4, dni);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phone);
            ps.setString(8, correo);
            
            int resp = ps.executeUpdate();
            
            
            if( resp > 0) {
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC Limit 1;");
                
                int idCliente;
                resultset = ps1.executeQuery();
                
                while(resultset.next()) {
                
                    idCliente = resultset.getInt("id");
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO cliente (id_persona) VALUES (?);");
                    PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO pre_inscripcion (fecha, estado, id_ludoteca, id_cliente) VALUES (NOW(),1,?,?);");
                    PreparedStatement ps4 = conexion.prepareStatement("INSERT INTO ludotecas_clientes (idLudoteca, idCliente) VALUES (?, ?);");
                    
                    ps2.setInt(1, idCliente);
                    ps3.setString(1, idLudoteca);
                    ps3.setInt(2, idCliente);
                    ps4.setString(1, idLudoteca);
                    ps4.setInt(2, idCliente);
                    
                    int resp2 = ps2.executeUpdate();
                    int resp3 = ps3.executeUpdate();
                    int resp4 = ps4.executeUpdate();
                    
                    if( resp2 > 0 && resp3 > 0 && resp4 > 0){
                        return true;
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
        
    }
    
    
    public boolean updateClienteMayor(String nombre, String apellidos, String fechaN, String dni, String phone, String user, String password, String correo, String idCliente) {
    
        try {
            PreparedStatement ps = conexion.prepareStatement("UPDATE persona set nombre = ?, apellidos = ?, fechaNacimiento = ?, dni = ?, user = ?, password = ?, phone = ?, correo = ? WHERE id = ?;");
            
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaN);
            ps.setString(4, dni);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phone);
            ps.setString(8, correo);
            ps.setString(9, idCliente);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean addClienteMenor(String nombreC, String apellidosC, String fechaC, String dniC, String phoneC, String user, String password, String correo, String nombreT, String apellidoT, String fechaT, String dniT, String phoneT, String codSeguridad, String idLudoteca) {
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, user, password, phone, correo) VALUES (?,?,?,?,?,?,?,?);");
            
            ps.setString(1, nombreC);
            ps.setString(2, apellidosC);
            ps.setString(3, fechaC);
            ps.setString(4, dniC);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phoneC);
            ps.setString(8, correo);
            
            int resp = ps.executeUpdate();
            
            if( resp > 0 ){
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC Limit 1;");
                
                int idCliente;
                resultset = ps1.executeQuery();
                
                while(resultset.next()){
                    idCliente = resultset.getInt("id");
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO cliente (id_persona) VALUES (?);");
                    PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO pre_inscripcion (fecha, estado, id_ludoteca, id_cliente) VALUES (NOW(),1,?,?);");
                    PreparedStatement ps4 = conexion.prepareStatement("INSERT INTO ludotecas_clientes (idLudoteca, idCliente) VALUES (?, ?);");
                    PreparedStatement ps5 = conexion.prepareStatement("INSERT INTO tutor (nombre, apellidos, fechaNacimiento, dni, phone, codSeguridad, id_cliente) values (?, ?, ?, ?, ?, ?, ?);");
                    
                    ps2.setInt(1, idCliente);
                    ps3.setString(1, idLudoteca);
                    ps3.setInt(2, idCliente);
                    ps4.setString(1, idLudoteca);
                    ps4.setInt(2, idCliente);
                    
                    ps5.setString(1, nombreT);
                    ps5.setString(2, apellidoT);
                    ps5.setString(3, fechaT);
                    ps5.setString(4, dniT);
                    ps5.setString(5, phoneT);
                    ps5.setString(6, codSeguridad);
                    ps5.setInt(7, idCliente);
                    
                    
                    int resp2 = ps2.executeUpdate();
                    int resp3 = ps3.executeUpdate();
                    int resp4 = ps4.executeUpdate();
                    int resp5 = ps5.executeUpdate();
                    
                    if( resp2 > 0 && resp3 > 0 && resp4 > 0 && resp5 > 0){
                        return true;
                    }
                }
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public boolean updateClienteMenor(String nombreC, String apellidosC, String fechaC, String dniC, String phoneC, String user, String password, String correo, String nombreT, String apellidoT, String fechaT, String dniT, String phoneT, String codSeguridad, String idCliente, String idTutor) {
    
        try {
        
            PreparedStatement ps = conexion.prepareStatement("UPDATE persona SET nombre = ?, apellidos = ?, fechaNacimiento = ?, dni = ?, user = ?, password = ?, phone = ?, correo = ? WHERE id = ?;");
            
            ps.setString(1, nombreC);
            ps.setString(2, apellidosC);
            ps.setString(3, fechaC);
            ps.setString(4, dniC);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phoneC);
            ps.setString(8, correo);
            ps.setString(9, idCliente);
            
            PreparedStatement ps1 = conexion.prepareStatement("UPDATE tutor SET nombre = ?, apellidos = ?, fechaNacimiento = ?, dni = ?, phone = ?, codSeguridad = ? WHERE id = ?");
            
            
            
            ps1.setString(1, nombreT);
            ps1.setString(2, apellidoT);
            ps1.setString(3, fechaT);
            ps1.setString(4, dniT);
            ps1.setString(5, phoneT);
            ps1.setString(6, codSeguridad);
            ps1.setString(7, idTutor);
            
            int resultado1 = ps.executeUpdate();
            int resultado2 = ps1.executeUpdate();
            
            if (resultado1 == 1 && resultado2 == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public boolean eliminarCliente(String idCliente, String idLudoteca) {
    
        try {
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM ludotecas_clientes WHERE idLudoteca = ? AND idCliente = ?;");
            
            ps.setString(1, idLudoteca);
            ps.setString(2, idCliente);
            
            int resultado = ps.executeUpdate();
            
            if ( resultado == 1){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public ArrayList<String> obtenerInscripciones(String idLudoteca) {
        
        ArrayList<String> lista = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT pre_inscripcion.fecha, pre_inscripcion.estado,persona.id, persona.nombre, persona.apellidos, persona.dni, persona.phone, tutor.id, tutor.nombre, tutor.dni FROM pre_inscripcion INNER JOIN cliente ON pre_inscripcion.id_cliente = cliente.id_persona INNER JOIN persona ON cliente.id_persona = persona.id LEFT JOIN tutor ON cliente.id_persona = tutor.id_cliente WHERE pre_inscripcion.id_ludoteca = ? AND pre_inscripcion.estado = 0;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                String mensaje = "";
                
                mensaje += resultset.getString("pre_inscripcion.fecha")+"--";
                mensaje += resultset.getString("pre_inscripcion.estado")+"--";
                mensaje += resultset.getString("persona.id")+"--";
                mensaje += resultset.getString("persona.nombre")+"--";
                mensaje += resultset.getString("persona.apellidos")+"--";
                mensaje += resultset.getString("persona.dni")+"--";
                mensaje += resultset.getString("persona.phone")+"--";
                mensaje += resultset.getString("tutor.id")+"--";
                mensaje += resultset.getString("tutor.nombre")+"--";
                mensaje += resultset.getString("tutor.dni");
                
                lista.add(mensaje);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    
    }
    
    
    public boolean validarInscripcion(String idCliente) {
        
        String ACCOUNT_SID = "AC2690cdc4a1b427602de5f7baa79b0618";
        String AUTH_TOKEN = "6f70bad0497beaf3d8ad7f284bc4cc76";
        
        try{
            
            PreparedStatement ps = conexion.prepareStatement("UPDATE pre_inscripcion SET estado = 1 WHERE id_cliente = ?;");
            
            ps.setString(1, idCliente);
            
            int resultado =  ps.executeUpdate();
            
            if( resultado == 1){
                
                return true;
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }
    
    public ArrayList<String> obtenerTiposActividades(String idLudoteca) {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try{
            
            
            PreparedStatement ps = conexion.prepareStatement("SELECT tipoactividad.id, tipoactividad.nombre, tipoactividad.precio, categorias.nombre FROM tipoactividad INNER JOIN categorias ON tipoactividad.idCategoria = categorias.id WHERE tipoactividad.idLudoteca = ?;");

            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                String mensaje = "";
                
                mensaje += resultset.getString("tipoactividad.id")+"--";
                mensaje += resultset.getString("tipoactividad.nombre")+"--";
                mensaje += resultset.getString("tipoactividad.precio")+"--";
                mensaje += resultset.getString("categorias.nombre");
                
                lista.add(mensaje);
                        
            }
  
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;       
        
    }
    public ArrayList<String> obtenerCategorias() {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre FROM categorias ORDER BY id ASC;");
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje = "";
                mensaje += resultset.getString("id")+"--";
                mensaje += resultset.getString("nombre");
                
                lista.add(mensaje);
            }
            
            
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public ArrayList<String> obtenerSalasT(String idLudoteca) {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre FROM salas WHERE id_ludoteca = ? ORDER BY id ASC;");
            
            ps.setString(1, idLudoteca);
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje = "";
                mensaje += resultset.getString("id")+"--";
                mensaje += resultset.getString("nombre");
                
                lista.add(mensaje);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
    public ArrayList<String> obtenerEmpleadosT(String idLudoteca) {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("SELECT empleados.id, nombre, apellidos FROM empleados INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id WHERE empleados.id_ludoteca = ?;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje ="";
                mensaje += resultset.getString("id")+"--";
                mensaje += resultset.getString("nombre")+ "--";
                mensaje += resultset.getString("apellidos");
                lista.add(mensaje);
            }
                    
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    
    public boolean addTipoActividad(String nombre, String precio, String idCategoria, String idLudoteca) {
      
        try {
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO tipoactividad (nombre, precio, idCategoria, idLudoteca) VALUES (?, ?, ?, ?);");
            
            ps.setString(1, nombre);
            ps.setString(2, precio);
            ps.setString(3, idCategoria);
            ps.setString(4, idLudoteca);           
            
            int resp = ps.executeUpdate();
            
            if( resp > 0){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
 
    }

    public String obtenerTipoById(String idTipo) {
    
        String mensaje ="";
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT tipoactividad.nombre, precio, categorias.nombre FROM tipoactividad INNER JOIN categorias ON categorias.id = tipoactividad.idCategoria WHERE tipoactividad.id = ?;");
            
            ps.setString(1, idTipo);

            resultset = ps.executeQuery();
            
            while(resultset.next()){
                mensaje += resultset.getString("tipoactividad.nombre")+"--";
                mensaje += resultset.getString("precio")+"--";
                mensaje += resultset.getString("categorias.nombre");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(mensaje);
        return mensaje;
    }
    
    public ArrayList<String> obtenerTiposXLudotecas(String idLudoteca) {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement("SELECT id, nombre FROM tipoactividad WHERE idLudoteca = ?;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje ="";
                mensaje += resultset.getString("id")+"--";
                mensaje += resultset.getString("nombre");
                lista.add(mensaje);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
                
    }

    public boolean updateTipoActividad(String nombre, String precio, String idCategoria, String idTipo) {
    
        try{
        
            PreparedStatement ps = conexion.prepareStatement("UPDATE tipoactividad SET nombre = ?, precio = ?, idCategoria = ? WHERE id = ?;");
            
            ps.setString(1, nombre);
            ps.setString(2, precio);
            ps.setString(3, idCategoria);
            ps.setString(4, idTipo);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
       
    }
    
    public boolean eliminarTActividad(String idTipoActivity) {
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM tipoactividad WHERE id = ?;");
            
            ps.setString(1, idTipoActivity);
            
            int resultado = ps.executeUpdate();
            
            if (resultado == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public ArrayList<String> obtenerPlanningsLudotecas(String idLudoteca) {
        ArrayList<String> lista = new ArrayList<>();
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT planning.id, planning.hora, planning.dias, tipoactividad.nombre, persona.nombre, persona.apellidos, salas.nombre FROM planning INNER JOIN tipoactividad ON planning.idTipo = tipoactividad.id INNER JOIN salas ON salas.id = planning.idSala INNER JOIN empleados ON empleados.id = planning.idEmpleado INNER JOIN personal ON personal.id_persona = empleados.id INNER JOIN persona ON personal.id_persona = persona.id WHERE tipoactividad.idLudoteca=?;");
            
            ps.setString(1, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje = "";
                mensaje += resultset.getString("planning.id")+"--";
                mensaje += resultset.getString("planning.hora")+"--";
                mensaje += resultset.getString("planning.dias")+"--";
                mensaje += resultset.getString("tipoactividad.nombre")+"--";
                mensaje += resultset.getString("persona.nombre")+"--";
                mensaje += resultset.getString("persona.apellidos")+"--";
                mensaje += resultset.getString("salas.nombre");
                lista.add(mensaje);
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;        

        
    }
    public boolean addPlanning(String idTipo, String hora, String dias, String tipo, String idEmpleado,String idSala) {
       
        try{
            
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO planning (hora, dias, tipo, idTipo, idEmpleado, idSala) VALUES (? ,?, ?, ?, ?, ?);");
            
            ps.setString(1, hora);
            ps.setString(2, dias);
            ps.setString(3, tipo);
            ps.setString(4, idTipo);
            ps.setString(5, idEmpleado);
            ps.setString(6, idSala);
            
            int resp = ps.executeUpdate();
            if(resp > 0){
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM planning ORDER BY id DESC LIMIT 1;");
                int idPlanning = -1;
                resultset = ps1.executeQuery();
                while(resultset.next()){
                    idPlanning = resultset.getInt("id");
                }
                
                String [] diasSemana = dias.split("-");
              

                for(String dia : diasSemana){

                    String diaIngles = convertirDiaIngles(dia);

                    LocalDate nextDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.valueOf(diaIngles.toUpperCase())));
                    

                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO activityrecurrente (dia, hora, idEmpleado, idSala, idTipoActividad, idPlanning) VALUES (?, ?, ?, ?, ?, ?);");
                    
                    ps2.setString(1, nextDate.toString());
                    ps2.setString(2, hora);
                    ps2.setString(3, idEmpleado);
                    ps2.setString(4, idSala);
                    ps2.setString(5, idTipo);
                    ps2.setInt(6, idPlanning);
                    
                    ps2.executeUpdate();
                    
                }
                
                return true;
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return false;

    }
    
    private static String convertirDiaIngles(String dia) {
        Map<String, String> diasIngles = new HashMap<>();
        diasIngles.put("lunes", "MONDAY");
        diasIngles.put("martes", "TUESDAY");
        diasIngles.put("miércoles", "WEDNESDAY");
        diasIngles.put("jueves", "THURSDAY");
        diasIngles.put("viernes", "FRIDAY");
        diasIngles.put("sábado", "SATURDAY");
        diasIngles.put("domingo", "SUNDAY");

        return diasIngles.get(dia.toLowerCase());
    }
    
    public boolean updatePlanning(String idTipo, String hora, String dias, String tipo, String idEmpleado, String idSala, String idPlanning) {
    
        try {
        
            PreparedStatement ps = conexion.prepareStatement("UPDATE planning SET hora = ?, dias = ?, tipo = ?, idTIpo = ?, idEmpleado = ?, idSala = ? WHERE id = ?;");
            
            ps.setString(1, hora);
            ps.setString(2, dias);
            ps.setString(3, tipo);
            ps.setString(4, idTipo);
            ps.setString(5, idEmpleado);
            ps.setString(6, idSala);
            ps.setString(7, idPlanning);
            int resp = ps.executeUpdate();
            if (resp > 0){
                
                
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    public boolean eliminarPlanning(String idPlanning) {
        
        try{
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM planning WHERE id = ?;");
            
            ps.setString(1, idPlanning);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }
    
    public void ejecucionPlanning(){   
        try {
        
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM planning;");
            
            resultset = ps.executeQuery();

            
            while(resultset.next()){
                int idPlanning = resultset.getInt("id");
                String hora = resultset.getString("hora");
                String dias = resultset.getString("dias");
                String idTipo = resultset.getString("idTipo");
                String idEmpleado = resultset.getString("idEmpleado");
                String [] diasSemana = dias.split("-");

                for(String dia : diasSemana){
                    String diaIngles = convertirDiaIngles(dia);
                    
                    LocalDate nextDate = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.valueOf(diaIngles.toUpperCase())));
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO activityrecurrente (dia, hora, idEmpleado, idTipoActividad, idPlanning) VALUES (?, ?, ?, ?, ?); ");
                    
                    ps2.setString(1, nextDate.toString());
                    ps2.setString(2, hora);
                    ps2.setString(3, idEmpleado);
                    ps2.setString(4, idTipo);
                    ps2.setInt(5, idPlanning);
                    
                    ps2.executeUpdate(); 
                }
                                                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String obtenerInfoPlanning(String idPlanning) {
        //vamos a devolver tipo,hora,empleado
        String mensaje ="";
        try{
        
            PreparedStatement ps = conexion.prepareStatement("Select hora, tipoactividad.nombre, persona.nombre, persona.apellidos, salas.nombre FROM planning INNER JOIN tipoactividad ON planning.idTipo = tipoactividad.id INNER JOIN empleados ON planning.idEmpleado = empleados.id INNER JOIN personal ON  empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id INNER JOIN salas ON planning.idSala = salas.id WHERE planning.id = ?;");
            
            ps.setString(1, idPlanning);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                mensaje += resultset.getString("hora")+"--";
                mensaje += resultset.getString("tipoactividad.nombre")+"--";
                mensaje += resultset.getString("persona.nombre")+"--";
                mensaje += resultset.getString("persona.apellidos")+"--";
                mensaje += resultset.getString("salas.nombre");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensaje;

    }
    
    public boolean updateActividadRecurrente(String hora, String dia, String idEmpleado,String idSala, String idActividad) {
    
        try{
            PreparedStatement ps = conexion.prepareStatement("UPDATE activityrecurrente SET dia = ?, hora = ?, idEmpleado = ?, idSala = ? WHERE id = ?; ");
            
            ps.setString(1, dia);
            ps.setString(2, hora);
            ps.setString(3, idEmpleado);
            ps.setString(4, idSala);
            ps.setString(5, idActividad);
            
            int resp = ps.executeUpdate();
            
            if(resp >0){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public boolean addActivitidadUnica(String idTipo, String hora, String dia, String idEmpleado, String idSala) {

        
        try{
            int id = -1;
            PreparedStatement ps = conexion.prepareStatement("SELECT id FROM activitynorecurrente WHERE dia = ? AND hora = ? AND idEmpleado = ? AND idSala = ? AND idTipoActivity = ?;");
            
            ps.setString(1, dia);
            ps.setString(2, hora);
            ps.setString(3, idEmpleado);
            ps.setString(4, idSala);
            ps.setString(5, idTipo);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                id = resultset.getInt("id");
            }
            
            if(id != -1){
                return false;
            }else{
            
                PreparedStatement ps1 = conexion.prepareStatement("INSERT INTO activitynorecurrente (dia, hora, idEmpleado, idSala, idTipoActivity  ) VALUES (?, ?, ?, ?, ?); ");
                           
                ps1.setString(1, dia);
                ps1.setString(2, hora);
                ps1.setString(3, idEmpleado);
                ps1.setString(4, idSala);
                ps1.setString(5, idTipo);

                int resp = ps1.executeUpdate();

                if(resp> 0){
                    return true;
                }
                
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    public boolean updateActividadUnica(String dia, String hora, String idEmpleado, String idSala ,String idActividad) {
    
        try{
        
            PreparedStatement ps = conexion.prepareStatement("UPDATE activitynorecurrente SET dia = ?, hora = ?, idEmpleado = ?, idSala = ? WHERE id = ?; ");
            
            ps.setString(1, dia);
            ps.setString(2, hora);
            ps.setString(3, idEmpleado);
            ps.setString(4, idSala);
            ps.setString(5, idActividad);
            
            int resp = ps.executeUpdate();
            if(resp > 0){
                return true;
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;

    }


    
    
    public ArrayList<Actividades> obtenerActividadesXLudoteca(String idLudoteca) {
      
        ArrayList<Actividades> listaActividades = new ArrayList<>();
        
        try {
        
            
            PreparedStatement ps = conexion.prepareStatement("SELECT activitynorecurrente.id , activitynorecurrente.dia, activitynorecurrente.hora, tipoactividad.nombre AS nombreTipo, 1 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala FROM activitynorecurrente INNER JOIN tipoactividad ON activitynorecurrente.idTipoActivity = tipoactividad.id INNER JOIN salas ON activitynorecurrente.idSala = salas.id INNER JOIN empleados ON activitynorecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id WHERE activitynorecurrente.dia > CURDATE() AND tipoactividad.idLudoteca = ? UNION ALL SELECT activityrecurrente.id, activityrecurrente.dia, activityrecurrente.hora, tipoactividad.nombre AS nombreTipo, 0 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala FROM activityrecurrente INNER JOIN tipoactividad ON activityrecurrente.idTipoActividad = tipoactividad.id INNER JOIN salas ON activityrecurrente.idSala = salas.id INNER JOIN empleados ON activityrecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id WHERE activityrecurrente.dia > CURDATE() AND tipoactividad.idLudoteca = ? ORDER BY dia, hora;");
            
            ps.setString(1, idLudoteca);
            ps.setString(2, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                Actividades a = new Actividades();
                
                
                a.setId(resultset.getInt("id"));
                a.setFecha(resultset.getString("dia"));
                a.setHora(resultset.getString("hora"));
                a.setNombreTipo(resultset.getString("nombreTipo"));
                a.setTipo(resultset.getInt("tipo"));
                a.setNombre(resultset.getString("nombre"));
                a.setApellidos(resultset.getString("apellidos"));
                a.setSala(resultset.getString("sala"));
                listaActividades.add(a);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaActividades;

    }

    public boolean eliminarActividadUnica(String idUnica) {
    
        try {
        
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM activitynorecurrente WHERE id = ?;");
            
            ps.setString(1, idUnica);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;

    }
    
    public boolean eliminarActividadPlanificada(String idPlanificada) {
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM activityrecurrente WHERE id = ?;");
            
            ps.setString(1, idPlanificada);
            
            int resultado = ps.executeUpdate();
            
            if(resultado == 1){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        
        return false;
    }
    
    public ArrayList<String> obtenerClientesActividadesPlanificadas(String idActividadPlanificada) {
        
        ArrayList<String> lista = new ArrayList<>();
        
        try{
            
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, persona.nombre, persona.apellidos, persona.phone FROM persona INNER JOIN cliente ON persona.id = cliente.id_persona INNER JOIN cliente_recurrente ON cliente.id_persona = cliente_recurrente.idCliente WHERE cliente_recurrente.idRecurrente = ?;");
            
            ps.setString(1, idActividadPlanificada);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje = "";
                mensaje += resultset.getString("persona.id")+"--";
                mensaje += resultset.getString("persona.nombre")+"--";
                mensaje += resultset.getString("persona.apellidos")+"--";
                mensaje += resultset.getString("persona.phone");
                lista.add(mensaje);
            }
            
            
        
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return lista;        

    }
    
    public ArrayList<String> obtenerClientesActividadesUnicos(String idActividadUnica) {
    
        ArrayList<String> lista = new ArrayList<>();
        
        try{
            
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, persona.nombre, persona.apellidos, persona.phone FROM persona INNER JOIN cliente ON persona.id = cliente.id_persona INNER JOIN cliente_norecurrente ON cliente.id_persona = cliente_norecurrente.idCliente WHERE cliente_norecurrente.idNoRecurrente = ?;");
            
            ps.setString(1, idActividadUnica);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                String mensaje = "";
                mensaje += resultset.getString("persona.id")+"--";
                mensaje += resultset.getString("persona.nombre")+"--";
                mensaje += resultset.getString("persona.apellidos")+"--";
                mensaje += resultset.getString("persona.phone");
                lista.add(mensaje);
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return lista;    
        
    }
    
    public ArrayList<Actividades> obtenerActividadesXEmpleado(String idEmpleado, String idLudoteca) {
     
        ArrayList<Actividades> listaActividades = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT activitynorecurrente.id, dia, hora, tipoactividad.nombre, 1 as tipo From activitynorecurrente INNER JOIN tipoactividad ON activitynorecurrente.idTipoActivity = tipoactividad.id WHERE dia > CURDATE() AND tipoactividad.idLudoteca = ? AND activitynorecurrente.idEmpleado = ? UNION ALL SELECT activityrecurrente.id, dia, planning.hora, tipoactividad.nombre, 0 as tipo FROM activityrecurrente INNER JOIN tipoactividad ON activityrecurrente.idTipoActividad = tipoactividad.id INNER JOIN planning ON activityrecurrente.idPlanning = planning.id WHERE dia > CURDATE() AND tipoactividad.idLudoteca = ? AND activityrecurrente.idEmpleado = ? ORDER BY dia;");
            
            ps.setString(1, idLudoteca);
            ps.setString(2, idEmpleado);
            ps.setString(3, idLudoteca);
            ps.setString(4, idEmpleado);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                Actividades a = new Actividades();
                
                a.setId(resultset.getInt("id"));
                a.setFecha(resultset.getString("dia"));
                a.setHora(resultset.getString("hora"));
                a.setNombre(resultset.getString("nombre"));
                a.setTipo(resultset.getInt("tipo"));
                listaActividades.add(a);
                
            }
            
            
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaActividades;

    }

    public ArrayList<Ludoteca> obtenerLudotecasMapaMovil() {
     
        ArrayList<Ludoteca> listaLudotecas = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT ludotecas.id, ludotecas.nombre, direccion, ludotecas.latitude, ludotecas.longitude, ludotecas.telefono, fecha_creacion, persona.nombre, persona.apellidos FROM ludotecas INNER JOIN propietario ON propietario.id = ludotecas.id_propietario INNER JOIN personal ON personal.id_persona = propietario.id INNER JOIN persona ON personal.id_persona = persona.id;");
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                Ludoteca l = new Ludoteca();
                
                l.setId(resultset.getInt("ludotecas.id"));
                l.setNombre(resultset.getString("ludotecas.nombre"));
                l.setDireccion(resultset.getString("direccion"));
                l.setLatitude(resultset.getDouble("ludotecas.latitude"));
                l.setLongitude(resultset.getDouble("ludotecas.longitude"));
                l.setPhone(resultset.getString("ludotecas.telefono"));
                l.setFecha_creacion(resultset.getDate("fecha_creacion"));
                l.setNombre_propietario(resultset.getString("persona.nombre"));
                l.setApellido_propietario(resultset.getString("persona.apellidos"));
                listaLudotecas.add(l);
            }           
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaLudotecas;
        
    }
    
    
    public boolean addClienteMayorMovil(String nombre, String apellidos, String fechaNacimiento, String dni, String phone, String correo, String user, String password) {
    
        String inputDate = fechaNacimiento;
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        String fechaBD = outputFormat.format(date);
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, user, password, phone, correo) VALUES (?,?,?,?,?,?,?,?);");
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setString(3, fechaBD);
            ps.setString(4, dni);
            ps.setString(5, user);
            ps.setString(6, password);
            ps.setString(7, phone);
            ps.setString(8, correo);
            
            int resp = ps.executeUpdate();
            
            if(resp>0){
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC Limit 1");
                int idCliente;
                resultset = ps1.executeQuery();
                
                while(resultset.next()){
                    idCliente = resultset.getInt("id");
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO cliente (id_persona) VALUES (?);");
                    
                    ps2.setInt(1, idCliente);
                    
                    int resp2 = ps2.executeUpdate();
                    
                    if(resp2 > 0){
                        return true;
                    }
                    
                }
                
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
        
     
    }
    
    
    
    public boolean addClienteMenorMovil(String nombreN, String apellidosN, String fechaNacimientoN, String dniN, String phoneN, String correo, String user, String password, String nombreT, String apellidosT, String fechaNacimientoT, String dniT, String phoneT, String codSeguridad) {
        
        //parseFecha niño
        String inputDate = fechaNacimientoN;
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        Date date = null;
        try {
            date = inputFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        String fechaNiñoBD = outputFormat.format(date);
        
        //parseFecha tutor
        String inputDateTutor = fechaNacimientoT;
        DateFormat inputFormatTutor = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat outputFormatTutor = new SimpleDateFormat("yyyy/MM/dd");
        
        Date dateT = null;
        try {
            dateT = inputFormatTutor.parse(inputDateTutor);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        String fechaTutorBD = outputFormatTutor.format(dateT);

        try{
            PreparedStatement ps = null;
            if(dniN.length() > 0){
                ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, user, password, phone, correo) VALUES (?,?,?,?,?,?,?,?);");

                ps.setString(1, nombreN);
                ps.setString(2, apellidosN);
                ps.setString(3, fechaNiñoBD);
                ps.setString(4, dniN);
                ps.setString(5, user);
                ps.setString(6, password);
                ps.setString(7, phoneN);
                ps.setString(8, correo);
                

            }else {
                ps = conexion.prepareStatement("INSERT INTO persona (nombre, apellidos, fechaNacimiento, dni, user, password, phone, correo) VALUES (?,?,?,NULL,?,?,?,?);");

                ps.setString(1, nombreN);
                ps.setString(2, apellidosN);
                ps.setString(3, fechaNiñoBD);
                
                ps.setString(4, user);
                ps.setString(5, password);
                ps.setString(6, phoneN);
                ps.setString(7, correo);
            }

            int resp = ps.executeUpdate();
            
            if( resp > 0 ){
                PreparedStatement ps1 = conexion.prepareStatement("SELECT id FROM persona ORDER BY id DESC Limit 1;");
                int idCliente;
                resultset = ps1.executeQuery();
                while(resultset.next()){
                    idCliente = resultset.getInt("id");
                    
                    PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO cliente (id_persona) VALUES (?);");
                    PreparedStatement ps3 = conexion.prepareStatement("INSERT INTO tutor (nombre,apellidos,fechaNacimiento,dni,phone,codSeguridad,id_cliente) VALUES (?,?,?,?,?,?,?);");
                    
                    ps2.setInt(1, idCliente);
                    
                    ps3.setString(1, nombreT);
                    ps3.setString(2, apellidosT);
                    ps3.setString(3, fechaTutorBD);
                    ps3.setString(4, dniT);
                    ps3.setString(5, phoneT);
                    ps3.setString(6, codSeguridad);
                    ps3.setInt(7, idCliente);
                    
                    int resp2 = ps2.executeUpdate();
                    int resp3 = ps3.executeUpdate();
                    
                    if(resp2 > 0 && resp3 > 0){
                        return true;
                    }
                }
            
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    
    }
    
       public boolean loginClienteMovil(String usuario, String contraseña) {
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT user, password FROM persona INNER JOIN cliente ON persona.id = cliente.id_persona WHERE user = ? AND password = ?;");
            
            ps.setString(1, usuario);
            ps.setString(2, contraseña);
            
            resultset = ps.executeQuery();
            
            while (resultset.next()){
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return false;
    }

    public Cliente obtenerDatosClienteMovil(String usuario) {
        
        Cliente c = new Cliente();
        
        try {
            PreparedStatement ps = conexion.prepareStatement("SELECT persona.id, persona.nombre, persona.apellidos, persona.fechaNacimiento, persona.dni, persona.user, persona.password, persona.phone, persona.correo, pre_inscripcion.estado, tutor.id, tutor.nombre, tutor.apellidos, tutor.fechaNacimiento, tutor.dni, tutor.phone, tutor.codSeguridad FROM `persona` INNER JOIN cliente ON persona.id = cliente.id_persona LEFT JOIN tutor ON cliente.id_persona = tutor.id_cliente LEFT JOIN pre_inscripcion on cliente.id_persona = pre_inscripcion.id_cliente WHERE user = ?;");
            
            ps.setString(1, usuario);
            
            resultset = ps.executeQuery();
            
            
            while(resultset.next()){
                c.setId(resultset.getInt("persona.id"));
                c.setNombreC(resultset.getString("persona.nombre"));
                c.setApellidosC(resultset.getString("persona.apellidos"));
                c.setFechaNacimiento(resultset.getString("persona.fechaNacimiento"));
                c.setDniC(resultset.getString("persona.dni"));
                c.setPhoneC(resultset.getString("persona.phone"));
                c.setCorreoC(resultset.getString("persona.correo"));
                c.setUserC(resultset.getString("persona.user"));
                c.setPasswordC(resultset.getString("persona.password"));
                
                Integer valueEstado = resultset.getInt("pre_inscripcion.estado");
                int value = (valueEstado != null) ? valueEstado : 0;
                c.setEstado(value);
                
                c.setIdTutor(resultset.getInt("tutor.id"));
                c.setNombreT(resultset.getString("tutor.nombre"));
                c.setApellidosT(resultset.getString("tutor.apellidos"));
                c.setFechaNacimientoTutor(resultset.getString("tutor.fechaNacimiento"));
                c.setDniT(resultset.getString("tutor.dni"));
                c.setPhoneT(resultset.getInt("tutor.phone"));
                c.setCodSeguridad(resultset.getInt("tutor.codSeguridad"));
                                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return c;
    }
    
    public String addInscripcionLudoteca(String idLudoteca, String idCliente) {
        System.out.println("hola desde manejo");
        System.out.println(idLudoteca);
        System.out.println(idCliente);
        try {
            int idPreinscipcion = -1;
            int estado = -1;
            
            PreparedStatement ps = conexion.prepareStatement("SELECT id, estado FROM pre_inscripcion WHERE id_cliente = ?;");
            
            ps.setString(1, idCliente);
            
            resultset = ps.executeQuery();
            
            System.out.println("idRecibido");
            System.out.println(idPreinscipcion);
            while(resultset.next()){
                idPreinscipcion = resultset.getInt("id");
                estado = resultset.getInt("estado");
            }
            
            if(idPreinscipcion != -1){
                //ya existe cliente en pre_inscripcion
                
                if( estado == 1){ //estado = 1
                    System.out.println("Dentro de estado 1");
                    System.out.println(idCliente);
                    int lc = -1;
                    PreparedStatement ps2 = conexion.prepareStatement("SELECT idCliente FROM ludotecas_clientes WHERE idLudoteca = ? AND idCliente = ?; ");

                    ps2.setString(1, idLudoteca);
                    ps2.setString(2, idCliente);

                    resultset = ps2.executeQuery();

                    while(resultset.next()){
                        lc = resultset.getInt("idCliente");
                    }

                    if(lc !=-1){
                        //ya existe en la tabla ludoteca_cliente ese idCliente por tanto ya esta inscrito
                        return "ERROR_INSCRITO--0";
                    }else {
                        PreparedStatement psludoteca = conexion.prepareStatement("INSERT INTO ludotecas_clientes (idLudoteca, idCliente) VALUES (?, ?);");

                        psludoteca.setString(1, idLudoteca);
                        psludoteca.setString(2, idCliente);   

                        psludoteca.executeUpdate();

                        return "CLIENTE_INSCRITO--0";
                    }       
                    
                }else { // estado = 0
                
                    PreparedStatement psLudoteca = conexion.prepareStatement("SELECT ludotecas.nombre FROM ludotecas INNER JOIN pre_inscripcion ON ludotecas.id = pre_inscripcion.id_ludoteca WHERE pre_inscripcion.id_cliente = ?; ");
                    
                    psLudoteca.setString(1, idCliente);
                    resultset = psLudoteca.executeQuery();
                    String nombreLudoteca ="";
                    
                    while(resultset.next()){
                        nombreLudoteca = resultset.getString("ludotecas.nombre");
                    }
                    
                    return "NOCONFIRMADO--"+nombreLudoteca;                    
                }
            
            } else {
                // NO existe en pre_inscripcion
                
                PreparedStatement psPreInscripion = conexion.prepareStatement("INSERT INTO pre_inscripcion (fecha, estado, id_ludoteca, id_cliente) VALUES (CURDATE(),0,?,?);");
                PreparedStatement psInscripcion  = conexion.prepareStatement("INSERT INTO ludotecas_clientes (idLudoteca, idCliente) VALUES (?, ?);");
                
                psPreInscripion.setString(1, idLudoteca);
                psPreInscripion.setString(2, idCliente);
                psInscripcion.setString(1, idLudoteca);
                psInscripcion.setString(2, idCliente);
                
                psPreInscripion.executeUpdate();
                psInscripcion.executeUpdate();
                
                return "ACEPTADOPRE--0";
            
            }
            
            
            
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "0";
    }


    public ArrayList<Actividades> obtenerActividadesLudoteca(String idLudoteca) {

        ArrayList<Actividades> listaActividades = new ArrayList<>();
        
        try {
        
            
            PreparedStatement ps = conexion.prepareStatement("SELECT activitynorecurrente.id , activitynorecurrente.dia, activitynorecurrente.hora, tipoactividad.nombre AS nombreTipo, 1 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala, tipoactividad.precio FROM activitynorecurrente INNER JOIN tipoactividad ON activitynorecurrente.idTipoActivity = tipoactividad.id INNER JOIN salas ON activitynorecurrente.idSala = salas.id INNER JOIN empleados ON activitynorecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id WHERE activitynorecurrente.dia > CURDATE() AND tipoactividad.idLudoteca = ? UNION ALL SELECT activityrecurrente.id, activityrecurrente.dia, activityrecurrente.hora, tipoactividad.nombre AS nombreTipo, 0 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala, tipoactividad.precio FROM activityrecurrente INNER JOIN tipoactividad ON activityrecurrente.idTipoActividad = tipoactividad.id INNER JOIN salas ON activityrecurrente.idSala = salas.id INNER JOIN empleados ON activityrecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id WHERE activityrecurrente.dia > CURDATE() AND tipoactividad.idLudoteca = ? ORDER BY dia, hora;");
            
            ps.setString(1, idLudoteca);
            ps.setString(2, idLudoteca);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
            
                Actividades a = new Actividades();
                
                
                a.setId(resultset.getInt("id"));
                a.setFecha(resultset.getString("dia"));
                a.setHora(resultset.getString("hora"));
                a.setNombreTipo(resultset.getString("nombreTipo"));
                a.setTipo(resultset.getInt("tipo"));
                a.setNombre(resultset.getString("nombre"));
                a.setApellidos(resultset.getString("apellidos"));
                a.setSala(resultset.getString("sala"));
                a.setPrecio(resultset.getInt("precio"));
                listaActividades.add(a);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaActividades;

    }
    
    public String addClienteActividad(String idActividad, String tipo, String idCliente, String idLudoteca) {
        
        System.out.println("hola desde manejo");
        System.out.println(idActividad);
        System.out.println(tipo);
        System.out.println(idCliente);
        System.out.println(idLudoteca);
        
        try{
        
            int idPreinscripcion = -1;
            int estado =-1;
            
            PreparedStatement ps = conexion.prepareStatement("SELECT id, estado FROM pre_inscripcion WHERE id_cliente = ?;");
        
            ps.setString(1, idCliente);
            
            resultset = ps.executeQuery();

            while(resultset.next()){
                idPreinscripcion = resultset.getInt("id");
                estado = resultset.getInt("estado");
            }
            
            if( idPreinscripcion != -1 ) {
            
                if( estado == 0 ) {
                    return "SINVALIDAR";
                } else{
                
                    // SU ESTADO ES 1 POR TANTO ES VALIDO
                    
                    int ld =-1;
                    PreparedStatement ps2 = conexion.prepareStatement("SELECT idLudoteca FROM ludotecas_clientes WHERE idLudoteca = ? AND idCliente = ?; ");
                    
                    ps2.setString(1, idLudoteca);
                    ps2.setString(2, idCliente);
                    
                    resultset = ps2.executeQuery();
                    
                    while(resultset.next()){
                        ld = resultset.getInt("idLudoteca");
                    }
                    
                    if( ld != -1 ){
                        //Comprobado que esta inscrito, ahora comprobamos que queda hueco para inscribirse a la actividad
                        
                        if(Integer.parseInt(tipo) == 1){
                            //actividades Unicas
                            
                            int nEncontrado = -1;
                            
                            PreparedStatement psSiActividad = conexion.prepareStatement("SELECT idCliente FROM cliente_norecurrente WHERE idCliente = ? AND idNoRecurrente = ?; ");
                            
                            psSiActividad.setString(1, idCliente);
                            psSiActividad.setString(2, idActividad);
                            
                            resultset = psSiActividad.executeQuery();
                            
                            while(resultset.next()){
                                nEncontrado = resultset.getInt("idCliente");
                            }
                            
                            if(nEncontrado != -1){
                                return "YAINSCRITO";
                            }
                                                        
                            int nTotal = -1;
                            PreparedStatement psTotalSala = conexion.prepareStatement("SELECT salas.aforo_maximo FROM salas INNER JOIN activitynorecurrente ON activitynorecurrente.idSala = salas.id INNER JOIN tipoactividad ON activitynorecurrente.idTipoActivity = tipoactividad.id WHERE activitynorecurrente.id = ?;");
                            
                            psTotalSala.setString(1, idActividad);
                            
                            resultset = psTotalSala.executeQuery();
                            
                            while(resultset.next()){
                                nTotal = resultset.getInt("salas.aforo_maximo");
                            }
                            
                            int nHuecosOcupados =-1;
                            PreparedStatement psOcupados = conexion.prepareStatement("SELECT COUNT(idCliente) as apuntados FROM cliente_norecurrente WHERE idNoRecurrente =?;");
                            
                            psOcupados.setString(1, idActividad);
                            
                            resultset = psOcupados.executeQuery();
                            
                            while(resultset.next()){
                                nHuecosOcupados = resultset.getInt("apuntados");
                            }
                            
                            int resultado = nTotal - nHuecosOcupados;
                            
                            if( resultado > 0){
                                //QUEDA HUECO
                                return "HUECOLIBRE";                                                                
                            } else {
                                return "SINESPACIO";
                            }
                            
                            
                        }else{
                            //actividades recurrentes
                            
                            int nEncontrado = -1;
                            
                            PreparedStatement psSiActividad = conexion.prepareStatement("SELECT idCliente FROM cliente_recurrente WHERE idCliente = ? AND idRecurrente = ?; ");
                            
                            psSiActividad.setString(1, idCliente);
                            psSiActividad.setString(2, idActividad);
                            
                            resultset = psSiActividad.executeQuery();
                            
                            while(resultset.next()){
                                nEncontrado = resultset.getInt("idCliente");
                            }
                            
                            if(nEncontrado != -1){
                                return "YAINSCRITO";
                            }                            
                            
                            int nTotal = -1;
                            
                            PreparedStatement psTotalSala = conexion.prepareStatement("SELECT salas.aforo_maximo FROM salas INNER JOIN activityrecurrente ON activityrecurrente.idSala = salas.id INNER JOIN tipoactividad ON activityrecurrente.idTipoActividad = tipoactividad.id WHERE activityrecurrente.id = ?;");
                            
                            psTotalSala.setString(1, idActividad);
                            
                            resultset = psTotalSala.executeQuery();
                            
                            while(resultset.next()){
                                nTotal = resultset.getInt("salas.aforo_maximo");
                            }
                            
                            int nHuecosOcupados =-1;
                            
                            PreparedStatement psOcupados = conexion.prepareStatement("SELECT COUNT(idCliente) as apuntados FROM cliente_recurrente WHERE idRecurrente = ?;");
                            
                            psOcupados.setString(1, idActividad);
                            
                            resultset = psOcupados.executeQuery();
                            
                            while(resultset.next()){
                                nHuecosOcupados = resultset.getInt("apuntados");
                            }
                            
                            int resultado = nTotal - nHuecosOcupados;
                            
                            if( resultado > 0){                            
                                //QUEDA HUECO
                                return "HUECOLIBRE";                                  
                            } else {
                                return "SINESPACIO";
                            }
                        }
  
                    } else {
                        return "NOINSCRITOLUDOTECA";
                    }
                }
            
            } else {
                return "SINPREINSCRIPCION";
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return "0";

    }

    public void pagoActividadRealizado(String idActividad, String idTipo, String idCliente) {
                       
        if (Integer.parseInt(idTipo) == 1) {
            //unica

            try{
                
                PreparedStatement ps = conexion.prepareStatement("INSERT INTO cliente_norecurrente (idCliente, idNoRecurrente) VALUES (?, ?);");
                PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO pago (idCliente, idRecurrente, idNoRecurrente) VALUES (?,NULL,?);");
                
                ps.setString(1, idCliente);
                ps.setString(2, idActividad);
                ps2.setString(1, idCliente);
                ps2.setString(2, idActividad);
                
                ps.executeUpdate();
                ps2.executeUpdate();
                
            
            } catch (SQLException ex) {
                Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //recurrente

            try{
                
                PreparedStatement ps = conexion.prepareStatement("INSERT INTO cliente_recurrente (idCliente, idRecurrente) VALUES (?, ?);");
                PreparedStatement ps2 = conexion.prepareStatement("INSERT INTO pago (idCliente, idRecurrente, idNoRecurrente) VALUES (?, ?, NULL);");
                
                ps.setString(1, idCliente);
                ps.setString(2, idActividad);
                ps2.setString(1, idCliente);
                ps2.setString(2, idActividad);
                
                ps.executeUpdate();
                ps2.executeUpdate();
                
            
            } catch (SQLException ex) {
                Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public ArrayList<Actividades> obtenerActividadesClienteInscritas(String idCliente) {
    
        ArrayList<Actividades> listaActividades = new ArrayList<>();
        
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT activitynorecurrente.id , activitynorecurrente.dia, activitynorecurrente.hora, tipoactividad.nombre AS nombreTipo, 1 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala FROM activitynorecurrente INNER JOIN tipoactividad ON activitynorecurrente.idTipoActivity = tipoactividad.id INNER JOIN salas ON activitynorecurrente.idSala = salas.id INNER JOIN empleados ON activitynorecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id INNER JOIN cliente_norecurrente ON activitynorecurrente.id = cliente_norecurrente.idNoRecurrente  WHERE activitynorecurrente.dia > CURDATE() AND cliente_norecurrente.idCliente = ?  UNION ALL SELECT activityrecurrente.id, activityrecurrente.dia, activityrecurrente.hora, tipoactividad.nombre AS nombreTipo, 0 AS tipo, persona.nombre, persona.apellidos, salas.nombre AS sala FROM activityrecurrente INNER JOIN tipoactividad ON activityrecurrente.idTipoActividad = tipoactividad.id INNER JOIN salas ON activityrecurrente.idSala = salas.id INNER JOIN empleados ON activityrecurrente.idEmpleado = empleados.id INNER JOIN personal ON empleados.id = personal.id_persona INNER JOIN persona ON personal.id_persona = persona.id INNER JOIN cliente_recurrente ON cliente_recurrente.idRecurrente = activityrecurrente.id WHERE activityrecurrente.dia > CURDATE() AND cliente_recurrente.idCliente = ? ORDER BY dia, hora;");
        
            
            ps.setString(1, idCliente);
            ps.setString(2, idCliente);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Actividades a = new Actividades();
                
                a.setId(resultset.getInt("id"));
                a.setFecha(resultset.getString("dia"));
                a.setHora(resultset.getString("hora"));
                a.setNombreTipo(resultset.getString("nombreTipo"));
                a.setTipo(resultset.getInt("tipo"));
                a.setNombre(resultset.getString("nombre"));
                a.setApellidos(resultset.getString("apellidos"));
                a.setSala(resultset.getString("sala"));
                listaActividades.add(a);
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaActividades;
        
    }
    
    public boolean abandonarActividad(String idActividad, String idTipo, String idCliente) {
        
  
        if (Integer.parseInt(idTipo) == 1){

            try{
                PreparedStatement ps = conexion.prepareStatement("DELETE FROM cliente_norecurrente WHERE idCliente = ? AND idNoRecurrente = ?;");
                PreparedStatement ps2 = conexion.prepareStatement("DELETE FROM pago WHERE idCliente = ? AND idNoRecurrente = ?;");
                
                ps.setString(1, idCliente);
                ps.setString(2, idActividad);
                
                ps2.setString(1, idCliente);
                ps2.setString(2, idActividad);
                
                int resultado1 = ps.executeUpdate();
                int resultado2 = ps2.executeUpdate();
                
                if(resultado1 == 1 && resultado2 == 1){
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try{
                PreparedStatement ps = conexion.prepareStatement("DELETE FROM cliente_recurrente WHERE idCliente = ? AND idRecurrente = ?;");
                PreparedStatement ps2 = conexion.prepareStatement("DELETE FROM pago WHERE idCliente = ? AND idRecurrente = ?;");
                
                ps.setString(1, idCliente);
                ps.setString(2, idActividad);
                
                ps2.setString(1, idCliente);
                ps2.setString(2, idActividad);
                
                int resultado1 = ps.executeUpdate();
                int resultado2 = ps2.executeUpdate();
                
                if(resultado1 == 1 && resultado2 == 1){
                    return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
            }

            
        }
        
        return false;

    }
    
    public int obtenerEstadoCliente(String idCliente) {
        int estado =0;
        try{
        
            PreparedStatement ps = conexion.prepareStatement("SELECT estado FROM pre_inscripcion INNER JOIN persona ON persona.id = pre_inscripcion.id_cliente WHERE persona.id = ?;");
            
            ps.setString(1, idCliente);
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                estado = resultset.getInt("estado");
            }                                   
            
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return estado;
    }
    
    
    public ArrayList<Ludoteca> obtenerListaMapaLudotecas() {
        
        ArrayList<Ludoteca> listaLudotecas = new ArrayList<>();
        
        try {
        
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM ludotecaprueba");
            
            resultset = ps.executeQuery();
            
            while(resultset.next()){
                Ludoteca l = new Ludoteca();
                l.setId(resultset.getInt("id"));
                l.setNombre(resultset.getString("nombre"));
                l.setLongitude(resultset.getDouble("longitude"));
                l.setLatitude(resultset.getDouble("latitude"));
                listaLudotecas.add(l);
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(ManejoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaLudotecas;
        
    }







}
