package pkr.zadanie2.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class ElGamalSystem {

    /*
        @ Class: ElGamalSystem

        @ Description: This class is responsible for implementing all methods required in process of singing a
        given file and verifying whether the signature is correct or not.
     */

    private BigInteger pNumber, gNumber, aNumber, hNumber, rNumber, sNo1Number, sNo2Number, reverseOfRNumber;
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

        @ Returned value:

        ElGamalSystem -> instance of this class object, that will be used in other parts of the program.

        @ Description: This method is used to create ElGamalSystem object instance. It main use is to set values
        for message digest and generated prime numbers length.
     */

    public ElGamalSystem(MessageDigest givenMessageDigest, int selectedPrimeNumberLength) {
        this.givenMessageDigest = givenMessageDigest;
        this.selectedPrimeNumberLength = selectedPrimeNumberLength;
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
        
    }

    /*
        @ Method: verifyIfSignatureIsCorrect

        @ Parameters:

        byte[] byteArrayFromGivenFile -> array of bytes read from a given file.
        byte[] signatureOnTheFile -> array of bytes representing signature made with ElGamal scheme.

        @ Returned value:

        boolean -> a boolean representing whether certain signature for a certain file is correct or incorrect.

        @ Description: This method is used for testing, whether some signature that user gets from different source
        is correct for a certain file. Assessment is based upon equality of hash values (one that is deciphered and one that
        is generated with message digest function).
     */

    public boolean verifyIfSignatureIsCorrect(byte[] byteArrayFromGivenFile, byte[] signatureOnTheFile) {

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
