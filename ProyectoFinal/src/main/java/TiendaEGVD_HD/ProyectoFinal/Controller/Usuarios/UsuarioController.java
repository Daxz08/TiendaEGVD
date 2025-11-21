package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import TiendaEGVD_HD.ProyectoFinal.Model.Role;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.RoleService;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    // --- 🔹 Mostrar lista y formulario en la misma vista ---
    @GetMapping
    public String listarUsuarios(Model model) {
        List<Role> roles = roleService.findAllRoles();

        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("usuario", new Usuario()); // Para creación o edición
        model.addAttribute("allRoles", roles); // Roles disponibles
        return "admin/usuarios/lista"; // Unifica lista y formulario
    }

    // --- 🔹 Cargar usuario para edición desde la misma vista ---
    @GetMapping("/editar/{id}")
    public String cargarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        List<Role> roles = roleService.findAllRoles();

        model.addAttribute("usuarios", usuarioService.listarTodos()); // Lista de usuarios
        model.addAttribute("usuario", usuario); // Usuario a editar
        model.addAttribute("allRoles", roles); // Roles disponibles
        return "admin/usuarios/lista"; // Mantiene la vista lista.html
    }

    // --- 🔹 Guardar usuario (Nuevo o Editado) ---
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // En caso de error, recargar datos necesarios
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "admin/usuarios/lista";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar usuario: " + e.getMessage());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "admin/usuarios/lista";
        }
    }

    // --- 🔹 Procesar edición y redirigir correctamente ---
    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // En caso de error, recargar datos necesarios
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "admin/usuarios/lista";
        }

        try {
            usuario.setId(id);
            usuarioService.guardarUsuario(usuario);
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar usuario: " + e.getMessage());
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("allRoles", roleService.findAllRoles());
            return "admin/usuarios/lista";
        }
    }

    // --- 🔹 Eliminar usuario ---
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    // --- 🔹 Autocompletado por email ---
    @GetMapping("/buscar-por-email")
    @ResponseBody
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(usuario); // Devuelve 200 con datos o 404 si no existe
    }
}