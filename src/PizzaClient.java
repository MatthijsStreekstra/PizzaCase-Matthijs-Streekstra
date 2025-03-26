import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class PizzaClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {

            // Diffie-Hellman sleuteluitwisseling
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Verstuur de publieke sleutel van de client naar de server
            outputStream.writeObject(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));

            // Ontvang de publieke sleutel van de server
            String serverPublicKeyString = (String) inputStream.readObject();
            byte[] serverPublicKeyBytes = Base64.getDecoder().decode(serverPublicKeyString);
            PublicKey serverPublicKey = KeyFactory.getInstance("DH")
                    .generatePublic(new X509EncodedKeySpec(serverPublicKeyBytes));

            KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
            keyAgree.init(keyPair.getPrivate());
            keyAgree.doPhase(serverPublicKey, true);

            // Genereer de gedeelde geheime bytes
            byte[] sharedSecret = keyAgree.generateSecret();

            // Gebruik SHA-256 om een AES-sleutel van 16 bytes te maken
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha256.digest(sharedSecret);
            SecretKey sharedSecretKey = new SecretKeySpec(Arrays.copyOf(keyBytes, 16), "AES");

            // Maak een order en zet op juiste format
            Order order = new Order();
            String bestellingString = order.createOrderString();

            // Versleutel de bestellingString
            BasicEncryption encryption = new BasicEncryption(bestellingString, sharedSecretKey);
            encryption.execute();
            String encryptedDataString = encryption.getEncryptedData();

            // Verstuur de versleutelde order als een string naar de server
            outputStream.writeObject(encryptedDataString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
