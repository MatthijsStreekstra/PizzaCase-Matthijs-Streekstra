import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class BasicDecryption implements Component {
    private String encryptedData;
    private SecretKey key;
    private String decryptedData;

    public BasicDecryption(String encryptedData, SecretKey key) {
        this.encryptedData = encryptedData;
        this.key = key;
    }

    @Override
    public void execute() {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            // Decodeer de Base64 string naar bytes
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            // Converteer de ontsleutelde bytes terug naar een string
            decryptedData = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getDecryptedData() {
        return decryptedData;
    }
}
