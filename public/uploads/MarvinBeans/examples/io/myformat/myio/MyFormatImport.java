/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */


package myio;

import chemaxon.struc.*;
import chemaxon.formats.MolInputStream;
import chemaxon.formats.MolFormatException;
import java.io.IOException;
import java.util.StringTokenizer;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolInputStream;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
/**
 * Importer class for "myformat".
 *
 * @version 5.2.3, 05/28/2009
 * @since   Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */

public class MyFormatImport extends chemaxon.marvin.io.MolImportModule {

    private MolInputStream istream;

    /** Initialization. */
    public void initMolImport(MolInputStream mis) {
	istream = mis;
    }

    /** Reads a molecule from the file. */
    public boolean readMol(Molecule mol) throws IOException {
	// initialize molecule object
	mol.clearForImport("myformat");
	mol.setStartPosition(istream.getFilePointer());

	// read the molecule
	String line = istream.readLine();
	if(line == null) // end of file
	    return false;
	mol.setName(line.substring(9).trim()); // set molecule name
	mol.setDim(2);            // set number of dimensions
	try {
	    line = istream.readLine();
	    int numAtoms = Integer.parseInt(line.trim());
	    for(int i = 0; i < numAtoms; ++i) {
		line = istream.readLine();
		StringTokenizer st = new StringTokenizer(line);
		int atno = MolAtom.numOf(st.nextToken());
		double x = Double.valueOf(st.nextToken()).doubleValue();
		double y = Double.valueOf(st.nextToken()).doubleValue();
		MolAtom a = mol.reuseAtom(atno, i);
		a.setXY(x, y);
	    }
	    mol.endReuse(numAtoms);
	    line = istream.readLine();
	    int numBonds = Integer.parseInt(line.trim());
	    for(int i = 0; i < numBonds; ++i) {
		line = istream.readLine();
		StringTokenizer st = new StringTokenizer(line);
		int atomIndex1 = Integer.parseInt(st.nextToken()) - 1;
		int atomIndex2 = Integer.parseInt(st.nextToken()) - 1;
		int order = Integer.parseInt(st.nextToken());
		MolBond b = new MolBond(mol.getAtom(atomIndex1),
					mol.getAtom(atomIndex2), order);
		mol.add(b);
	    }
	    mol.valenceCheck(); // to set the implicit hydrogens
	} catch(NumberFormatException ex) {
	    throw new MolFormatException("error in molecule file");
	}

	// memorize the last file position corresponding to this molecule
	mol.setEndPosition(istream.getFilePointer());
	return true;
    }

    /**
     * Creates a new target molecule object.
     * @return new {@link Molecule}
     */
    public Molecule createMol() {
	return new Molecule();
    }
}
