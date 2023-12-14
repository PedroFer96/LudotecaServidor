/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ManejoDB;

/**
 *
 * @author pedro
 */
public class Servidor {

    public static final int PORT = 9999;
    private ArrayList<HiloCliente> hilos;
    
   
    public Servidor() {
        hilos = new ArrayList<>();
        

        ManejoDB manejodb = new ManejoDB();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        
        Runnable tarea = () -> {
            System.out.println("ejecucion ");
            try{
                manejodb.ejecucionPlanning();
            }catch (Exception e) {                
                e.printStackTrace();
            }

        };
        
        //scheduler.scheduleAtFixedRate(tarea, 0, 5, TimeUnit.SECONDS); // con esto funciona, si veo que no funciona el metodo de semanal, puedo calcular los segundos de una semanas y usar esto
        

        //metodo semanal
        //Codigo para que se ejecute esta noche 
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime proximoViernes = ahora.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).withHour(1).withMinute(0).withSecond(0);
        long inicialDelay = ahora.until(proximoViernes, ChronoUnit.SECONDS);

        // Programa la tarea para que se ejecute cada semana
        scheduler.scheduleAtFixedRate(tarea, inicialDelay, TimeUnit.DAYS.toSeconds(7), TimeUnit.SECONDS);
        
        
    }
    
    public static void main(String[] args) {
        
        System.out.println("Servidor Iniciado......");
        System.out.println("Esperando cliente");
        Servidor s = new Servidor ();
        s.run();

  
    }

    private void run() {
        
        ServerSocket socketServidor = null;
        
        try{
            socketServidor = new ServerSocket(PORT);
        }catch (IOException e){
            System.out.println("No puede escuchar en el puerto: " + PORT);
            
        }
        
        Socket socketCliente = null;
        
        while(true) {
            try{
                socketCliente = socketServidor.accept();
                System.out.println("Conexión Aceptada " + socketCliente);
                
                HiloCliente h = new HiloCliente ( socketCliente, this);
                h.start();
                hilos.add(h);
                        
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
  
}
