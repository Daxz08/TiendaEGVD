package TiendaEGVD_HD.ProyectoFinal.Service;


import TiendaEGVD_HD.ProyectoFinal.Model.Categoria;
import TiendaEGVD_HD.ProyectoFinal.Model.Producto;
import TiendaEGVD_HD.ProyectoFinal.Model.ProductoDto;
import TiendaEGVD_HD.ProyectoFinal.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaService categoriaService;

    @Transactional
    public Producto guardarProducto(ProductoDto productoDto) {
        System.out.println("💾 Guardando producto: " + productoDto.getNombre());

        Categoria categoria = categoriaService.buscarPorId(productoDto.getCategoriaId());

        Producto producto;

        // Si tiene ID, es una edición
        if (productoDto.getId() != null) {
            System.out.println("🔄 Editando producto existente con ID: " + productoDto.getId());
            producto = productoRepository.findById(productoDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        } else {
            System.out.println("🆕 Creando nuevo producto");
            producto = new Producto();
        }

        // Actualizar datos
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setPrecio(productoDto.getPrecio());
        producto.setStock(productoDto.getStock());
        producto.setCategoria(categoria);

        // Lógica automática de estado
        producto.setEstado(determinarEstadoProducto(productoDto.getStock(), productoDto.getEstado()));

        // Lógica para guardar la imagen (implementar según necesidad)
        if (productoDto.getImagen() != null && !productoDto.getImagen().isEmpty()) {
            System.out.println("📷 Procesando imagen...");
            producto.setImagenUrl(almacenarImagen(productoDto.getImagen()));
        }

        Producto productoGuardado = productoRepository.save(producto);
        System.out.println("✅ Producto guardado con ID: " + productoGuardado.getId());

        return productoGuardado;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarDisponibles() {
        return productoRepository.findByEstado(TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DISPONIBLE);
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    @Transactional
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    private String almacenarImagen(MultipartFile imagen) {
        try {
            // Crear directorio externo para imágenes (fuera del classpath)
            String uploadDir = System.getProperty("user.dir") + "/uploads/img/";

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                System.out.println("📁 Directorio creado: " + uploadDir + " - Éxito: " + created);
            }

            // Generar nombre único para la imagen
            String originalFilename = imagen.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                System.err.println("❌ Nombre de archivo inválido");
                return null;
            }

            String extension = "";
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            String filename = System.currentTimeMillis() + "_" +
                            originalFilename.replaceAll("[^a-zA-Z0-9.]", "_");

            // Guardar archivo
            String filePath = uploadDir + filename;
            imagen.transferTo(new File(filePath));

            System.out.println("📷 Imagen guardada en: " + filePath);
            System.out.println("📷 Ruta para HTML: " + filename);

            // Retornar solo el nombre del archivo (se guardará en /img/)
            return filename;

        } catch (Exception e) {
            System.err.println("❌ Error al guardar imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto determinarEstadoProducto(int stock, TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto estadoSolicitado) {
        // Si el estado solicitado es DESCONTINUADO, siempre respetarlo
        if (estadoSolicitado == TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DESCONTINUADO) {
            return TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DESCONTINUADO;
        }

        // Si el stock es 0, automáticamente es AGOTADO
        if (stock == 0) {
            return TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.AGOTADO;
        }

        // Si hay stock y el estado solicitado es AGOTADO, cambiar a DISPONIBLE
        if (stock > 0 && estadoSolicitado == TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.AGOTADO) {
            return TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DISPONIBLE;
        }

        // En otros casos, usar el estado solicitado
        return estadoSolicitado != null ? estadoSolicitado : TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DISPONIBLE;
    }

    @Transactional
    public void actualizarEstadoProducto(Long productoId, TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto nuevoEstado) {
        Producto producto = buscarPorId(productoId);
        producto.setEstado(nuevoEstado);
        productoRepository.save(producto);
    }

    @Transactional
    public void verificarYActualizarEstadosPorStock() {
        // Cambiar a AGOTADO todos los productos DISPONIBLES con stock 0
        productoRepository.findAll().forEach(producto -> {
            if (producto.getStock() == 0 &&
                producto.getEstado() == TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DISPONIBLE) {
                producto.setEstado(TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.AGOTADO);
                productoRepository.save(producto);
            } else if (producto.getStock() > 0 &&
                       producto.getEstado() == TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.AGOTADO) {
                producto.setEstado(TiendaEGVD_HD.ProyectoFinal.enums.EstadoProducto.DISPONIBLE);
                productoRepository.save(producto);
            }
        });
    }
}