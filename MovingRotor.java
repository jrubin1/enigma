package enigma;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Josh Rubin
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        for (int i = 0; i < notches.length(); i++) {
            _notches.add(notches.charAt(i));
        }
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        char c = alphabet().toChar(setting());
        for (Character notch : _notches) {
            if (notch.equals(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set((setting() + 1) % size());
    }

    /** ArrayList of Notches. */
    private ArrayList<Character> _notches = new ArrayList<Character>();

}
