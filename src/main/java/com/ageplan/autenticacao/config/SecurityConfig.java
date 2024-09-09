package com.ageplan.autenticacao.config;

import com.ageplan.autenticacao.usuario.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração de segurança do Spring Security.
 * Define as configurações de autenticação e autorização para a aplicação.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Define o codificador de senhas a ser utilizado na aplicação.
     * Utiliza o algoritmo BCrypt para codificação.
     *
     * @return uma instância de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o gerenciador de autenticação com um provedor de autenticação DAO.
     * O provedor utiliza o serviço de usuário e o codificador de senhas definidos.
     *
     * @param usuarioService o serviço de usuário a ser utilizado pelo provedor de autenticação
     * @param passwordEncoder o codificador de senhas a ser utilizado pelo provedor de autenticação
     * @return uma instância de {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    /**
     * Configura a cadeia de filtros de segurança do Spring Security.
     * Define as regras de autorização para diferentes endpoints e configura o login e logout.
     *
     * @param http o objeto {@link HttpSecurity} a ser configurado
     * @return uma instância de {@link SecurityFilterChain}
     * @throws Exception se ocorrer um erro durante a configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**", "/api/auth/**", "/login", "/logout", "/api/usuarios/registro").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/professor/**").hasRole("PROFESSOR")
                        .requestMatchers("/api/aluno/**").hasRole("ALUNO")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/api/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .httpBasic(httpBasic -> {
                });

        return http.build();
    }
}