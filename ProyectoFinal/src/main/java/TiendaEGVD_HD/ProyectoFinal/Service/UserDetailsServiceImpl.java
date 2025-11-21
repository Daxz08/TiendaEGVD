package TiendaEGVD_HD.ProyectoFinal.Service;

import TiendaEGVD_HD.ProyectoFinal.Model.Usuario;
import TiendaEGVD_HD.ProyectoFinal.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

@Autowired
 private UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       System.out.println("🔍 Intentando autenticar usuario: " + usernameOrEmail);

       try {
           // Intentar buscar por username primero
           Usuario usuario = usuarioRepository.findByUsername(usernameOrEmail)
                   .orElse(null);

           // Si no se encuentra por username, intentar por email
           if (usuario == null) {
               System.out.println("❌ No encontrado por username, intentando por email...");
               usuario = usuarioRepository.findByEmail(usernameOrEmail)
                       .orElse(null);
           }

           // Si aún no se encuentra, lanzar excepción
           if (usuario == null) {
               System.err.println("❌ Usuario no encontrado: " + usernameOrEmail);
               throw new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail);
           }

           System.out.println("✅ Usuario encontrado: " + usuario.getUsername() + " con roles: " + usuario.getRoles().size());
           return usuario; // Usuario ya implementa UserDetails

       } catch (Exception e) {
           System.err.println("❌ Error en loadUserByUsername: " + e.getMessage());
           e.printStackTrace();
           throw new UsernameNotFoundException("Error al cargar usuario: " + usernameOrEmail, e);
       }
 }

}