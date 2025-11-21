package TiendaEGVD_HD.ProyectoFinal.Controller.Pedidos;

import TiendaEGVD_HD.ProyectoFinal.Service.PedidoService;
import TiendaEGVD_HD.ProyectoFinal.enums.EstadoPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos1")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "admin/pedidos/lista";
    }

    @GetMapping("/detalles/{id}")
    public String verDetalles(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "admin/pedidos/detalles";
    }

    @PostMapping("/{id}/cambiar-estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido nuevoEstado,
            RedirectAttributes redirectAttributes) {

        try {
            pedidoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido actualizado correctamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/pedidos";
    }
}