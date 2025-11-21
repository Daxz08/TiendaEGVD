package TiendaEGVD_HD.ProyectoFinal.Service;


import TiendaEGVD_HD.ProyectoFinal.Model.CarritoItem;
import TiendaEGVD_HD.ProyectoFinal.Model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarritoService {

    @Autowired
    private ProductoService productoService;

    // Agregar producto al carrito
    public void agregarProducto(Map<Long, CarritoItem> carrito, Long productoId, Integer cantidad) {
        Producto producto = productoService.buscarPorId(productoId);
        
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        
        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        CarritoItem item = carrito.get(productoId);
        
        if (item != null) {
            // Si ya existe, actualizar cantidad
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (nuevaCantidad > producto.getStock()) {
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
            }
            item.setCantidad(nuevaCantidad);
        } else {
            // Si no existe, crear nuevo item
            item = new CarritoItem(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getImagenUrl(),
                cantidad,
                producto.getStock()
            );
            carrito.put(productoId, item);
        }
    }

    // Actualizar cantidad de un producto
    public void actualizarCantidad(Map<Long, CarritoItem> carrito, Long productoId, Integer nuevaCantidad) {
        CarritoItem item = carrito.get(productoId);
        if (item == null) {
            throw new IllegalArgumentException("Producto no encontrado en el carrito");
        }

        Producto producto = productoService.buscarPorId(productoId);
        if (nuevaCantidad > producto.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        if (nuevaCantidad <= 0) {
            carrito.remove(productoId);
        } else {
            item.setCantidad(nuevaCantidad);
        }
    }

    // Eliminar producto del carrito
    public void eliminarProducto(Map<Long, CarritoItem> carrito, Long productoId) {
        carrito.remove(productoId);
    }

    // Limpiar carrito
    public void limpiarCarrito(Map<Long, CarritoItem> carrito) {
        carrito.clear();
    }

    // Calcular total del carrito
    public Double calcularTotal(Map<Long, CarritoItem> carrito) {
        return carrito.values().stream()
                .mapToDouble(CarritoItem::getSubtotal)
                .sum();
    }

    // Obtener cantidad total de items
    public Integer obtenerCantidadTotal(Map<Long, CarritoItem> carrito) {
        return carrito.values().stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }

    // Convertir carrito a lista para mostrar
    public List<CarritoItem> obtenerItems(Map<Long, CarritoItem> carrito) {
        return new ArrayList<>(carrito.values());
    }

    // Validar stock antes de procesar pedido
    public boolean validarStock(Map<Long, CarritoItem> carrito) {
        for (CarritoItem item : carrito.values()) {
            Producto producto = productoService.buscarPorId(item.getProductoId());
            if (producto.getStock() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    // Convertir carrito a Map para PedidoService
    public Map<Long, Integer> convertirAMapaCantidades(Map<Long, CarritoItem> carrito) {
        Map<Long, Integer> productosConCantidad = new HashMap<>();
        for (CarritoItem item : carrito.values()) {
            productosConCantidad.put(item.getProductoId(), item.getCantidad());
        }
        return productosConCantidad;
    }
}
