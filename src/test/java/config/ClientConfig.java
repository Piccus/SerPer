package config;

import com.piccus.serper.client.SerperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by Piccus on 2016/12/22.
 */
@Configuration
@PropertySource("classpath:serper.properties")
public class ClientConfig {

    @Autowired
    Environment environment;

    @Bean
    public SerperClient serperClient() {
        return new SerperClient(environment.getProperty("server.address"));
    }
}
