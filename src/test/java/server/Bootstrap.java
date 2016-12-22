package server;

import config.ServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Piccus on 2016/12/22.
 */
public class Bootstrap {

    public static void main(String[] args) {

        new AnnotationConfigApplicationContext(ServerConfig.class);

    }

}
