package TiendaEGVD_HD.ProyectoFinal.Config;

import TiendaEGVD_HD.ProyectoFinal.Model.Role;
import TiendaEGVD_HD.ProyectoFinal.Repository.RoleRepository;
import TiendaEGVD_HD.ProyectoFinal.enums.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            System.out.println("🔧 Inicializando roles...");
            for (RoleName roleName : RoleName.values()) {
                if (!roleRepository.findByName(roleName).isPresent()) {
                    roleRepository.save(new Role(roleName));
                    System.out.println("✅ Rol creado: " + roleName);
                } else {
                    System.out.println("ℹ️ Rol ya existe: " + roleName);
                }
            }
            System.out.println("🔧 Inicialización de roles completada");
        };
    }

}
