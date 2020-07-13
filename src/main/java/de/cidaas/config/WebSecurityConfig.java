package de.cidaas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import de.cidaas.interceptor.config.JwtWebSecurityConfigurer;

@Configuration
@PropertySource("classpath:cidaas_config.properties")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();		
		configuration.setAllowCredentials(true);
		configuration.addAllowedHeader("Authorization");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		JwtWebSecurityConfigurer.forRS256(env.getProperty("client_id"), env.getProperty("base_url"))
				.configure(http).authorizeRequests()				
				.antMatchers(HttpMethod.GET, "/myprofile").authenticated()
				.antMatchers(HttpMethod.GET, "/v1/**").authenticated()
				.antMatchers(HttpMethod.GET, "/employeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/holidaylist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/localholidaylist").permitAll()
				.antMatchers(HttpMethod.GET, "/leavetype").denyAll();
		
		
	}
}
