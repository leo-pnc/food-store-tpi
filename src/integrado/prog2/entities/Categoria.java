package integrado.prog2.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;

//constructor:
    public Categoria(String nombre, String descripcion) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    //constructor vacio:
    public Categoria(){
        super();
    }
    //constructor completo:
    public Categoria(Long id, boolean eliminado, LocalDateTime createdAT, String nombre, String descripcion) {
        super (id, eliminado, createdAT);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
//getts y setts
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Categoria #" + getId() + " - " + nombre + " (" + descripcion + ")";
    }


}
