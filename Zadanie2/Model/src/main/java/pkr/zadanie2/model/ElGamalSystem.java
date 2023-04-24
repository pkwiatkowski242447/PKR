package pkr.zadanie2.model;

import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;

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
    private MessageDigest givenMessageDigest;
    private Random randomIntNumber = new Random();
    /*
        selectedPrimeNumberLength -> number of generated prime numbers in bits.
     */
    private int selectedPrimeNumberLength;

    /*
        @ Method: ElGamalSystem -> Constructor

        @ Parameters:

        MessageDigest givenMessageDigest -> message digest algorithm, which will be used for signing files.
        int selectedPrimeNumberLength -> generated p number length in bits.
        BigInteger[] publicKey -> array of big numbers containing public key, that is values for gNumber, pNumber,
        hNumber in that specific order.

        @ Returned value:

        ElGamalSystem -> instance of this class object, that will be used in other parts of the program.

        @ Description: This method is used to create ElGamalSystem object instance. It main use is to set values
        for message digest and generated prime numbers length.
     */

    public ElGamalSystem(String givenMessageDigestInString, int selectedPrimeNumberLength, BigInteger[] publicKey) throws IncorrectMessageDigestAlgorithm {
        this.selectedPrimeNumberLength = selectedPrimeNumberLength;
        if (publicKey != null) {
            gNumber = publicKey[0];
            pNumber = publicKey[1];
            hNumber = publicKey[2];
        } else {
            generateKeyMethod();
        }
        try {
            this.givenMessageDigest = MessageDigest.getInstance(givenMessageDigestInString);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new IncorrectMessageDigestAlgorithm("Entered message digest algorithm: " + givenMessageDigestInString + " is incorrect ; Cause: " + noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
        }
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
            pNumber = BigInteger.probablePrime(selectedPrimeNumberLength + 2, randomIntNumber);
            isNumberPrime = millerRabinPrimalityTest(pNumber);
        } while (isNumberPrime);
        aNumber = new BigInteger(selectedPrimeNumberLength, randomIntNumber);
        gNumber = new BigInteger(selectedPrimeNumberLength, randomIntNumber);
        hNumber = gNumber.modPow(aNumber, pNumber);
        pNumberMinusOne = pNumber.subtract(BigInteger.ONE);
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
        sNo1Number = gNumber.modPow(rNumber, pNumberMinusOne);
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

    }
}
