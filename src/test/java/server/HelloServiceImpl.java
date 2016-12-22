package server;

import client.HelloService;
import com.piccus.serper.core.SerperService;

/**
 * Created by Piccus on 2016/12/22.
 */
@SerperService(HelloService.class)
public class HelloServiceImpl implements HelloService{


    @Override
    public String test() {
        return "Hello, Service";
    }
}
