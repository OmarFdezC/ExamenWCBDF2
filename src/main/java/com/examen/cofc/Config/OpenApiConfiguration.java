package com.examen.cofc.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configuración de la seguridad personalizada
        return httpSecurity
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/api/v1/inventories/**") // Ignorar CSRF en rutas
                        // específicas
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-resources/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/inventories/**").hasAnyAuthority("READ")
                            .requestMatchers(HttpMethod.POST, "/api/v1/inventories/**").hasAnyAuthority("CREATE")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/inventories/**").hasAnyAuthority("UPDATE")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/inventories/**").hasAnyAuthority("DELETE")
                            .anyRequest().denyAll();
                })
                .build();
    }

    //authentication manager - Lo obtenemos de una instancia que ya existe
    public AuthenticationManager authenticationManager() throws Exception{
        return  authenticationConfiguration.getAuthenticationManager();
    }

    // authentication provider - DAO - Va a proporcionar la autenticacion
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    // Password encoder
    public PasswordEncoder passwordEncoder(){
        //return  new BCryptPasswordEncoder();
        return  NoOpPasswordEncoder.getInstance();
    }

    // UserDetailsService - base de datos o usuarios en memoria
    @Bean
    public UserDetailsService userDetailsService(){
        // Definir usuario en memoria por el momento
        UserDetails usuarioOmar = User.withUsername("Omar").password(passwordEncoder().encode("omar1234")).roles("ADMIN").authorities(getAuthorities("ADMIN")).build();
        UserDetails usuarioMiguel = User.withUsername("Miguel").password(passwordEncoder().encode("miguel1234")).roles("USER").authorities(getAuthorities("USER")).build();
        UserDetails usuarioAlejandro = User.withUsername("Alejandro").password(passwordEncoder().encode("alejandro1234")).roles("MODERATOR").authorities(getAuthorities("MODERATOR")).build();
        UserDetails usuarioAxel = User.withUsername("Axel").password(passwordEncoder().encode("axel1234")).roles("EDITOR").authorities(getAuthorities("EDITOR")).build();
        UserDetails usuarioGiovanni = User.withUsername("Giovanni").password(passwordEncoder().encode("giovanni1234")).roles("DEVELOPER").authorities(getAuthorities("DEVELOPER")).build();
        UserDetails usuarioDesiree = User.withUsername("Desiree").password(passwordEncoder().encode("desiree1234")).roles("ANALYST").authorities(getAuthorities("ANALYST")).build();




        List<UserDetails> userDetailsList = new ArrayList<>();
        userDetailsList.add(usuarioOmar);
        userDetailsList.add(usuarioMiguel);
        userDetailsList.add(usuarioAlejandro);
        userDetailsList.add(usuarioAxel);
        userDetailsList.add(usuarioGiovanni);
        userDetailsList.add(usuarioDesiree);
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    private String[] getAuthorities(String role) {
        switch (role) {
            case "ADMIN":
                return new String[]{"READ", "CREATE", "UPDATE", "DELETE"};
            case "USER":
                return new String[]{"READ"};
            case "MODERATOR":
                return new String[]{"READ", "UPDATE"};
            case "EDITOR":
                return new String[]{"UPDATE"};
            case "DEVELOPER":
                return new String[]{"READ", "CREATE", "UPDATE", "DELETE", "CREATE-USER"};
            case "ANALYST":
                return new String[]{"READ", "DELETE"};
            default:
                return new String[]{};
        }
    }
}
