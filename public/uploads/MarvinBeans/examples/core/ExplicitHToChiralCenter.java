/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

package core;

import java.io.IOException;

import chemaxon.struc.Molecule;
import chemaxon.struc.MolAtom;
import chemaxon.calculations.clean.Cleaner;
import chemaxon.calculations.hydrogenize.Hydrogenize;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.StereoConstants;

public class ExplicitHToChiralCenter {

    /**
     * Example to add Explicit H to Chiral centers only.
     * @param args   command line arguments
     * @throws java.io.IOException 
     * 
     * @version 5.1 04/24/2008
     * @since Marvin 5.1
     * @author Andras Volford
     */

    // ODD and EVEN parity values
    static int ODD = StereoConstants.PARITY_ODD;
    static int EVEN = StereoConstants.PARITY_EVEN;

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java ExplicitHToChiralCenter filename");
            System.exit(0);
        }

        // create importer for the file argument
        String s = (String) args[0];
        MolImporter molimp = new MolImporter(s);

        // store the imported molecules in m
        Molecule m = new Molecule();

        while (molimp.read(m)) {  // read molecules from the file
            int ac = m.getAtomCount();

            // Atoms with odd or even parity
            MolAtom[] t = new MolAtom[ac];
            int n = 0;
            for (int i = 0; i < ac; i++){
                int p = m.getParity(i);
                boolean add = p == ODD || p == EVEN;

                // if the atom has ODD or EVEN parity
                if (add) {
                    t[n++] = m.getAtom(i);
                }
            }

            // reduce atom array
            MolAtom[] a = new MolAtom[n];
            System.arraycopy(t, 0, a, 0, n);

            // add explicit H
            Hydrogenize.convertImplicitHToExplicit(m, a, 0);
            
            if (m.getDim() != 2)
                Cleaner.clean(m, 2, null);
            
            // write the result
            System.out.print(MolExporter.exportToFormat(m, "sdf")); 
        }
    }
}
