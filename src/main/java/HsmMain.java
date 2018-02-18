import Communicator.HttpRequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HsmMain {

    public static void main(String[] args) {
        try {
            HttpServer l_webServer = HttpServer.create(new InetSocketAddress(8000), 0);
            l_webServer.createContext("/", new HttpRequestHandler());
            l_webServer.setExecutor(null); // creates a default executor
            System.out.println("web server starting...");
            l_webServer.start();
        }
        catch (Exception e)
        {
            System.out.println("Impossible de créer le serveur web");
        }
    }
}
