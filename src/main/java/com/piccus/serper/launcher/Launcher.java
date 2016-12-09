package com.piccus.serper.launcher;

import com.piccus.serper.net.Client;
import com.piccus.serper.net.Server;

/**
 * Created by Piccus on 2016/12/7.
 */
public class Launcher {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("This jar must be using through 'java -jar serper.jar [-options]'." );
            System.out.println("Using '--help' to see the help information.");
            return;
        }
        if (args[0].equals("--help")) {
            System.out.println("serper is a light rpc framework based on netty.The options includes:");
            System.out.println("    --version   show the version of serper");
            System.out.println("    --help      show the help information");
            System.out.println("    --client    startup by client mode");
            System.out.println("    --server    startup by server mode");
            return;
        }
        if (args[0].equals("--version")) {
            System.out.println("serper 1.00 version powered by Piccus");
            return;
        }
        if (args[0].equals("--client")) {
            System.out.println("serper client is starting -----");
            Client client = new Client();
            client.connect("10.32.3.39", 8000);
            return;
        }
        if (args[0].equals("--server")) {
            System.out.println("serper server is starting -----");
            Server server = new Server();
            server.start(8000);
        }

        System.out.println("The options is not valid,use '--help' to see the help information.");
        return;
    }
}
