/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.util.ArrayList;

/**
 *
 * @author mauri
 */
public class Carrito {
    
    ArrayList<Cancion> Canciones;
    double costoTotal;

    public Carrito() {
        this.Canciones =  new ArrayList<Cancion>();
        double costoTotal = 0;
    }
    
    public void agregarCancion(Catalogo catalogo,int idCancion){
        Cancion cancion = catalogo.buscarCancion(idCancion);
        Canciones.add(cancion);
        costoTotal+=cancion.precio;
    }
    
    public int buscarPosicionCancion(int idCancion){
        int i = 0;
        for(Cancion c: this.Canciones){
            if(idCancion==c.id){
                return i;
            }
            i++;
        }
        return -1;
    }
    
    public void eliminarCancion(int idCancion){
        int pos = buscarPosicionCancion(idCancion);
        if(pos>=0){
            costoTotal-=Canciones.get(pos).precio;
            Canciones.remove(pos);
        }else{
            System.out.println("No existe el elemento");
        }
    }
    
    public void mostrarCarrito(){
        String formatoPrint = "| %-4d | %-30s | %-20s | %-10s |%n";

        System.out.format("+------+--------------------------------+----------------------+------------+%n");
        System.out.format("| ID   | Nombre                         |      Autor           |   Precio   |%n");
        System.out.format("+------+--------------------------------+----------------------+------------+%n");
        
        
        for (Cancion c : Canciones) {
            System.out.format(formatoPrint, c.id,c.nombre, c.autor, "$"+c.precio);
            System.out.format("+------+--------------------------------+----------------------+------------+%n");
        }
        System.out.format("|    %-55s   | %-10s |%n",  "Total", "$"+this.costoTotal);
        System.out.format("+--------------------------------------------------------------+------------+%n");
        
    }
    
    
    
    
}
