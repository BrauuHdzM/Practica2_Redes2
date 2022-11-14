/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.practica2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author brauu
 */
public class Practica2 {

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        //File prueba = new File("catalogo\\Sparks.mp3");
        
        Catalogo cat = new Catalogo();
        cat.mostrarCatalogo();

        /*
        JSONObject json = (JSONObject) (new JSONParser()).parse(new FileReader("precios.json"));
        JSONArray precios = (JSONArray) json.get("PreciosCanciones");
        
        Iterator<JSONObject> iterator = precios.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().get("precio"));
        }*/

        /*String city = (String) person.get("city");
        System.out.println(city);

        String job = (String) person.get("job");
        System.out.println(job);

        JSONArray cars = (JSONArray) person.get("cars");*/

         
        
        
        /*Cancion cancion= new Cancion(prueba,1,350);
        System.out.println(cancion.album);
        System.out.println(cancion.ano);
        System.out.println(cancion.autor);
        System.out.println(cancion.duracion);
        System.out.println(cancion.nombre);
        System.out.println("This is my pull");*/
    }
}
