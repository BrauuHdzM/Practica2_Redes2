/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author brauu
 */
public class Servidor {
    public static void main(String[] args) throws SocketException{
        int pto=1234;
        DatagramSocket s = new DatagramSocket(pto);
        s.setReuseAddress(true);
        while(true){
            try{  
                
                String msj="";
                
               // s.setBroadcast(true);
                System.out.println("Servidor iniciado... espedando datagramas..");
                byte[] b=new byte[65535];
                DatagramPacket p = new DatagramPacket(b,b.length);
                s.receive(p);
                msj = new String(p.getData(),0,p.getLength());
                System.out.println("Se ha recibido datagrama desde "+p.getAddress()+":"+p.getPort()+" con el mensaje:"+msj);
                
                enviarCatalogo();
                getCarrito();
            }catch(Exception e){
                e.printStackTrace();
            }//catch
        }
    }
    
    public static void enviarCatalogo(){
        int puerto = 8000;
        DatagramPacket dp= null;
        DatagramSocket c = null;
        ObjectOutputStream oos=null;
        ByteArrayOutputStream bos=null;
        Catalogo catalogo =null;

        try{
            c = new DatagramSocket();
            dp = new DatagramPacket(new byte[1024],1024);
            InetAddress direccion = InetAddress.getByName("127.0.0.1");
            dp.setAddress(direccion);
            dp.setPort(puerto);
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            byte[] buf= new byte[1024];
            catalogo = new Catalogo();
            oos.writeObject(catalogo);
            oos.flush();
            buf = bos.toByteArray();
            dp.setData(buf);
            c.send(dp);
            oos.close();
        }catch(Exception e){System.err.println(e);}
      System.out.println("Termina el contenido del datagrama...");
    }
    
    public static Carrito getCarrito(){
        int puerto = 8000;
        DatagramPacket dp= null;
        DatagramSocket s = null;
        //ObjectOutputStream oos=null;
        ObjectInputStream ois = null;
        //ByteArrayInputStream bis;
        Carrito carrito =null;

        try{
            s = new DatagramSocket(puerto);
            System.out.println("Servidor UDP iniciado en el puerto "+s.getLocalPort());
            System.out.println("Recibiendo datos...");
            
            dp = new DatagramPacket(new byte[1024],1024);
            s.receive(dp);
            System.out.println("Datagrama recibido... extrayendo informaciĂłn");
            System.out.println("Host remoto:"+dp.getAddress().getHostAddress()+":"+dp.getPort());
            System.out.println("Datos del paquete:");
            ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            carrito = (Carrito)ois.readObject();
            ois.close();
            
            s.close();
            
        }catch(IOException | ClassNotFoundException e){System.err.println(e);}
        System.out.println("Termina el contenido del datagrama...");
        return carrito;

}
}

