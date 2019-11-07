package eksamen66.Service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter()
{
    //encrypts the password
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder
    {
        return BCryptPasswordEncoder()
    }

    //creates the users and roles
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?)
    {
        auth!!.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
    }

    //gives specific rights on the site
    //NOTE: anyrequest is permitall here instead of authenticated.
    //NOTE: .csrf().disable() is necesarry to post/delete without making use of other functions
    override fun configure(http: HttpSecurity)
    {
        //This tells Spring Security to authorize all requests
        //We use formLogin and httpBasic
        //also sets up login and logout to be accessible by all users
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/room").permitAll()
                .antMatchers("/room/**").permitAll()
                .antMatchers("/roomstyle.css").permitAll()
                .antMatchers("/indexstyle.css").permitAll()
                .antMatchers("/adminstyle.css").hasRole("ADMIN")
                .antMatchers("/adminroomstyle.css").hasRole("ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/delete/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()

    }
}