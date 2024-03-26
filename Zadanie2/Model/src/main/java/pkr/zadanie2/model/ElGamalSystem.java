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

    /*
        @ Variable: int selectedPrimeNumberLength

        @ Description: selectedPrimeNumberLength -> length of generated prime number in bits.
     */
    private int selectedPrimeNumberLength = 1024;

    /*
        @ Variables of BigInteger type:

        * pNumber -> random prime number of selectedPrimeNumberLength length in bits. It is one of three elements
        (together with gNumber and hNumber) that together create public key.
        * gNumber -> random number between 1 and pNumber - 1. One of three elements inside public key.
        * aNumber -> random number between 1 and pNumber - 1. It is user private key.
        * hNumber -> value calculated from equation: h = gNumber ^ aNumber mod pNumber
        * sNo1Number -> first element of signature (since signature in ElGamal crypto-system consists of 2 parts)
        * sNo2Number -> second element of signature
        * pNumberMinusOne -> value of pNumber - 1, which is used in generation of gNumber and aNumber, as well as
        making a signature
        * rNumber -> value which is used during signing processes, for getting sNo1Number value.
        * reverseOfRNumber -> value which is used during signing processes, for getting sNo2Number value.
     */

    private BigInteger pNumber, gNumber, aNumber, hNumber, rNumber, sNo1Number, sNo2Number, reverseOfRNumber, pNumberMinusOne;
    /*
        @ Variable: MessageDigest givenMessageDigest

        @ Description: givenMessageDigest is a message digest algorithm. In this program SHA-512 is chosen in order
        to make messageDigest (could be chosen by the user, but then the other user somehow would need to know
        the name of this algorithm to check its validity).
     */
    private final MessageDigest givenMessageDigest;
    private final Random randomIntNumber = new Random();
    private int numberOfIterationsInMillerRabinTest = 32;

    /*
        @ Method: ElGamalSystem -> Constructor

        @ Parameters: None

        @ Returned value:

        * ElGamalSystem -> instance of this class object, that will be used in other parts of the program.

        @ Description: This method is used to create ElGamalSystem object instance. Its main use is to set values
        for generated prime number length.
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
        throw new NullValueException("Reading value from reference to null is impossible.");
    }

    public byte[] getGNumber() {
        if (gNumber != null) {
            return gNumber.toByteArray();
        }
        throw new NullValueException("Reading value from reference to null is impossible.");
    }

    public byte[] getHNumber() {
        if (hNumber != null) {
            return hNumber.toByteArray();
        }
        throw new NullValueException("Reading value from reference to null is impossible.");
    }

    public byte[] getANumber() {
        if (aNumber != null) {
            return aNumber.toByteArray();
        }
        throw new NullValueException("Reading value from reference to null is impossible.");
    }

    public int getSelectedPrimeNumberLength() {
        return selectedPrimeNumberLength;
    }

    /*
        @ Method: setPublicKey

        @ Parameters:

        * BigInteger gNumber -> BigInteger value representing value of gNumber.
        * BigInteger pNumber -> BigInteger value representing value of pNumber.
        * BigInteger hNumber -> BigInteger value representing value of hNumber.

        Values of all of above parameters are typically read from a file, that contains a formatted public key.

        @ Returns: None

        @ Description: This method is used for setting entire public key. Each one of the values is typically read from a
        file that contains a formatted public key.
     */

    public void setPublicKey(BigInteger gNumber, BigInteger pNumber, BigInteger hNumber) {
        this.hNumber = hNumber;
        this.pNumber = pNumber;
        this.gNumber = gNumber;
    }

    /*
        @ Method: setSelectedPrimeNumberLength

        @ Parameters:

        * int selectedPrimeNumberLength ->

        @ Returns: None

        @ Description: This method is used for setting new length of a generated prime number. It is used only
        when new keys are generated, so by default this value stays as 1024 bits.
     */

    public void setSelectedPrimeNumberLength(int selectedPrimeNumberLength) {
        this.selectedPrimeNumberLength = selectedPrimeNumberLength;
    }

    /*
        @ Method: setPrivateKey

        @ Parameters:

        * BigInteger aNumber -> BigInteger value representing value of aNumber.

        @ Returns:

        @ Description: This method is used for setting value of a private key.
     */

    public void setPrivateKey(BigInteger aNumber) {
        this.aNumber = aNumber;
    }

    /*
        @ Method: generateKeyMethod

        @ Parameters: None

        @ Returned value: None

        @ Description: This method is used for generating primeNumber pNumber, and values of gNumber and aNumber
        and based on these three values hNumber is generated. Since pNumber, hNumber and gNumber create a
        public key - this name of this method is self-explanatory, and aNumber is private key.
     */

    public void generateKeyMethod() {
        boolean isNumberPrime;
        do {
            // Generating pNumber values as long as it is not a valid prime number.
            pNumber = BigInteger.probablePrime(selectedPrimeNumberLength, randomIntNumber);
            isNumberPrime = millerRabinPrimalityTest(pNumber);
        } while (!isNumberPrime);
        // When pNumber is generated we can generate next values: pNumberMinusOne, gNumber, aNumber and hNumber, since
        // their values depend on pNumber value.
        pNumberMinusOne = pNumber.subtract(BigInteger.ONE);
        aNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, pNumberMinusOne);
        gNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, pNumberMinusOne);
        hNumber = gNumber.modPow(aNumber, pNumber);
    }

    /*
        @ Method: signGivenFile

        @ Parameters:

        * byte[] byteArrayFromGivenFile -> Byte array read from a given file, which later will be signed with
        ElGamal signature.

        @ Returned value:

        * BigInteger[] -> Big integer array containing signature for a given file.

        @ Description: This method is designed to take byte array from a certain file and sign it with
        ElGamal signature. After getting signature, it is returned by this method.
     */

    public BigInteger[] signGivenFile(byte[] byteArrayFromGivenFile) {
        BigInteger[] generatedSignature = new BigInteger[2];
        givenMessageDigest.update(byteArrayFromGivenFile);
        // Getting digest value for a byte array taken from a file or text input.
        BigInteger messageDigestValue = new BigInteger(1, givenMessageDigest.digest());
        // Generating rNumber value for getting sNo1Number value.
        rNumber = BigInteger.probablePrime(selectedPrimeNumberLength, randomIntNumber);
        while(!rNumber.gcd(pNumberMinusOne).equals(BigInteger.ONE)) {
            rNumber = rNumber.nextProbablePrime();
        };
        // Getting reverse of rNumber number modulo pNumberMinusOne
        reverseOfRNumber = rNumber.modInverse(pNumberMinusOne);
        // Getting sNo1Number value using formula: sNo1Number =  gNumber ^ rNumber mod pNumber
        sNo1Number = gNumber.modPow(rNumber, pNumber);
        // Getting sNo2Number value using formula sNo2Number = (messageDigestValue - aNumber * sNo1Number) * reverseOfRNumber mod pNumberMinusOne
        sNo2Number = (messageDigestValue.subtract(aNumber.multiply(sNo1Number))).multiply(reverseOfRNumber).mod(pNumberMinusOne);
        // Putting values of sNo1Number and sNo2Number in BigInteger 2 element array.
        generatedSignature[0] = sNo1Number;
        generatedSignature[1] = sNo2Number;
        return generatedSignature;
    }

    /*
        @ Method: verifyIfSignatureIsCorrect

        @ Parameters:

        * byte[] byteArrayFromGivenFile -> array of bytes read from a given file.
        * BigInteger[] signatureOnTheFile -> array containing both s1 and s2 for signature.

        @ Returned value:

        * boolean -> a boolean representing whether certain signature for a certain file is correct or incorrect.

        @ Description: This method is used for testing, whether some signature that user gets from different source
        is correct for a certain file. Assessment is based upon equality of hash values (one that is deciphered and one that
        is generated with message digest function).
     */

    public boolean verifyIfSignatureIsCorrect(byte[] byteArrayFromGivenFile, BigInteger[] signatureOnTheFile) {
        // Getting sNo1Number and sNo2Number values from a passed BigInteger 2 element array.
        sNo1Number = signatureOnTheFile[0];
        sNo2Number = signatureOnTheFile[1];
        givenMessageDigest.update(byteArrayFromGivenFile);
        // Getting messageDigest value for a read byte array.
        BigInteger generatedHashValue = new BigInteger(1, givenMessageDigest.digest());
        // Getting left side of the equation, that is: gNumber ^ generatedHashValue mod pNumber
        BigInteger valueToBeComparedNo1 = gNumber.modPow(generatedHashValue, pNumber);
        // Getting right side of the equation, that is: h ^ sNo1Number * sNo1Number ^ sNo2Number mod pNumber.
        BigInteger valueToBeComparedNo2 = hNumber.modPow(sNo1Number, pNumber).multiply(sNo1Number.modPow(sNo2Number, pNumber)).mod(pNumber);
        // Comparing left side of the equation to the right, if they are equal, then signature is correct, if not - then
        // signature is not correct.
        if (valueToBeComparedNo1.compareTo(valueToBeComparedNo2) == 0) {
            return true;
        }
        return false;
    }

    /*
        @ Method: millerRabinPrimalityTest

        @ Parameters:

        * BigInteger numberThatIsSupposedToBePrime -> big integer value that will be tested for being a prime value.

        @ Returned value:

        * boolean -> boolean value representing whether parameter numberThatIsSupposedToBePrime is a prime number - so
        true when in fact it is, and false when it is not.

        @ Description: This method is used for testing whether generated BigInteger number is actually a prime or not. The
        result of assessment is returned as boolean value by this method. In order to make the test return as correct
        result as possible, it is repeated numberOfIterationInMillerRabinTest number of times.
     */

    public boolean millerRabinPrimalityTest(BigInteger numberThatIsSupposedToBePrime) {
        // Finding mNumber value, that: pNumber - 1 = (2 ^ s) * mNumber
        BigInteger mNumber = numberThatIsSupposedToBePrime.subtract(BigInteger.ONE);
        do {
            mNumber = mNumber.divide(BigInteger.TWO);
        } while(mNumber.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0);

        boolean testResult = true;

        for (int i = 0; i < numberOfIterationsInMillerRabinTest; i++) {
            // Choosing randomNumber value, that fits requirement: 1 < randomNumber < pNumber
            BigInteger randomNumber = getRandomBigIntegerSmallerThanN(BigInteger.ONE, numberThatIsSupposedToBePrime);

            // Checking whether NWD(randomNumber, pNumber) != 1 : if yes then the number is composite, so it is not a prime
            // in the other case: it fulfills requirement for being a prime.
            if (randomNumber.gcd(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0) {
                testResult = false;
                break;
            }
            // Calculating y0Value in order to check if y0 = 1 (mod n)
            // if it is then it means that numberThatIsSupposedToBePrime is actually a prime
            BigInteger y0Value = randomNumber.modPow(mNumber, numberThatIsSupposedToBePrime);
            if (y0Value.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) == 0) {
                testResult = true;
                continue;
            }
            BigInteger yIValue;
            do {
                yIValue = y0Value.modPow(BigInteger.TWO, numberThatIsSupposedToBePrime);
                y0Value = yIValue;
            } while(yIValue.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0
                    && yIValue.mod(numberThatIsSupposedToBePrime).compareTo(new BigInteger("-1")) != 0);
            if (yIValue.mod(numberThatIsSupposedToBePrime).compareTo(BigInteger.ONE) != 0) {
                testResult = false;
                break;
            } else {
                testResult = true;
            }
        }
        return testResult;
    }

    /*
        @ Method: getRandomBigIntegerSmallerThanN

        @ Parameters:

        * BigInteger minimumValue -> minimum value of big integer that can be returned
        * BigInteger maximumValue -> maximum value of big integer that can be returned

        @ Returned value:

        * BitInteger -> reference to BigInteger object, which contains value between minimumValue and
        maximumValue

        @ Description: The aim of this method is to create a BigInteger object, which value is between
        minimumValue and maximumValue. It is used mainly, to get values random values for gNumber and aNumber
        between 1 and pNumber, when pNumber is generated prime number.
     */

    private BigInteger getRandomBigIntegerSmallerThanN(BigInteger minimumValue, BigInteger maximumValue) {
        BigInteger result;
        do {
            result = new BigInteger(maximumValue.bitLength(), randomIntNumber);
        } while(result.compareTo(minimumValue) <= 0 || result.compareTo(maximumValue) >= 0);
        return result;
    }
}
