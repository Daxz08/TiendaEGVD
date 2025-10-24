package TiendaEGVD_HD.ProyectoFinal.Repository;

import TiendaEGVD_HD.ProyectoFinal.Model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);

    // Para validar nombres únicos
    Optional<Categoria> findByNombre(String nombre);

    // Búsqueda rápida (ej: autocompletar)
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}