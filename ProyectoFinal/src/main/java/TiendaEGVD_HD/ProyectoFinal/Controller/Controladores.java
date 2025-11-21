package TiendaEGVD_HD.ProyectoFinal.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controladores {

    @GetMapping("/")
    public String index(Model model) {
        // Obtener información del usuario autenticado si existe
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("usuarioAutenticado", auth.getName());
            model.addAttribute("roles", auth.getAuthorities());
        }

        return "index";
    }

    @GetMapping("/index")
    public String indexAlternativo(Model model) {
        return index(model);
    }
}

