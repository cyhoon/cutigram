package jeff.cutigram.lib;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Encryption {
    public static String getSHA512(String plainText) {
        String cryptoText = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(plainText.getBytes("utf-8"));
            cryptoText = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cryptoText;
    }
}
