/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.practica2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 *
 * @author brauu
 */
public class Practica2 {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException  {
        File f = new File("catalogo\\Sparks.mp3");
        System.out.println(f.getAbsolutePath());
        Catalogo cat = new Catalogo();
        cat.mostrarCatalogo();
        
        

    }
}
