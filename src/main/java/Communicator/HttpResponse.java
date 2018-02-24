package Communicator;

import com.sun.net.httpserver.HttpExchange;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Serializer.*;

public class HttpResponse {
    // region members
    private int responseValue;
    private String responseBody;
    private int responseSize;
    private HttpExchange _request;
    private Hashtable<String, String> _keyValDico;
    // endregion

    //region Constructor

    HttpResponse(HttpExchange in_httpExchange)
    {
        _keyValDico = new Hashtable<>();
        _request = in_httpExchange;

        try {
            FillDico();
        }
        catch (Exception e)
        {
            this.responseValue = 404;
            this.responseBody = e.getMessage();
            this.responseSize = e.getMessage().length();
            return;
        }
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

    // endregion

    // region private methodes
    private void FillDico() throws Exception
    {
        String qs = _request.getRequestURI().getRawQuery();
        if (qs != null) {

            int last = 0, next, l = qs.length();
            while (last < l) {
                next = qs.indexOf('&', last);
                if (next == -1)
                    next = l;

                if (next > last) {
                    int eqPos = qs.indexOf('=', last);
                        if (eqPos < 0 || eqPos > next)
                            _keyValDico.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
                        else
                            _keyValDico.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"), URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
                }
                last = next + 1;
            }
        }
    }
    private List<String> CheckDico() {
        ArrayList<String> tmplist = new ArrayList<>();

        return tmplist;
    }
    // endregion

    // region RequestMethodes methodes
    private void SetDelete() {
        SerializedManager manager = new SerializedManager();
        List<String> missingValues = CheckDico();
        this.responseValue = 200;
        this.responseBody = "DELETED";
        this.responseSize = 7;
    }

    private void SetUpdate() {
        SerializedManager manager = new SerializedManager();
        this.responseValue = 200;
        this.responseBody = "PUT";
        this.responseSize = 3;
    }

    private void SetPost() {
        SerializedManager manager = new SerializedManager();
        this.responseValue = 200;
        this.responseBody = "ADDED";
        this.responseSize = 5;
    }

    private void SetGet() {
        SerializedManager manager = new SerializedManager();
        this.responseValue = 200;
        this.responseBody = "{Certif = \"toto\"}";
        this.responseSize = 17;
    }

    // endregion

    // region getters
    public int getResponseValue() {
        return responseValue;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getResponseSize() {
        return responseSize;
    }
    // endregion
}
