package TiendaEGVD_HD.ProyectoFinal.validator;

import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Usuario> {

    private UsuarioRepository usuarioRepository;

    public UniqueEmailValidator() {
        // Constructor por defecto requerido por Hibernate Validator
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return true; // Dejar que @NotBlank maneje la validación de campo vacío
        }

        String email = usuario.getEmail();
        Long userId = usuario.getId();

        if (usuarioRepository == null) {
            return true; // Si no hay repositorio, no podemos validar
        }

        // Buscar si existe otro usuario con el mismo email
        return usuarioRepository.findByEmail(email)
                .map(existingUser -> existingUser.getId().equals(userId)) // Si existe, debe ser el mismo usuario
                .orElse(true); // Si no existe, es válido
    }
}