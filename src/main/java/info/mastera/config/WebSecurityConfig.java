package info.mastera.config;

import info.mastera.authorization.RestAuthenticationEntryPoint;
import info.mastera.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Config for Spring Security
 * Need to be extended from {@link WebSecurityConfigurerAdapter }
 */
@Configuration
//is used to enable web security in a project
@EnableWebSecurity
//is used to enable Spring Security global method security
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScans({@ComponentScan("info.mastera.authorization"),
        @ComponentScan("info.mastera.filter"),
        @ComponentScan("info.mastera.security"),
        @ComponentScan("info.mastera.api.security")})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Указываем Spring контейнеру, что надо инициализировать ShaPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Authorization configuration
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        auth.userDetailsService(userDetailsService)
                // encoder is required
                .passwordEncoder(encoder());
    }

    /**
     * Default access configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                //в случае необходимости логина перенаправляет на этот ресурс.
                // А там настроено так, что выкидывает 401 UNAUTHORIZED.
                // Т.е. надо сначала залогиниться, а потом совершать операции.
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                // указываем правила запросов
                // по которым будет определятся доступ к ресурсам и остальным данным
                //Удаления разрешены только пользователям с ролью ADMIN
                .antMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")
                .antMatchers("/auth/login").permitAll()
                //получение информации о пользователях разрешено только авторизованным пользователям
                //для всех операций /user/?что-то там? (пример /user/1)
                .antMatchers("/user/*").authenticated()
                //для всех операций /user с любой дальнейшей вложенностью,
                // т.е. перекрывает предыдущее( пример /user для получения всех пользователей)
                .antMatchers("/user/**").authenticated()
                //операции с foo разрешены всем
                .antMatchers("/foos").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}