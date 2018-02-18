package Communicator;

import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import Serializer.*;

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
        SerializedManager manager = new SerializedManager();
        manager.DeleteCertificate(new SerializedCertificate());
        this.responseValue = 200;
        this.responseBody = "DELETED";
        this.responseSize = 7;
    }

    private void SetUpdate() {
        SerializedManager manager = new SerializedManager();
        manager.UpdateCertificate(new SerializedCertificate());
        this.responseValue = 200;
        this.responseBody = "PUT";
        this.responseSize = 3;
    }

    private void SetPost() {
        SerializedManager manager = new SerializedManager();
        manager.AddCertificate(new SerializedCertificate());
        this.responseValue = 200;
        this.responseBody = "ADDED";
        this.responseSize = 5;
    }

    private void SetGet() {
        SerializedManager manager = new SerializedManager();
        manager.GetCertificate("kek");
        this.responseValue = 200;
        this.responseBody = "{Certif = \"toto\"}";
        this.responseSize = 17;
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
