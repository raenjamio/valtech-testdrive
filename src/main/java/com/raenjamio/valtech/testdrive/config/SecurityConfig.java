package com.raenjamio.valtech.testdrive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build());
	}

	public static class Roles {
		public static final String ANONYMOUS = "ANONYMOUS";
		public static final String USER = "USER";
		static public final String ADMIN = "ADMIN";

		private static final String ROLE_ = "ROLE_";
		public static final String ROLE_ANONYMOUS = ROLE_ + ANONYMOUS;
		public static final String ROLE_USER = ROLE_ + USER;
		static public final String ROLE_ADMIN = ROLE_ + ADMIN;
	}

	@Configuration
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
					"/configuration/security", "/swagger-ui.html", "/webjars/**", "/v2/swagger.json");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
				http.httpBasic().and().authorizeRequests()//

						.antMatchers("/api/v1/**").hasAnyRole(Roles.USER)//
						//.antMatchers("/api/client/**").hasRole(Roles.USER)//
						//.antMatchers("/api/admin/**").hasAnyRole(Roles.ADMIN)//
						//.antMatchers("/health/**").hasAnyRole(Roles.ADMIN)//
						//.antMatchers("/**").denyAll()//
						.and().csrf().disable()//
						.anonymous().authorities(Roles.USER);//
		}
	}
}

/*
 * 
 * @EnableGlobalMethodSecurity(securedEnabled = true) public class
 * SecurityConfig {
 * 
 * public static class Roles { public static final String ANONYMOUS =
 * "ANONYMOUS"; public static final String USER = "USER"; static public final
 * String ADMIN = "ADMIN";
 * 
 * private static final String ROLE_ = "ROLE_"; public static final String
 * ROLE_ANONYMOUS = ROLE_ + ANONYMOUS; public static final String ROLE_USER =
 * ROLE_ + USER; static public final String ROLE_ADMIN = ROLE_ + ADMIN; }
 * 
 * 
 * @Configuration
 * 
 * @Order(SecurityProperties.BASIC_AUTH_ORDER) protected static class
 * ApplicationSecurity extends WebSecurityConfigurerAdapter {
 * 
 * private static final String[] AUTH_WHITELIST = {
 * 
 * // -- swagger ui "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs",
 * "/webjars/**", "/configuration/ui", "/configuration/security",
 * "/v2/swagger.json"
 * 
 * };
 * 
 * @Override public void configure(WebSecurity web) throws Exception {
 * web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui",
 * "/swagger-resources", "/configuration/security", "/swagger-ui.html",
 * "/webjars/**", "/v2/swagger.json"); }
 * 
 * @Override protected void configure(HttpSecurity http) throws Exception {
 * http.httpBasic().and().authorizeRequests()//
 * 
 * .antMatchers("/api/v1/**").hasAnyRole(Roles.ANONYMOUS)//
 * //.antMatchers("/api/client/**").hasRole(Roles.USER)//
 * //.antMatchers("/api/admin/**").hasAnyRole(Roles.ADMIN)//
 * //.antMatchers("/health/**").hasAnyRole(Roles.ADMIN)//
 * //.antMatchers("/**").denyAll()// .and().csrf().disable()//
 * .anonymous().authorities(Roles.ROLE_ANONYMOUS);// }
 * 
 * 
 * } }
 */