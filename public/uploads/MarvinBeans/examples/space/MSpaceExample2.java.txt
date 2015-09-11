package examples.space;

import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

import javax.swing.*;

public class MSpaceExample2 {

public void createSimpleMarvinSpaceFrame() throws Exception {
    final chemaxon.marvin.space.MSpaceEasy mspace = new chemaxon.marvin.space.MSpaceEasy(true);

    JFrame frame = new JFrame();
    frame.setTitle(chemaxon.marvin.space.gui.MSpace.programName+" "+chemaxon.marvin.space.gui.MSpace.version);
    frame.setSize(800, 750);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mspace.addCanvas(frame.getContentPane());
    mspace.addPopupMenu();
    mspace.addMenuBar(frame);
    mspace.setSize(600, 600);

//    final String molS = "C1C2=CC=CC=C2C3=C4CC5=CC=CC=C5C4=C6CC7=CC=CC=C7C6=C13";
//    Molecule mol = MolImporter.importMol(molS);

    mspace.addMolecule("http://www.chemaxon.com/shared/MarvinSpace/pdb/1AID.pdb");

    frame.pack();
    frame.setVisible(true);
}

    public static void main(String[] args) {
        MSpaceExample2 e = new MSpaceExample2();
        try{
            e.createSimpleMarvinSpaceFrame();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
