package daedan.mes.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Autowired
	private Environment env;

	private static final String[] EXCLUDE_PATHS = {
			"/api/daedan/mes/**",
			"/api/daedan/mes/user/signin",
			"/fileroot/**",
		    "/static/**",
			"/error/**",
			};
	@Autowired
	private JwtInterceptor jwtInterceptor;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
				.addPathPatterns("/api/daedan/**")
				.excludePathPatterns(EXCLUDE_PATHS);
	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/daedan/**")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD");
    }


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//.addResourceLocations("file:/Users/mjkang/dev/bo/daedan_bo/fileroot/") //리눅스 root에서 시작하는 폴더 경로
		StringBuffer buf = new StringBuffer();
		buf.append("file:/").append(env.getProperty(("file.root.path")));
		registry.addResourceHandler("/file/**")
				.addResourceLocations(buf.toString()) //리눅스 root에서 시작하는 폴더 경로
				.setCachePeriod(3600)
				.resourceChain(true) .addResolver(new PathResourceResolver());
	}

}
