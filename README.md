# About this project  
This is the annotation based approach for securing endpoints in spring with cidaas.
  
## Prerequisites
You must have JAVA and its package manager \(maven\) installed. You can get them from [_JAVA here_](https://java.com/en/download/) and [_MAVEN here_](https://maven.apache.org/install.html).You can download the postman from [_here_](https://www.getpostman.com/apps)
  
## Add the cidaas spring security interceptor 
```
 <!-- add the following dependency in your pom.xml --> 
 
<dependency>
    <groupId>de.cidaas</groupId>
    <artifactId>cidaas-interceptor-spring-security</artifactId>
    <version>0.0.1</version>
</dependency>

```
## Usage

Create a ``WebSecurityConfig.java`` file in your project.
Add the following code into the ``WebSecurityConfig`` file.
```java
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

    // the following apis are configure with our examples.

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		JwtWebSecurityConfigurer.forRS256(env.getProperty("client_id"), env.getProperty("base_url"))
				.configure(http).authorizeRequests()				
				.antMatchers(HttpMethod.GET, "/myprofile").authenticated()
                .antMatchers(HttpMethod.GET, "/v1/api/**").authenticated() // this will authenticate all url's followed by {{baseurl}}/v1/api/
				.antMatchers(HttpMethod.GET, "/employeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/holidaylist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/localholidaylist").permitAll()
				.antMatchers(HttpMethod.GET, "/leavetype").denyAll();
		
		
	}
}
```

## Initialize the spring security config : 

Create a ``WebApplicationInitializer.java`` file in your project.

To initialize the spring security config , add the following code in ``WebApplicationInitializer.java``.

```java
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  // Load spring security configuration
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] { WebSecurityConfig.class };
  }

  // Load spring web configuration
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[] { 
    		WebConfig.class 
    		};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }

}
```
## Property configuration: 

To use cidaas for authentication, you will need to create the `cidaas_config.properties` file inside your `resources` directory & mention this file in the WebSecurityConfig. -> (@PropertySource("classpath:cidaas_config.properties"))

```
base_url=https://<cidaas-base-url>
client_id=<non-interactive-app-client-id>
client_secret=<non-interactive-app-client-secret>
```
  
Fill in the base_url of your cidaas instance and the client_id & client_secret for your app created in the cidaas dashboard. Then you're ready to go!
