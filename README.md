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
## The ``WebSecurityConfig`` file defines which endpoints should be secured and how (offline or with introspection-API):

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
		JwtWebSecurityConfigurer
				.offlineValidation(env.getProperty("client_id"), env.getProperty("base_url"))
		//		.introspectionValidation(env.getProperty("client_id"), env.getProperty("base_url"))
				.configure(http).authorizeRequests()				
				.antMatchers(HttpMethod.GET, "/myprofile").authenticated()
                .antMatchers(HttpMethod.GET, "/v1/api/**").authenticated() // it will authenticate all the url followed by {{baseurl}}/v1/api/
				.antMatchers(HttpMethod.GET, "/employeelist").hasRole("HR")
				.antMatchers(HttpMethod.GET, "/holidaylist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAnyAuthority("holidaylist:read", "ROLE_HR")
				.antMatchers(HttpMethod.GET, "/localholidaylist").permitAll()
				.antMatchers(HttpMethod.GET, "/leavetype").denyAll();
		
		
	}

	// Or like this, if we use @PreAuthorize() for method-level security

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// 	http.cors();
	// 	JwtSpringInterceptor
	//			.offlineValidation(env.getProperty("client_id"), env.getProperty("base_url"))
	//			.introspectionValidation(env.getProperty("client_id"), env.getProperty("base_url"))
	//			.configure(http).antMatcher("/**")  
    //    		.authorizeRequests()  
    //   		.antMatchers("/").permitAll()  
    //    		.anyRequest().authenticated();  	
	//}
}
```

Warning: Don't use 

```
                .antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasRole("HR")
```
to give endpoint role & authority as [roles and authorities are similar in Spring](https://www.baeldung.com/spring-security-expressions). 

hasRole("HR") is actually the same as hasAuthority("ROLE_HR"). 

the above check has the same logic as 

```
                .antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("holidaylist:read")
				.antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAuthority("ROLE_HR")
```
and it only makes endpoint /holidayandemployeelist to be accessible only with authority "holidaylist:read" in the first line, and override it to be accesible only with role HR in the second line.

instead use

```
                .antMatchers(HttpMethod.GET, "/holidayandemployeelist").hasAnyAuthority("holidaylist:read", "ROLE_HR")
```

or (for method-level security)

```
               @PreAuthorize("hasAnyAuthority('a:read', 'ROLE_HR')")
```

to make the endpoint to be accessible with authority "holidaylist:read" `OR` role "HR"


If you want to make the endpoint to be accessible with authority "holidaylist:read" `AND` role "HR" use this

```
                @PreAuthorize("hasRole('HR') && hasAuthority('holidaylist:read')")
```

## The spring security config : 

The ``WebApplicationInitializer.java`` configures spring to load the ``WebSecurityConfig.java``.  

```java
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[] {WebSecurityConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return null;
  }

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/"};
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

## Test the endpoint

With standard configuration the tomcat server is available under http://localhost:8080/cidaas-interceptor-spring-security-example/.
You can for example test the http://localhost:8080/cidaas-interceptor-spring-security-example/myprofile - API. If you provide a valid access token gathered via the client_credentials flow for your configured application, you can access the API. It will block your access with the message access denied if you provide no token or your token is invalid. 
You will need to provide the access token as 'Bearer Token', e.g. in Postman. You can build the Authorization-header yourself if you add a new Header with the key "Authorization" and with the value "Bearer <your_token>"

  
You can get more information about cidaas [here](https://docs.cidaas.de/)
