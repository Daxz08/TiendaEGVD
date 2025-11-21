package TiendaEGVD_HD.ProyectoFinal.Controller.Contacto;

import TiendaEGVD_HD.ProyectoFinal.Model.Contacto;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.ContactoService;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contacto")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;
    @Autowired
    private UsuarioService usuarioService;

    // --- 🔹 Mostrar formulario ---
    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("contacto", new Contacto()); // Inicializa un objeto vacío
        return "contacto";
    }

    // --- 🔹 Procesar formulario con validación ---
    @PostMapping
    public String guardarContacto(
            @ModelAttribute("contacto") @Valid Contacto contacto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            System.out.println("❌ Errores de validación detectados.");
            model.addAttribute("contacto", contacto); // Mantiene datos ingresados
            return "contacto"; // Regresa a contacto.html con errores visibles
        }

        // Vincular con usuario si existe
        Usuario usuario = usuarioService.buscarPorEmail(contacto.getEmail());
        if (usuario != null) {
            contacto.setUsuario(usuario);
        }

        contactoService.guardarContacto(contacto);
        return "redirect:/contacto"; // Regresa al formulario después de guardar
    }
}