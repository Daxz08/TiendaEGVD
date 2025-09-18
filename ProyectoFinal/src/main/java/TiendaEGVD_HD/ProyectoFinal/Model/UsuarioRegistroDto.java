package TiendaEGVD_HD.ProyectoFinal.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioRegistroDto {

    private Long id_usuario; // Para compatibilidad con la plantilla

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe incluir al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&)"
    )
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido (ejemplo@correo.com)")
    private String email;

    @Pattern(regexp = "\\d{9}", message = "El celular debe tener exactamente 9 dígitos")
    private String celular;

    // Constructores
    public UsuarioRegistroDto() {
    }

    public UsuarioRegistroDto(String username, String password, String nombre, String email, String celular) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
    }

    // Método para convertir a Usuario
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id_usuario);
        usuario.setUsername(this.username);
        usuario.setPassword(this.password);
        usuario.setNombre(this.nombre);
        usuario.setEmail(this.email);
        usuario.setCelular(this.celular);
        return usuario;
    }

    // Getters y Setters
    public Long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return "UsuarioRegistroDto{" +
                "id_usuario=" + id_usuario +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", celular='" + celular + '\'' +
                '}';
    }
}
