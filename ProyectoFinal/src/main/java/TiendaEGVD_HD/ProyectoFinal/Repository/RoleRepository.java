package TiendaEGVD_HD.ProyectoFinal.Repository;

import com.miproyecto.ProyectoFinal.Model.Role;
import com.miproyecto.ProyectoFinal.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}