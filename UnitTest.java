package enigma;

import org.junit.Test;
import ucb.junit.textui;
import java.util.ArrayList;
import static enigma.TestUtils.*;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the enigma package.
 *  @author Josh Rubin
 */
public class UnitTest {

    @Test
    public void testMachine() {
        UpperCaseAlphabet alphabet = new UpperCaseAlphabet();
        Permutation pRotorI = new Permutation("(AELTPHQXRU) "
                +
                "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", alphabet);
        Permutation pRotorII = new Permutation("(FIXVYOMW) "
                +
                "(CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)", alphabet);
        Permutation pRotorIII = new Permutation("(A"
                +
                "BDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", alphabet);
        Permutation pRotorIV = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH)"
                +
                " (DV) (KU)", alphabet);
        Permutation pRotorV = new Permutation("(AVOLDRWFIUQ)"
                +
                " (BZKSMNHYC) (EGTJPX)", alphabet);
        Permutation pRotorVI = new Permutation("(AJQDVLEOZWIYTS)"
                +
                " (CGMNHFUX) (BPRK)", alphabet);
        Permutation pRotorVII = new Permutation(""
                + "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)", alphabet);
        Permutation pRotorVIII = new Permutation("(AFLSETWUNDHOZVICQ) "
                + "(BKJ) (GXY) (MPR)", alphabet);
        Permutation pRotorBeta = new Permutation("(ALB"
                + "EVFCYODJWUGNMQTZSKPR) (HIX)", alphabet);
        Permutation pRotorGamma = new Permutation("(AFNIRL"
                + "BSQWVXGUZDKMTPCOYJHE)", alphabet);
        Permutation pReflectorB = new Permutation("(AE) (BN) (CK) (DQ)"
                + " (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", alphabet);
        Permutation pReflectorC = new Permutation("(AR) (BD) (CO) (EJ)"
                + " (FN) (GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY)", alphabet);
        MovingRotor rotorI = new MovingRotor("rotorI", pRotorI, "Q");
        MovingRotor rotorII = new MovingRotor("rotorII", pRotorII, "E");
        MovingRotor rotorIII = new MovingRotor("rotorIII", pRotorIII, "V");
        MovingRotor rotorIV = new MovingRotor("rotorIV", pRotorIV, "J");
        MovingRotor rotorV = new MovingRotor("rotorV", pRotorV, "Z");
        MovingRotor rotorVI = new MovingRotor("rotorVI", pRotorVI, "ZM");
        MovingRotor rotorVII = new MovingRotor("rotorVII", pRotorVII, "ZM");
        MovingRotor rotorVIII = new MovingRotor("rotorVIII", pRotorVIII, "ZM");
        FixedRotor rotorBeta = new FixedRotor("rotorBeta", pRotorBeta);
        FixedRotor rotorGamma = new FixedRotor("rotorGamma", pRotorGamma);
        Reflector reflectorB = new Reflector("reflectorB", pReflectorB);
        Reflector reflectorC = new Reflector("reflectorC", pReflectorC);
        ArrayList<Rotor> allRotors = new ArrayList<>();
        allRotors.add(rotorI); allRotors.add(rotorII); allRotors.add(rotorIII);
        allRotors.add(rotorIV); allRotors.add(rotorV); allRotors.add(rotorVI);
        allRotors.add(rotorVII); allRotors.add(rotorVIII);
        allRotors.add(rotorBeta);
        allRotors.add(rotorGamma); allRotors.add(reflectorB);
        allRotors.add(reflectorC);
        Machine machine = new Machine(alphabet, 5, 3, allRotors);
        String[] rotors = new String[]
        {"reflectorB", "rotorBeta", "rotorIII", "rotorIV", "rotorI"};
        machine.insertRotors(rotors);
        machine.setRotors("AXLE");
        Permutation plugboard = new Permutation("(YF) (ZH)", alphabet);
        machine.setPlugboard(plugboard);
        assertEquals(25, machine.convert(24));
    }

    @Test
    public void testPermutationSize() {
        Permutation perm = new Permutation("(AB)", UPPER);
        assertEquals(perm.size(), UPPER.size());
    }

    @Test
    public void tesPermutation() {
        Permutation perm = new Permutation("(AB)", UPPER);
        assertEquals('B', perm.permute('A'));
    }
    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

}


