package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/auth/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        System.out.println("🔍 Accediendo a /auth/login");
        model.addAttribute("error", error != null ? "Email o contraseña incorrectos" : null);
        model.addAttribute("logout", logout != null ? "Has cerrado sesión correctamente" : null);

        return "auth/login";
    }

    // Redirección para /login -> /auth/login
    @GetMapping("/login")
    public String redirigirLogin() {
        System.out.println("🔄 Redirigiendo de /login a /auth/login");
        return "redirect:/auth/login";
    }
}