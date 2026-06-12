package integrado.prog2.entities;

public class Producto extends Base{
    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private Boolean disponible;
    private Categoria categria;

    //Constructor vacio
    public Producto(){
    }
    //Constructor con todos los parametros
    public Producto(String nombre, Double precio, String descripcion, int stock, String imagen, Boolean disponible, Categoria categria){
        this.nombre = getNombre();
        this.precio = getPrecio();
        this.descripcion= getDescripcion();
        this.stock = getStock();
        this.imagen = getImagen();
        this.disponible = getDisponible();
        this.categria = getCategria();
    }

    //Getters & Setters

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Double getPrecio() {return precio;}
    public void setPrecio(Double precio) {this.precio = precio;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}

    public Boolean getDisponible() {return disponible;}
    public void setDisponible(Boolean disponible) {this.disponible = disponible;}

    public Categoria getCategria() {return categria;}
    public void setCategria(Categoria categria) {this.categria = categria;}
}
