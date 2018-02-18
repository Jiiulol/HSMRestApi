package Serializer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SerializedCertificate {
    public String Country;
    public String Organization;
    public String Domain;
    public String Validity;
    public String BitsForKey;
    public String PassPhrase;
    public String OrganizationUnitName;
    public String LocalityName;
    public String Mail;
    public String Certificate;
    public String PrivateKey;

    public SerializedCertificate(String country, String organization, String domain, String validity, String bitsForKey, String passPhrase, String organizationUnitName, String localityName, String mail, String certificate, String privateKey)
    {
        Country = country;
        Organization = organization;
        Domain = domain;
        Validity = validity;
        BitsForKey = bitsForKey;
        PassPhrase = passPhrase;
        OrganizationUnitName = organizationUnitName;
        LocalityName = localityName;
        Mail = mail;
        Certificate = certificate;
        PrivateKey = privateKey;
    }

    public SerializedCertificate()
    {
        Country = "";
        Organization = "";
        Domain = "";
        Validity = "";
        BitsForKey = "";
        PassPhrase = "";
        OrganizationUnitName = "";
        LocalityName = "";
        Mail = "";
        Certificate = "";
        PrivateKey = "";
    }

    public String Serialize()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {

            SerializedCertificate certif = (SerializedCertificate)obj;

            Gson gson = new Gson();
            if (gson.toJson(this).equals(gson.toJson(certif)))
                return true;
        }
        return false;
    }

    public static SerializedCertificate Deserialize(String serialized)
    {
        Gson gson = new Gson();
        return gson.fromJson(serialized, SerializedCertificate.class);
    }

    public static List<SerializedCertificate> DeserializeList(String serialized)
    {
        Type targetClassType = new TypeToken<ArrayList<SerializedCertificate>>() { }.getType();
        return new Gson().fromJson(serialized, targetClassType);
    }

    public static String SerializeList(List<SerializedCertificate> certifList) {
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<SerializedCertificate>>(){}.getType();
        return gson.toJson(certifList, listOfTestObject);
    }
}

