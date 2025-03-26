import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PizzaServerUDP {

    private static PizzaServerUDP instance;

    private PizzaServerUDP() {
    }

    public static PizzaServerUDP getInstanceUDP() {
        if (instance == null) {
            instance = new PizzaServerUDP();
        }
        return instance;
    }

    public static void start() {
        try {
            DatagramSocket socket = new DatagramSocket(5001);

            // Stap 1: Genereer Diffie-Hellman-sleutelpaar (server-side)
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Ontvang de publieke sleutel van de client
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String clientPublicKeyString = new String(packet.getData(), 0, packet.getLength());
            byte[] clientPublicKeyBytes = Base64.getDecoder().decode(clientPublicKeyString);
            PublicKey clientPublicKey = KeyFactory.getInstance("DH")
                    .generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));

            // Verstuur de publieke sleutel van de server naar de client
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);
            buffer = publicKeyString.getBytes();
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            packet = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
            socket.send(packet);

            // Stap 2: Diffie-Hellman-sleutelovereenkomst (server-side)
            KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
            keyAgree.init(keyPair.getPrivate());
            keyAgree.doPhase(clientPublicKey, true);

            // Stap 3: Genereer gedeeld geheime sleutel
            byte[] sharedSecret = keyAgree.generateSecret();

            // Stap 4: Gebruik SHA-256 om een AES-sleutel van 16 bytes te maken
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha256.digest(sharedSecret);
            SecretKey sharedSecretKey = new SecretKeySpec(keyBytes, 0, 16, "AES");

            // Stap 5: Ontvang de versleutelde order via UDP
            buffer = new byte[4096];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String encryptedOrderString = new String(packet.getData(), 0, packet.getLength());

            // Stap 6: Decrypt de order
            BasicDecryption decryption = new BasicDecryption(encryptedOrderString, sharedSecretKey);
            decryption.execute();
            String decryptedDataString = decryption.getDecryptedData();

            // afdrukken van decrypted string
            System.out.println("Ontvangen bestelling:\n" + decryptedDataString);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
