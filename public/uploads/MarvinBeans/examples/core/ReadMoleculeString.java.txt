package core;

import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;

/**
 * Simple example of building a molecule from a SMILES string.
 *
 * @author Andras Volford, Miklos Vargyas
 * 
 */
public class ReadMoleculeString {

    public static void main(String[] args) throws IOException {
	
        try {
            String smiles = "CC>>CC";

            Molecule m = MolImporter.importMol(smiles);

            System.out.println(m.getAtomCount());
            System.out.println(MolExporter.exportToFormat(m, "mol"));
	    
        } 
        catch (MolFormatException e) {
            System.err.println("Format not recognised.");
        }
    }
}

