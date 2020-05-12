# About this project  
This is the example project for the [cidaas interceptor with the annotation-based configuration approach](https://github.com/Cidaas/cidaas-interceptor-spring-security)
  
## Prerequisites
To get started You can simply clone the **_cidaas-interceptor-spring-security-example_** repository and install the dependencies.
  
You must have JAVA and its package manager \(maven\) installed. You can get them from [_JAVA here_](https://java.com/en/download/) and [_MAVEN here_](https://maven.apache.org/install.html).You can download the postman from [_here_](https://www.getpostman.com/apps)
  
## Clone cidaas-interceptor-spring-security-example
Clone the **cidaas-interceptor-spring-security-example** repository using git:
```
git clone https://github.com/Cidaas/cidaas-interceptor-spring-security-example.git

```

Install the dependencies:  
In your project root directory, excute: 
  
```
mvn clean install

```

## Add the cidaas spring security interceptor 
```
 <!-- add the following dependency in pom.xml --> 
 
<dependency>
    <groupId>de.cidaas</groupId>
    <artifactId>cidaas-interceptor-spring-security</artifactId>
    <version>0.0.1</version>
</dependency>

```
## The ``WebSecurityConfig`` file defines which endpoints should be secured:

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

    // the following apis are configured with our examples.

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		JwtWebSecurityConfigurer.forRS256(env.getProperty("client_id"), env.getProperty("base_url"))
				.configure(http).authorizeRequests()				
				.antMatchers(HttpMethod.GET, "/myprofile").authenticated()
                .antMatchers(HttpMethod.GET, "/v1/api/**").authenticated() // it will authenticate all the url followed by {{baseurl}}/v1/api/
				.antMatchers(HttpMethod.GET, "/employeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/holidaylist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/localholidaylist").permitAll()
				.antMatchers(HttpMethod.GET, "/leavetype").denyAll();
		
		
	}
}
```
  
## The spring security config : 

The ``WebApplicationInitializer.java`` configures spring to load the ``WebSecurityConfig.java``.  

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

To test the example, you will need to fill in the information required in the `cidaas_config.properties` file inside the `resources` directory. This is getting loaded in the WebSecurityConfig -> (@PropertySource("classpath:cidaas_config.properties"))

```
base_url=https://<cidaas-base-url>
client_id=<non-interactive-app-client-id>
client_secret=<non-interactive-app-client-secret>
```
  
## Execute the example project
  
To start the example project, right click the project root directory and select 'Run as' -> 'Run on server' and select your local tomcat installation.  
You can download tomcat [here](https://tomcat.apache.org/download-90.cgi).
  
You can get more information about cidaas [here](https://docs.cidaas.de/)
