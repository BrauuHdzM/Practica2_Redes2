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
        /*boolean flag = true;
        
            try{
                DatagramSocket socket = new DatagramSocket(8000);
                byte[] receiveFileName = new byte[1024]; // Where we store the data of datagram of the name
                for(int a =0; a<2;a++){
                    DatagramPacket receiveFileNamePacket = new DatagramPacket(receiveFileName, receiveFileName.length);
                    socket.receive(receiveFileNamePacket); // Receive the datagram with the name of the file

                    System.out.println("Receiving file name");
                    byte [] data = receiveFileNamePacket.getData(); // Reading the name in bytes
                    String fileName = new String(data, 0, receiveFileNamePacket.getLength()); // Converting the name to string

                    System.out.println("Creating file");
                    File f = new File ("libreria\\" + fileName); // Creating the file
                    FileOutputStream outToFile = new FileOutputStream(f);
                    receiveFile(outToFile, socket);
                }
            }catch(Exception e){
                System.out.println(e);
            }*/
        
        
        
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
                carrito.mostrarCarrito();
                break;
            case 2:
                enviarCarrito(carrito);
                recibirCancion(carrito);
                break;
        }
    }
    
    public void descargarCanciones(){
        
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
        ObjectInputStream ois = null;
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
        int puerto = 8001;
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
    
    public static void recibirCancion(Carrito carrito){
        try{
            DatagramSocket socket = new DatagramSocket(8050);
            byte[] receiveFileName = new byte[1024]; 
            for(int a =0; a<carrito.Canciones.size();a++){
                DatagramPacket receiveFileNamePacket = new DatagramPacket(receiveFileName, receiveFileName.length);
                socket.receive(receiveFileNamePacket); 

                byte [] data = receiveFileNamePacket.getData(); 
                String fileName = new String(data, 0, receiveFileNamePacket.getLength()); // Converting the name to string

                File f = new File ("libreria\\" + fileName); 
                FileOutputStream outToFile = new FileOutputStream(f);
                receiveFile(outToFile, socket);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    
    private static void receiveFile(FileOutputStream outToFile, DatagramSocket socket) throws IOException {
        System.out.println("Receiving file");
        boolean flag; // Have we reached end of file
        int sequenceNumber = 0; // Order of sequences
        int foundLast = 0; // The las sequence found
        
        while (true) {
            byte[] message = new byte[1024]; // Where the data from the received datagram is stored
            byte[] fileByteArray = new byte[1021]; // Where we store the data to be writen to the file

            // Receive packet and retrieve the data
            DatagramPacket receivedPacket = new DatagramPacket(message, message.length);
            socket.receive(receivedPacket);
            message = receivedPacket.getData(); // Data to be written to the file

            // Get port and address for sending acknowledgment
            InetAddress address = receivedPacket.getAddress();
            int port = receivedPacket.getPort();

            // Retrieve sequence number
            sequenceNumber = ((message[0] & 0xff) << 8) + (message[1] & 0xff);
            // Check if we reached last datagram (end of file)
            flag = (message[2] & 0xff) == 1;
            
            // If sequence number is the last seen + 1, then it is correct
            // We get the data from the message and write the ack that it has been received correctly
            if (sequenceNumber == (foundLast + 1)) {

                // set the last sequence number to be the one we just received
                foundLast = sequenceNumber;

                // Retrieve data from message
                System.arraycopy(message, 3, fileByteArray, 0, 1021);

                // Write the retrieved data to the file and print received data sequence number
                outToFile.write(fileByteArray);
                System.out.println("Received: Sequence number:" + foundLast);

                // Send acknowledgement
                sendAck(foundLast, socket, address, port);
            } else {
                System.out.println("Expected sequence number: " + (foundLast + 1) + " but received " + sequenceNumber + ". DISCARDING");
                // Re send the acknowledgement
                sendAck(foundLast, socket, address, port);
            }
            // Check for last datagram
            if (flag) {
                outToFile.close();
                break;
            }
        }
    }    
    
    private static void sendAck(int foundLast, DatagramSocket socket, InetAddress address, int port) throws IOException {
        // send acknowledgement
        byte[] ackPacket = new byte[2];
        ackPacket[0] = (byte) (foundLast >> 8);
        ackPacket[1] = (byte) (foundLast);
        // the datagram packet to be sent
        DatagramPacket acknowledgement = new DatagramPacket(ackPacket, ackPacket.length, address, port);
        socket.send(acknowledgement);
        System.out.println("Sent ack: Sequence Number = " + foundLast);
    }
}
    
    
    

