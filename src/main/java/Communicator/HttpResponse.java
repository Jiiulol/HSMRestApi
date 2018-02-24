package Communicator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
        JsonObject obj = new JsonObject();
        try {
            FillDico();
        }
        catch (Exception e)
        {
            obj.addProperty("Error", e.getMessage());
            this.responseValue = 404;
            this.responseBody = obj.toString();
            this.responseSize = obj.toString().length();
            return;
        }
        switch (in_httpExchange.getRequestMethod()) {
            case "GET":
                try {
                    SetGet();
                } catch (Exception e) {
                    e.getMessage();
                    obj.addProperty("Error", e.getMessage());
                    this.responseBody = obj.toString();
                    this.responseSize = obj.toString().length();
                }
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
                obj.addProperty("Errror", "Unhandled Request");
                this.responseBody = obj.toString();
                this.responseSize = obj.toString().length();
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

        if (!_keyValDico.containsKey("C"))
            tmplist.add("C");
        if (!_keyValDico.containsKey("ST"))
            tmplist.add("ST");
        if (!_keyValDico.containsKey("L"))
            tmplist.add("L");
        if (!_keyValDico.containsKey("O"))
            tmplist.add("O");
        if (!_keyValDico.containsKey("OU"))
            tmplist.add("OU");
        if (!_keyValDico.containsKey("CN"))
            tmplist.add("CN");
        if (!_keyValDico.containsKey("email"))
            tmplist.add("email");
        if (!_keyValDico.containsKey("Certif"))
            tmplist.add("Certif");
        return tmplist;
    }
    // endregion

    // region RequestMethodes methodes
    private void SetDelete() {
        SerializedManager manager = new SerializedManager();
        List<String> missingValues = CheckDico();
        this.responseValue = 200;
        JsonObject obj = new JsonObject();
        if (missingValues.contains("Certif")) {
            obj.addProperty("Deleted", "Fail");
            obj.addProperty("error", "No certificate value");
        } else {
            try {
                manager.DeleteCertificate(manager.GetCertificate(_keyValDico.get("Certif")));
                obj.addProperty("Deleted", "Success");
            } catch (Exception e) {
                e.printStackTrace();
                obj.addProperty("Deleted", "Fail");
                obj.addProperty("error", e.getMessage());
            }
            this.responseBody = obj.toString();
            this.responseSize = obj.toString().length();
        }
    }

    private void SetUpdate() {
        SerializedManager manager = new SerializedManager();
        List<String> missingValues = CheckDico();
        this.responseValue = 200;
        JsonObject obj = new JsonObject();
        if (missingValues.contains("Certif")) {
            obj.addProperty("Updated", "Fail");
            obj.addProperty("error", "No certificat value");
        } else
            obj.addProperty("Updated", "Success");
        this.responseBody = obj.toString();
        this.responseSize = obj.toString().length();
    }

    private void SetPost() {
        SerializedManager manager = new SerializedManager();
        List<String> missingValues = CheckDico();
        this.responseValue = 200;
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        if (missingValues.size() > 1 || missingValues.get(0).equals("Certif")) {
            obj.addProperty("Created", "fail");
            String response = "missing values : ";
            for (String val : missingValues) {
                if (val.equals(missingValues.get(missingValues.size() - 1))) {
                    array.add(val);
                }
            }
            obj.add("MissingList", array);
        } else {
            SerializableCertificate certif = manager.AddCertificate(_keyValDico);
            obj.addProperty("Created", "Success");
            obj.addProperty("certif", certif.get_certifString());
            obj.addProperty("publicKey", certif.get_publicKeyString());
        }
        this.responseBody = obj.toString();
        this.responseSize = obj.toString().length();
    }

    private void SetGet() throws Exception {
        SerializedManager manager = new SerializedManager();
        this.responseValue = 200;
        JsonObject obj = new JsonObject();
        if (_keyValDico.containsKey("Certif"))
        {
            SerializableCertificate cert = manager.GetCertificate(_keyValDico.get("Certif"));
            if (cert != null)
            {
            obj.addProperty("Found", "Success");
            obj.addProperty("certif", cert.get_certifString());
            obj.addProperty("publicKey", cert.get_publicKeyString());
            }
            else
            {
             obj.addProperty("Found", "Not found");
            }
        }
        else
        {
            SerializableCertificate cert = manager.GetCertificate(_keyValDico);
            if (cert == null)
                obj.addProperty("Found", "Fail");
            else
            {
                obj.addProperty("Found", "Success");
                obj.addProperty("certif", cert.get_certifString());
                obj.addProperty("publicKey", cert.get_publicKeyString());
            }
        }
        this.responseBody = obj.toString();
        this.responseSize = obj.toString().length();
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
