/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.practica2;

import java.io.File;

/**
 *
 * @author brauu
 */
public class Practica2 {

    public static void main(String[] args) {
        File prueba = new File("C:\\Users\\brauu\\Documents\\NetBeansProjects\\Practica2\\catalogo\\Sparks.mp3");
        Cancion cancion= new Cancion(prueba,1,350);
        System.out.println(cancion.album);
        System.out.println(cancion.ano);
        System.out.println(cancion.autor);
        System.out.println(cancion.duracion);
        System.out.println(cancion.nombre);
    }
}
