package careless.util;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class ES256KKeyLoader {

    /**
     * Loads a secp256k1 Public Key from a 65-byte uncompressed public key.
     * (The format is: 0x04 || X (32 bytes) || Y (32 bytes))
     */
    public PublicKey loadES256KPublicKey(byte[] pubKeyBytes) throws Exception {

        // 1. Get the parameter specification for the secp256k1 curve
        ECNamedCurveParameterSpec curveSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        // 2. Convert the byte array into an ECPoint
        // The curveSpec.getCurve().decodePoint handles the 0x04 prefix and coordinates.
        ECPoint point = curveSpec.getCurve().decodePoint(pubKeyBytes);

        // 3. Create a Bouncy Castle public key specification
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, curveSpec);

        // 4. Get a KeyFactory instance for "EC" (Elliptic Curve) using the BC provider
        KeyFactory keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);

        // 5. Generate the standard Java PublicKey object
        return keyFactory.generatePublic(pubKeySpec);
    }

    public PublicKey loadPublicKeyFromSPKI(byte[] spkiBytes) throws Exception {

        // The input byte array 'spkiBytes' should be your DER-encoded X.509 SPKI key (the one starting with 0x30)
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(spkiBytes);

        // Get a KeyFactory instance for "EC" (Elliptic Curve) using the BC provider
        // BC will automatically read the OID for the curve (like secp256k1) from the SPKI structure.
        KeyFactory keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);

        // Generate the standard Java PublicKey object
        return keyFactory.generatePublic(x509KeySpec);
    }

    public PublicKey loadPublicKeyFromSPKIBouncy(byte[] spkiBytes) throws Exception {

        // 1. Specify the "EC" algorithm.
        // 2. IMPORTANT: Specify BouncyCastleProvider.PROVIDER_NAME
        KeyFactory keyFactory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(spkiBytes);

        return keyFactory.generatePublic(x509KeySpec);
    }
}
