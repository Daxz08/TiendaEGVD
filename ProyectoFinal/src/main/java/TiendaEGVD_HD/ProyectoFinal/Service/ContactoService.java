package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.Contacto;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Repository.ContactoRepository;
import TiendaEGVD_HD.ProyectoFinal.enums.EstadoContacto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Contacto guardarContacto(Contacto contacto) {
        // Vincular con usuario si existe el email
        Usuario usuario = usuarioService.buscarPorEmail(contacto.getEmail());
        if (usuario != null) {
            contacto.setUsuario(usuario);
        }
        return contactoRepository.save(contacto);
    }

    @Transactional(readOnly = true)
    public List<Contacto> listarTodos() {
        return contactoRepository.findAllOrderByFechaDesc();
    }

    @Transactional(readOnly = true)
    public List<Contacto> listarPorEstado(EstadoContacto estado) {
        return contactoRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Optional<Contacto> buscarPorId(Long id) {
        return contactoRepository.findById(id);
    }

    @Transactional
    public Contacto cambiarEstado(Long id, EstadoContacto nuevoEstado) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contacto no encontrado"));
        contacto.setEstado(nuevoEstado);
        return contactoRepository.save(contacto);
    }

    @Transactional(readOnly = true)
    public long contarPorEstado(EstadoContacto estado) {
        return contactoRepository.countByEstado(estado);
    }

    @Transactional
    public void eliminar(Long id) {
        contactoRepository.deleteById(id);
    }
}