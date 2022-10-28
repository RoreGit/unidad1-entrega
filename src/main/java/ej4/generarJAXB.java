package ej4;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class generarJAXB {
    public static void main(String[] args) {
        generarXML();
        leerXML();
    }
    public static void generarXML(){
        Juego j1 = new Juego(1,"Super mario","Nientiendo",1983);
        Juego j2 = new Juego(2,"Super Metroid","Noentiendo",1990);
        Juegos coleccion = new Juegos();
        coleccion.addJuego(j1);
        coleccion.addJuego(j2);

        try {
            JAXBContext contexto = JAXBContext.newInstance(coleccion.getClass());
            Marshaller marshall = contexto.createMarshaller();
            marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            marshall.marshal(coleccion,new File("juegosJAXB.xml"));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    public static void leerXML(){
        try {
            Juegos coleccion;
            JAXBContext contexto = JAXBContext.newInstance(Juegos.class);
            Unmarshaller unmarshal = contexto.createUnmarshaller();
            coleccion = (Juegos) unmarshal.unmarshal(new File("juegosJAXB.xml"));
            coleccion.muestraJuegos();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
