package Serializer;

import CertificatManager.CertificateGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SerializableCertificate {

    // region members
    private final String CERTIFICATE_FIRST_LINE = "-----BEGIN CERTIFICATE-----";
    private final String CERTIFICATE_LAST_LINE = "-----END CERTIFICATE-----";
    private String CertificatBase64;
    private String PublicKeyString;
    private String PrivateKeyString;

    private transient X509Certificate certif;
    private transient KeyPair keyPair;
    // endregion

    // region Constructors

    public SerializableCertificate(X509Certificate certif, KeyPair keypair)
    {
        CertificateGenerator generator = new CertificateGenerator();
        try {
            this.certif = certif;
            keyPair = keypair;
            CertificatBase64 = generator.convertToPem(certif);
            PublicKeyString = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
            PrivateKeyString = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
    }

    public SerializableCertificate(String CertificatBase64, String PublicKeyString, String PrivateKeyString) {
        this.CertificatBase64 = CertificatBase64;
        this.PublicKeyString = PublicKeyString;
        this.PrivateKeyString = PrivateKeyString;
        Initialize();

    }

    private void Initialize()
    {
        try {

        CertificateGenerator generator = new CertificateGenerator();
        certif = generator.CertificateFromString(CertificatBase64);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] publicBytes = Base64.getDecoder().decode(PublicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        byte[] privateBytes = Base64.getDecoder().decode(PrivateKeyString);
        PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(privateBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privatekeySpec);

        keyPair = new KeyPair(pubKey, privateKey);

        } catch (CertificateException e) {
                e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (InvalidKeySpecException e) {
                e.printStackTrace();
        }
    }

    // endregion

    // region Getter / setter
    public String getCertificatBase64() {
        return CertificatBase64;
    }

    public void setCertificatBase64(String certificatBase64) {
        this.CertificatBase64 = certificatBase64;
    }

    public String getPublicKeyString() {
        return PublicKeyString;
    }

    public void setPublicKeyString(String publicKeyString) {
        this.PublicKeyString = publicKeyString;
    }

    public String getPrivateKeyString() {
        return PrivateKeyString;
    }

    public void setPrivateKeyString(String privateKeyString) {
        this.PrivateKeyString = privateKeyString;
    }

    public X509Certificate getCertif() {
        return certif;
    }

    public void setCertif(X509Certificate certif) {
        this.certif = certif;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
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

            SerializableCertificate certif = (SerializableCertificate)obj;

            if (this.CertificatBase64 == certif.getCertificatBase64() &&
                    this.getPublicKeyString() == certif.getPublicKeyString())
            return true;
        }
        return false;
    }

    public static SerializableCertificate Deserialize(String serialized)
    {
        Gson gson = new Gson();
        SerializableCertificate certif = gson.fromJson(serialized, SerializableCertificate.class);
        certif.Initialize();
        return certif;
    }

    public static List<SerializableCertificate> DeserializeList(String serialized)
    {
        Type targetClassType = new TypeToken<ArrayList<SerializableCertificate>>() { }.getType();

        List<SerializableCertificate> list = new Gson().fromJson(serialized, targetClassType);
        for (SerializableCertificate cert : list)
        {
            cert.Initialize();
        }
        return list;
    }

    public static String SerializeList(List<SerializableCertificate> certifList) {
        Gson gson = new Gson();
        Type listOfTestObject = new TypeToken<List<SerializableCertificate>>(){}.getType();
        return gson.toJson(certifList, listOfTestObject);
    }

    // endregion
}

