package integrado.prog2.entities;
import java.time.LocalDateTime;
public class DetallePedido extends Base{
    private int cantidad;
    private Double subtotal;
    private Producto producto;

    //Constructor vacío
    public DetallePedido(){
        super();
    }
    //Constructor con todos los parametros
    public DetallePedido(int cantidad, Double subtotal, Producto producto){
        super();
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    // 3. Constructor completo de 6 parámetros
    public DetallePedido(Long id, boolean eliminado, LocalDateTime createdAt, int cantidad, Double subtotal, Producto producto) {
        super(id, eliminado, createdAt);
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    //Getters & Setters

    public int getCantidad() {return cantidad;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public Double getSubtotal() {return subtotal;}
    public void setSubtotal(Double subtotal) {this.subtotal = subtotal;}

    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}

    @Override
    public String toString() {
        String prodNombre = (producto != null) ? producto.getNombre() : "Sin producto";
        return "DetallePedido [Cantidad=" + cantidad + ", Subtotal=$" + subtotal + ", Producto=" + prodNombre + "]";
    }
}


