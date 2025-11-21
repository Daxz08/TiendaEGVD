package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.Role;
import TiendaEGVD_HD.ProyectoFinal.Repository.RoleRepository;
import TiendaEGVD_HD.ProyectoFinal.enums.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Obtener todos los roles
     */
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Buscar rol por nombre
     */
    @Transactional(readOnly = true)
    public Optional<Role> findByName(RoleName roleName) {
        return roleRepository.findByName(roleName);
    }

    /**
     * Buscar rol por ID
     */
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Guardar rol
     */
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Crear rol si no existe
     */
    public Role createRoleIfNotExists(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role(roleName);
                    return roleRepository.save(newRole);
                });
    }

    /**
     * Verificar si existe un rol
     */
    @Transactional(readOnly = true)
    public boolean existsByName(RoleName roleName) {
        return roleRepository.findByName(roleName).isPresent();
    }

    /**
     * Obtener rol de usuario por defecto
     */
    @Transactional(readOnly = true)
    public Role getDefaultUserRole() {
        return roleRepository.findByName(RoleName.ROLE_USUARIO)
                .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));
    }

    /**
     * Obtener rol de administrador
     */
    @Transactional(readOnly = true)
    public Role getAdminRole() {
        return roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));
    }
}
