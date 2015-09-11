package core;

import java.io.IOException;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;

import chemaxon.struc.Molecule;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;


/**
 * Example class to demonstrate how to access atoms and bonds of the molecule. 
 *
 * @author Andras Volford, Miklos Vargyas
 * 
 */ 
public class MoleculeAtoms {

    public static void main(String[] args) {

        String filename = args[0];
        
        try {
            // create a molecule importer for the given file
            MolImporter mi = new MolImporter(filename);
	    
            // read the first molecule from the file
            Molecule m = mi.read();
            
            while (m != null) {
                printAtoms(m);
                printBonds(m);
                
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
        
	private static void printAtoms( Molecule m ) {
        m.calcHybridization();
	    System.out.println("Atoms in the molecule\natomic number\tcharge\thybridisation");
	    for (int i = 0; i < m.getAtomCount(); i++) {
            MolAtom a = m.getAtom(i);
            System.out.println( i + "th atom: " + a.getAtno() + "\t\t" + a.getCharge() + 
                "\t" + a.getHybridizationState());
	    }
    }
    
    
	private static void printBonds( Molecule m ) {
	    System.out.println("Bonds in the molecule\nbond order\tcoodinate");
	    for (int i = 0; i < m.getBondCount(); i++) {
            MolBond b = m.getBond(i);
            System.out.println( b.getType() + "\t\t" + b.isCoordinate() + "  " 
                + m.indexOf( b.getAtom1() ) + "-" + m.indexOf( b.getAtom2() )  );
	    }	   
    }
    
}

