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


import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
/**
 * Exporter class for "myformat".
 *
 * @version 5.2.3, 05/28/2009
 * @since   Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */
public class MyFormatExport extends chemaxon.marvin.io.MolExportModule {

    public Object convert(Molecule mol) {
	StringBuffer s = stringBuffer;
	s.setLength(0);
	s.append("MyFormat " + mol.getName());
	s.append('\n');
	// atoms
	s.append(String.valueOf(mol.getAtomCount()));
	s.append('\n');
	for(int i = 0; i < mol.getAtomCount(); ++i) {
	    MolAtom a = mol.getAtom(i);
	    s.append(a.getSymbol());
	    s.append('\t');
	    s.append(String.valueOf(a.getX()));
	    s.append('\t');
	    s.append(String.valueOf(a.getY()));
	    s.append('\n');
	}
	// bonds
	s.append(String.valueOf(mol.getBondCount()));
	s.append('\n');
	for(int i = 0; i < mol.getBondCount(); ++i) {
	    MolBond b = mol.getBond(i);
	    s.append(String.valueOf(mol.indexOf(b.getAtom1()) + 1));
	    s.append('\t');
	    s.append(String.valueOf(mol.indexOf(b.getAtom2()) + 1));
	    s.append('\t');
	    s.append(String.valueOf(b.getType()));
	    s.append('\n');
	}
	return s.toString();
    }
}
