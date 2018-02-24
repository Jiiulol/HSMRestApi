import Communicator.HttpRequestHandler;
import Serializer.SerializedManager;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class HsmMain {

    public static void main(String[] args) {
        try {
            if (args.length == 1)
            {
                SerializedManager.set_certifPath(args[0]);
                HttpServer l_webServer = HttpServer.create(new InetSocketAddress(8000), 0);
                l_webServer.createContext("/", new HttpRequestHandler());
                l_webServer.setExecutor(null);
                System.out.println("web server starting...");
                l_webServer.start();
            }
            else
                throw new Exception("No arguments found. please give a path corresponding to the location where you want to store your certifiactes");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
