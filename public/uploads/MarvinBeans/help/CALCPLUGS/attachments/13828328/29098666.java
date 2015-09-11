/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
 */

import java.io.IOException;

import chemaxon.marvin.calculations.ElementalAnalyserPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

/**
 * This example shows how to work with Calculator Plugins via Java API.
 * 
 * <p>Usage:
 * <pre>   java ElementalAnalyserPluginExample [molFile]</pre>
 * 
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/examples/plugin/index.html
 *
 * @version 5.0.4 04/22/2008
 * @since   Marvin 5.0.4
 * @author  Zsolt Mohacsi
 */
public class ElementalAnalyserPluginExample {

    public static void main(String[] args) {
	try {
	    // instantiate the plugin object
	    ElementalAnalyserPlugin plugin = new ElementalAnalyserPlugin();

	    // set the parameters for the calculation (plugin specific))
	    plugin.setDoublePrecision(2);

	    // read the input molecules and perform the calculations
	    MolImporter importer = new MolImporter(args[0]);
	    Molecule mol;
	    while ((mol = importer.read()) != null) {

	        // set the input molecule
	        plugin.setMolecule(mol);

	        // run the calculation
	        plugin.run();

	        // get the results (plugin specific)
	        // mass and exact mass
	        double mass = plugin.getMass();
	        double exactMass = plugin.getExactMass();
	        // the number of all atoms in the molecule
	        int atomCount = plugin.getAllAtomCount();
	        // carbon atom count
	        int countOfC = plugin.getAtomCount(6); 
	        // carbon-14 isotope count
	        int countOfC14 = plugin.getAtomCount(6, 14);
	        // formula
	        String formula = plugin.getFormula();
	        // composition
	        String composition = plugin.getComposition(); 

	        // display the results
	        System.out.println(mol.toFormat("smiles") + "\n  formula: "
	    	    + formula + ", mass: " + mass + ", exact mass: "
	    	    + exactMass + "\n  number of atoms (" + atomCount
	    	    + "): C (" + countOfC + "), C-14 (" + countOfC14 + ")"
	    	    + "\n  composition: " + composition + "\n");
	    }
	    importer.close();
	} catch (IOException e) {
	    System.err.println("I/O error has occurred.");
	    e.printStackTrace();
	} catch (PluginException e) {
	    System.err.println("Plugin processing or calculation error.");
	    e.printStackTrace();
	}
    }
}
