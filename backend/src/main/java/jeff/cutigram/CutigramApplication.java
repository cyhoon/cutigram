package jeff.cutigram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CutigramApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CutigramApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:///D:/Personal Project/cutigram/mediadata/uploads/");
    }
}
