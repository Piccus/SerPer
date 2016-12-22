package client;

import com.piccus.serper.core.SerperInvoker;
import config.ClientConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Piccus on 2016/12/22.
 */
public class Test {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ClientConfig.class);

        HelloService helloService = SerperInvoker.getProxyInstance(HelloService.class);
        String str = helloService.test();
        System.out.println("Test client get a ans, length: " + str.length() + ", content: " + str);
    }
}
