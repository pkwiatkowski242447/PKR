package org.example;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ElGamal {
    private BigInteger a, p, g, h, p_2, s_1, s_2, r;
    private final int length = 1024;
    private MessageDigest messageDigest;
    private Random rand = new Random();

    public BigInteger[] signMessage(byte[] text) throws NoSuchAlgorithmException {
        BigInteger[] signature = new BigInteger[2];
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(text);
        BigInteger messageDigestV = new BigInteger(messageDigest.digest());
        r = generateRandomNumberInRange(p_2);
        BigInteger s_1 = g.modPow(r, p);
        BigInteger r_apostrophe = r.modPow(BigInteger.valueOf(-1), p.subtract(BigInteger.ONE));
        BigInteger s_2 = messageDigestV.subtract(a.multiply(s_1)).multiply(r_apostrophe.mod(p.subtract(BigInteger.ONE)));
        signature[0] = s_1;
        signature[1] = s_2;
        return signature;
    }

    public boolean verifySignature(BigInteger[] messageSignature, byte[] message) {
        s_1 = messageSignature[0];
        s_2 = messageSignature[1];
        messageDigest.update(message);
        BigInteger messageDigestV = new BigInteger(messageDigest.digest());
        if (g.modPow(messageDigestV, p).compareTo(h.modPow(s_1, p).multiply(s_1.modPow(s_2, p)).mod(p)) != 0) {
            return false;
        }
        return true;
    }

    public void generatePrimeNumbers() {
        boolean isValid = false;
        while (!isValid) {
            p = BigInteger.probablePrime(1024, rand);
            isValid = Rabin_Miller_Test(p);
        }
        p_2 = p.subtract(BigInteger.ONE);
        g = generateRandomNumberInRange(p_2);
        a = generateRandomNumberInRange(p_2);
        h = g.modPow(a, p);
    }

    public boolean Rabin_Miller_Test(BigInteger p) {
        /// p - 1 = 2 ^ s * m
        boolean result = false;
        BigInteger m = p.subtract(BigInteger.ONE);
        int iterator = 10;
        BigInteger yi = null;
        while (m.mod(BigInteger.TWO) != BigInteger.ZERO) {
            m = (p.subtract(BigInteger.ONE)).divide(BigInteger.TWO);
        }
        while (iterator > 0) {
            BigInteger x = generateRandomNumberInRange(p);
            if (x.gcd(p).compareTo(BigInteger.ONE) != 0) {
                break;
            }
            BigInteger y0 = x.modPow(m, p);
            if (y0.mod(p).compareTo(BigInteger.ONE) != 0) {
                result = true;
                break;
            }
            while (yi.mod(p).compareTo(BigInteger.ONE) != 0 && yi.mod(p).compareTo(new BigInteger("-1")) != 0) {
                yi = y0.modPow(BigInteger.TWO, p);
                y0 = yi;
            }
            if (yi.mod(p).compareTo(BigInteger.ONE) == 0) {
                break;
            }
            if (yi.mod(p).compareTo(new BigInteger("-1")) == 0) {
                result = true;
                break;
            }
            iterator--;
        }
        return result;
    }

    public BigInteger generateRandomNumberInRange(BigInteger max) {
        BigInteger minLimit = BigInteger.ONE;
        BigInteger x;
        do {
            x = new BigInteger(max.bitLength(), rand);
        } while (x.compareTo(minLimit) > 0 && x.compareTo(max) < 0);
        return x;
    }
}
