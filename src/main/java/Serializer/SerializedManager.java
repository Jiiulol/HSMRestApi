package Serializer;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializedManager {

    public void AddCertificate(SerializedCertificate certif)
    {
        try
        {
            List<SerializedCertificate> certifList = GetCertifList();
            int tmp = Find(certif);
            if (tmp == -1) { // Certificat non existant donc on peut ajouter a la liste
                certifList.add(certif);
                SetCertifList(certifList);
                System.out.println("Certificate Added");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void DeleteCertificate(SerializedCertificate certif)
    {
        try
        {
            List<SerializedCertificate> certifList = GetCertifList();
            int tmp = Find(certif);
            if (tmp >= 0) { // Certificat existant donc on peut le supprimer
                certifList.remove(tmp);
                SetCertifList(certifList);
                System.out.println("Certificate Removed");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateCertificate(SerializedCertificate certif)
    {
        try
        {
            List<SerializedCertificate> certifList = GetCertifList();
            int tmp = Find(certif);
            if (tmp >= 0) { // Certificat existant donc on peut le modifier
                certifList.remove(tmp);
                certifList.add(certif);
                SetCertifList(certifList);
                System.out.println("Certificate Updated");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public SerializedCertificate GetCertificate(String infos)
    {
        return null;
        // TODO
    }

    private int Find(SerializedCertificate certif)
    {
        try
        {
            List<SerializedCertificate> certifList = GetCertifList();
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

    private List<SerializedCertificate> GetCertifList() throws Exception {
        File file = new File(_path);

        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");

        List<SerializedCertificate> certifList;
        if (str.length() > 0)
            certifList = SerializedCertificate.DeserializeList(str);
        else
            certifList = new ArrayList<>();

        return certifList;
    }

    private void SetCertifList(List<SerializedCertificate> certifList) throws Exception
    {
        try
        {
            File file = new File(_path);
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(SerializedCertificate.SerializeList(certifList));
            fileWriter.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private static String _path;

    public static String get_path() {
        return _path;
    }

    public static void set_path(String in_path) throws Exception {
        File file = new File(in_path);

        if (!file.exists())
            throw new Exception("Invalid path. the specified directory doesn't exist.");
        if (!file.isDirectory())
            throw new Exception("Invalid path. Specify a directory.");

        _path = in_path + File.separator + "certificats.json";
        file = new File(_path);

        if (!file.exists())
            file.createNewFile();
    }
}
