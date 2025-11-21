package TiendaEGVD_HD.ProyectoFinal.Security;

import TiendaEGVD_HD.ProyectoFinal.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeHttpRequests(authz -> authz
                        // Recursos estáticos y páginas públicas
                        .requestMatchers(
                                "/",
                                "/index",
                                "/auth/**",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/uploads/**",
                                "/static/**",
                                "/error",
                                "/contacto",
                                "/productos/catalogo",
                                "/productos/listar",
                                "/carrito/**",
                                "/registro",
                                "/recuperar-password",
                                "/h2-console/**"  // Consola H2 para desarrollo
                        ).permitAll()

                        // Rutas de administración
                        .requestMatchers(
                                "/admin/**",
                                "/usuarios/**",
                                "/productos/formulario/**",
                                "/productos/editar/**",
                                "/productos/eliminar/**",
                                "/pedidos/lista",
                                "/pedidos/gestionar"
                        ).hasRole("ADMIN") // Equivale a ROLE_ADMIN

                        // Rutas para usuarios registrados (tanto ADMIN como USUARIO)
                        .requestMatchers(
                                "/pedidos/**",
                                "/productos/crear",
                                "/perfil/**",
                                "/carrito/**"
                        ).hasAnyRole("ADMIN", "USUARIO")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}