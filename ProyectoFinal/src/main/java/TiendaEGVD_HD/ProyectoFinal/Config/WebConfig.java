package TiendaEGVD_HD.ProyectoFinal.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configurar el directorio de imágenes externo
        String uploadDir = System.getProperty("user.dir") + "/uploads/img/";
        
        // Crear el directorio si no existe
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            System.out.println("📁 Directorio de uploads creado: " + uploadDir + " - Éxito: " + created);
        }
        
        // Mapear /img/** a la carpeta de uploads externa
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + uploadDir)
                .addResourceLocations("classpath:/static/img/"); // Fallback a recursos estáticos
        
        // Configuración adicional para otros recursos estáticos
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
                
        System.out.println("🔧 Configuración de recursos estáticos completada");
        System.out.println("📁 Directorio de imágenes: " + uploadDir);
    }
}
