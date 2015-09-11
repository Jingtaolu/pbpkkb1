package core;

import java.io.IOException;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.io.MPropHandler;
import chemaxon.struc.Molecule;

/**
 * Example class for molecule import.
 *
 * @author Andras Volford, Miklos Vargyas
 * 
 */
public class ReadMoleculeFile {

    public static void main(String[] args) {
	
        String filename = args[0];
        
        try {
            // create a molecule importer for the given file
            MolImporter mi = new MolImporter(filename);
	    
            // read the first molecule from the file
            Molecule m = mi.read();
            
            while (m != null) {
                
                printProperties( m );                
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
    
    private static void printProperties( Molecule m ) {
        System.out.println( "\nMolecule " + m.getName() );
        int nProps = m.getPropertyCount();
        for ( int i = 0; i < nProps; i++ ) {
            String propKey = m.getPropertyKey( i );
            String propValue = MPropHandler.convertToString(m.properties(), propKey);
            System.out.println( propKey + " = " + propValue );
        }
    }
}

