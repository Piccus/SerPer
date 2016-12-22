# SerPer
A light rpc framework based on netty and spring framework.It works on JDK8.0 and higher.<br>
The rpc framework works throught HTTP protcol and doesn't support distributed framework right now.The next time i'll update it.

##Usage
###Server
+ **Config**<br><br>You can use java config or xml.Remeber to add a **serper.properties** to tell the server the address binding to.
There is the java config sample.

		@Component
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
There is the serper.properties.

		#serper server
		server.address=127.0.0.1:8000

+ **Service**<br><br>The service you want to call on the server must implement the interface on the client 
and must be annotated with `@SerperService(interface)`
+ **Bootstarp**<br><br>Just one class is enough to bootstrap the server.

		public class Bootstrap {
   			public static void main(String[] args) {
        		new AnnotationConfigApplicationContext(ServerConfig.class);
    		}
		}

###Client
+ **Config**<br><br>The client config is as same as the server.Also,it need a **serper.properties**.

+ **Sample**<br><br>You can call the service like a local method.It's simple and fast.

		public class Sample {
			public static void main(String[] args) {
				new AnnotationConfigApplicationContext(ClientConfig.class);
				Interface object = SerperInvoker.getProxyInstance(Interface.class);
				object.method();
			}
		}



