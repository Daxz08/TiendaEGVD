package TiendaEGVD_HD.ProyectoFinal.Controller.Productos;

import TiendaEGVD_HD.ProyectoFinal.Model.Categoria;
import TiendaEGVD_HD.ProyectoFinal.Service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Listar categorías (para menú desplegable)
    @ModelAttribute("categorias")
    public List<Categoria> listarCategorias() {
        return categoriaService.listarTodas();
    }

    // Formulario de nueva categoría
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.guardarCategoria(categoria);
        return "redirect:/productos/nuevo"; // Redirige al form de producto
    }
}