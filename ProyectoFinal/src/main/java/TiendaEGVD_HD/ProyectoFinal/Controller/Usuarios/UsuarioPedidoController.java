package TiendaEGVD_HD.ProyectoFinal.Controller.Usuarios;

import TiendaEGVD_HD.ProyectoFinal.Model.Pedido;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.DetallePedidoService;
import TiendaEGVD_HD.ProyectoFinal.Service.PedidoService;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class UsuarioPedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private DetallePedidoService detallePedidoService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarPedidos(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        
        Usuario usuario = usuarioService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<Pedido> pedidos = pedidoService.listarPorUsuario(usuario.getId());
        
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("usuario", usuario);
        
        return "pedidos/lista";
    }

    @GetMapping("/{id}")
    public String verDetallePedido(@PathVariable Long id, Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        
        Usuario usuario = usuarioService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Pedido pedido = pedidoService.buscarPorId(id);
        
        // Verificar que el pedido pertenece al usuario actual
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/pedidos";
        }
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", pedido.getDetalles());
        
        return "pedidos/detalles";
    }
}
