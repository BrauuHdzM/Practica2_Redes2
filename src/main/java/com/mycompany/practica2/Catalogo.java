/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practica2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Mauricio Beltrán
 */
public class Catalogo implements Serializable{
    
    ArrayList<Cancion> Canciones;
    
    public Catalogo() throws FileNotFoundException, IOException, ParseException{
        this.Canciones =  new ArrayList<Cancion>();
        //Se carga el json que tiene los ID y sus respectivos precios
        JSONObject json = (JSONObject) (new JSONParser()).parse(new FileReader("precios.json"));
        JSONArray precios = (JSONArray) json.get("PreciosCanciones");
        
        //Se carga los mp3 que estan en la carpeta catalogo
        File catalogoPath = new File("catalogo");
        File[] listaCanciones = catalogoPath.listFiles();
        
        Iterator<JSONObject> iterator = precios.iterator();
        int i=0;
        while (iterator.hasNext()) {
            JSONObject var = iterator.next();
            //En el orden de aparicion de los archivos se les va asignando el precio y el id conforme aparezca en el json
            Canciones.add(new Cancion(listaCanciones[i], (double)var.get("precio"),((Long)var.get("id")).intValue()));
            i++;
        }
    }
    
    
    public void mostrarCatalogo(){
        String formatoPrint = "| %-4d | %-30s | %-20s | %-25s | %-5s | %-8s | %-10s |%n";

        System.out.format("+------+--------------------------------+----------------------+---------------------------+-------+----------+------------+%n");
        System.out.format("| ID   | Nombre                         |      Autor           |          Album            |  Año  | Duración |   Precio   |%n");
        System.out.format("+------+--------------------------------+----------------------+---------------------------+-------+----------+------------+%n");
        
        
        for (Cancion c : Canciones) {
            System.out.format(formatoPrint, c.id,c.nombre, c.autor, c.album, c.ano, c.duracion, "$"+c.precio);
            System.out.format("+------+--------------------------------+----------------------+---------------------------+-------+----------+------------+%n");
        }
        
    }
    
    
    public Cancion buscarCancion(int idCancion){
        for(Cancion c: this.Canciones){
            if(idCancion==c.id){
                return c;
            }
        }
        return null;
    }
    
    
    
}


