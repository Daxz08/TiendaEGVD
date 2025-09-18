package TiendaEGVD_HD.ProyectoFinal.Model;

import com.miproyecto.ProyectoFinal.validator.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "usuarios")
@UniqueEmail
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = " El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = " El nombre de usuario debe tener entre 4 y 20 caracteres")
    @Column(unique = true)
    private String username;

    // Validación de contraseña removida porque se valida en el DTO antes de encriptar
    // La contraseña encriptada no debe validarse con patrones
    private String password;

    @NotBlank(message = " El nombre es obligatorio")
    @Size(max = 50, message = " El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(message = " El email es obligatorio")
    @Email(message = " Formato de email inválido (ejemplo@correo.com)")
    @Column(unique = true)
    private String email;


    // Validación de celular removida - se valida en el DTO
    private String celular;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contacto> contactos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Usuario() {
    }

    public Usuario(Long id, String username, String password, String nombre, String email, String celular, List<Contacto> contactos, List<Pedido> pedidos, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
        this.celular = celular;
        this.contactos = contactos;
        this.pedidos = pedidos;
        this.roles = roles;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            System.err.println("⚠️ Roles es null para usuario: " + username);
            return new HashSet<>();
        }
        System.out.println("✅ Roles para " + username + ": " + roles.size());
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}