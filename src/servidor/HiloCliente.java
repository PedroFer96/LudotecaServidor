/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Actividades;
import models.Administrador;
import models.Cliente;
import models.Empleado;
import models.Ludoteca;
import models.Propietario;
import models.Sala;
import utils.ManejoDB;
import utils.Mensajes;

/**
 *
 * @author pedro
 */
public class HiloCliente extends Thread{
    
    private Socket socket;
    private Servidor servidor;
    private BufferedReader entrada;
    private PrintWriter salida;
    private ManejoDB manejodb;
    
    public HiloCliente(Socket socket, Servidor servidor){
        
        manejodb = new ManejoDB();
        this.socket = socket;
        this.servidor = servidor;
        
        try{
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        } catch (IOException ex) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run(){
        System.out.println("Hilo cliente lanzado");
        try{
        
            String received="";
            String flag="";
            String[] args;
            
            
            while(true){
                try{
                    received = entrada.readLine();
                    System.out.println(received);
                } catch (IOException ex) {
                    Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
                
               
                args = received.split("--");    
                flag = args[0];

                if(flag.equals(Mensajes.PETICION_LOGIN)){                                                           
                    
                    int tipo = manejodb.loginTipo(args[1],args[2]);
                    
                    if(tipo == 0){
                        // login admin
                        
                        Administrador ad = manejodb.loginAdmin(args[1], args[2]);
                        
                        salida.println(Mensajes.PETICION_LOGIN_CORRECTO_ADMIN + "--" + ad.getId() + "--" + ad.getNombre() + "--" + ad.getApellidos());
                        
                    }else if(tipo == 1){
                        //login Propietario
                        
                        Propietario pr = manejodb.loginPropietario(args[1], args[2]);
                                                                        
                        salida.println(Mensajes.PETICION_LOGIN_CORRECTO_DUEÑO + "--" + pr.getId() + "--" + pr.getNombre() + "--" + pr.getApellidos() + "--" + pr.getFechaNacimiento() + "--" + pr.getDni() + "--" + pr.getCorreo()  + "--" + pr.getPhone() + "--" + pr.getUser() + "--" + pr.getPassword() + "--" + pr.getTipo() );
                    
                    }else if(tipo == 2){
                    
                        //login empleado
                        
                        Empleado em = manejodb.loginEmpleado(args[1],args[2]);
                         
                        salida.println(Mensajes.PETICION_LOGIN_CORRECTO_EMPLEADO + "--" + em.getId() + "--" + em.getNombre() + "--" + em.getApellidos() + "--" + em.getFechaNacimiento() + "--" + em.getDni() + "--" + em.getPhone() + "--" + em.getUser() + "--" + em.getPassword() + "--" + em.getCorreo() + "--" + em.getIdLudoteca());
                        
                    }else{
                        salida.println(Mensajes.PETICION_LOGIN_ERROR);
                        System.out.println("Login Error");
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_NOMBRE_PROPIETARIO)){
                    
                    Propietario pr = manejodb.nombrePropietario(args[1]);
                    
                    salida.println(pr.getNombre() + "--" + pr.getApellidos());
                } else if (flag.equals(Mensajes.PETICION_INFORMACION_PROPIETARIO)){
                    
                    Propietario pr = manejodb.informacionPropietario(args[1]);
                    
                    salida.println(pr.getId() + "--" + pr.getNombre() + "--" + pr.getApellidos() + "--" + pr.getFechaNacimiento() + "--" + pr.getDni() + "--" + pr.getCorreo() + "--" + pr.getPhone() + "--" + pr.getUser() + "--" + pr.getPassword() + "--" + pr.getTipo());
            
                } else if (flag.equals(Mensajes.PETICION_PERFIL_EMPLEADO)) {
                    
                    Empleado em = manejodb.informacionEmpleado(args[1]);
                    
                    salida.println(Mensajes.PETICION_PERFIL_EMPLEADO + "--" + em.getId() + "--" + em.getNombre() + "--" + em.getApellidos() + "--" + em.getFechaNacimiento() + "--" + em.getDni() + "--" + em.getPhone() + "--" + em.getUser() + "--" + em.getPassword() + "--" + em.getCorreo() + "--" + em.getIdLudoteca());
                
                }else if (flag.equals(Mensajes.PETICION_CREAR_PROPIETARIO)){
                    
                    if(manejodb.addPropietario(args[1],args[2],args[3],args[4],args[5],args[6],args[7], args[8])){
                        salida.println(Mensajes.PETICION_CREAR_PROPIETARIO_CORRECTA);
                    }else{
                        salida.println(Mensajes.PETICION_CREAR_PROPIETARIO_ERROR);
                    }

                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_PROPIETARIOS)){
                    
                    ArrayList<Propietario> listPropietarios = manejodb.obtenerPropietarios();
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_PROPIETARIOS_CORRECTO + "--" + listPropietarios.size());
                    
                    for(int i = 0; i<listPropietarios.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_PROPIETARIOS_CORRECTO + "--" + listPropietarios.get(i).getId() + "--" + listPropietarios.get(i).getNombre() + "--" + listPropietarios.get(i).getApellidos() + "--" + listPropietarios.get(i).getFechaNacimiento() + "--" + listPropietarios.get(i).getDni() + "--" + listPropietarios.get(i).getCorreo() + "--" +listPropietarios.get(i).getPhone());
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_PROPIETARIO)){
                    boolean confirmacion = manejodb.eliminarPropietario(args[1]);
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_PROPIETARIO_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_PROPIETARIO_ERROR);
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_CREAR_LUCOTECA)){
                    
                    if(manejodb.addLudoteca(args[1], args[2], args[3], args[4], args[5], args[6])){
                        salida.println(Mensajes.PETICION_CREAR_LUDOTECA_CORRECTA);
                    }else{
                        salida.println(Mensajes.PETICION_CREAR_LUDOTECA_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_LUDOTECAS)){
                    
                    ArrayList<Ludoteca> listaLudoteca = manejodb.obtenerLudotecas();
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_LUDOTECAS_CORRECTA + "--"+ listaLudoteca.size());
                    
                    for(int i = 0; i< listaLudoteca.size(); i++){                        
                        salida.println(Mensajes.PETICION_MOSTRAR_LUDOTECAS_CORRECTA + "--" + listaLudoteca.get(i).getId() + "--" + listaLudoteca.get(i).getNombre() + "--" + listaLudoteca.get(i).getDireccion() + "--" + listaLudoteca.get(i).getPhone() + "--" + listaLudoteca.get(i).getFecha_creacion()+ "--"+ listaLudoteca.get(i).getId_propietario() + "--" + listaLudoteca.get(i).getNombre_propietario());
                    }
                
                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_LUDOTECAS_POR_PROPIETARIO)) {
                    
                    ArrayList<Ludoteca> listaLudoteca = manejodb.obtenerLudotecasPropietarios(args[1]);
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_LUDOTECAS_POR_PROPIETARIO_CORRECTA + "--" + listaLudoteca.size());
                    
                    for(int i = 0; i< listaLudoteca.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_LUDOTECAS_POR_PROPIETARIO_CORRECTA + "--" + listaLudoteca.get(i).getId() + "--" + listaLudoteca.get(i).getNombre() + "--" + listaLudoteca.get(i).getDireccion() + "--" + listaLudoteca.get(i).getLatitude() + "--" + listaLudoteca.get(i).getLongitude() + "--" + listaLudoteca.get(i).getPhone() + "--" + listaLudoteca.get(i).getFecha_creacion() + "--" + listaLudoteca.get(i).getId_propietario());                        
                    }
                    
                } else if(flag.equals(Mensajes.PETICION_MOSTRAR_EMPLEADOS_POR_LUDOTECA)){
                    
                    ArrayList<Empleado> listaEmpleadosLudoteca = manejodb.obtenerEmpleadosLudoteca(args[1]);
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_EMPLEADOS_POR_LUDOTECA_CORRECTO + "--" + listaEmpleadosLudoteca.size());
                    for(int i = 0; i < listaEmpleadosLudoteca.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_EMPLEADOS_POR_LUDOTECA_CORRECTO + "--" + listaEmpleadosLudoteca.get(i).getId() + "--" + listaEmpleadosLudoteca.get(i).getNombre() + "--" + listaEmpleadosLudoteca.get(i).getApellidos() + "--" + listaEmpleadosLudoteca.get(i).getFechaNacimiento() +"--" + listaEmpleadosLudoteca.get(i).getDni()  + "--" + listaEmpleadosLudoteca.get(i).getUser() + "--" + listaEmpleadosLudoteca.get(i).getPassword() + "--" + listaEmpleadosLudoteca.get(i).getPhone() + "--" + listaEmpleadosLudoteca.get(i).getCorreo() );
                    }
                
                } else if(flag.equals(Mensajes.PETICION_MOSTRAR_EDITAR_PROPIETARIO)){
                    
                    ArrayList<Propietario>  propietario = manejodb.obtenerPropietario(args[1]);

                    for(int i = 0; i< propietario.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_EDITAR_PROPIETARIO_CORRECTO + "--" + propietario.get(i).getId() + "--" + propietario.get(i).getNombre() + "--" + propietario.get(i).getApellidos() + "--" + propietario.get(i).getFechaNacimiento() + "--" + propietario.get(i).getDni() + "--" + propietario.get(i).getCorreo() + "--" + propietario.get(i).getUser() + "--" + propietario.get(i).getPassword() + "--" + propietario.get(i).getPhone() );
                    }
                
                } else if (flag.equals(Mensajes.PETICION_EDITAR_PROPIETARIO)){
                    
                    boolean modificado = false;
                    
                    modificado = manejodb.modificarPropietario(args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8] ,args[9]);
                    
                    if (modificado){
                        salida.println(Mensajes.PETICION_EDITAR_PROPIETARIO_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_EDITAR_PROPIETARIO_ERROR);
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_LUDOTECA)){
                    
                    boolean confirmacion = manejodb.eliminarLudoteca(args[1]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_LUDOTECA_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_LUDOTECA_ERROR);
                    }
                    
                
                } else if(flag.equals(Mensajes.PETICION_MOSTRAR_SALAS_POR_LUDOTECA)){

                    ArrayList<Sala> listaSalas = manejodb.obtenerSalas(args[1]);
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_SALAS_POR_LUDOTECA_CORRECTO + "--" + listaSalas.size());
                    
                    for(int i = 0; i< listaSalas.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_SALAS_POR_LUDOTECA_CORRECTO + "--" + listaSalas.get(i).getId() + "--" + listaSalas.get(i).getNombre() + "--" + listaSalas.get(i).getAforo_maximo() + "--" + listaSalas.get(i).getId_ludoteca() );
                    }

                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_SALA)){
                
                    boolean confirmacion = manejodb.eliminarSala(args[1]);                   
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_SALA_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_SALA_ERROR);
                    }
                
                
                } else if (flag.equals(Mensajes.PETICION_CREAR_SALA)){                    
                    
                    if (manejodb.addSala(args[1], args[2], args[3])){
                        salida.println(Mensajes.PETICION_CREAR_SALA_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_SALA_ERROR);
                    }
           
                } else if (flag.equals(Mensajes.PETICION_UPDATE_SALA)) {
                    
                    if (manejodb.updateSala(args[1], args[2], args[3])){
                        salida.println(Mensajes.PETICION_UPDATE_SALA_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_UPDATE_SALA_ERROR);
                    }
            
            
                } else if (flag.equals(Mensajes.PETICION_CREAR_EMPLEADO)) {
                
                    System.out.println(args);
                    if(manejodb.addEmpleado(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])){
                        salida.println(Mensajes.PETICION_CREAR_EMPLEADO_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_EMPLEADO_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_UPDATE_EMPLEADO)) {
                    
                    
                    boolean confirmacion = manejodb.updateEmpleado(args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_UPDATE_EMPLEADO_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_UPDATE_EMPLEADO_ERROR);
                    }
                
                }else if (flag.equals(Mensajes.PETICION_ELIMINAR_EMPLEADO)){
                
                    
                    boolean confirmacion = manejodb.eliminarEmpleado(args[1]);
                    
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_EMPLEADO_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_EMPLEADO_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_CLIENTES_POR_LUDOTECA)){
                    
                    ArrayList<Cliente> listaClientes = manejodb.obtenerClientes(args[1]);
                
                    
                    salida.println(Mensajes.PETICION_MOSTRAR_CLIENTES_POR_LUDOTECA_CORRECTO + "--" + listaClientes.size());
                    
                    for(int i = 0; i< listaClientes.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_CLIENTES_POR_LUDOTECA + "--" + listaClientes.get(i).getId() + "--" + listaClientes.get(i).getNombreC() + "--" + listaClientes.get(i).getApellidosC() + "--" + listaClientes.get(i).getFechaNacimiento() + "--" + listaClientes.get(i).getDniC() + "--" + listaClientes.get(i).getPhoneC() + "--" + listaClientes.get(i).getCorreoC() + "--" + listaClientes.get(i).getUserC() + "--" + listaClientes.get(i).getPasswordC() + "--" + listaClientes.get(i).getEstado() + "--" + listaClientes.get(i).getIdTutor() + "--" + listaClientes.get(i).getNombreT() + "--" + listaClientes.get(i).getApellidosT() +"--" + listaClientes.get(i).getFechaNacimientoTutor() + "--" + listaClientes.get(i).getDniT()+ "--" + listaClientes.get(i).getPhoneT() + "--" + listaClientes.get(i).getCodSeguridad() );                        
                    }
                
                } else if (flag.equals(Mensajes.PETICION_CREAR_CLIENTE_MAYOR) ){               

                    if(manejodb.addClienteMayor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])){
                        salida.println(Mensajes.PETICION_CREAR_CLIENTE_MAYOR_CORRECTA);
                    }else{
                        salida.println(Mensajes.PETICION_CREAR_CLIENTE_MAYOR_ERROR);
                    }
                
                } else if(flag.equals(Mensajes.PETICION_UPDATE_CLIENTE_MAYOR)) {
                
                    if (manejodb.updateClienteMayor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])){
                        salida.println(Mensajes.PETICION_UPDATE_CLIENTE_MAYOR_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_UPDATE_CLIENTE_MAYOR_ERROR);
                    }

                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_CLIENTE)) {
                
                    boolean confirmacion = manejodb.eliminarCliente(args[1], args[2]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_CLIENTE_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_ELIMINAR_CLIENTE_ERROR);
                    }
                    
                } else if(flag.equals(Mensajes.PETICION_CREAR_CLIENTE_MENOR)){
                    
                    if(manejodb.addClienteMenor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])){
                        salida.println(Mensajes.PETICION_CREAR_CLIENTE_MENOR_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_CLIENTE_MENOR_ERROR);
                    }
                
                } else if(flag.equals(Mensajes.PETICION_UPDATE_CLIENTE_MENOR)) {
                    
                    if(manejodb.updateClienteMenor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16])){
                        salida.println(Mensajes.PETICION_UPDATE_CLIENTE_MENOR_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_UPDATE_CLIENTE_MENOR_ERROR);
                    }
                
                
                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_PREINSCRIPCION_POR_LUDOTECA)) {
                    
                    ArrayList<String> listadoInscripciones = manejodb.obtenerInscripciones(args[1]);

                    salida.println(Mensajes.PETICION_MOSTRAR_PREINSCRIPCION_POR_LUDOTECA_CORRECTO + "--" + listadoInscripciones.size());
                    
                    for (int i = 0 ; i < listadoInscripciones.size(); i++) {                    
                        salida.println(Mensajes.PETICION_MOSTRAR_PREINSCRIPCION_POR_LUDOTECA_CORRECTO + "--" + listadoInscripciones.get(i));
                    }
                
                    
                } else if (flag.equals(Mensajes.PETICION_VALIDAR_INSCRIPCION)) {
                
                    if(manejodb.validarInscripcion(args[1])){
                        salida.println(Mensajes.PETICION_VALIDAR_INSCRIPCION_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_VALIDAR_INSCRIPCION_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_MOSTRAR_TIPOS_ACTIVIDADES)){
                    
                    ArrayList<String> listaTipos = manejodb.obtenerTiposActividades(args[1]);

                    salida.println(Mensajes.PETICION_MOSTRAR_TIPOS_ACTIVIDADES_CORRECTO + "--" + listaTipos.size());
                    
                    for (int i = 0; i< listaTipos.size(); i++){
                        salida.println(Mensajes.PETICION_MOSTRAR_TIPOS_ACTIVIDADES_CORRECTO + "--" + listaTipos.get(i));
                    }
                
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_CATEGORIAS)) {

                    ArrayList<String> listaCategorias = manejodb.obtenerCategorias();
                    
                    salida.println(Mensajes.PETICION_OBTENER_CATEGORIAS + "--" + listaCategorias.size());
                    for(int i = 0; i< listaCategorias.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_CATEGORIAS + "--" + listaCategorias.get(i));
                    }
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_SALAS)) {
                    
                    ArrayList<String> listaSalas = manejodb.obtenerSalasT(args[1]);

                    salida.println(Mensajes.PETICION_OBTENER_CATEGORIAS + "--" + listaSalas.size());
                    for(int i = 0; i< listaSalas.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_SALAS + "--" + listaSalas.get(i));
                    }

                } else if (flag.equals(Mensajes.PETICION_OBTENER_EMPLEADOS_TIPO_ACTIVITY)) {
                    
                    ArrayList<String> listaEmpleados = manejodb.obtenerEmpleadosT(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_EMPLEADOS_TIPO_ACTIVITY+ "--"+ listaEmpleados.size());
                    for(int i = 0; i< listaEmpleados.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_EMPLEADOS_TIPO_ACTIVITY + "--" + listaEmpleados.get(i));
                    }
                    
                    
                } else if (flag.equals(Mensajes.PETICION_CREAR_NUEVO_TIPO_ACTIVIDAD)) {
                
                    if(manejodb.addTipoActividad(args[1], args[2], args[3], args[4])){
                        salida.println(Mensajes.PETICION_CREAR_NUEVO_TIPO_ACTIVIDAD_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_NUEVO_TIPO_ACTIVIDAD_ERROR);
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_OBTENER_TIPO)){
                
                    String tipo = manejodb.obtenerTipoById(args[1]);
                    salida.println(Mensajes.PETICION_OBTENER_TIPO + "--" + tipo);
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_TIPO_X_LUDOTECA)) {
                    
                    ArrayList<String> listaTipoLudoteca = manejodb.obtenerTiposXLudotecas(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_TIPO_X_LUDOTECA+ "--"+ listaTipoLudoteca.size());
                    
                    for(int i = 0; i< listaTipoLudoteca.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_TIPO_X_LUDOTECA + "--" + listaTipoLudoteca.get(i));
                    }
                
                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_TIPO_ACTIVIDAD)) {
                    
                    boolean confirmacion = manejodb.eliminarTActividad(args[1]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_TIPO_ACTIVIDAD_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_TIPO_ACTIVIDAD_ERROR);
                    }                                   
                
                }else if (flag.equals(Mensajes.PETICION_UPDATE_TIPO_ACTIVIDAD)){
                
                    if (manejodb.updateTipoActividad(args[1], args[2], args[3], args[4])){
                        salida.println(Mensajes.PETICION_UPDATE_TIPO_ACTIVIDAD_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_UPDATE_TIPO_ACTIVIDAD_ERROR);
                    }                                    
                
                } else if (flag.equals(Mensajes.PETICION_CREAR_PLANNING)) {
                    
                    if(manejodb.addPlanning(args[1], args[2], args[3], args[4], args[5], args [6])){
                        salida.println(Mensajes.PETICION_CREAR_PLANNING_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_PLANNING_ERROR);
                    }
          
                
                } else if(flag.equals(Mensajes.PETICION_UPDATE_PLANNING)){
                
                    boolean confirmaction = manejodb.updatePlanning(args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
                    
                    if(confirmaction){
                        salida.println(Mensajes.PETICION_UPDATE_PLANNING_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_UPDATE_PLANNING_ERROR);
                    }
                        
                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_PLANNING)) {
                    
                    boolean confirmacion = manejodb.eliminarPlanning(args[1]);
                
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_PLANNING_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ELIMINAR_PLANNING_ERROR);
                    }                   
                
                }else if (flag.equals(Mensajes.PETICION_OBTENER_PLANNINGS_X_LUDOTECA)) {
                    
                    ArrayList<String> listaPlanningLudoteca =  manejodb.obtenerPlanningsLudotecas(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_PLANNINGS_X_LUDOTECA_CORRECTO + "--" + listaPlanningLudoteca.size());
                    
                    for(int i = 0; i< listaPlanningLudoteca.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_PLANNINGS_X_LUDOTECA_CORRECTO + "--" + listaPlanningLudoteca.get(i));
                    }

                } else if (flag.equals(Mensajes.PETICION_OBTENER_INFO_PLANNING)){
       
                    String planning = manejodb.obtenerInfoPlanning(args[1]);
                    salida.println(Mensajes.PETICION_OBTENER_INFO_PLANNING + "--" + planning);
                    
                
                } else if (flag.equals(Mensajes.PETICION_UPDATE_ACTIVIDAD_RECURRENTE)) {
                    
                    boolean confirmacion = manejodb.updateActividadRecurrente(args[1],args[2],args[3], args[4],args[5]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_UPDATE_ACTIVIDAD_RECURRENTE_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_UPDATE_ACTIVIDAD_RECURRENTE_ERROR);
                    }  
                
                } else if (flag.equals(Mensajes.PETICION_CREAR_ACTIVIDAD_UNICA)) {
                    
                    if(manejodb.addActivitidadUnica(args[1],args[2], args[3], args[4], args[5])){
                        salida.println(Mensajes.PETICION_CREAR_ACTIVIDAD_UNICA_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_CREAR_ACTIVIDAD_UNICA_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_UPDATE_ACTIVIDAD_UNICA)){
                    
                    boolean confirmacion = manejodb.updateActividadUnica(args[1],args[2], args[3], args[4], args[5]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_UPDATE_ACTIVIDAD_UNICA_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_UPDATE_ACTIVIDAD_UNICA_ERROR);
                    }                           
                
                }else if (flag.equals(Mensajes.PETICION_OBTENER_ALL_ACTIVIDADES_X_LUDOTECA)) {
                
                    ArrayList<Actividades> listaActividades = manejodb.obtenerActividadesXLudoteca(args[1]);
                       
                    salida.println(Mensajes.PETICION_OBTENER_ALL_ACTIVIDADES_X_LUDOTECA+ "--" + listaActividades.size());
                    
                    for(int i = 0; i< listaActividades.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_ALL_ACTIVIDADES_X_LUDOTECA + "--" + listaActividades.get(i).getId() + "--" + listaActividades.get(i).getHora() + "--" + listaActividades.get(i).getFecha() + "--" + listaActividades.get(i).getNombreTipo()+ "--" + listaActividades.get(i).getTipo() + "--" + listaActividades.get(i).getNombre() + "--" + listaActividades.get(i).getApellidos() + "--" + listaActividades.get(i).getSala());
                    }

                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_UNICA)){
                    
                    boolean confirmacion = manejodb.eliminarActividadUnica(args[1]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_UNICA_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_UNICA_ERROR);
                    }

                
                } else if (flag.equals(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_PLANIFICADA)) {
                
                    boolean confirmacion = manejodb.eliminarActividadPlanificada(args[1]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_PLANIFICADA_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_ELIMINAR_ACTIVIDAD_PLANIFICADA_ERROR);
                    }
                
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_PLANIFICADAS)) {
                
                    ArrayList<String> listaClientesPlanificadas = manejodb.obtenerClientesActividadesPlanificadas(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_PLANIFICADAS + "--" + listaClientesPlanificadas.size());
                    
                    for(int i = 0; i<listaClientesPlanificadas.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_PLANIFICADAS + "--" + listaClientesPlanificadas.get(i)) ;
                    }
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_UNICAS)) {
                    
                     ArrayList<String> listaClientesUnicos = manejodb.obtenerClientesActividadesUnicos(args[1]);
                     
                     salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_UNICAS + "--" + listaClientesUnicos.size());
                     
                     for(int i = 0; i< listaClientesUnicos.size(); i++){
                         salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_CLIENTES_UNICAS + "--" + listaClientesUnicos.get(i)) ;
                     }
                     
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ACTIVIDADES_X_EMPLEADO)){
                    
                    ArrayList<Actividades> listaActividades = manejodb.obtenerActividadesXEmpleado(args[1],args[2]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_X_EMPLEADO + "--" + listaActividades.size());
                    
                    for(int i = 0; i<listaActividades.size(); i++) {
                        salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_X_EMPLEADO + "--" + listaActividades.get(i).getId() + "--" + listaActividades.get(i).getHora() + "--" + listaActividades.get(i).getFecha() + "--" + listaActividades.get(i).getNombre() + "--" + listaActividades.get(i).getTipo() );
                    }

                
                } else if (flag.equals(Mensajes.PETICION_MAPA_LUDOTECAS_MOVIL)){
                    
                    ArrayList<Ludoteca> listaLudotecas = manejodb.obtenerLudotecasMapaMovil();
                    
                    salida.println(Mensajes.PETICION_MAPA_LUDOTECAS_MOVIL_CORRECTA + "--" + listaLudotecas.size());
                    
                    for ( int i = 0 ; i< listaLudotecas.size(); i++){
                        salida.println(listaLudotecas.get(i).getId() + "--" + listaLudotecas.get(i).getNombre() + "--" + listaLudotecas.get(i).getDireccion() + "--" + listaLudotecas.get(i).getLatitude() + "--" + listaLudotecas.get(i).getLongitude() + "--" + listaLudotecas.get(i).getPhone() + "--" + listaLudotecas.get(i).getFecha_creacion() + "--" +listaLudotecas.get(i).getNombre_propietario() + "--" + listaLudotecas.get(i).getApellido_propietario()  );
                    }                                       
                
                } else if (flag.equals(Mensajes.PETICION_REGISTRO_MAYOR_MOVIL)) {
                    
                    if(manejodb.addClienteMayorMovil(args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8])){
                        salida.println(Mensajes.PETICION_REGISTRO_MAYOR_MOVIL_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_REGISTRO_MAYOR_MOVIL_ERROR);
                    }            
                
                
                } else if(flag.equals(Mensajes.PETICION_REGISTRO_MENOR_MOVIL)){
                    
                    if(manejodb.addClienteMenorMovil(args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8],args[9],args[10],args[11],args[12],args[13],args[14])){
                        salida.println(Mensajes.PETICION_REGISTRO_MENOR_MOVIL_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_REGISTRO_MENOR_MOVIL_ERROR);
                    }
                
                } else if(flag.equals(Mensajes.PETICION_LOGIN_CLIENTE_MOVIL)){
                    
                    if(manejodb.loginClienteMovil(args[1], args[2])){                        
                        Cliente c = manejodb.obtenerDatosClienteMovil(args[1]);
                        salida.println(Mensajes.PETICION_LOGIN_CLIENTE_MOVIL_CORRECTO + "--" + c.getId() + "--" + c.getNombreC() + "--" + c.getApellidosC() + "--" + c.getFechaNacimiento() + "--" + c.getDniC() + "--" + c.getPhoneC() + "--" + c.getCorreoC() + "--" + c.getUserC() + "--" + c.getPasswordC() + "--" + c.getEstado() + "--" + c.getIdTutor() + "--" + c.getNombreT() + "--" + c.getApellidosT() + "--" + c.getFechaNacimientoTutor() + "--" + c.getDniT() + "--" + c.getPhoneT() + "--" + c.getCodSeguridad());
                        
                    } else {
                        System.out.println("Error login Cliente");
                        salida.println("PETICION_LOGIN_ERROR");
                    }
                    
                } else if (flag.equals(Mensajes.PETICION_INSCRIBIRSE_LUDOTECA_MOVIL)) {
                
                    String confirmacion = manejodb.addInscripcionLudoteca(args[1],args[2]);
                    
                    String [] mensaje = confirmacion.split("--");
                    String respuesta = mensaje[0];
                    
                    switch (respuesta) {
                        case "ERROR_INSCRITO":                            
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_LUDOTECA_MOVIL_ERROR_INSCRITO);                                                        
                            break;
                        case "CLIENTE_INSCRITO" :
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_LUDOTECA_MOVIL_CORRECTO_INSCRITO);
                            break;
                        case "NOCONFIRMADO":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_LUDOTECA_MOVIL_ERROR_NO_CONFIRMADO + "--" + mensaje[1]);
                            break;                            
                        case "ACEPTADOPRE":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_LUDOTECA_MOVIL_CORRECTO_PREINSCRITO);
                            break;
                        default:
                            throw new AssertionError();
                    }
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ACTIVIDADES_LUDOTECA_MOVIL)){
                
                    ArrayList<Actividades> listaActividades = manejodb.obtenerActividadesLudoteca(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_LUDOTECA_MOVIL_CORRECTO + "--" + listaActividades.size());
                    
                    for(int i = 0; i< listaActividades.size() ; i++){
                        salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_LUDOTECA_MOVIL_CORRECTO + "--" + listaActividades.get(i).getId() + "--" + listaActividades.get(i).getHora() + "--" + listaActividades.get(i).getFecha() + "--" + listaActividades.get(i).getNombreTipo()+ "--" + listaActividades.get(i).getTipo() + "--" + listaActividades.get(i).getNombre() + "--" + listaActividades.get(i).getApellidos() + "--" + listaActividades.get(i).getSala() + "--" + listaActividades.get(i).getPrecio());
                    }

                } else if (flag.equals(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL)){
                    
                    String confirmacion = manejodb.addClienteActividad(args[1],args[2],args[3],args[4]);
                    
                    String [] mensaje = confirmacion.split("--");
                    String respuesta = mensaje[0];
                    
                    switch (respuesta) {
                        case "SINPREINSCRIPCION":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_ERROR_PREINSCRITO);
                            break;
                        case "SINVALIDAR":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_ERROR_VALIDAR);
                            break;
                        case "NOINSCRITOLUDOTECA":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_ERROR_INSCRIPCIONLUDOTECA);
                            break;
                        case "YAINSCRITO":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_ERROR_INSCRIPCIONACTIVIDAD);
                            break;
                        case "SINESPACIO":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_ERROR_SINESPACIO);
                            break;
                        case "HUECOLIBRE":
                            salida.println(Mensajes.PETICION_INSCRIBIRSE_ACTIVIDAD_LUDOTECA_MOVIL_CORRECTO);
                            break;    
                        default:
                            throw new AssertionError();
                    }
                
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ACTIVIDADES_INSCRITAS_MOVIL)) {
                    
                    ArrayList<Actividades> listaActividades = manejodb.obtenerActividadesClienteInscritas(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_INSCRITAS_MOVIL_CORRECTO + "--" + listaActividades.size());
                    
                    for(int i = 0; i< listaActividades.size(); i++){
                        salida.println(Mensajes.PETICION_OBTENER_ACTIVIDADES_INSCRITAS_MOVIL_CORRECTO + "--" + listaActividades.get(i).getId() + "--" + listaActividades.get(i).getHora() + "--" + listaActividades.get(i).getFecha() + "--" + listaActividades.get(i).getNombreTipo()+ "--" + listaActividades.get(i).getTipo() + "--" + listaActividades.get(i).getNombre() + "--" + listaActividades.get(i).getApellidos() + "--" + listaActividades.get(i).getSala());
                    }
                    
                
                } else if (flag.equals(Mensajes.PETICION_PAGO_ACTIVIDAD_REALIZADA_MOVIL)) {
                    
                    manejodb.pagoActividadRealizado(args[1], args[2], args[3]);
                                
                } else if (flag.equals(Mensajes.PETICION_ABANDONAR_ACTIVIDAD_MOVIL)) {
                    
                    boolean confirmacion = manejodb.abandonarActividad(args[1], args[2], args[3]);
                    
                    if(confirmacion){
                        salida.println(Mensajes.PETICION_ABANDONAR_ACTIVIDAD_MOVIL_CORRECTO);
                    }else{
                        salida.println(Mensajes.PETICION_ABANDONAR_ACTIVIDAD_MOVIL_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_OBTENER_ESTADO_CLIENTE_MOVIL)) {
                    
                    int estado = manejodb.obtenerEstadoCliente(args[1]);
                    
                    salida.println(Mensajes.PETICION_OBTENER_ESTADO_CLIENTE_MOVIL + "--" + estado);
                
                
                } else if (flag.equals(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MAYOR_MOVIL)) {
                
                    if (manejodb.updateClienteMayor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])){
                        salida.println(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MAYOR_MOVIL_CORRECTO);
                    } else {
                        salida.println(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MAYOR_MOVIL_ERROR);
                    }
                
                } else if (flag.equals(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MENOR_MOVIL)){
                
                    if(manejodb.updateClienteMenor(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16])){
                        salida.println(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MENOR_MOVIL_CORRECTO);
                    }else {
                        salida.println(Mensajes.PETICION_ACTUALIZAR_CLIENTE_MENOR_MOVIL_ERROR);
                    }
                
                }else if (flag.equals(Mensajes.PETICION_MAPA_LUDOTECAS)){ // peticion mapa de pruebas de la app, luego no valdra o habra que reformarla

                
                
                }else{
                    System.out.println("mensaje de error");
                }
            }
        }catch (NullPointerException ex){
            System.out.println(ex);
            System.out.println("Cliente Desconectado");
            
            try{
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }catch(Exception e){
            System.out.println("Cliente Desconectado");
        }
    
    }
    
    
}
