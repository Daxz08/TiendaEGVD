package TiendaEGVD_HD.ProyectoFinal.Controller.Productos;

import TiendaEGVD_HD.ProyectoFinal.Model.Producto;
import TiendaEGVD_HD.ProyectoFinal.Model.ProductoDto;
import TiendaEGVD_HD.ProyectoFinal.Service.CategoriaService;
import TiendaEGVD_HD.ProyectoFinal.Service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;

    // --- 🔹 Mostrar lista y formulario en la misma vista ---
    @GetMapping
    public String listarProductos(
            @RequestParam(value = "exito", required = false) String exito,
            Model model) {
        System.out.println("🔍 Accediendo a /admin/productos");

        try {
            System.out.println("📦 Cargando productos...");
            List<Producto> productos = productoService.listarTodos();
            model.addAttribute("productos", productos);
            System.out.println("📦 Productos cargados: " + productos.size());

            System.out.println("📝 Creando ProductoDto para formulario...");
            model.addAttribute("producto", new ProductoDto()); // Usar DTO para el formulario

            System.out.println("🏷️ Cargando categorías...");
            model.addAttribute("categorias", categoriaService.listarTodas());

            // Agregar mensaje de éxito si existe
            if (exito != null) {
                model.addAttribute("exito", exito);
            }

            System.out.println("✅ Retornando vista: admin/productos/lista");
            return "admin/productos/lista"; // Vista original

        } catch (Exception e) {
            System.err.println("❌ Error en listarProductos: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    // --- 🔹 Cargar producto para edición ---
    @GetMapping("/editar/{id}")
    public String cargarProducto(@PathVariable Long id, Model model) {
        try {
            Producto producto = productoService.buscarPorId(id);

            // Convertir Producto a ProductoDto para el formulario
            ProductoDto productoDto = new ProductoDto();
            productoDto.setId(producto.getId()); // Importante para ediciones
            productoDto.setNombre(producto.getNombre());
            productoDto.setDescripcion(producto.getDescripcion());
            productoDto.setPrecio(producto.getPrecio());
            productoDto.setStock(producto.getStock());
            productoDto.setEstado(producto.getEstado());
            productoDto.setCategoriaId(producto.getCategoria().getId());

            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("producto", productoDto); // DTO para edición
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("productoId", id); // ID para el formulario de edición

            return "admin/productos/lista";
        } catch (Exception e) {
            return "redirect:/admin/productos";
        }
    }

    // --- 🔹 Guardar producto (Nuevo o Editado) ---
    @PostMapping("/guardar")
    public String guardarProducto(
            @ModelAttribute("producto") @Valid ProductoDto productoDto,
            BindingResult result,
            Model model
    ) {
        System.out.println("🔄 Recibiendo producto para guardar...");
        System.out.println("📝 ID: " + productoDto.getId());
        System.out.println("📝 Nombre: " + productoDto.getNombre());
        System.out.println("📝 Descripción: " + productoDto.getDescripcion());
        System.out.println("📝 Precio: " + productoDto.getPrecio());
        System.out.println("📝 Categoría ID: " + productoDto.getCategoriaId());
        System.out.println("📝 Imagen: " + (productoDto.getImagen() != null ? productoDto.getImagen().getOriginalFilename() : "null"));

        if (result.hasErrors()) {
            System.out.println("❌ Errores de validación detectados: " + result.getAllErrors());
            model.addAttribute("productos", productoService.listarTodos()); // Mantiene la lista
            model.addAttribute("categorias", categoriaService.listarTodas()); // Mantiene categorías
            return "admin/productos/lista"; // Regresar a la misma vista con datos visibles
        }

        try {
            Producto productoGuardado = productoService.guardarProducto(productoDto);
            System.out.println("✅ Producto guardado correctamente con ID: " + productoGuardado.getId());

            // Agregar mensaje de éxito y redirigir
            return "redirect:/admin/productos?exito=Producto guardado correctamente";
        } catch (Exception e) {
            System.err.println("❌ Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar producto: " + e.getMessage());
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "admin/productos/lista";
        }
    }

    // --- 🔹 Eliminar producto ---
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/admin/productos";
    }
}