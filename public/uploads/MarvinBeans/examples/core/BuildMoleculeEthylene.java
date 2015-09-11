package core;

import java.io.IOException;

import chemaxon.calculations.clean.Cleaner;
import chemaxon.formats.MolExporter;
import chemaxon.struc.*;

/**
 * Example class for structure manipulation. Creates ethylene C/C=C=/C
 * atom by atom.
 *
 * @author Andras Volford, Miklos Vargyas 
 */
public class BuildMoleculeEthylene {

    public static void main(String[] args) throws IOException {

        // create an empty Molecule
        Molecule m = new Molecule();

        // create a Carbon atom
        MolAtom a1 = new MolAtom(6);
        // and add it to the molecule 
        m.add(a1);

        // create anoter Carbon atom
        MolAtom a2 = new MolAtom(6);
        // and add it to the molecule
        m.add(a2);
        
        // create a bond between atoms, bond order 
        MolBond b = new MolBond(a1, a2, 2);
        m.add(b);
        
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        // this prints C=C
        

        // add ligands
        MolAtom l1 = new MolAtom(6);
        MolAtom l2 = new MolAtom(6);
        m.add(l1);
        m.add(l2);
        m.add(new MolBond(a1, l1));
        m.add(new MolBond(a2, l2));
	
        System.out.println(MolExporter.exportToFormat(m, "smiles"));
        System.out.println(MolExporter.exportToFormat(m, "mol"));
        
        // generate 2D coordinates
        Cleaner.clean(m, 2, null);
        System.out.println(MolExporter.exportToFormat(m, "mol"));

        // CIS/TRANS information is not defined, the bond is wiggly
        b = m.getBond(0);
        System.out.println("cis=" + MolBond.CIS );
        System.out.println("trans=" + MolBond.TRANS );
        System.out.println("stereo flag before setting: " + (b.getFlags() & MolBond.STEREO_MASK));

        // set bond to CIS
        b.setFlags(MolBond.CIS, MolBond.STEREO_MASK);
        System.out.println("stereo flag after setting: " + (b.getFlags() & MolBond.STEREO_MASK));
        System.out.println(MolExporter.exportToFormat(m, "mol"));
	
        // render again in 2D
        System.out.println("Cleaned again");
        Cleaner.clean(m, 2, null);
        System.out.println("stereo flag: " + (b.getFlags() & MolBond.STEREO_MASK));
        System.out.println(MolExporter.exportToFormat(m, "mol"));
        
        m.setDim(0);
        b.setFlags(MolBond.CIS, MolBond.STEREO_MASK);
        System.out.println("stereo flag after setting: " + (b.getFlags() & MolBond.STEREO_MASK));
        System.out.println(MolExporter.exportToFormat(m, "mol"));
        // render again in 2D
        System.out.println("Cleaned the 3rd time after setdim(0)");
        Cleaner.clean(m, 2, null);
        System.out.println("stereo flag: " + (b.getFlags() & MolBond.STEREO_MASK));
        System.out.println(MolExporter.exportToFormat(m, "mol"));
    }
}

