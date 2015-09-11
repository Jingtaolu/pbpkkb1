package core;
/**
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;
import chemaxon.struc.RgMolecule;

/**
 * Example of R-group structure: 
 * how to access R-group definitions and the root structure.
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/help/developer/core/rgroups.html
 * 
 *
 * @author  Erika Biro
 * @version 5.0.3 04/20/2008
 * @since   Marvin 5.0
 */
public class RgroupExample  {
    
    public static void main(String[] args) throws MolFormatException, IOException {
	//import an R-group structure
	RgMolecule rgmol = (RgMolecule) MolImporter.importMol("$MDL  REV  1 0417081732\n$MOL\n$HDR\n\n  Marvin  04170817322D          \n\n"+
		"$END HDR\n$CTAB\n  9  9  0  0  0  0            999 V2000\n"+
		"   -1.6250    0.8875    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -2.3395    0.4750    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -2.3395   -0.3500    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -1.6250   -0.7625    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -0.9105   -0.3500    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -0.9105    0.4750    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -0.1961    0.8875    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -0.1961   -0.7625    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"   -3.0540   -0.7625    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"  1  2  1  0  0  0  0\n  1  6  2  0  0  0  0\n  2  3  2  0  0  0  0\n"+
		"  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\n  5  6  1  0  0  0  0\n"+
		"  6  7  1  0  0  0  0\n  5  8  1  0  0  0  0\n  3  9  1  0  0  0  0\n"+
		"M  LOG  1   1   0   0   >0\nM  LOG  1   2   0   0   >0\n"+
		"M  LOG  1   3   0   0   >0\nM  RGP  3   7   1   8   2   9   3\nM  END\n"+
		"$END CTAB\n$RGP\n  1\n$CTAB\n  1  0  0  0  0  0            999 V2000\n"+
		"    4.8422    2.0579    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"M  APO  1   1   1\nM  END\n$END CTAB\n$CTAB\n"+
		"  2  1  0  0  0  0            999 V2000\n"+
		"    7.1249    2.0752    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"    7.9499    2.0752    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"  1  2  1  0  0  0  0\nM  APO  1   1   1\nM  END\n$END CTAB\n$END RGP\n"+
		"$RGP\n  2\n$CTAB\n  1  0  0  0  0  0            999 V2000\n"+
		"    5.1016    0.1556    0.0000 A   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"A    1\n'alkyl'\nM  APO  1   1   1\nM  END\n$END CTAB\n$END RGP\n$RGP\n  3\n"+
		"$CTAB\n  1  0  0  0  0  0            999 V2000\n"+
		"    5.1708   -1.4527    0.0000 A   0  0  0  0  0  0  0  0  0  0  0  0\n"+
		"A    1\n'amino'\nM  APO  1   1   1\nM  END\n$END CTAB\n$END RGP\n$END MOL\n"
	);
	
	//get the root structure and enumerate the atoms in the root and count 
	//the R-atoms in the root structure.
        Molecule root = rgmol.getRoot();
        int count = 0;
        for (int i = root.getAtomCount() - 1; i >= 0; --i){
            MolAtom atom = root.getAtom(i);
            if (atom.getAtno() == MolAtom.RGROUP){
                System.out.print("atom symbol: " + atom.getSymbol() + ", ");
                System.out.println("atom alias: " + atom.getAliasstr());
                count++;
            }
        }
        System.out.println("R-atom count of the scaffold: " + count);
        //enumerate the R-group definitions and its fragments
        //print the smiles format of the fragments in each definition
        int nr = rgmol.getRgroupCount();
        for(int i = 0; i < nr; ++i) {
            int nrm = rgmol.getRgroupMemberCount(i);
            System.out.println("fragments in definition R" + rgmol.getRgroupId(i) + ":");
	    for (int j = 0; j < nrm; ++j) {
		System.out.println("      "
			+ MolExporter.exportToFormat(
				rgmol.getRgroupMember(i, j), "smiles"));
	    }
        }
    }
}
