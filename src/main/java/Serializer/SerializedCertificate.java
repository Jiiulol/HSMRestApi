package Serializer;

import CertificatManager.CertificateGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.KeyPair;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SerializedCertificate {

    // region members
    private String _certifString;
    private String _publicKeyString;
    private String _privateKeyString;
    // endregion

    // region Constructors

    public SerializedCertificate(X509Certificate certif, KeyPair keypair)
    {
        CertificateGenerator generator = new CertificateGenerator();
        try {
            _certifString = generator.convertToPem(certif);
            _publicKeyString = keypair.getPublic().toString();
            _publicKeyString = keypair.getPrivate().toString();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
    }

    public SerializedCertificate(String _certifString, String _publicKeyString, String _privateKeyString) {
        this._certifString = _certifString;
        this._publicKeyString = _publicKeyString;
        this._privateKeyString = _privateKeyString;
    }

    // endregion

    // region Generation
    public X509Certificate GetCertificate() throws CertificateException {
        CertificateGenerator generator = new CertificateGenerator();
        return generator.CertificateFromString(_certifString);
    }
    // endregion

    // region Serialization
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
            return gson.toJson(this).equals(gson.toJson(certif));
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

    // endregion
}

