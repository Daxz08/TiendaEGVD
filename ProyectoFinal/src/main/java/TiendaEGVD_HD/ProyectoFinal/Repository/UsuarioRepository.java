package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<Usuario> findByUsername(String username);
}