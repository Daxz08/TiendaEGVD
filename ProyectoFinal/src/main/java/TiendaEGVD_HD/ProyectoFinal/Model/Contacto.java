package TiendaEGVD_HD.ProyectoFinal.Model;

import TiendaEGVD_HD.ProyectoFinal.enums.EstadoContacto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "contactos")
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    private String nombre;

    @NotBlank @Email
    private String email;

    @Size(max = 20)
    private String telefono;

    @Size(max = 50)
    private String pais;

    @Size(max = 50)
    private String asunto;

    @NotBlank @Lob
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoContacto estado = EstadoContacto.PENDIENTE;

    private LocalDateTime fechaHoraMensaje = LocalDateTime.now();

    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Constructores, getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public EstadoContacto getEstado() {
        return estado;
    }

    public void setEstado(EstadoContacto estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHoraMensaje() {
        return fechaHoraMensaje;
    }

    public void setFechaHoraMensaje(LocalDateTime fechaHoraMensaje) {
        this.fechaHoraMensaje = fechaHoraMensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Contacto(Long id, String nombre, String email, String telefono, String pais, String asunto, String mensaje, EstadoContacto estado, LocalDateTime fechaHoraMensaje, Usuario usuario, Producto producto) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.pais = pais;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fechaHoraMensaje = fechaHoraMensaje;
        this.usuario = usuario;
        this.producto = producto;
    }

    public Contacto() {

    }
}