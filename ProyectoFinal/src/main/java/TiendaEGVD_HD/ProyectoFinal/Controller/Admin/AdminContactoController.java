package TiendaEGVD_HD.ProyectoFinal.Controller.Admin;

import TiendaEGVD_HD.ProyectoFinal.Model.Contacto;
import TiendaEGVD_HD.ProyectoFinal.Service.ContactoService;
import TiendaEGVD_HD.ProyectoFinal.enums.EstadoContacto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/contactos")
public class AdminContactoController {

    @Autowired
    private ContactoService contactoService;

    @GetMapping
    public String listarContactos(
            @RequestParam(value = "estado", required = false) EstadoContacto estado,
            Model model) {
        
        List<Contacto> contactos;
        
        if (estado != null) {
            contactos = contactoService.listarPorEstado(estado);
        } else {
            contactos = contactoService.listarTodos();
        }
        
        // Estadísticas para el dashboard
        model.addAttribute("contactos", contactos);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("totalContactos", contactoService.listarTodos().size());
        model.addAttribute("pendientes", contactoService.contarPorEstado(EstadoContacto.PENDIENTE));
        model.addAttribute("enProceso", contactoService.contarPorEstado(EstadoContacto.EN_PROCESO));
        model.addAttribute("respondidos", contactoService.contarPorEstado(EstadoContacto.RESPONDIDO));
        model.addAttribute("cerrados", contactoService.contarPorEstado(EstadoContacto.CERRADO));
        model.addAttribute("estados", EstadoContacto.values());
        
        return "admin/contactos/lista";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        Contacto contacto = contactoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Contacto no encontrado"));
        
        model.addAttribute("contacto", contacto);
        model.addAttribute("estados", EstadoContacto.values());
        
        return "admin/contactos/detalle";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoContacto nuevoEstado,
            RedirectAttributes redirectAttributes) {
        
        try {
            contactoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/admin/contactos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarContacto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contactoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Contacto eliminado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el contacto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/admin/contactos";
    }
}
