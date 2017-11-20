package net.galvin.ops.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ImportResource("classpath:application-lvmm-log-provider.xml")
public class LvammaLogApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LvammaLogApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LvammaLogApplication.class);
    }

}
