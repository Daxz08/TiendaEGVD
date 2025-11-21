package TiendaEGVD_HD.ProyectoFinal.Controller.Productos;

import TiendaEGVD_HD.ProyectoFinal.Service.CategoriaService;
import TiendaEGVD_HD.ProyectoFinal.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productos")
public class ProductosViewController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("")
    public String mostrarProductos() {
        return "productos";  // Esto apuntará a tu HTML llamado productos.html
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        System.out.println("🛍️ Accediendo al catálogo de productos");

        try {
            model.addAttribute("productos", productoService.listarDisponibles());
            model.addAttribute("categorias", categoriaService.listarTodas());
            System.out.println("✅ Productos disponibles cargados: " + productoService.listarDisponibles().size());
            return "productos/catalogo";
        } catch (Exception e) {
            System.err.println("❌ Error al cargar catálogo: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}
