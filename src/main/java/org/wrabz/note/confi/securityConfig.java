package org.wrabz.note.confi;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.wrabz.note.model.AppRole;
import org.wrabz.note.model.Role;
import org.wrabz.note.model.User;
import org.wrabz.note.repository.RoleRepository;
import org.wrabz.note.repository.UserRepository;
import org.wrabz.note.security.jwt.AuthEntryPointJwt;
import org.wrabz.note.security.jwt.AuthTokenFilter;
import org.wrabz.note.security.jwt.JwtUtils;
import org.wrabz.note.security.service.UserDetailsServiceImpl;

import java.time.LocalDate;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class securityConfig {

   private final AuthEntryPointJwt unauthorizedHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/auth/public/**"))

                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/csrf-token").permitAll()
                                .requestMatchers("/api/auth/public/**").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))

                .addFilterBefore(authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {

            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUsername("user")) {
                User user = new User("user",
                        "user@example.com",
                        passwordEncoder().encode("password"));

                user.setAccountNonLocked(true);
                user.setAccountNonExpired(true);
                user.setCredentialNonexpired(true);
                user.setEnabled(true);

                user.setCredentialExpiryDate(LocalDate.now().plusYears(1));
                user.setAccountExpiryDate(LocalDate.now().plusYears(1));

                user.setTwoFactorEnabled(false);
                user.setSignUpMethod("email");
                user.setRole(userRole);

                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder().encode("password"));

                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialNonexpired(true);
                admin.setEnabled(true);

                admin.setCredentialExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));

                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);

                userRepository.save(admin);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception {
        return authentication.getAuthenticationManager();
    }
}