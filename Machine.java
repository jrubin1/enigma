package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Josh Rubin
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            ArrayList<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotors = new Rotor[numRotors];

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor rotor : _allRotors) {
                if (rotors[i].equalsIgnoreCase(rotor.name())) {
                    _rotors[i] = rotor;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotors[i + 1].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] rotorMoves = new boolean[_rotors.length];
        int movingRotors = numPawls();
        boolean[] rotorsThatMove = new boolean[_rotors.length];
        for (int i = _rotors.length - 1; i > 0; i--) {
            if (movingRotors > 0) {
                rotorsThatMove[i] = true;
                movingRotors--;
            }
        }
        boolean rotorToMyRightMovedByRule1 = false;
        for (int i = _rotors.length - 1; i > 0; i--) {
            if (_rotors[i].atNotch() && rotorsThatMove[i - 1]) {
                rotorMoves[i] = true;
                rotorToMyRightMovedByRule1 = true;
            } else if (rotorToMyRightMovedByRule1) {
                rotorMoves[i] = true;
                rotorToMyRightMovedByRule1 = false;
            }
        }
        rotorMoves[numRotors() - 1] = true;
        for (int i = _rotors.length - numPawls(); i < _rotors.length; i++) {
            if (rotorMoves[i]) {
                _rotors[i].advance();
            }
        }
        c = _plugboard.permute(c);
        for (int i = _rotors.length - 1; i >= 0; i--) {
            c = _rotors[i].convertForward(c);
        }
        for (int i = 1; i < _rotors.length; i++) {
            c = _rotors[i].convertBackward(c);
        }
        c = _plugboard.permute(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] arr = msg.toCharArray();
        int curr;
        for (int i = 0; i < msg.length(); i++) {
            curr = _alphabet.toInt(arr[i]);
            curr = convert(curr);
            arr[i] = _alphabet.toChar(curr);
        }
        String val = "";
        for (int i = 0; i < arr.length; i++) {
            val = val + arr[i];
        }
        return val;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of Rotors. */
    private int _numRotors;

    /** Number of Pawls. */
    private int _pawls;

    /** ArrayList of All Rotors. */
    private ArrayList<Rotor> _allRotors;

    /** Array of Rotor slots. */
    private Rotor[] _rotors;

    /** Plugboard of Enigma Machine. */
    private Permutation _plugboard;
}
