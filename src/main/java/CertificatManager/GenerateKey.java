package CertificatManager;

import org.bouncycastle.asn1.x509.TBSCertificateStructure;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public class GenerateKey {


    public static void Generate() {
       /* RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
        gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3), sr, 1024, 80));
        AsymmetricCipherKeyPair keypair = gen.generateKeyPair();
        RSAKeyParameters publicKey = (RSAKeyParameters) keypair.getPublic();
        RSAPrivateCrtKeyParameters privateKey = (RSAPrivateCrtKeyParameters) keypair.getPrivate();
        // used to get proper encoding for the certificate
        RSAPublicKeyStructure pkStruct = new RSAPublicKeyStructure(publicKey.getModulus(), publicKey.getExponent());
        // JCE format needed for the certificate - because getEncoded() is necessary...
        pubKey = KeyFactory.getInstance("RSA").generatePublic(
                new RSAPublicKeySpec(publicKey.getModulus(), publicKey.getExponent()));
        // and this one for the KeyStore
        privKey = KeyFactory.getInstance("RSA").generatePrivate(
                new RSAPrivateCrtKeySpec(publicKey.getModulus(), publicKey.getExponent(),
                        privateKey.getExponent(), privateKey.getP(), privateKey.getQ(),
                        privateKey.getDP(), privateKey.getDQ(), privateKey.getQInv()));
    */
    }

    private static KeyPair generateKeyPair() throws Exception {
        KeyPair keypair = null;
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(4096, sr);
        keypair = keyGen.generateKeyPair();
        //PrivateKey privKey = keypair.getPrivate();
        //PublicKey pubKey = keypair.getPublic();


        return keypair;
    }
    private static TBSCertificateStructure StructCertif() {
        return null;
    }
}
