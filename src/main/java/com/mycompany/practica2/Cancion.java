
package com.mycompany.practica2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author brauu
 */
public class Cancion implements Serializable{
    String nombre;
    String autor;
    String album;
    String duracion;
    String ano;
    double precio;
    int id;
    
public Cancion(String nombre, String autor, String album, String duracion, String ano, float precio, int id){
    this.nombre=nombre;
    this.autor=autor;
    this.album=album;
    this.duracion=duracion;
    this.ano=ano;
    this.precio=precio;
    this.id=id;
}

public Cancion(File file, double precio, int id){
        try {
        InputStream input = new FileInputStream(file);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();
        
        // List all metadata
        this.nombre=metadata.get("dc:title");
        this.autor=metadata.get("xmpDM:albumArtist");
        this.album=metadata.get("xmpDM:album");
        this.duracion=metadata.get("xmpDM:duration");
        this.ano=metadata.get("xmpDM:releaseDate");
        double tiempo=Double.parseDouble(duracion);
        int tiempo2= (int)tiempo;
        this.duracion=""+tiempo2/60+":"+tiempo2%60;
        this.precio=precio;
        this.id=id;
        
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        } catch (SAXException e) {
        e.printStackTrace();
        } catch (TikaException e) {
        e.printStackTrace();
        }
        
    }

  
    
   
}
