package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.Role;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Repository.RoleRepository;
import TiendaEGVD_HD.ProyectoFinal.Repository.UsuarioRepository;
import TiendaEGVD_HD.ProyectoFinal.enums.RoleName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }


    // Método para guardar/actualizar usuario
    public Usuario save(Usuario usuario) {
        System.out.println("💾 Iniciando guardado de usuario...");
        System.out.println("🆔 ID del usuario: " + usuario.getId());

        try {
            if (usuario.getId() == null) {
                System.out.println("🆕 Nuevo usuario - procesando...");

                // Nuevo usuario - encriptar contraseña y asignar rol por defecto
                System.out.println("🔐 Encriptando contraseña...");
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                System.out.println("✅ Contraseña encriptada");

                // Asignar rol USUARIO por defecto si no tiene roles
                if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
                    System.out.println("👥 Asignando rol por defecto...");
                    Set<Role> roles = new HashSet<>();
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USUARIO)
                            .orElseThrow(() -> new RuntimeException("Error: Rol USUARIO no encontrado."));
                    roles.add(userRole);
                    usuario.setRoles(roles);
                    System.out.println("✅ Rol USUARIO asignado");
                } else {
                    System.out.println("ℹ️ Usuario ya tiene roles asignados: " + usuario.getRoles().size());
                }
            } else {
                System.out.println("🔄 Usuario existente - actualizando...");
                // Usuario existente - mantener contraseña si no se proporciona nueva
                usuarioRepository.findById(usuario.getId()).ifPresent(existing -> {
                    if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                        usuario.setPassword(existing.getPassword());
                    } else {
                        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                    }
                });
            }

            System.out.println("💾 Guardando en base de datos...");
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            System.out.println("✅ Usuario guardado exitosamente con ID: " + usuarioGuardado.getId());

            return usuarioGuardado;

        } catch (Exception e) {
            System.err.println("❌ Error en save: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("❌ Causa: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            throw e;
        }
    }

    // Método específico para guardar usuario (usado en controladores)
    public Usuario guardarUsuario(Usuario usuario) {
        return save(usuario);
    }

    // Método para registrar nuevo usuario con validaciones
    public Usuario registrarUsuario(Usuario usuario) {
        System.out.println("🔍 Iniciando validaciones de registro...");
        System.out.println("📧 Email a verificar: " + usuario.getEmail());
        System.out.println("👤 Username a verificar: " + usuario.getUsername());

        try {
            // Verificar si el email ya existe
            System.out.println("🔄 Verificando si existe email...");
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                System.err.println("❌ Email ya existe: " + usuario.getEmail());
                throw new IllegalArgumentException("Ya existe un usuario con este email");
            }
            System.out.println("✅ Email disponible");

            // Verificar si el username ya existe
            System.out.println("🔄 Verificando si existe username...");
            if (usuarioRepository.existsByUsername(usuario.getUsername())) {
                System.err.println("❌ Username ya existe: " + usuario.getUsername());
                throw new IllegalArgumentException("Ya existe un usuario con este nombre de usuario");
            }
            System.out.println("✅ Username disponible");

            System.out.println("🔄 Llamando al método save...");
            Usuario usuarioGuardado = save(usuario);
            System.out.println("✅ Usuario guardado con ID: " + usuarioGuardado.getId());

            return usuarioGuardado;

        } catch (Exception e) {
            System.err.println("❌ Error en registrarUsuario: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("❌ Causa: " + e.getCause().getMessage());
            }
            throw e;
        }
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Métodos de validación
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}