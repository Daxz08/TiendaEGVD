package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Pedidos de un usuario (para historial)
    List<Pedido> findByUsuarioId(Long Id);

    // Filtrar por estado (ej: "PENDIENTE", "ENTREGADO")
    List<Pedido> findByEstado(String estado);

}