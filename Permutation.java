package enigma;

import static enigma.EnigmaException.*;
import java.util.HashMap;
import java.util.Map;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Josh Rubin
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters not
     *  included in any cycle map to themselves. Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        int numOpenParen = 0;
        int numClosedParen = 0;
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                numOpenParen++;
            } else if (cycles.charAt(i) == ')') {
                numClosedParen++;
            }
        }
        if (numOpenParen != numClosedParen) {
            throw new EnigmaException("Cycles format Error");
        }
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                String cycle = cycles.substring(i + 1, cycles.indexOf(')', i));
                addCycle(cycle);
            }
        }
        for (int i = 0; i < alphabet.size(); i++) {
            char letter = alphabet.toChar(i);
            if (!_cycles.containsKey(letter)) {
                _cycles.put(letter, letter);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char start = cycle.charAt(0);
        for (int i = 0; i < cycle.length(); i++) {

            char thisChar = cycle.charAt(i);
            if (cycle.length() == 1) {
                _cycles.put(thisChar, thisChar);
            } else {
                char nextChar;
                if (i == cycle.length() - 1) {
                    nextChar = start;
                } else {
                    nextChar = cycle.charAt(i + 1);
                }
                _cycles.put(thisChar, nextChar);
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        Object letter = _cycles.get(alphabet().toChar(p % this.size()));
        return alphabet().toInt((char) letter);
    }

    /** Return key associated with value if in the HashMap,
     * otherwise return null. Source: StackOverflow Rupesh Yadav
     * @param hm This is the input map.
     * @param value This is the input value from the map. */
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        Object letter = getKeyFromValue(_cycles,
                alphabet().toChar(c % this.size()));
        return alphabet().toInt((char) letter);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return (char) _cycles.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    int invert(char c) {
        return (char) getKeyFromValue(_cycles, c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < alphabet().size(); i++) {
            char letter = alphabet().toChar(i);
            if ((char) _cycles.get(letter) == letter) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Hashmap of cycles. */
    private HashMap<Character, Character>
            _cycles = new HashMap<Character, Character>();

}

