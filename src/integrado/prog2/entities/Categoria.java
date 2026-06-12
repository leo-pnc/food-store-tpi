package integrado.prog2.entities;

public class Categoria extends Base {
    private String nombre;
    private String descripcion;

//constructor:
    public Categoria(String nombre, String descripcion) {
        super();
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
//toString
    @Override
    public String toString() {
        return "Categoria #" + getId() + " - " + nombre + " (" + descripcion + ")";
    }


}
