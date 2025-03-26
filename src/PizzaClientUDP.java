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

public class PizzaClientUDP {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");

            // Stap 1: Genereer Diffie-Hellman-sleutelpaar (client-side)
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Verstuur publieke sleutel van client naar server
            byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
            String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);
            byte[] buffer = publicKeyString.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 5001);
            socket.send(packet);

            // Ontvang publieke sleutel van de server
            buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String serverPublicKeyString = new String(packet.getData(), 0, packet.getLength());
            byte[] serverPublicKeyBytes = Base64.getDecoder().decode(serverPublicKeyString);
            PublicKey serverPublicKey = KeyFactory.getInstance("DH")
                    .generatePublic(new X509EncodedKeySpec(serverPublicKeyBytes));

            // Stap 2: Diffie-Hellman-sleutelovereenkomst (client-side)
            KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
            keyAgree.init(keyPair.getPrivate());
            keyAgree.doPhase(serverPublicKey, true);

            // Stap 3: Genereer gedeeld geheim
            byte[] sharedSecret = keyAgree.generateSecret();

            // Stap 4: Gebruik SHA-256 om een AES-sleutel van 16 bytes te maken
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = sha256.digest(sharedSecret);
            SecretKey sharedSecretKey = new SecretKeySpec(keyBytes, 0, 16, "AES");

            // Stap 5: Seriële data voorbereiden (kan hetzelfde blijven als TCP)
            Order order = new Order();
            String bestellingString = order.createOrderString();

            // Stap 6: Encrypt de order (kan hetzelfde blijven als TCP)
            BasicEncryption encryption = new BasicEncryption(bestellingString, sharedSecretKey);
            encryption.execute();
            String encryptedData = encryption.getEncryptedData();

            // Stap 7: Stuur de versleutelde bestelling via UDP
            buffer = encryptedData.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, 5001);
            socket.send(packet);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
