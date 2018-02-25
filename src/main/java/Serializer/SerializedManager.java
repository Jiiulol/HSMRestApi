package Serializer;

import CertificatManager.CertificateGenerator;
import sun.security.x509.X500Name;

import java.io.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SerializedManager {

    private static String _RevokePath;
    private static String _certifPath;

    // region Gestion certifs
    public SerializableCertificate AddCertificate(Map<String, String> certifInfos)
    {
        try
        {
            List<SerializableCertificate> certifList = GetCertifList();
            CertificateGenerator certGen = new CertificateGenerator();
            X509Certificate certificate = certGen.generateCertificate(certifInfos);
            SerializableCertificate certif = new SerializableCertificate(certificate, certGen.getLastKeypair());
            certifList.add(certif);
            SetCertifList(certifList);
            System.out.println("Certificate Added");
            return certif;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void DeleteCertificate(SerializableCertificate certif)
    {
        try
        {
            List<SerializableCertificate> certifList = GetCertifList();
            List<SerializableCertificate> RevokedCertifList = GetRevokedList();
            if ( certif != null) { // Certificat existant donc on peut le supprimer
                certifList.remove(certif);
                RevokedCertifList.add(certif);
                SetCertifList(certifList);
                SetRevokeList(RevokedCertifList);
                System.out.println("Certificate Removed");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateCertificate(SerializableCertificate certif, Hashtable<String, String> keyValDico)
    {
        try
        {
            if (certif != null) {
                List<SerializableCertificate> certifList = GetCertifList();
                X509Certificate cert = certif.getCertif();
                CertificateGenerator generator = new CertificateGenerator();
                String dn = cert.getSubjectX500Principal().getName();
                X500Name principal = new X500Name(dn);
                String NewCertString = "";
                if (keyValDico.containsKey("C"))
                    NewCertString += "C=" + keyValDico.get("C") + ", ";
                else
                    NewCertString += "C=" + principal.getCountry() + ", ";

                if (keyValDico.containsKey("ST"))
                    NewCertString += "ST=" + keyValDico.get("ST") + ", ";
                else
                    NewCertString += "ST=" + principal.getState() + ", ";

                if (keyValDico.containsKey("L"))
                    NewCertString += "L=" + keyValDico.get("L") + ", ";
                else
                    NewCertString += "L=" + principal.getLocality() + ", ";
                if (keyValDico.containsKey("O"))
                    NewCertString += "O=" + keyValDico.get("O") + ", ";
                else
                    NewCertString += "O=" + principal.getOrganization() + ", ";
                if (keyValDico.containsKey("OU"))
                    NewCertString += "OU=" + keyValDico.get("OU") + ", ";
                else
                    NewCertString += "OU=" + principal.getOrganizationalUnit() + ", ";
                if (keyValDico.containsKey("CN"))
                    NewCertString += "CN=" + keyValDico.get("CN") + ", ";
                else
                    NewCertString += "CN=" + principal.getCommonName() + ", ";
                if (keyValDico.containsKey("EMAIL"))
                    NewCertString += "EMAIL=" + keyValDico.get("EMAIL") + ", ";
                else
                    NewCertString += "EMAIL=" + principal.getGivenName()+ ", ";


                NewCertString = "C=SuperCertif, ST=SuperCertif, L=SuperCertif, O=SuperCertif, OU=SuperCertif, CN=SuperCertif, EMAIL=SuperCertif@certif.com";
                X509Certificate newCert = generator.generateCertificate("", certif.getKeyPair(), 365, "SHA256withRSA");
                SerializableCertificate newCertif = new SerializableCertificate(newCert, certif.getKeyPair());
                 // Certificat existant donc on peut le modifier
                certifList.remove(certif);
                certifList.add(newCertif);
                SetCertifList(certifList);
                System.out.println("Certificate Updated");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public SerializableCertificate GetCertificate(Map<String, String> CertifInfo) throws Exception {
        String distinguishedName = "";
        for (int i = 0; i < CertifInfo.size(); ++i) {
            distinguishedName += CertifInfo.keySet().toArray()[i] + "=" + CertifInfo.values().toArray()[i];
            if (i != CertifInfo.size() - 1)
                distinguishedName += ", ";
        }

        List<SerializableCertificate> list = GetCertifList();
        for (SerializableCertificate cert : list)
        {
            X509Certificate certif = cert.getCertif();
            if (certif.getSubjectX500Principal().toString().equals(distinguishedName))
                return cert;
        }
        return null;
    }

    public SerializableCertificate GetCertificate(String certif) throws Exception {
        List<SerializableCertificate> list = GetCertifList();
        for (SerializableCertificate cert : list)
        {
            if (cert.getCertificatBase64().equals(certif))
                return cert;
        }
        return null;
    }

    private int Find(SerializableCertificate certif)
    {
        try
        {
            List<SerializableCertificate> certifList = GetCertifList();
            for (int i = 0; i < certifList.size(); ++i)
            {
                if (certifList.get(i).equals(certif))
                    return i;
            }
            return -1;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    // endregion

    // region certifList
    private List<SerializableCertificate> GetCertifList() throws Exception {
        return getSerializableCertificates(_certifPath);
    }

    private List<SerializableCertificate> GetRevokedList() throws Exception{
        return getSerializableCertificates(_RevokePath);
    }

    private List<SerializableCertificate> getSerializableCertificates(String path) throws IOException {
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");

        List<SerializableCertificate> certifList;
        if (str.length() > 0)
            certifList = SerializableCertificate.DeserializeList(str);
        else
            certifList = new ArrayList<>();

        return certifList;
    }


    private void SetCertifList(List<SerializableCertificate> certifList) throws Exception
    {
        SetList(certifList, _certifPath);
    }


    private void SetRevokeList(List<SerializableCertificate> certifList) throws Exception
    {
        SetList(certifList, _RevokePath);
    }

    private void SetList(List<SerializableCertificate> certifList, String revokePath) throws IOException {
        try
        {
            File file = new File(revokePath);
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(SerializableCertificate.SerializeList(certifList));
            fileWriter.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public static String get_certifPath() {
        return _certifPath;
    }

    public static void set_certifPath(String in_path) throws Exception {
        File file = new File(in_path);

        if (!file.exists())
            throw new Exception("Invalid path. the specified directory doesn't exist.");
        if (!file.isDirectory())
            throw new Exception("Invalid path. Specify a directory.");

        _RevokePath = in_path + File.separator + "Revocation.json";
        _certifPath = in_path + File.separator + "certificats.json";
        file = new File(_certifPath);

        if (!file.exists())
            file.createNewFile();

        file = new File(_RevokePath);

        if (!file.exists())
            file.createNewFile();
    }

    // endregion
}
