package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.Producto;
import com.miproyecto.ProyectoFinal.enums.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Búsqueda por estado
    List<Producto> findByEstado(EstadoProducto estado);

    // Búsqueda por categoría (para filtros)
    List<Producto> findByCategoriaId(Long categoriaId);

    // Búsqueda por nombre (ignorar mayúsculas/minúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}