package com.mycompany.practica2;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        
        Catalogo catalogo = new Catalogo();//esta variable debe de ser asignada al catalogo que se reciba del servidor
        Carrito carrito = new Carrito();
        boolean varSalir = true;
        
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
                    menuCarrito();
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
    public static void  menuCarrito(){
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                           Ingrese una opcion:
                           1.Eliminar cancion
                           2.Proceder al pago
                           3.Regresar""");
        
        switch(sc.nextInt()){
            case 1:
                
            break;
        }
    }
    
}
