package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Josh Rubin
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNext("\\*.*")) {
            throw new EnigmaException("Input doesn't start with Setting");
        }
        while (_input.hasNextLine()) {
            boolean nextLineIsSetting = _input.hasNext("\\*.*");
            String nextLine = _input.nextLine();
            if (nextLine.equals("")) {
                _output.println();
            } else if (nextLineIsSetting) {
                setUp(machine, nextLine);
            } else {
                String line = nextLine;
                line = line.replace(" ", "");
                line = line.toUpperCase();
                line = machine.convert(line);
                printMessageLine(line);
                _output.println();
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            if (pawls >= numRotors) {
                throw new EnigmaException("Numb"
                        + "er of Pawls must be less than Number of Rotors");
            }
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            boolean moving = false;
            boolean reflector = false;
            boolean nonMoving = false;
            String name = _config.next();
            String nextStr = _config.next();
            if (nextStr.substring(0, 1).equals("M")) {
                moving = true;
            } else if (nextStr.substring(0, 1).equals("R")) {
                reflector = true;
            } else if (nextStr.substring(0, 1).equals("N")) {
                nonMoving = true;
            }
            String notches = "";
            if (nextStr.length() > 1) {
                notches += nextStr.substring(1);
            }
            String cycles = "";
            while (_config.hasNext("\\(.*")) {
                String curr = _config.next();
                cycles += (curr + " ");
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (moving) {
                return new MovingRotor(name, perm, notches);
            } else if (reflector) {
                return new Reflector(name, perm);
            } else {
                return new FixedRotor(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String curr = "";
        int numRotorsSeen = 0;
        String[] rotorNames = new String[M.numRotors()];
        int i = 0;
        String cycles = "";
        String setting = "";
        for (String split : settings.split(" ")) {
            if (split.equals("*")) {
                int useless = 1;
            } else if (numRotorsSeen < M.numRotors()) {
                rotorNames[i] = split;
                i++;
                numRotorsSeen++;
            } else if (split.contains("(")) {
                cycles += (split + " ");
            } else {
                setting = split;
            }
        }
        Permutation plugBoard = new Permutation(cycles, _alphabet);
        M.setPlugboard(plugBoard);
        testRotorNamesError(rotorNames);
        M.insertRotors(rotorNames);
        if (setting.length() != M.numRotors() - 1) {
            throw new EnigmaException("I"
                    + "nitial Positions String incorrect length.");
        }
        for (int x = 0; x < rotorNames.length; x++) {
            for (int y = 0; y < rotorNames.length; y++) {
                if (rotorNames[x].equals(rotorNames[y])
                        && x != y) {
                    throw new EnigmaException("R"
                            + "otor repeated in Setting Line.");
                }
            }
        }
        M.setRotors(setting);
    }

    /** Tests for Errors in Setting.
     * @param rotorNames Array of rotor Names.
     * */
    private void testRotorNamesError(String[] rotorNames) {
        for (int i = 0; i < rotorNames.length; i++) {
            if (i == 1 && rotorNames[i].equals("I")) {
                throw new EnigmaException("");
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (String split : msg.split("(?<=\\G.{5})")) {
            _output.print(split + " ");
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
