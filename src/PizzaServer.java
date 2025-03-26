import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.KeyAgreement;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PizzaServer {
    private static PizzaServer instance;

    private PizzaServer() {
    }

    public static PizzaServer getInstance() {
        if (instance == null) {
            instance = new PizzaServer();
        }
        return instance;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server gestart. Wachten op connecties...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
                    System.out.println("Client connected.");

                    // Ontvang de publieke sleutel van de client
                    String clientPublicKeyString = (String) inputStream.readObject();
                    byte[] clientPublicKeyBytes = Base64.getDecoder().decode(clientPublicKeyString);
                    PublicKey clientPublicKey = KeyFactory.getInstance("DH")
                            .generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));

                    // Diffie-Hellman sleuteluitwisseling
                    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
                    keyPairGen.initialize(2048);
                    KeyPair keyPair = keyPairGen.generateKeyPair();

                    KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
                    keyAgree.init(keyPair.getPrivate());
                    keyAgree.doPhase(clientPublicKey, true);

                    // Verstuur de publieke sleutel van de server naar de client
                    String serverPublicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                    outputStream.writeObject(serverPublicKeyString);

                    // Genereer de gedeelde geheime bytes
                    byte[] sharedSecret = keyAgree.generateSecret();

                    // Gebruik SHA-256 om een AES-sleutel van 16 bytes te maken
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] keyBytes = sha256.digest(sharedSecret);
                    SecretKey sharedSecretKey = new SecretKeySpec(Arrays.copyOf(keyBytes, 16), "AES");

                    // Ontvang de versleutelde order als string
                    String encryptedOrderString = (String) inputStream.readObject();

                    // Ontsleutel de string
                    BasicDecryption decryption = new BasicDecryption(encryptedOrderString, sharedSecretKey);
                    decryption.execute();
                    String decryptedDataString = decryption.getDecryptedData();

                    // afdrukken van decrypted string
                    System.out.println("Ontvangen bestelling:\n" + decryptedDataString);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
