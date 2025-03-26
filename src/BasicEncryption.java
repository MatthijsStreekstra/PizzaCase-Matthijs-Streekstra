import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class BasicEncryption implements Component {
    private String dataToEncrypt;
    private SecretKey key;
    private String encryptedData;

    public BasicEncryption(String dataToEncrypt, SecretKey key) {
        this.dataToEncrypt = dataToEncrypt;
        this.key = key;
    }

    @Override
    public void execute() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(dataToEncrypt.getBytes());
            // Converteer de versleutelde bytes naar een Base64 string
            encryptedData = Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getEncryptedData() {
        return encryptedData;
    }
}
