package CertificatManager;

import javafx.util.Pair;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import sun.security.x509.*;
import java.io.StringBufferInputStream;
import java.security.cert.*;
import java.security.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;
import java.util.List;

public class CertificateGenerator {

    public X509Certificate generateCertificate(List<Pair<String, String>> ownerInfo)
            throws GeneralSecurityException, IOException
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String distinguishedName = "";

        for (int i = 0; i < ownerInfo.size(); ++i) {
            distinguishedName += ownerInfo.get(i).getKey() + "=" + ownerInfo.get(i).getValue();
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
        BASE64Encoder encoder = new BASE64Encoder();
        String s = X509Factory.BEGIN_CERT + System.lineSeparator();
        s += encoder.encode(cert.getEncoded());
        s += System.lineSeparator() + X509Factory.END_CERT;
        return s;
    }

    public static void main (String[] argv) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        CertificateGenerator certGen = new CertificateGenerator();
        List<Pair<String, String>> l = new ArrayList<>();
        l.add(new Pair<>("C", "toto"));
        l.add(new Pair<>("ST", "toto2"));
        l.add(new Pair<>("L", "toto3"));
        l.add(new Pair<>("O", "toto4"));
        l.add(new Pair<>("OU", "toto5"));
        l.add(new Pair<>("CN", "toto6"));
        l.add(new Pair<>("email", "toto@toto.com"));
        X509Certificate certificate = certGen.generateCertificate(l);
        System.out.println(certGen.convertToPem(certificate));
    }

    public X509Certificate CertificateFromString(String certifString) throws CertificateException {

        StringBufferInputStream inputstream = new StringBufferInputStream(certifString);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate)certFactory.generateCertificate(inputstream);
    }
}
