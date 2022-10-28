package ej4;

public class Juego {
    private int id;
    private String nombre,estudio;
    private int lanzamiento;
    public Juego() {
    }

    public Juego(int id, String nombre, String estudio,int lanzamiento) {
        this.id = id;
        this.lanzamiento = lanzamiento;
        this.nombre = nombre;
        this.estudio = estudio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanzamiento() {
        return lanzamiento;
    }

    public void setLanzamiento(int lanzamiento) {
        this.lanzamiento = lanzamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estudio='" + estudio + '\'' +
                ", lanzamiento=" + lanzamiento +
                '}';
    }
}
