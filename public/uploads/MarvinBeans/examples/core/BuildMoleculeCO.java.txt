package core;

import java.io.IOException;

import chemaxon.formats.MolExporter;
import chemaxon.struc.*;

/**
 * Example class for structure manipulation. Creates CO
 *
 * @author Andras Volford, Miklos Vargyas
 * 
 */
public class BuildMoleculeCO {

    public static void main(String[] args) throws IOException {

        // create an empty Molecule
        Molecule m = new Molecule();

        // create the Carbon atom
        MolAtom a1 = new MolAtom(6);
        // and add it to the molecule 
        m.add(a1);

        // create the Oxygen atom
        MolAtom a2 = new MolAtom(8);
        // and add it to the molecule
        m.add(a2);
        
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        // this prints C.O as no bond has been defined yet 

        // create a bond between atoms, bond order 
        MolBond b = new MolBond(a1, a2, 2);
        m.add(b);
        
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        // this prints C=O 
    }
}

