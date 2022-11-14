package ej4;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Juegos {
    ArrayList<Juego> juego;

    public Juegos(){
        juego = new ArrayList<>();
    }

    public void addJuego(Juego j){
        juego.add(j);
    }
    public void muestraJuegos(){
        for(Juego j : juego){
            System.out.println(j);
        }
    }
}
