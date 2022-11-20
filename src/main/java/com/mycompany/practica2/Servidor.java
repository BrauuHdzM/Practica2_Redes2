/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 *
 * @author brauu
 */
public class Servidor {
    public static void main(String[] args) throws SocketException{
        
        
        DatagramSocket s = new DatagramSocket(8000);
        s.setReuseAddress(true);
        while(true){
            try{  
                
                
                waitConnection(s);
                enviarCatalogo(s);
                sendCanciones(getCarrito(s));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void waitConnection(DatagramSocket s) throws SocketException, IOException{
        
        String msj="";
        
        System.out.println("Servidor iniciado... espedando conexion..");
        byte[] b=new byte[65535];
        DatagramPacket p = new DatagramPacket(b,b.length);
        s.receive(p);
        msj = new String(p.getData(),0,p.getLength());
        System.out.println("Se ha recibido conexion desde "+p.getAddress()+":"+p.getPort());
    }
    
    public static void enviarCatalogo(DatagramSocket c ){
        int puerto = 8001;
        DatagramPacket dp= null;
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
            System.out.println("Enviando Catalogo...");
            oos.writeObject(catalogo);
            oos.flush();
            buf = bos.toByteArray();
            dp.setData(buf);
            c.send(dp);
            oos.close();
        }catch(Exception e){System.err.println(e);}
      System.out.println("Catalogo enviado");
    }
    
    public static Carrito getCarrito(DatagramSocket s){
        int puerto = 8001;
        DatagramPacket dp= null;
        ObjectInputStream ois = null;
        Carrito carrito =null;

        try{
            System.out.println("Esperando Carrito...");
            
            dp = new DatagramPacket(new byte[1024],1024);
            s.receive(dp);
            ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            carrito = (Carrito)ois.readObject();
            
            ois.close();
            
            
        }catch(IOException | ClassNotFoundException e){System.err.println(e);}
        System.out.println("Carrito recibido");
        return carrito;

}
    
    public static void sendCanciones(Carrito carrito){
        for (int i = 0; i<carrito.Canciones.size();i++){
            try{
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("127.0.0.1");
                String fileName;
                File f = new File(carrito.Canciones.get(i).path);
                fileName = f.getName();
                byte[] fileNameBytes = fileName.getBytes(); 
                DatagramPacket fileStatPacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, address, 8001); // File name packet
                
                socket.send(fileStatPacket); 

                byte[] fileByteArray = readFileToByteArray(f); 
                sendFile(socket, fileByteArray, address, 8001);
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
    
    private static void sendFile(DatagramSocket socket, byte[] fileByteArray, InetAddress address, int port) throws IOException {
        System.out.println("Enviando archivo");
        int sequenceNumber = 0; 
        boolean flag; 
        int ackSequence = 0; 

        for (int i = 0; i < fileByteArray.length; i = i + 1021) {
            sequenceNumber += 1;

            byte[] message = new byte[1024]; 
            message[0] = (byte) (sequenceNumber >> 8);
            message[1] = (byte) (sequenceNumber);

            if ((i + 1021) >= fileByteArray.length) { 
                flag = true;
                message[2] = (byte) (1); // Recibio archivo
            } else {
                flag = false;
                message[2] = (byte) (0); // No recibio archivo
            }

            if (!flag) {
                System.arraycopy(fileByteArray, i, message, 3, 1021);
            } else { 
                System.arraycopy(fileByteArray, i, message, 3, fileByteArray.length - i);
            }

            DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port); 
            socket.send(sendPacket); 
            System.out.println("Enviando: paquete num = " + sequenceNumber);
            
            System.out.println("Progreso: "+i+"/"+fileByteArray.length);

            boolean ackRec; 

            while (true) {
                byte[] ack = new byte[2]; 
                DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

                try {
                    socket.setSoTimeout(50); 
                    socket.receive(ackpack);
                    ackSequence = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff);
                    ackRec = true; 
                } catch (SocketTimeoutException e) {
                    System.out.println("El socket termino de esperar");
                    ackRec = false; 
                }

                
                if ((ackSequence == sequenceNumber) && (ackRec)) {
                    System.out.println("Ack recibido: paquete num = " + ackSequence);
                    break;
                } 
                else {
                    socket.send(sendPacket);
                    System.out.println("Reenviando: paquete num = " + sequenceNumber);
                }
            }
        }
    }
  
    private static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        byte[] bArray = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        } catch (IOException ioExp) {
            ioExp.printStackTrace();
        }
        return bArray;
    }
}

