package com.fruitstore.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fruitstore.service.impl.UserSecurityService;
//import com.fruitstore.utility.SecurityUtility;
import com.fruitstore.utility.PasswordEncoderTest;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserSecurityService userSecurityService;
	
//	private BCryptPasswordEncoder passwordEncoder() {
//		return SecurityUtility.passwordEncoder();
//	}
	
	//@Bean
	public PasswordEncoder passwordEncoder(){
	    return new PasswordEncoderTest();
	}
	
	private static final String[] PUBLIC_MATCHERS = {
		"/css/**",
		"/js/**",
		"/images/**",
		"/fonts/**",
		"/",
		"/login",
		"/index",
		"/profile",
		"/newUser",
		"/forgetPassword",
		"/allProducts",
		"productDetail/**",
		"/sig"
		
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().
			/* antMatchers("/**"). */
			antMatchers(PUBLIC_MATCHERS).
			permitAll().anyRequest().authenticated();
			
			http
				.csrf().disable().cors().disable()
				.formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
				.loginPage("/login").permitAll()
				.and()
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
				.and()
				.rememberMe();
			
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
	
}
