package pkr.zadanie2.model;

import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;
import pkr.zadanie2.exceptions.NullValueException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ElGamalSystem {

    /*
        @ Class: ElGamalSystem

        @ Description: This class is responsible for implementing all methods required in process of singing a
        given file and verifying whether the signature is correct or not.
     */

    private BigInteger pNumber, gNumber, aNumber, hNumber, rNumber, sNo1Number, sNo2Number, reverseOfRNumber, pNumberMinusOne;
    /*
        givenMessageDigest -> message digest algorithm, which could be chosen by the user.
     */
    private final MessageDigest givenMessageDigest;
    private final Random randomIntNumber = new Random();
    /*
        selectedPrimeNumberLength -> number of generated prime numbers in bits.
     */
    private final int selectedPrimeNumberLength = 1024;

    /*
        @ Method: ElGamalSystem -> Constructor

        @ Parameters: None

        @ Returned value:

        ElGamalSystem -> instance of this class object, that will be used in other parts of the program.

        @ Description: This method is used to create ElGamalSystem object instance. It main use is to set values
        for message digest and generated prime numbers length.
     */

    public ElGamalSystem() throws IncorrectMessageDigestAlgorithm {
        try {
            this.givenMessageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IncorrectMessageDigestAlgorithm("Entered message digest algorithm: SHA-512  is incorrect ; Cause: " + noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
        }
    }

    /*
        @ Getters:
     */

    public byte[] getPNumber() {
        if (pNumber != null) {
            return pNumber.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public byte[] getGNumber() {
        if (gNumber != null) {
            return gNumber.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public byte[] getHNumber() {
        if (hNumber != null) {
            return hNumber.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public byte[] getANumber() {
        if (aNumber != null) {
            return aNumber.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public byte[] getsNo1Number() {
        if (sNo1Number != null) {
            return sNo1Number.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public byte[] getsNo2Number() {
        if (sNo2Number != null) {
            return sNo2Number.toByteArray();
        }
        throw new NullValueException("Próbowano odczytać wartość, która jest wskaźnikiem do null'a.");
    }

    public void setPublicKey(BigInteger gNumber, BigInteger pNumber, BigInteger hNumber) {
        this.hNumber = hNumber;
        this.pNumber = pNumber;
        this.gNumber = gNumber;
    }

    public void setPrivateKey(BigInteger aNumber) {
        this.aNumber = aNumber;
    }

    /*
        @ Method: generateKeyMethod

        @ Parameters: None

        @ Returned value: None

        @ Description: This method is used for generating primeNumber p, and values of gNumber and aNumber
        and base on these three values hNumber is generated. Since pNumber, hNumber and gNumber create a
        public key - this name of this method is self-explanatory, and aNumber is private key.
     */

    public void generateKeyMethod() {
        boolean isNumberPrime;
        do {
            pNumber = BigInteger.probablePrime(selectedPrimeNumberLength, randomIntNumber);
            isNumberPrime = millerRabinPrimalityTest(pNumber);
        } while (!isNumberPrime);
        pNumberMinusOne = pNumber.subtract(BigInteger.ONE);
        aNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, pNumberMinusOne);
        gNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, pNumberMinusOne);
        hNumber = gNumber.modPow(aNumber, pNumber);
    }

    /*
        @ Method: signGivenFile

        @ Parameters:

        byte[] byteArrayFromGivenFile -> byte array read from a given file, which later will be signed with
        ElGamal signature.

        @ Returned value:

        BigInteger[] -> big integer array containing signature for a given file

        @ Description: This method is designed to take byte array from a certain file and sign it with
        ElGamal signature. After getting signature, it is returned by this method.
     */

    public BigInteger[] signGivenFile(byte[] byteArrayFromGivenFile) {
        BigInteger[] generatedSignature = new BigInteger[2];
        givenMessageDigest.update(byteArrayFromGivenFile);
        BigInteger messageDigestValue = new BigInteger(1, givenMessageDigest.digest());
        rNumber = BigInteger.probablePrime(selectedPrimeNumberLength, randomIntNumber);
        while(!rNumber.gcd(pNumberMinusOne).equals(BigInteger.ONE)) {
            rNumber = rNumber.nextProbablePrime();
        };
        reverseOfRNumber = rNumber.modInverse(pNumberMinusOne);
        sNo1Number = gNumber.modPow(rNumber, pNumber);
        sNo2Number = (messageDigestValue.subtract(aNumber.multiply(sNo1Number))).multiply(reverseOfRNumber).mod(pNumberMinusOne);
        generatedSignature[0] = sNo1Number;
        generatedSignature[1] = sNo2Number;
        return generatedSignature;
    }

    /*
        @ Method: verifyIfSignatureIsCorrect

        @ Parameters:

        byte[] byteArrayFromGivenFile -> array of bytes read from a given file.
        BigInteger[] signatureOnTheFile -> array containing both s1 and s2 for signature.

        @ Returned value:

        boolean -> a boolean representing whether certain signature for a certain file is correct or incorrect.

        @ Description: This method is used for testing, whether some signature that user gets from different source
        is correct for a certain file. Assessment is based upon equality of hash values (one that is deciphered and one that
        is generated with message digest function).
     */

    public boolean verifyIfSignatureIsCorrect(byte[] byteArrayFromGivenFile, BigInteger[] signatureOnTheFile) {
        sNo1Number = signatureOnTheFile[0];
        sNo2Number = signatureOnTheFile[1];
        givenMessageDigest.update(byteArrayFromGivenFile);
        BigInteger generatedHashValue = new BigInteger(1, givenMessageDigest.digest());
        BigInteger valueToBeComparedNo1 = gNumber.modPow(generatedHashValue, pNumber);
        BigInteger valueToBeComparedNo2 = hNumber.modPow(sNo1Number, pNumber).multiply(sNo1Number.modPow(sNo2Number, pNumber)).mod(pNumber);
        if (valueToBeComparedNo1.compareTo(valueToBeComparedNo2) == 0) {
            return true;
        }
        return false;
    }

    /*
        @ Method: millerRabinPrimalityTest

        @ Parameters:

        BigInteger numberThatIsSupposedToBePrime -> big integer value that will be tested for being a prime value.

        @ Returned value:

        boolean -> boolean value representing whether parameter numberThatIsSupposedToBePrime is a prime number - so
        true when in fact it is, and false when it is not.

        @ Description: This method is used for testing whether generated BigInteger number is actually a prime or not. The
        result of assessment is returned as boolean value by this very method.
     */

    public boolean millerRabinPrimalityTest(BigInteger numberThatIsSupposedToBePrime) {
        // Choosing randomNumber value, that fits requirement: 1 < randomNumber < pNumber
        BigInteger randomNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, numberThatIsSupposedToBePrime);
        // Finding mNumber value, that: pNumber - 1 = 2 ^ s * mNumber
        BigInteger mNumber = numberThatIsSupposedToBePrime.subtract(BigInteger.ONE);
        do {
            mNumber = mNumber.divide(BigInteger.TWO);
        } while(mNumber.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0);
        // Checking whether NWD(randomNumber, pNumber) != 1 : if yes then the number is composite so it is not a prime
        // in the other case: it fulfills requirement for being a prime.
        if (randomNumber.gcd(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0) {
            return false;
        }
        // Calculating y0Value in order to check if y0 = 1 (mod n)
        // if it is then it means that numberThatIsSupposedToBePrime is actually a prime
        BigInteger y0Value = randomNumber.modPow(mNumber, numberThatIsSupposedToBePrime);
        if (y0Value.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) == 0) {
            return true;
        }
        BigInteger yIValue;
        do {
            yIValue = y0Value.modPow(BigInteger.TWO, numberThatIsSupposedToBePrime);
            y0Value = yIValue;
        } while(yIValue.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0
                && yIValue.mod(numberThatIsSupposedToBePrime).compareTo(new BigInteger("-1")) != 0);
        if (yIValue.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0) {
            return false;
        }
        return true;
    }

    /*
        @ Method: getRandomBigIntegerSmallerThanN

        @ Parameters:

        BigInteger minimumValue -> minimum value of big integer that can be returned
        BigInteger maximumValue -> maximum value of big integer that can be returned

        @ Returned value:

        BitInteger -> reference to BigInteger object, which contains value between minimumValue and
        maximumValue

        @ Description: The aim of this method is to create a BigInteger object, which value is between
        minimumValue and maximumValue.
     */

    private BigInteger getRandomBigIntegerSmallerThanN(BigInteger minimumValue, BigInteger maximumValue) {
        BigInteger result;
        do {
            result = new BigInteger(maximumValue.bitLength(), randomIntNumber);
        } while(result.compareTo(minimumValue) <= 0 || result.compareTo(maximumValue) >= 0);
        return result;
    }
}
