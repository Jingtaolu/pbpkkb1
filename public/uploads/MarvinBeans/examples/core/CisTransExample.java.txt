/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

package core;
import java.io.IOException;

import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;
import chemaxon.struc.MolBond;
import chemaxon.struc.StereoConstants;
import chemaxon.formats.MolImporter;

/**
 * Example to get double bond stereo information of double bonds.
 * <p>Usage:
 * <pre> java CisTransExample filename</pre>
 *
 * @version 5.1 04/24/2008
 * @since Marvin 5.1
 * @author Andras Volford
 */
public class CisTransExample {

    /**
     * Main method.
     * @param args   command line arguments (filename)
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.err.println("Usage: java CisTransTest filename");
            System.exit(0);
        }

        // create importer for the file argument
        String s = (String)args[0];
        MolImporter molimp = new MolImporter(s);

        // store the imported molecules in m
        Molecule m = new Molecule();

        // counter for molecules
        int n = 0;

        while(molimp.read(m)){  // read molecules from the file
            ++n;                // increment counter
            System.err.println("mol "+n);

            // calculate double bond stereo for every double bond
            // which have at least one ligand on each node of the double bond
            for (int i=0; i<m.getBondCount(); i++){
                MolBond b = m.getBond(i);
                if (b.getType() == 2){

                    // get the default frame
                    MolAtom c1 = b.getCTAtom1();
                    MolAtom c2 = b.getAtom1();
                    MolAtom c3 = b.getAtom2();
                    MolAtom c4 = b.getCTAtom4();

                    if (c1 != null && c4 != null){

                        // cis/trans stereo for the default frame
                        int ct = m.getStereo2(b, c1, c4, true);
                        
                        System.out.println(m.indexOf(c1)+"-"+
                        m.indexOf(c2)+"="+m.indexOf(c3)+"-"+
                        m.indexOf(c4)+"  "+
                            ((ct == MolBond.CIS) ? "CIS" : 
                            (ct == MolBond.TRANS) ? "TRANS" :
                            (ct == (MolBond.TRANS|MolBond.CIS)) ? 
				         "CIS|TRANS"
                            : (""+ct) )+
                        (((ct & StereoConstants.CTUNSPEC) != 0) ?
	 	  "CTUNSPEC" : " ")
                        );

                        // E/Z stereo
                        ct = m.getStereo2(b);

                        System.out.println("E/Z "+
                            m.indexOf(c2)+"="+m.indexOf(c3)+"   "+
                            ((ct == MolBond.CIS) ? "Z" : 
                            (ct == MolBond.TRANS) ? "E" : ""+ct )+
                        (((ct & StereoConstants.CTUNSPEC) != 0) ? 
		   "CTUNSPEC" : "")
                        );
                    }
                }
            }
        }
    }
}
