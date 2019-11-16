package info.mastera.config;

import info.mastera.authorization.RestAuthenticationEntryPoint;
import info.mastera.authorization.SavedRequestAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Config for Spring Security
 * Need to be extended from {@link WebSecurityConfigurerAdapter }
 */
@Configuration
@EnableWebSecurity
@ComponentScan("info.mastera.authorization")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private SavedRequestAuthenticationSuccessHandler mySuccessHandler;

    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    /**
     * Указываем Spring контейнеру, что надо инициализировать ShaPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * In-memory realisation of users
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adm")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder().encode("u")).roles("USER");
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
                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                //получение информации о пользователях разрешено только авторизованным пользователям
                //для всех операций /user/?что-то там? (пример /user/1)
                .antMatchers("/user/*").authenticated()
                //для всех операций /user с любой дальнейшей вложенностью,
                // т.е. перекрывает предыдущее( пример /user для получения всех пользователей)
                .antMatchers("/user/**").authenticated()
                //операции с foo разрешены всем
                .antMatchers("/foos").permitAll()
                .and()
                //The formLogin element will create this filter and also provides additional
                // methods successHandler and failureHandler to set our custom authentication success and failure handlers respectively.
                //для текущей авторизации нужен POST запрос с открытой передаче данных логина
                // пример http://localhost:8080/login?username=user&password=u
                // или curl -i -X POST -d username=user password=u -c ./cookies.txt  http://localhost:8080/login
                // или использовать вкладку Authorization c Basic Auth в Postman, но только не для формы логина (/login), а для всех остальных
                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                // sample logout customization
                .and()
                .logout()
                .deleteCookies("remove")
                .and()
                .httpBasic();
    }
}