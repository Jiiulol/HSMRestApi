package Communicator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HttpRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpResponse response= new HttpResponse(httpExchange);

        httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        httpExchange.sendResponseHeaders(response.getResponseValue(),response.getResponseSize());
        OutputStream l_os = httpExchange.getResponseBody();

        l_os.write(response.getResponseBody().getBytes());
        l_os.close();
    }
}
