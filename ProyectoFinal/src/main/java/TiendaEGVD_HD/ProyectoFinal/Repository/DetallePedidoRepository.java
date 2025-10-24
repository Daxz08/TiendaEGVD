package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, DetallePedido.DetallePedidoId> {

    // Detalles de un pedido específico
    List<DetallePedido> findByPedidoId(Long pedidoId);

    // Productos más vendidos (ej: para dashboard)
    @Query("SELECT dp.producto, SUM(dp.cantidad) as total " +
            "FROM DetallePedido dp " +
            "GROUP BY dp.producto " +
            "ORDER BY total DESC")
    List<Object[]> findProductosMasVendidos();
}