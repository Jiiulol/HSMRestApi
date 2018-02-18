import com.sun.net.httpserver.HttpExchange;

import java.net.URI;

public class HttpResponse {
    private int responseValue;
    private String responseBody;
    private int responseSize;
    private URI _request;

    HttpResponse(HttpExchange in_httpExchange)
    {
        _request = in_httpExchange.getRequestURI();
        switch (in_httpExchange.getRequestMethod()) {
            case "GET":
                SetGet();
                break;
            case "POST":
                SetPost();
                break;
            case "DELETE":
                SetDelete();
                break;
            case "PUT":
                SetUpdate();
                break;
            default:
                this.responseValue = 404;
                this.responseBody = "Page Not found";
                this.responseSize = 14;
                break;
        }
    }

    private void SetDelete() {
        this.responseValue = 200;
        this.responseBody = "DELETED";
        this.responseSize = 7;
    }

    private void SetUpdate() {
        this.responseValue = 200;
        this.responseBody = "PUT";
        this.responseSize = 3;
    }

    private void SetPost() {
        this.responseValue = 200;
        this.responseBody = "ADDED";
        this.responseSize = 5;
    }

    private void SetGet() {
        System.out.println(_request.getPath());
        System.out.println(_request.getQuery());
        Certificate cert = new Certificate();
        cert.Serialize();
        this.responseValue = 200;
        this.responseBody = "TODO";
        this.responseSize = 4;
    }

    public int getResponseValue() {
        return responseValue;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getResponseSize() {
        return responseSize;
    }
}
