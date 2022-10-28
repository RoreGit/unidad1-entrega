package ej1a3;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class libGames {
    private static final int maxString = 50;
    private static final int sizeGame = 2 * (maxString + 1) + 2 * Integer.SIZE / 8;
    public static RandomAccessFile raf;
    public static DataOutputStream dos;
    public static DataInputStream dis;
    public static File duplicados;
    public static File games;
    private static Scanner scan = null;
    public static FileChannel fileChannel;
    public static FileChannel fileCopy;

    public static boolean lookForDupes(int id) throws IOException {
        int idDupe;
        raf.seek((long) (id - 1) * sizeGame);
        if (games.exists()) {
            try {
                idDupe = raf.readInt();
            } catch (EOFException e) {
                return true;
            }
            if (idDupe == 0) {
                return true;
            }
            return idDupe != id;
        }
        return true;
    }
    /* ESTE MÉTODO ESTABA BIEN PARA RELLENAR STRINGS, PERO ME DABA PROBLEMAS AL BUSCAR EQUIVALENCIAS
    public static String padRight(String s) {
        return String.format("%-36s", s);
    }
    */

    public static String normalizeNom(String nombre) {
        if (nombre.length() < 36)
            return nombre;
        else
            return nombre.substring(0, 36);
    }

    public static void askAddJuego() throws IOException {
        int id;
        System.out.println("Introduce el ID del Juego");
        id = scan.nextInt();
        scan.nextLine();
        addJuego(id);
    }

    public static void doThings(char c) {
        if (c == 's')
            System.out.print("GUARDANDO");
        if (c == 'b')
            System.out.print("BUSCANDO");
        if (c == 'e')
            System.out.print("SALIENDO");
        if (c == 'd')
            System.out.print("BORRANDO");
        if (c == 'c')
            System.out.print("CANCELANDO");
        if (c == 'q')
            System.out.println("COPIANDO");
        if (c == 'x')
            System.out.println("GENERANDO XML");
        try {
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(300);
            }
            System.out.println();
            if (c == 's')
                System.out.println("GUARDADO SATISFACTORIAMENTE");
            if (c == 'q')
                System.out.println("COPIA CREADA CON ÉXITO");
            if (c == 'x')
                System.out.println("XML GENERADO CON ÉXITO");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }

    }

    public static void addJuego(int id) throws IOException {
        int year;
        String title, studio;
        if (lookForDupes(id)) {
            raf.seek((long) (id - 1) * sizeGame);
            raf.writeInt(id);
            System.out.println("Introduce el nombre del Juego");
            title = scan.nextLine();
            raf.writeUTF(normalizeNom(title));
            System.out.println("Introduce el estudio que ha diseñado el Juego");
            studio = scan.nextLine();
            raf.writeUTF(normalizeNom(studio));
            System.out.println("Introduce el año de lanzamiento del Juego");
            year = scan.nextInt();
            raf.writeInt(year);
            doThings('s');
        } else {
            dos.writeInt(id);
            System.out.println("Introduce el nombre del Juego");
            title = scan.nextLine();
            dos.writeUTF(normalizeNom(title));
            System.out.println("Introduce el estudio que ha diseñado el Juego");
            studio = scan.nextLine();
            dos.writeUTF(normalizeNom(studio));
            System.out.println("Introduce el año de lanzamiento del Juego");
            year = scan.nextInt();
            dos.writeInt(year);
            doThings('s');
        }
    }

    public static void showDupes() throws IOException {
        int id, year;
        String title, studio;
        dis = new DataInputStream(new FileInputStream("dupes.dat"));
        doThings('b');
        while (dis.available() > 0) {
            id = dis.readInt();
            title = dis.readUTF();
            studio = dis.readUTF();
            year = dis.readInt();
            System.out.println("ID: " + id + "\nTítulo: " + title + "\nEstudio: " + studio + "\nAño: " + year + "\n");
        }
        dis.close();
    }

    public static void askQueryJuego() {
        int id;
        System.out.println("INTRODUCE EL ID DEL JUEGO A BUSCAR");
        id = scan.nextInt();
        queryJuego(id);
    }

    public static void queryJuego(int id) {
        int year;
        String studio, title;
        try {
            raf.seek((long) (id - 1) * sizeGame);
            id = raf.readInt();
            title = raf.readUTF();
            studio = raf.readUTF();
            year = raf.readInt();
            doThings('b');
            if (id == 0) {
                System.out.println("Juego no encontrado");
                return;
            }
            System.out.println();
            System.out.println("JUEGO ENCONTRADO");
            Thread.sleep(500);
            System.out.println("ID: " + id);
            System.out.println("NOMBRE: " + title);
            System.out.println("ESTUDIO: " + studio);
            System.out.println("AÑO: " + year);
            if (!lookForDupes(id)) {
                if (duplicados.exists()) {
                    dis = new DataInputStream(new FileInputStream("dupes.dat"));
                    int idDupe;
                    System.out.println();
                    while (dis.available() > 0) {
                        idDupe = dis.readInt();
                        title = dis.readUTF();
                        studio = dis.readUTF();
                        year = dis.readInt();
                        if (idDupe == id) {
                            System.out.println("ID: " + id + "\nTítulo: " + title + "\nEstudio: " + studio + "\nAño: " + year + "\n");
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        } catch (IOException a) {
            System.out.println("Juego no encontrado");
        }
    }

    public static boolean confirmErase() {
        char choice;
        System.out.println("¿QUIERES BORRARLO? S/N");
        while (true) {
            choice = scan.nextLine().charAt(0);
            if (choice == 'S' || choice == 'N') {
                if (choice == 'S') {
                    System.out.println("PROCEDIENDO AL BORRADO");
                    return true;
                } else {
                    System.out.println("CANCELANDO EL BORRADO");
                    return false;
                }
            } else
                System.out.println("Introduce S o N");
        }
    }

    public static void askEraseGame() throws IOException {
        int id;
        String title;
        System.out.println("INTRODUCE EL ID DEL JUEGO A BORRAR");
        id = scan.nextInt();
        scan.nextLine();
        queryJuego(id);
        if (lookForDupes(id)) {
            System.out.println("SE HAN ENCONTRADO DUPLICADOS, INTRODUCE EL NOMBRE DEL JUEGO A BORRAR");
            String nombreDupe = scan.nextLine();
            raf.seek((long) (id - 1) * sizeGame);
            id = raf.readInt();
            title = raf.readUTF();
            if (title.equals(nombreDupe)) {
                if (confirmErase()) {
                    eraseGame(id);
                    doThings('d');
                }
            } else if (confirmErase()) {
                eraseGame(nombreDupe);
                doThings('d');
            }
        } else {
            if (confirmErase()) {
                eraseGame(id);
                doThings('d');
            }
        }
    }

    public static void eraseGame(int id) throws IOException {
        raf.seek((long) (id - 1) * sizeGame);
        raf.writeInt(0);
        raf.writeUTF("x");
        raf.writeUTF("x");
        raf.writeInt(0);
    }

    public static void eraseGame(String name) {
        int id, year;
        String nombre, studio;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(duplicados));
             DataOutputStream dosCopy = new DataOutputStream(new FileOutputStream(duplicados))) {
            while (dis.available() > 0) {
                id = dis.readInt();
                nombre = dis.readUTF();
                studio = dis.readUTF();
                year = dis.readInt();
                if (!nombre.equals(name)) {
                    dosCopy.writeInt(id);
                    dosCopy.writeUTF(nombre);
                    dosCopy.writeUTF(studio);
                    dosCopy.writeInt(year);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation dom = db.getDOMImplementation();
        Document documento = dom.createDocument(null,"xml",null);
        Element raiz = documento.createElement("videojuegos");
        Element nodoJuego, nodoDatos;
        Text texto;
        documento.getDocumentElement().appendChild(raiz);
        int id;
        try {
            for (int i = 1;true;i++) {
                raf.seek((long) (i - 1) * sizeGame);
                id = raf.readInt();
                if (id != 0) {
                    nodoJuego = documento.createElement("videojuego");
                    raiz.appendChild(nodoJuego);
                    nodoDatos = documento.createElement("id");
                    nodoJuego.appendChild(nodoDatos);
                    texto = documento.createTextNode(String.valueOf(id));
                    nodoDatos.appendChild(texto);
                    nodoDatos = documento.createElement("nombre");
                    nodoJuego.appendChild(nodoDatos);
                    texto = documento.createTextNode(raf.readUTF());
                    nodoDatos.appendChild(texto);
                    nodoDatos = documento.createElement("estudio");
                    nodoJuego.appendChild(nodoDatos);
                    texto = documento.createTextNode(raf.readUTF());
                    nodoDatos.appendChild(texto);
                    nodoDatos = documento.createElement("año");
                    nodoJuego.appendChild(nodoDatos);
                    texto = documento.createTextNode(String.valueOf(raf.readInt()));
                    nodoDatos.appendChild(texto);
                }
            }
        }
        catch (IOException e){
            doThings('x');
        }
        Source source = new DOMSource(documento);
        Result resultado = new StreamResult(new File("archivo.xml"));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source,resultado);
    }

    public static void readXML(){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(new File("archivo.xml"));
            NodeList juegos = documento.getElementsByTagName("videojuego");
            Node juego;
            Element element;
            for(int i=0;i<juegos.getLength();i++){
                juego = juegos.item(i);
                element = (Element) juego;
                System.out.println("ID: "+element.getElementsByTagName("id").item(0).getTextContent());
                System.out.println("Nombre: "+element.getElementsByTagName("nombre").item(0).getTextContent());
                System.out.println("Estudio: "+element.getElementsByTagName("estudio").item(0).getTextContent());
                System.out.println("Año: "+element.getElementsByTagName("año").item(0).getTextContent());
            }
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("XML NO CREADO, POR FAVOR USE PRIMERO LA OPCIÓN 6.- CREAR XML");
        }


    }
    public static void copyRaf() throws IOException {
        File prueba = new File("games_copy.dat");
        if (prueba.exists())
            prueba.delete();
        ByteBuffer bf = ByteBuffer.allocate(1024);
        while(fileChannel.read(bf)>0){
            bf.flip();
            fileCopy.write(bf);
            bf.rewind();
        }
        doThings('q');
    }

    public static boolean deleteAll(){
        char choice;
        System.out.println("¿SEGURO QUE QUIERES BORRAR LA BASE DE DATOS? S/N");
        scan.nextLine();
        while (true) {
            choice = scan.nextLine().charAt(0);
            if (choice == 'S' || choice == 'N') {
                if (choice == 'S') {
                    System.out.println("¿AHORA EN SERIO, DE VERDAD QUIERES BORRARLA? S/N");
                    while (true) {
                        choice = scan.nextLine().charAt(0);
                        if (choice == 'S' || choice == 'N') {
                            if (choice == 'S') {
                                doThings('d');
                                return true;
                            } else {
                                doThings('c');
                                return false;
                            }
                        } else
                            System.out.println("Introduce S o N");
                    }
                } else {
                    doThings('c');
                    return false;
                }
            } else
                System.out.println("Introduce S o N");
        }
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        games = new File("games.dat");
        duplicados = new File("dupes.dat");
        raf = new RandomAccessFile("games.dat", "rw");
        dos = new DataOutputStream(new FileOutputStream(duplicados, true));
        scan = new Scanner(System.in);
        fileChannel = raf.getChannel();
        Path ruta = Paths.get("games_copy.dat");
        Set<StandardOpenOption> options = new HashSet<>();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.APPEND);
        fileCopy = FileChannel.open(ruta,options);
        int i;
        while (true) {
            System.out.println("------GESTOR DE VIDEOJUEGOS------\n\t1.- AÑADIR VIDEOJUEGO\n\t2.- CONSULTAR VIDEOJUEGO\n\t3.- ENSEÑAR DUPLICADOS\n\t4.- BORRAR JUEGO\n\t5.- COPIAR ARCHIVO\n\t6.- CREAR XML\n\t7.- LEER XML\n\t99.- BORRAR BASE DE DATOS\n\t0.- SALIR");
            System.out.print("SELECCIONA LO QUE QUIERAS: ");
            i = scan.nextInt();
            switch (i) {
                case 1 -> askAddJuego();
                case 2 -> askQueryJuego();
                case 3 -> showDupes();
                case 4 -> askEraseGame();
                case 5 -> copyRaf();
                case 6 -> createXML();
                case 7 -> readXML(); //TODO hacer esto
                case 99 -> {
                    if (deleteAll()) {
                        raf.close();
                        dos.close();
                        games.delete();
                        duplicados.delete();
                        fileChannel.close();
                        fileCopy.close();
                        raf = new RandomAccessFile("games.dat", "rw");
                        dos = new DataOutputStream(new FileOutputStream(duplicados, true));
                    }
                }
                case 0 -> {
                    raf.close();
                    dos.close();
                    fileChannel.close();
                    fileCopy.close();
                    doThings('e');
                    System.exit(0);
                }
                default -> System.out.println("Número erróneo.");
            }
        }
    }
}