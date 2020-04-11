package com.application.instagramm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.application.instagramm.user.AppUserRepository;
import com.application.instagramm.user.UserPrincipalDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private UserPrincipalDetailsService UserPrincipalDetailsService;
	private AppUserRepository appUserRepository;
	
	@Autowired
	public WebSecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService, AppUserRepository appUserRepository) {
		this.UserPrincipalDetailsService = userPrincipalDetailsService;
		this.appUserRepository = appUserRepository;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.appUserRepository))
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers("/api/user/registration", "/api/user/login", "/api/user/addfriend")
				.permitAll()
				.antMatchers("/api/user/roleTest").hasRole("USER")
				.anyRequest()
				.authenticated();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(this.UserPrincipalDetailsService);
		
		return daoAuthenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
