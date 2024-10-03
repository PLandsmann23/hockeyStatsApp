import cz.landspa.statsapp.repository.UserRepository;
import cz.landspa.statsapp.service.impl.UserDetailsServiceImpl;
import cz.landspa.statsapp.util.CustomAuthenticationFailureHandler;
import cz.landspa.statsapp.util.JwtUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

  //  private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final CustomAuthenticationProvider customAuthenticationProvider;

 //   private final PasswordEncoder passwordEncoder;



    public SecurityConfig(JwtRequestFilter jwtRequestFilter,/* UserDetailsServiceImpl userDetailsService,*/ JwtUtil jwtUtil, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationProvider customAuthenticationProvider/*, PasswordEncoder passwordEncoder*/) {
        this.jwtRequestFilter = jwtRequestFilter;
      //  this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
     //   this.passwordEncoder = passwordEncoder;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /*

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws  Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
*/

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }



    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests)-> requests
                    .requestMatchers("/css/**", "/img/**", "/js/**", "/svg/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .requestMatchers("/**").authenticated()
                    .anyRequest().permitAll()
                )
                .formLogin((form)-> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler((request, response, authentication) -> {
                            // Vygeneruj JWT token po úspěšném přihlášení
                            String token = jwtUtil.generateToken(authentication.getName());
                            // Nastavit jako JSON odpověď
                            response.setContentType("application/json");
                            response.getWriter().write("{\"token\":\"" + token + "\"}");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );




        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
