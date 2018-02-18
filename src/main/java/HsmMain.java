import Communicator.HttpRequestHandler;
import Serializer.SerializedCertificate;
import Serializer.SerializedManager;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HsmMain {

    public static void main(String[] args) {
        try {
            if (args.length == 1)
            {
                SerializedManager.set_path(args[0]);
                HttpServer l_webServer = HttpServer.create(new InetSocketAddress(8000), 0);
                l_webServer.createContext("/", new HttpRequestHandler());
                l_webServer.setExecutor(null);
                System.out.println("web server starting...");
                l_webServer.start();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
