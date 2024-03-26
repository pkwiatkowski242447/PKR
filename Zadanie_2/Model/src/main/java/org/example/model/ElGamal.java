package org.example.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ElGamal {
    private BigInteger a, p, g, h, p_2, s_1, s_2, r;
    private final int length = 1024;
    private MessageDigest messageDigest;
    private Random rand = new Random();


    public ElGamal() throws NoSuchAlgorithmException {
        generatePrimeNumbers();
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public BigInteger[] signMessage(byte[] text) throws LengthException {
        if (text.length == 0) {
            throw new LengthException("Nie można wygenerować podpisu bez tekstu");
        }
        BigInteger[] signature = new BigInteger[2];
        messageDigest.update(text);
        BigInteger messageDigestV = new BigInteger(1, messageDigest.digest());
        r = generateRandomNumberInRange(p_2);
        while (!r.gcd(p_2).equals(BigInteger.ONE)) {
            r = r.nextProbablePrime();
        }
        BigInteger r_apostrophe = r.modInverse(p_2);
        BigInteger s_1 = g.modPow(r, p);
        BigInteger s_2 = (messageDigestV.subtract(a.multiply(s_1))).multiply(r_apostrophe).mod(p_2);
        signature[0] = s_1;
        signature[1] = s_2;
        return signature;
    }

    public boolean verifySignature(BigInteger[] messageSignature, byte[] message) throws LengthException {
        if (message.length == 0) {
            throw new LengthException("Nie można wygenerować podpisu bez tekstu");
        }
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
            p = BigInteger.probablePrime(length, rand);
            isValid = RabinMillerTest(p);
        }
        p_2 = p.subtract(BigInteger.ONE);
        g = generateRandomNumberInRange(p_2);
        a = generateRandomNumberInRange(p_2);
        h = g.modPow(a, p);
    }

    public boolean RabinMillerTest(BigInteger p) {
        /// p - 1 = 2 ^ s * m
        boolean result = true;
        BigInteger m = p.subtract(BigInteger.ONE);
        int iterator = 10;
        BigInteger yi;
        while (m.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            m = m.divide(BigInteger.TWO);
        }
        while (iterator > 0) {
            BigInteger x = generateRandomNumberInRange(p);
            if (x.gcd(p).compareTo(BigInteger.ONE) != 0) {
                result = false;
                break;
            }
            BigInteger y0 = x.modPow(m, p);
            do {
                yi = y0.modPow(BigInteger.TWO, p);
                y0 = yi;
            } while (yi.mod(p).compareTo(BigInteger.ONE) != 0 && yi.mod(p).compareTo(new BigInteger("-1")) != 0);
            if (yi.mod(p).compareTo(BigInteger.ONE) != 0) {
                result = false;
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

    public String getPrivateKey() {
        return a.toString();
    }

    public String getPublicKey() {
        return p.toString() + g.toString() + h.toString();
    }


}
