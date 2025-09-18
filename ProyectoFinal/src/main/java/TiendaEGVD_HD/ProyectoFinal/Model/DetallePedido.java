package TiendaEGVD_HD.ProyectoFinal.Model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {


    @EmbeddedId
    private DetallePedidoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productoId")
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;


    @Embeddable
    public static class DetallePedidoId implements Serializable {

        @Column(name = "pedido_id")
        private Long pedidoId;

        @Column(name = "producto_id")
        private Long productoId;


        public DetallePedidoId() {}


        public DetallePedidoId(Long pedidoId, Long productoId) {
            this.pedidoId = pedidoId;
            this.productoId = productoId;
        }


        public Long getPedidoId() {
            return pedidoId;
        }

        public void setPedidoId(Long pedidoId) {
            this.pedidoId = pedidoId;
        }

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DetallePedidoId that = (DetallePedidoId) o;
            return Objects.equals(pedidoId, that.pedidoId) &&
                    Objects.equals(productoId, that.productoId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pedidoId, productoId);
        }
    }


    public DetallePedido() {}


    public DetallePedido(Pedido pedido, Producto producto, int cantidad, double precioUnitario) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.id = new DetallePedidoId(pedido.getId(), producto.getId());
    }


    public double getSubtotal() {
        return cantidad * precioUnitario;
    }


    public DetallePedidoId getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }


    @Override
    public String toString() {
        return "DetallePedido{" +
                "producto=" + producto.getNombre() +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}
