package org.example;

import java.math.BigInteger;
import java.util.Random;

public class ElGamal {
    private BigInteger a, p, g, h;
    private int length = 1024;
    private Random rand = new Random();

    public void generatePrimeNumbers() {
        boolean isValid = false;
        while (isValid) {
            p = BigInteger.probablePrime(1024, rand);
            isValid = Rabin_Miller_Test(p);
        }
        BigInteger p_2 = p.subtract(BigInteger.ONE);
        g = generateXNumber(p_2);
        a = generateXNumber(p_2);
        h = g.modPow(a, p);
    }

    public boolean Rabin_Miller_Test(BigInteger p) {
        /// p - 1 = 2 ^ s * m
        boolean result = false;
        BigInteger m = p.subtract(BigInteger.ONE);
        int iterator = 10;
        BigInteger yi;
        while (m.mod(BigInteger.TWO) != BigInteger.ZERO) {
            m = (p.subtract(BigInteger.ONE)).divide(BigInteger.TWO);
        }
        while (iterator > 0) {
            BigInteger x = generateXNumber(p);
            if (x.gcd(p).compareTo(BigInteger.ONE) != 0) {
                break;
            }
            BigInteger y0 = x.modPow(m, p);
            if (y0.mod(p).compareTo(BigInteger.ONE) != 0) {
                result = true;
                break;
            }
            do {
                yi = y0.modPow(BigInteger.TWO, p);
                y0 = yi;
            } while (yi.mod(p).compareTo(BigInteger.ONE) != 0 && yi.mod(p).compareTo(new BigInteger("-1")) != 0);
            if (yi.mod(p).compareTo(BigInteger.ONE) == 0) {
                break;
            }
            if (yi.mod(p).compareTo(new BigInteger("-1")) == 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    public BigInteger generateXNumber(BigInteger max) {
        BigInteger minLimit = BigInteger.ONE;
        BigInteger x;
        do {
            x = new BigInteger(max.bitLength(), rand);
        } while (x.compareTo(minLimit) > 0 && x.compareTo(max) < 0);
        return x;
    }
}
