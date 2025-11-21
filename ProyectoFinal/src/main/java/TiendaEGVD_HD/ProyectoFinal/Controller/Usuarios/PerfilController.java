package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String verPerfil(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        
        Usuario usuario = usuarioService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        model.addAttribute("usuario", usuario);
        return "perfil/mi-perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        
        try {
            // Obtener el usuario actual de la base de datos
            Usuario usuarioActual = usuarioService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            // Validar que no se cambie el username a uno existente
            if (!usuarioActual.getUsername().equals(usuario.getUsername())) {
                if (usuarioService.findByUsername(usuario.getUsername()).isPresent()) {
                    result.rejectValue("username", "error.usuario", "El nombre de usuario ya existe");
                }
            }
            
            // Validar que no se cambie el email a uno existente
            if (!usuarioActual.getEmail().equals(usuario.getEmail())) {
                if (usuarioService.findByEmail(usuario.getEmail()).isPresent()) {
                    result.rejectValue("email", "error.usuario", "El email ya está registrado");
                }
            }
            
            if (result.hasErrors()) {
                return "perfil/mi-perfil";
            }
            
            // Actualizar solo los campos permitidos
            usuarioActual.setUsername(usuario.getUsername());
            usuarioActual.setEmail(usuario.getEmail());
            usuarioActual.setNombre(usuario.getNombre());
            usuarioActual.setCelular(usuario.getCelular());
            
            usuarioService.save(usuarioActual);
            
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar perfil: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/perfil";
    }

    @PostMapping("/cambiar-password")
    public String cambiarPassword(
            @RequestParam String passwordActual,
            @RequestParam String nuevaPassword,
            @RequestParam String confirmarPassword,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        
        try {
            Usuario usuario = usuarioService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            // Validar password actual
            if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
                redirectAttributes.addFlashAttribute("mensaje", "La contraseña actual es incorrecta");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/perfil";
            }
            
            // Validar que las nuevas contraseñas coincidan
            if (!nuevaPassword.equals(confirmarPassword)) {
                redirectAttributes.addFlashAttribute("mensaje", "Las nuevas contraseñas no coinciden");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/perfil";
            }
            
            // Validar longitud mínima
            if (nuevaPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("mensaje", "La nueva contraseña debe tener al menos 6 caracteres");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/perfil";
            }
            
            // Actualizar contraseña
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioService.save(usuario);
            
            redirectAttributes.addFlashAttribute("mensaje", "Contraseña actualizada correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar contraseña: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/perfil";
    }
}
