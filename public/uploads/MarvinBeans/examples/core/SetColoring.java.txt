/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */
 
package core;
import java.awt.Color;

import chemaxon.struc.*;
import chemaxon.struc.graphics.MFont;
import chemaxon.formats.*;

/**
 * 
 * The following code example shows how to change the format of some atoms and bonds
 * in a 11 atom long chain. A new color and font setting for a set of atoms, and
 * a new color and thickness setting for a set of bonds will be 
 * created.
 *
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/help/developer/sets.html
 *
 *
 * @author  Erika Biro
 * @version 5.0 04/21/2008
 * @since   Marvin 5.0
 */
public class SetColoring {

    static public void main(String[] args) throws Exception {
	//import a simple chain
	Molecule mol = MolImporter.importMol("CCNCCCCCNCC");
	//create a document to register color, font, size in the lookup table
	MDocument mdoc = new MDocument(mol);
	//define two colors
        Color green = new Color(0, 255, 0);
        Color blue = new Color(0, 0, 255);
        //define a new font
        MFont font = new MFont("Helvetica", MFont.BOLD, 20 );
        //specify a new atom-style by setting color and font of the 1. atom set
        //set the coloring mode
        mdoc.setAtomSetColorMode(1, MDocument.SETCOLOR_SPECIFIED);
        //set the color of the 1. atom-set
        mdoc.setAtomSetRGB(1, green.getRGB());
        //set the font of the 1. atom-set
        mdoc.setAtomSetFont(1, font);
        //set the color and font of the 2. and 8. atoms in the molecule
        //by adding these atoms to the 1. atom set.
        mol.getAtom(2).setSetSeq(1);
        mol.getAtom(8).setSetSeq(1);
        //specify a new bond-style by setting color and thickness of the 2. atom set
        //set the coloring mode
        mdoc.setBondSetColorMode(2, MDocument.SETCOLOR_SPECIFIED);
        //set the color of the 2. bond-set
        mdoc.setBondSetRGB(2, blue.getRGB());
        //set the thickness of the 2. bond-set
        mdoc.setBondSetThickness(2, 0.2);
        //set the color and thickness of 4. bond in the molecule
        //by adding this bond to the 2. atom set.
        mol.getBond(4).setSetSeq(2);
		System.out.println(MolExporter.exportToFormat(mol, "mrv"));
    } 
}
