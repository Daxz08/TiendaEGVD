package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioAuthController {

    private final UsuarioService usuarioService;

    public UsuarioAuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/perfil")
    public String verPerfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String usernameOrEmail = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(usernameOrEmail)
                .orElseGet(() -> usuarioService.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));

        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEdicion(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String usernameOrEmail = userDetails.getUsername();
        Usuario usuario = usuarioService.findByUsername(usernameOrEmail)
                .orElseGet(() -> usuarioService.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));

        model.addAttribute("usuario", usuario);
        return "usuario/editar";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuario usuario) {
        usuarioService.save(usuario);
        return "redirect:/usuario/perfil?actualizado";
    }
}