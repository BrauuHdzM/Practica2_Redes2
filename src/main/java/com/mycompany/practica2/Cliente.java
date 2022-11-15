package com.mycompany.practica2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import org.json.simple.parser.ParseException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mauri
 */
public class Cliente {
    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException{
        conexion();
        Catalogo catalogo = getCatalogo();//esta variable debe de ser asignada al catalogo que se reciba del servidor
        Carrito carrito = new Carrito();
        boolean varSalir = true;
        System.out.println("holi ");
        
        while(varSalir){
            switch(menu()){
                case 1:
                    catalogo.mostrarCatalogo();
                break;
                case 2:
                    catalogo.mostrarCatalogo();
                    System.out.println("Ingresa Id de la cancion:");
                    carrito.agregarCancion(catalogo,(new Scanner(System.in).nextInt()));
                    carrito.mostrarCarrito();
                break;
                case 3:
                    menuCarrito(carrito);
                break;
                case 4:
                    varSalir=false;
                 
            
            }
        }
        
    }
    
    public static int  menu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                           Ingrese una opcion:
                           1.Ver catalogo
                           2.Agregar cancion al carrito
                           3.Ver carrito
                           4.Salir""");
        return sc.nextInt();
    }
    public static void  menuCarrito(Carrito carrito){
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                           Ingrese una opcion:
                           1.Eliminar cancion
                           2.Proceder al pago y descargar
                           3.Regresar""");
        
        switch(sc.nextInt()){
            case 1: 
                System.out.println("Ingresa el ID de la cancion a eliminar");
                carrito.eliminarCancion(new Scanner(System.in).nextInt());
                break;
            case 2:
                enviarCarrito(carrito);
                break;
        }
    }
    
    public static void conexion() throws UnknownHostException, SocketException, IOException{
        int pto=1234;
        String dir="127.0.0.1";
        InetAddress dst= InetAddress.getByName(dir);
        InetAddress org= InetAddress.getLocalHost();
        DatagramSocket cl = new DatagramSocket();
        String msj = "Hola servidor";
        int tam = msj.length();
        byte[]b = msj.getBytes();
        DatagramPacket p= new DatagramPacket(b,b.length,dst,pto);
        cl.send(p);
    }
    
    public static Catalogo getCatalogo(){
        int puerto = 8000;
        DatagramPacket dp= null;
        DatagramSocket s = null;
        //ObjectOutputStream oos=null;
        ObjectInputStream ois = null;
        //ByteArrayInputStream bis;
        Catalogo catalogo =null;

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
            catalogo = (Catalogo)ois.readObject();
            catalogo.mostrarCatalogo();
            ois.close();
            
            s.close();
            
        }catch(IOException | ClassNotFoundException e){System.err.println(e);}
        System.out.println("Termina el contenido del datagrama...");
        return catalogo;
    }
    
    public static void enviarCarrito(Carrito carrito){
        int puerto = 8000;
        DatagramPacket dp= null;
        DatagramSocket c = null;
        ObjectOutputStream oos=null;
        ByteArrayOutputStream bos=null;

        try{
            c = new DatagramSocket();
            dp = new DatagramPacket(new byte[1024],1024);
            InetAddress direccion = InetAddress.getByName("127.0.0.1");
            dp.setAddress(direccion);
            dp.setPort(puerto);
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            byte[] buf= new byte[1024];
            oos.writeObject(carrito);
            oos.flush();
            buf = bos.toByteArray();
            dp.setData(buf);
            c.send(dp);
            oos.close();
        }catch(Exception e){System.err.println(e);}
      System.out.println("Termina el contenido del datagrama...");
    }
    }
    
    
    

