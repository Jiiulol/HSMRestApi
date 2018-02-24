package CertificatManager;

import sun.security.x509.*;

import javax.xml.bind.DatatypeConverter;
import java.io.StringBufferInputStream;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.*;
import java.io.IOException;

public class CertificateGenerator {

    private KeyPair lastKeypair;

    public KeyPair getLastKeypair() {
        return lastKeypair;
    }

    public X509Certificate generateCertificate(Map<String, String> ownerInfo)
            throws GeneralSecurityException, IOException
        {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        lastKeypair = keyPair;

        String distinguishedName = "";

        for (int i = 0; i < ownerInfo.size(); ++i) {
            distinguishedName += ownerInfo.keySet().toArray()[i] + "=" + ownerInfo.values().toArray()[i];
            if (i != ownerInfo.size() - 1)
                distinguishedName += ", ";
        }

        return generateCertificate(distinguishedName, keyPair, 365, "SHA256withRSA");

    }
    private X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
            throws GeneralSecurityException, IOException
    {
        PrivateKey privkey = pair.getPrivate();
        X509CertInfo info = new X509CertInfo();
        Date from = new Date();
        Date to = new Date(from.getTime() + days * 86400000L);
        CertificateValidity interval = new CertificateValidity(from, to);
        BigInteger sn = new BigInteger(64, new SecureRandom());
        X500Name owner = new X500Name(dn);
        X500Name issuer = new X500Name("C=toto, ST=bob, L=yey, O=kek, OU=wow, CN=rofl, EMAIL=hehe@pouet.com");

        info.set(X509CertInfo.VALIDITY, interval);
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
        info.set(X509CertInfo.SUBJECT, owner);
        info.set(X509CertInfo.ISSUER, issuer);
        info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

        // Sign the cert to identify the algorithm that's used.
        X509CertImpl cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        // Update the algorith, and resign.
        algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
        info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
        cert = new X509CertImpl(info);
        cert.sign(privkey, algorithm);

        return cert;
    }

    public String convertToPem(X509Certificate cert) throws CertificateEncodingException {

        String s = "";
        s += "-----BEGIN CERTIFICATE-----\n";
        s += DatatypeConverter.printBase64Binary(cert.getEncoded()) + "\n";
        s += "-----END CERTIFICATE-----";
        return s;
    }

    public X509Certificate CertificateFromString(String certifString) throws CertificateException {

        StringBufferInputStream inputstream = new StringBufferInputStream(certifString);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate)certFactory.generateCertificate(inputstream);
    }

    public static void main (String[] argv) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        CertificateGenerator certGen = new CertificateGenerator();
        Map<String, String> l = new Hashtable<>();

        l.put("C", "toto");
        l.put("ST", "toto2");
        l.put("L", "toto3");
        l.put("O", "toto4");
        l.put("OU", "toto5");
        l.put("CN", "toto6");
        l.put("email", "toto@toto.com");
        X509Certificate certificate = certGen.generateCertificate(l);
        System.out.println(certGen.convertToPem(certificate));
    }
}
