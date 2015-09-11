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
public class AromaticNitration {

    public static void main(String[] args) throws IOException {

        // create an empty Molecule
        RxnMolecule m = new RxnMolecule();

        try{
            Molecule reactant = MolImporter.importMol("c1ccccc1");
            Molecule agent = MolImporter.importMol("N(O)(=O)=O.S(O)(O)(=O)=O");
            Molecule product = MolImporter.importMol("c1ccccc1N(=O)=O");

            m.addComponent(reactant, RxnMolecule.REACTANTS);
            m.addComponent(agent, RxnMolecule.AGENTS);
            m.addComponent(product, RxnMolecule.PRODUCTS);
            m.addComponent(MolImporter.importMol("O"), RxnMolecule.PRODUCTS);
            System.out.println(MolExporter.exportToFormat(m, "mrv:P"));

        } catch (MolFormatException e) {
            System.err.println("Format not recognised.");
        }
    }
}

