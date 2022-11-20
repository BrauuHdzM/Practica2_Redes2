package com.mycompany.practica2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public static void main(String[] args) throws SocketException, IOException {
        int ptoDst=8000;
        String dirDst="127.0.0.1";
        DatagramSocket s = new DatagramSocket(8001);
        s.setReuseAddress(true);
        
        boolean varSalir = true;
        
        while(varSalir){
            conexion(ptoDst, dirDst, s);
            Catalogo catalogo = getCatalogo(s);    
            Carrito carrito = new Carrito();
            boolean varSalir2 =true;
            while(varSalir2){
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
                        carrito.mostrarCarrito();
                        varSalir2 = menuCarrito(carrito, s);
                    break;
                    case 4:
                        varSalir=false;
                        varSalir2=false;

                }
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
    public static boolean  menuCarrito(Carrito carrito, DatagramSocket s){
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
                carrito.mostrarCarrito();
                break;
            case 2:
                enviarCarrito(carrito, s);
                recibirCancion(carrito, s);
                return false;
                
        }
        return true;
    }
    
    public void descargarCanciones(){
        
    }
    
    public static void conexion(int pto, String dir, DatagramSocket cl) throws UnknownHostException, SocketException, IOException{
        
        InetAddress dst= InetAddress.getByName(dir);
        String msj = "Hola servidor";
        int tam = msj.length();
        byte[]b = msj.getBytes();
        DatagramPacket p= new DatagramPacket(b,b.length,dst,pto);
        cl.send(p);
        //System.out.println("Solicitud de conexion enviada");
    }
    
    public static Catalogo getCatalogo(DatagramSocket s){
        
        DatagramPacket dp= null;
        
        ObjectInputStream ois = null;
        Catalogo catalogo =null;

        try{
            //System.out.println("Recibiendo catalogo...");
            
            dp = new DatagramPacket(new byte[1024],1024);
            s.receive(dp);
            
            ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            catalogo = (Catalogo)ois.readObject();
            catalogo.mostrarCatalogo();
            ois.close();
            
            
        }catch(IOException | ClassNotFoundException e){System.err.println(e);}
        //System.out.println("Catalogo enviado...");
        return catalogo;
    }
    
    public static void enviarCarrito(Carrito carrito, DatagramSocket c){
        int puerto = 8000;
        DatagramPacket dp= null;
        
        ObjectOutputStream oos=null;
        ByteArrayOutputStream bos=null;

        try{
            
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
      //System.out.println("Carrito enviado...");
      
      
    }
    
    public static void recibirCancion(Carrito carrito, DatagramSocket socket){
        try{
            //DatagramSocket socket = new DatagramSocket(8050);
            byte[] receiveFileName = new byte[1024]; 
            for(int a =0; a<carrito.Canciones.size();a++){
                DatagramPacket receiveFileNamePacket = new DatagramPacket(receiveFileName, receiveFileName.length);
                socket.receive(receiveFileNamePacket); 

                byte [] data = receiveFileNamePacket.getData(); 
                String fileName = new String(data, 0, receiveFileNamePacket.getLength()); 

                File f = new File ("libreria\\" + fileName); 
                FileOutputStream outToFile = new FileOutputStream(f);
                System.out.println("-------------------------");
                System.out.println("Descargando: "+fileName);
                receiveFile(outToFile, socket);
                //System.out.println("-------------------------");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    
    private static void receiveFile(FileOutputStream outToFile, DatagramSocket socket) throws IOException {
        
        boolean flag; 
        int sequenceNumber = 0; 
        int foundLast = 0; 
        
        while (true) {
            byte[] message = new byte[1024];
            byte[] fileByteArray = new byte[1021]; 

            
            DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
            socket.receive(receivedPacket);
            message = receivedPacket.getData(); 

            InetAddress address = receivedPacket.getAddress();
            int port = receivedPacket.getPort();

            
            sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
            
            flag = (message[2] & 0xff) == 1;
            
            if (sequenceNumber == (foundLast + 1)) {

                
                foundLast = sequenceNumber;
                System.arraycopy(message, 3, fileByteArray, 0, 1021);

                outToFile.write(fileByteArray);
                //System.out.println("Recibido: paquete num:" + foundLast);

                // Envio ack
                sendAck(foundLast, socket, address, port);
            } else {
                
                // Reenvio de ack
                sendAck(foundLast, socket, address, port);
            }
            
            if (flag) {
                outToFile.close();
                break;
            }
        }
    }    
    
    private static void sendAck(int foundLast, DatagramSocket socket, InetAddress address, int port) throws IOException {
        
        byte[] ackPacket = new byte[2];
        ackPacket[0] = (byte) (foundLast >> 8);
        ackPacket[1] = (byte) (foundLast);
        
        DatagramPacket acknowledgement = new DatagramPacket(ackPacket, ackPacket.length, address, port);
        socket.send(acknowledgement);
        //System.out.println("Enviando ack: paquete num = " + foundLast);
    }
}
    
    
    

