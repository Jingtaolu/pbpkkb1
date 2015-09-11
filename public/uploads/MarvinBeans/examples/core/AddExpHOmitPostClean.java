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
import chemaxon.calculations.clean.Cleaner;
import chemaxon.calculations.hydrogenize.Hydrogenize;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import chemaxon.struc.MoleculeGraph;

/**
 * 
 * The following code example shows how to convert implicit Hydrogens to 
 * explicit ones without additional cleaning.
 *
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/help/developer/hydrogens.html
 *
 *
 * @author  Andras Volford
 * @version 5.1 04/30/2008
 * @since   Marvin 5.1
 */
public class AddExpHOmitPostClean {

    public static void main(String args[]) throws Exception {
        
        String filename = args[0];
        MolImporter mi = new MolImporter(filename);
	Molecule m = new Molecule();

        while ((m = mi.read()) != null){

            // clean the molecule to 2d if necessary
            if (m.getDim() != 2){
                Cleaner.clean(m, 2, null);
            }

            // add explicit Hydrogens
            Hydrogenize.convertImplicitHToExplicit(m, null, MoleculeGraph.OMIT_POSTCLEAN);
            
            // output to std out
            System.out.println(MolExporter.exportToFormat(m, "sdf"));
        }
    }
}
