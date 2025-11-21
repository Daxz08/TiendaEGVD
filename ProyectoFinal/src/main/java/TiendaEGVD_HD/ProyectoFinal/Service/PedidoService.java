package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.DetallePedido;
import TiendaEGVD_HD.ProyectoFinal.Model.Pedido;
import TiendaEGVD_HD.ProyectoFinal.Model.Producto;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Repository.DetallePedidoRepository;
import TiendaEGVD_HD.ProyectoFinal.Repository.PedidoRepository;
import TiendaEGVD_HD.ProyectoFinal.Repository.ProductoRepository;
import TiendaEGVD_HD.ProyectoFinal.enums.EstadoPedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TiendaEGVD_HD.ProyectoFinal.Repository.ProductoRepository productoRepository;

    @Transactional
    public Pedido crearPedido(Long usuarioId, Map<Long, Integer> productosConCantidad) {
        System.out.println("🛒 Iniciando creación de pedido para usuario ID: " + usuarioId);

        // 1. Obtener usuario
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        // 2. Calcular total
        double total = 0;
        for (Map.Entry<Long, Integer> entry : productosConCantidad.entrySet()) {
            Producto producto = productoService.buscarPorId(entry.getKey());
            total += (entry.getValue() * producto.getPrecio());
        }

        // 3. Crear pedido básico
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setFechaEntrega(LocalDateTime.now().plusDays(15).toLocalDate());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(total);

        // 4. Guardar pedido SIN detalles primero
        pedido = pedidoRepository.save(pedido);
        entityManager.flush(); // Forzar escritura inmediata
        System.out.println("✅ Pedido básico guardado con ID: " + pedido.getId());

        // 5. Crear y guardar detalles uno por uno
        for (Map.Entry<Long, Integer> entry : productosConCantidad.entrySet()) {
            Producto producto = productoService.buscarPorId(entry.getKey());
            int cantidad = entry.getValue();

            // Usar el constructor que maneja automáticamente la clave compuesta
            DetallePedido detalle = new DetallePedido(pedido, producto, cantidad, producto.getPrecio());

            detallePedidoRepository.save(detalle);
            entityManager.flush(); // Forzar escritura inmediata del detalle

            // Descontar stock
            int nuevoStock = producto.getStock() - cantidad;
            entityManager.createQuery(
                "UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :productoId")
                .setParameter("cantidad", cantidad)
                .setParameter("productoId", producto.getId())
                .executeUpdate();

            // Si el stock llega a 0, cambiar estado a AGOTADO
            if (nuevoStock == 0) {
                entityManager.createQuery(
                    "UPDATE Producto p SET p.estado = :estado WHERE p.id = :productoId")
                    .setParameter("estado", TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.AGOTADO)
                    .setParameter("productoId", producto.getId())
                    .executeUpdate();
            }

            entityManager.flush(); // Forzar escritura inmediata del stock

            System.out.println("✅ Detalle guardado: " + producto.getNombre() + " x" + cantidad);
        }

        System.out.println("🎉 Pedido completado exitosamente");
        return pedido;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido cambiarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        EstadoPedido estadoAnterior = pedido.getEstado();

        // Cambiar el estado primero
        pedido.setEstado(nuevoEstado);
        Pedido pedidoActualizado = pedidoRepository.save(pedido);

        // Si se cancela un pedido que estaba pendiente, restaurar stock después
        if (nuevoEstado == EstadoPedido.CANCELADO && estadoAnterior == EstadoPedido.PENDIENTE) {
            restaurarStock(pedidoActualizado);
        }

        return pedidoActualizado;
    }

    private void restaurarStock(Pedido pedido) {
        try {
            for (DetallePedido detalle : pedido.getDetalles()) {
                // Restaurar stock usando query directa para evitar validaciones
                int filasActualizadas = entityManager.createQuery(
                    "UPDATE Producto p SET p.stock = p.stock + :cantidad WHERE p.id = :productoId")
                    .setParameter("cantidad", detalle.getCantidad())
                    .setParameter("productoId", detalle.getProducto().getId())
                    .executeUpdate();

                entityManager.flush();

                System.out.println("✅ Stock restaurado para producto ID: " + detalle.getProducto().getId() +
                                 " - Cantidad restaurada: " + detalle.getCantidad() +
                                 " - Filas actualizadas: " + filasActualizadas);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al restaurar stock: " + e.getMessage());
            throw new RuntimeException("Error al restaurar stock", e);
        }
    }


}