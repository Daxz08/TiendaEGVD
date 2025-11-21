package TiendaEGVD_HD.ProyectoFinal.Controller.Productos;

import TiendaEGVD_HD.ProyectoFinal.Model.CarritoItem;
import TiendaEGVD_HD.ProyectoFinal.Model.Pedido;
import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Service.CarritoService;
import TiendaEGVD_HD.ProyectoFinal.Service.PedidoService;
import TiendaEGVD_HD.ProyectoFinal.Service.ProductoService;
import TiendaEGVD_HD.ProyectoFinal.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ProductoService productoService;

    // Obtener o crear carrito en sesión
    @SuppressWarnings("unchecked")
    private Map<Long, CarritoItem> obtenerCarrito(HttpSession session) {
        Map<Long, CarritoItem> carrito = (Map<Long, CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    // Ver carrito
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        List<CarritoItem> items = carritoService.obtenerItems(carrito);
        Double total = carritoService.calcularTotal(carrito);
        Integer cantidadTotal = carritoService.obtenerCantidadTotal(carrito);

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("cantidadTotal", cantidadTotal);
        model.addAttribute("carritoVacio", items.isEmpty());

        return "carrito/ver";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String agregarProducto(
            @RequestParam Long productoId,
            @RequestParam(defaultValue = "1") Integer cantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Map<Long, CarritoItem> carrito = obtenerCarrito(session);
            carritoService.agregarProducto(carrito, productoId, cantidad);
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/productos/catalogo";
    }

    // Actualizar cantidad
    @PostMapping("/actualizar")
    public String actualizarCantidad(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            Map<Long, CarritoItem> carrito = obtenerCarrito(session);
            carritoService.actualizarCantidad(carrito, productoId, cantidad);
            
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/carrito";
    }

    // Eliminar producto
    @PostMapping("/eliminar")
    public String eliminarProducto(
            @RequestParam Long productoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        carritoService.eliminarProducto(carrito, productoId);
        
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        
        return "redirect:/carrito";
    }

    // Limpiar carrito
    @PostMapping("/limpiar")
    public String limpiarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        carritoService.limpiarCarrito(carrito);
        
        redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        
        return "redirect:/carrito";
    }

    // Procesar pedido
    @PostMapping("/procesar-pedido")
    public String procesarPedido(
            HttpSession session,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        if (authentication == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión para realizar un pedido");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/auth/login";
        }
        
        try {
            Map<Long, CarritoItem> carrito = obtenerCarrito(session);
            
            if (carrito.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "El carrito está vacío");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/carrito";
            }
            
            // Validar stock
            if (!carritoService.validarStock(carrito)) {
                redirectAttributes.addFlashAttribute("mensaje", "Algunos productos no tienen stock suficiente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/carrito";
            }
            
            // Obtener usuario
            Usuario usuario = usuarioService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            
            // Crear pedido
            Map<Long, Integer> productosConCantidad = carritoService.convertirAMapaCantidades(carrito);
            Pedido pedido = pedidoService.crearPedido(usuario.getId(), productosConCantidad);
            
            // Limpiar carrito
            carritoService.limpiarCarrito(carrito);
            
            redirectAttributes.addFlashAttribute("mensaje", "Pedido creado exitosamente. ID: " + pedido.getId());
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/pedidos";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar pedido: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/carrito";
        }
    }

    // API para obtener cantidad del carrito (para navbar)
    @GetMapping("/cantidad")
    @ResponseBody
    public Map<String, Object> obtenerCantidadCarrito(HttpSession session) {
        Map<Long, CarritoItem> carrito = obtenerCarrito(session);
        Integer cantidad = carritoService.obtenerCantidadTotal(carrito);
        
        Map<String, Object> response = new HashMap<>();
        response.put("cantidad", cantidad);
        return response;
    }
}
