package core;

import java.io.IOException;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;

import chemaxon.struc.Molecule;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;

import chemaxon.util.iterator.IteratorFactory;
import chemaxon.util.iterator.IteratorFactory.AtomIterator;
import chemaxon.util.iterator.IteratorFactory.BondIterator;

/**
 * Example class to demonstrate how to access atoms and bonds of the molecule using 
 * Iterators.
 *
 * @author Andras Volford, Miklos Vargyas
 *
 */ 
public class MoleculeIterators {

    public static void main(String[] args) {

        String filename = args[0];
        
        try {
            // create a molecule importer for the given file
            MolImporter mi = new MolImporter(filename);
	    
            // read the first molecule from the file
            Molecule m = mi.read();
            
            while (m != null) {
                IteratorFactory itFac = new IteratorFactory(m, 
                        IteratorFactory.INCLUDE_CHEMICAL_ATOMS_ONLY,
                        IteratorFactory.REPLACE_COORDINATE_BONDS );
                printAtoms(itFac,m);
                printBonds(itFac,m);
                
                // read the next molecule from the input file
                m = mi.read();
            }
            mi.close();
	    }
        catch (MolFormatException e) {
            System.err.println("Molecule format not recognised.");
        }
        catch (IOException e) {
            System.err.println("I/O error:" + e);
        }
    }
        
	private static void printAtoms( IteratorFactory itFac, Molecule m ) {
        AtomIterator ai = itFac.createAtomIterator();
        
	    System.out.println("Atoms in the molecule\natomic number\tcharge");
	    while (ai.hasNext()) {
            MolAtom a = ai.next();
            System.out.println( a.getAtno() + "\t\t" + a.getCharge() );
	    }
    }
        
	private static void printBonds( IteratorFactory itFac, Molecule m ) {
        BondIterator bi = itFac.createBondIterator();

	    System.out.println("Bonds in the molecule\nbond order\tcoodinate");

	    while (bi.hasNext()) {
            MolBond b = bi.next();
            System.out.println( b.getType() + "\t\t" + b.isCoordinate() 
                    + " " + m.indexOf(b.getAtom1()) + "-" + m.indexOf(b.getAtom2()));
	    }	   
    }
    
}

