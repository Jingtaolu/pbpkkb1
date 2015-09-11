package core;

import java.io.IOException;

import chemaxon.struc.*;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolFormatException;

/**
 * Example class for structure manipulation. 
 * Creates a simple reaction.
 *
 * @author Andras Volford
 * 
 */
public class RxnMoleculeExample {

    public static void main(String[] args) throws IOException {

        // create an empty Molecule
        RxnMolecule m = new RxnMolecule();

        try{
            Molecule reactant = MolImporter.importMol("C=C(C)C");
            Molecule agent = MolImporter.importMol("CCOCC");
            Molecule product = MolImporter.importMol("CC([Cl])(C)C");

            m.addComponent(reactant, RxnMolecule.REACTANTS);
            m.addComponent(agent, RxnMolecule.AGENTS);
            m.addComponent(product, RxnMolecule.PRODUCTS);
            m.addComponent(MolImporter.importMol("Cl"), RxnMolecule.REACTANTS);
            System.out.println(MolExporter.exportToFormat(m, "mrv:P"));

        } catch (MolFormatException e) {
            System.err.println("Format not recognised.");
        }
    }
}

