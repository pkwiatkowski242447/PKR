import org.junit.jupiter.api.Test;
import pkr.zadanie2.exceptions.IncorrectMessageDigestAlgorithm;
import pkr.zadanie2.model.ElGamalSystem;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ElGamalSystemTest {

    @Test
    public void signatureGenerationTestAndVerifyingIt() {
        try {
            ElGamalSystem elGamal = new ElGamalSystem();
            assertNotNull(elGamal);
            System.out.println("=== Debug ===");
            System.out.println(new BigInteger(elGamal.getGNumber()));
            System.out.println(new BigInteger(elGamal.getPNumber()));
            System.out.println(new BigInteger(elGamal.getHNumber()));
            byte[] someInput = "Some input to sign with digital signature.".getBytes(StandardCharsets.UTF_8);
            BigInteger[] signature = elGamal.signGivenFile(someInput);
            System.out.println("=== Debug ===");
            System.out.println(signature[0]);
            System.out.println(signature[1]);
            boolean verified = elGamal.verifyIfSignatureIsCorrect(someInput, signature);
            if (verified) {
                System.out.println("Podpis jest prawidłowy.");
            } else {
                System.out.println("Podpis nie jest prawidłowy.");
            }
        } catch(IncorrectMessageDigestAlgorithm exception) {
            fail();
        }
    }

    @Test
    public void signatureGenerationTestAndVerifyingWithOtherSignature() {
        try {
            ElGamalSystem elGamal = new ElGamalSystem();
            assertNotNull(elGamal);
            System.out.println("=== Debug ===");
            System.out.println(new BigInteger(elGamal.getGNumber()));
            System.out.println(new BigInteger(elGamal.getPNumber()));
            System.out.println(new BigInteger(elGamal.getHNumber()));
            byte[] someInput = "Some input to sign with digital signature.".getBytes(StandardCharsets.UTF_8);
            BigInteger[] signature = elGamal.signGivenFile(someInput);
            System.out.println("=== Debug ===");
            signature[0] = BigInteger.ONE;
            System.out.println(signature[0]);
            System.out.println(signature[1]);
            boolean verified = elGamal.verifyIfSignatureIsCorrect(someInput, signature);
            if (verified) {
                System.out.println("Podpis jest prawidłowy.");
            } else {
                System.out.println("Podpis nie jest prawidłowy.");
            }
        } catch(IncorrectMessageDigestAlgorithm exception) {
            fail();
        }
    }
}
