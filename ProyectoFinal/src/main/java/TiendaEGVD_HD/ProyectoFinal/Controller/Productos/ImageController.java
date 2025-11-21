package TiendaEGVD_HD.ProyectoFinal.Controller.Productos;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/uploads")
public class ImageController {

    @GetMapping("/img/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        try {
            System.out.println("🖼️ Solicitando imagen de uploads: " + filename);

            // Solo buscar en el directorio de uploads (para productos subidos)
            String uploadDir = System.getProperty("user.dir") + "/uploads/img/";
            Path uploadPath = Paths.get(uploadDir + filename);

            if (Files.exists(uploadPath)) {
                System.out.println("✅ Imagen encontrada en uploads: " + uploadPath);
                Resource resource = new FileSystemResource(uploadPath.toFile());

                // Determinar el tipo de contenido
                String contentType = Files.probeContentType(uploadPath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            }

            System.out.println("❌ Imagen no encontrada en uploads: " + filename);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            System.err.println("❌ Error al servir imagen " + filename + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
