package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Model.UsuarioRegistroDto;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Mostrar formulario de registro
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        System.out.println("🔍 Accediendo a /auth/nuevo - Mostrando formulario de registro");
        model.addAttribute("usuarioRegister", new UsuarioRegistroDto());
        System.out.println("✅ Retornando vista: auth/usuario-register");
        return "auth/usuario-register";
    }

    /**
     * Procesar registro de nuevo usuario
     */
    @PostMapping("/guardar1")
    public String registrarUsuario(
            @ModelAttribute("usuarioRegister") @Valid UsuarioRegistroDto usuarioDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Verificar errores de validación
        if (result.hasErrors()) {
            System.out.println("❌ Errores de validación en registro: " + result.getAllErrors());
            model.addAttribute("usuarioRegister", usuarioDto);
            return "auth/usuario-register";
        }

        try {
            // Convertir DTO a Usuario y registrar
            Usuario usuario = usuarioDto.toUsuario();
            usuarioService.registrarUsuario(usuario);

            // Registro exitoso
            redirectAttributes.addFlashAttribute("exito",
                "¡Registro exitoso! Ya puedes iniciar sesión con tu cuenta.");

            return "redirect:/auth/login";

        } catch (IllegalArgumentException e) {
            // Error de validación de negocio (email o username duplicado)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuarioRegister", usuarioDto);
            return "auth/usuario-register";

        } catch (Exception e) {
            // Error inesperado
            System.err.println("❌ Error inesperado en registro: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("error", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo.");
            model.addAttribute("usuarioRegister", usuarioDto);
            return "auth/usuario-register";
        }
    }
}
