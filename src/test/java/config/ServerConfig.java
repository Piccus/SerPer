package config;

import com.piccus.serper.server.SerperServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Piccus on 2016/12/22.
 */
@Configuration
@ComponentScan(basePackages = "server")
@PropertySource("classpath:serper.properties")
public class ServerConfig {

    @Autowired
    Environment environment;

    @Bean
    public SerperServer serperServer() {
        return new SerperServer(environment.getProperty("server.address"));
    }

}
