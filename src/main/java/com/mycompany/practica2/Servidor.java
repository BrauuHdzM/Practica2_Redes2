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
        
        
        int pto=8000;
        DatagramSocket s = new DatagramSocket(pto);
        s.setReuseAddress(true);
        
        
        while(true){
            try{  
                
                
                waitConnection(s);
                enviarCatalogo(s);
                sendCanciones(getCarrito(s));
            }catch(Exception e){
                e.printStackTrace();
            }//catch
        }
    }
    
    public static void waitConnection(DatagramSocket s) throws SocketException, IOException{
        
        //s.setReuseAddress(true);
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
        //DatagramSocket c = null;
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
        //DatagramSocket s = null;
        //ObjectOutputStream oos=null;
        ObjectInputStream ois = null;
        //ByteArrayInputStream bis;
        Carrito carrito =null;

        try{
            //s = new DatagramSocket(puerto);
            System.out.println("Esperando Carrito...");
            
            dp = new DatagramPacket(new byte[1024],1024);
            s.receive(dp);
            ois = new ObjectInputStream(new ByteArrayInputStream(dp.getData()));
            carrito = (Carrito)ois.readObject();
            System.out.println("Carrito recibido");
            ois.close();
            
            
        }catch(IOException | ClassNotFoundException e){System.err.println(e);}
        System.out.println("Termina el contenido del datagrama...");
        return carrito;

}
    
    public static void sendCanciones(Carrito carrito){
        for (int i = 0; i<carrito.Canciones.size();i++){
            try{
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("127.0.0.1");
                String fileName;
                File f = new File(carrito.Canciones.get(i).path);//new File("catalogo\\Sparks.mp3");
                fileName = f.getName();
                byte[] fileNameBytes = fileName.getBytes(); // File name as bytes to send it
                DatagramPacket fileStatPacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, address, 8001); // File name packet
                
                socket.send(fileStatPacket); // Sending the packet with the file name

                byte[] fileByteArray = readFileToByteArray(f); // Array of bytes the file is made of
                sendFile(socket, fileByteArray, address, 8001);
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
    
    private static void sendFile(DatagramSocket socket, byte[] fileByteArray, InetAddress address, int port) throws IOException {
        System.out.println("Sending file");
        int sequenceNumber = 0; // For order
        boolean flag; // To see if we got to the end of the file
        int ackSequence = 0; // To see if the datagram was received correctly

        for (int i = 0; i < fileByteArray.length; i = i + 1021) {
            sequenceNumber += 1;

            // Create message
            byte[] message = new byte[1024]; // First two bytes of the data are for control (datagram integrity and order)
            message[0] = (byte) (sequenceNumber >> 8);
            message[1] = (byte) (sequenceNumber);

            if ((i + 1021) >= fileByteArray.length) { // Have we reached the end of file?
                flag = true;
                message[2] = (byte) (1); // We reached the end of the file (last datagram to be send)
            } else {
                flag = false;
                message[2] = (byte) (0); // We haven't reached the end of the file, still sending datagrams
            }

            if (!flag) {
                System.arraycopy(fileByteArray, i, message, 3, 1021);
            } else { // If it is the last datagram
                System.arraycopy(fileByteArray, i, message, 3, fileByteArray.length - i);
            }

            DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port); // The data to be sent
            socket.send(sendPacket); // Sending the data
            System.out.println("Sent: Sequence number = " + sequenceNumber);

            boolean ackRec; // Was the datagram received?

            while (true) {
                byte[] ack = new byte[2]; // Create another packet for datagram ackknowledgement
                DatagramPacket ackpack = new DatagramPacket(ack, ack.length);

                try {
                    socket.setSoTimeout(50); // Waiting for the server to send the ack
                    socket.receive(ackpack);
                    ackSequence = ((ack[0] & 0xff) << 8) + (ack[1] & 0xff); // Figuring the sequence number
                    ackRec = true; // We received the ack
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timed out waiting for ack");
                    ackRec = false; // We did not receive an ack
                }

                // If the package was received correctly next packet can be sent
                if ((ackSequence == sequenceNumber) && (ackRec)) {
                    System.out.println("Ack received: Sequence Number = " + ackSequence);
                    break;
                } // Package was not received, so we resend it
                else {
                    socket.send(sendPacket);
                    System.out.println("Resending: Sequence Number = " + sequenceNumber);
                }
            }
        }
    }
  
    private static byte[] readFileToByteArray(File file) {
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
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

