package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Josh Rubin
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        alphabet = chars;
    }

    /** Returns the size of the alphabet. */
    int size() {
        return alphabet.length();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return alphabet.contains(Character.toString(c));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alphabet.charAt(index);
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        return alphabet.indexOf(c);
    }

    /** Variable to keep track of alphabet. */
    private String alphabet;
}
