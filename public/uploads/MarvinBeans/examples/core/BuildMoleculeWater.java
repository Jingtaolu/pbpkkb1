package core;

import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.struc.*;

/**
 * Example class for structure manipulation. Creates water.
 *
 * @author Andras Volford
 * 
 */
public class BuildMoleculeWater {

    public static void main(String[] args) throws IOException {

        // create an empty Molecule
        Molecule m = new Molecule();

        // create the Carbon atom
        MolAtom a1 = new MolAtom(6);
        // and add it to the molecule 
        m.add(a1);

        // create the Hydrogen atom
        MolAtom a2 = new MolAtom(1);
        // and add it to the molecule
        m.add(a2);

        // create the Hydrogen atom
        MolAtom a3 = new MolAtom(1);
        // and add it to the molecule
        m.add(a3);
        
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        // this prints [H+].[H+].C as no bond has been defined yet 

        // create a bond between atoms, bond order 
        MolBond b1 = new MolBond(a1, a2, 1);
        m.add(b1);
        MolBond b2 = new MolBond(a1, a3, 1);
        m.add(b2);
        
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        // this prints water 
    }
}

