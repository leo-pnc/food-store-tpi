package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = new ArrayList<>(); //vacia, no null
        this.total = 0.0;
    }

    // Constructor completo (con campos de Base, necesario para recuperar datos con JDBC)
    public Pedido(Long id, boolean eliminado, LocalDateTime createdAt, LocalDate fecha, Estado estado, Double total, FormaPago formaPago, Usuario usuario, List<DetallePedido> detalles) {
        super(id, eliminado, createdAt);
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }

    //arma el detalle y recalcula el total
    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        DetallePedido detalle = new DetallePedido(cantidad, cantidad * precioUnitario, producto);
        detalles.add(detalle);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        for (DetallePedido d : detalles) {
            if (d.getProducto().getId().equals(producto.getId())) {
                return d;
            }
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido d = findDetallePedidoByProducto(producto);
        if (d != null) {
            detalles.remove(d);
            calcularTotal();
        }
    }

    @Override
    public double calcularTotal() {
        double suma = 0;
        for (DetallePedido d : detalles) {
            suma += d.getSubtotal();
        }
        this.total = suma;
        return suma;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Double getTotal() {
        return total; }
    public void setTotal(Double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "Pedido #" + getId() + " - " + fecha + " - " + estado + " - " + formaPago +
                " - Total: $" + total + " - Detalles: " + detalles.size();
    }
}