package TiendaEGVD_HD.ProyectoFinal.Model;

import java.io.Serializable;

public class CarritoItem implements Serializable {
    private Long productoId;
    private String nombre;
    private Double precio;
    private String imagenUrl;
    private Integer cantidad;
    private Integer stockDisponible;

    // Constructores
    public CarritoItem() {}

    public CarritoItem(Long productoId, String nombre, Double precio, String imagenUrl, Integer cantidad, Integer stockDisponible) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
        this.cantidad = cantidad;
        this.stockDisponible = stockDisponible;
    }

    // Método para calcular subtotal
    public Double getSubtotal() {
        return precio * cantidad;
    }

    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }
}
