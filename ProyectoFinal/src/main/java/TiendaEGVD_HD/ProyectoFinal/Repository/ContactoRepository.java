package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.Contacto;
import com.miproyecto.ProyectoFinal.enums.EstadoContacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    // Listar contactos por usuario
    List<Contacto> findByUsuarioId(Long usuarioId);

    // Filtrar por estado
    List<Contacto> findByEstado(EstadoContacto estado);

    // Obtener todos los contactos ordenados por fecha (más recientes primero)
    @Query("SELECT c FROM Contacto c ORDER BY c.fechaHoraMensaje DESC")
    List<Contacto> findAllOrderByFechaDesc();

    // Contar contactos por estado
    long countByEstado(EstadoContacto estado);
}