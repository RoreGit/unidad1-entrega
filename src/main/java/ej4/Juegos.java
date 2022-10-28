package ej4;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Juegos {
    ArrayList<Juego> coleccion;

    public Juegos(){
        coleccion = new ArrayList<>();
    }

    public void addJuego(Juego j){
        coleccion.add(j);
    }
    public void muestraJuegos(){
        for(Juego j : coleccion){
            System.out.println(j);
        }
    }
}
