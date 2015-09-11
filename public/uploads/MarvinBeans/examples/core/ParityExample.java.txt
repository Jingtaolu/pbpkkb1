/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */
package core;

import java.io.IOException;

import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.struc.StereoConstants;

public class ParityExample {

    /**
     * Example to get the parity and chirality of the atoms.
     * @param args   command line arguments
     * @throws java.io.IOException 
     * 
     * @version 5.1 04/24/2008
     * @since Marvin 5.1
     * @author Andras Volford
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java ParityExample filename");
            System.exit(0);
        }

        // create importer for the file argument
        String s = (String) args[0];
        MolImporter molimp = new MolImporter(s);

        // store the imported molecules in m
        Molecule m = new Molecule();

        // counter for molecules
        int n = 0;

        while (molimp.read(m)) {  // read molecules from the file
            ++n;                // increment counter
            System.out.println("mol " + n);

            // print parity information followed by the chirality
            for (int i = 0; i < m.getAtomCount(); i++) {
                int c = m.getChirality(i);
                System.out.println(
                    i + " Parity " + m.getParity(i) +
                    " Chirality " +
                    ((c == StereoConstants.CHIRALITY_R) ? "R" : 
                        (c == StereoConstants.CHIRALITY_S) ? "S" : 
                            ("" + c)) + " " + c);
            }
        }
    }
}
