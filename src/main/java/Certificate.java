import com.google.gson.Gson;

public class Certificate {
    public String _certifValue;
    public String _key1;
    public String _key2;

    public Certificate() {
      _certifValue = "bob";
      _key1 = "bob1";
      _key2 = "bob2";
    }

    public void Serialize() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(this));
    }
}

