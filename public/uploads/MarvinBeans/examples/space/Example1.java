/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */
package examples.space;

import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.PolarizabilityPlugin;
import chemaxon.marvin.space.MSpaceInstaller;
import chemaxon.marvin.space.MSpaceEasy;
import chemaxon.marvin.space.MoleculeComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * MarvinSpace example, see "marvin/doc/dev/space-map.html" for description.
 * @author Judit Papp
 */
public class Example1 {
    public static void main(String[] args) {
        Example1 ef = new Example1();
        try{
            final String molS = "C1C2=CC=CC=C2C3=C4CC5=CC=CC=C5C4=C6CC7=CC=CC=C7C6=C13";
            Molecule mol = MolImporter.importMol(molS);
            PolarizabilityPlugin plugin = new PolarizabilityPlugin();
            plugin.setMolecule(mol);
            plugin.run();
            ArrayList values = new ArrayList();
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setMaximumFractionDigits( 3 );
            for(int i=0; i<mol.getAtomCount(); i++) {
                values.add( Float.valueOf( nf.format( ((Double)plugin.getResult(i)).floatValue())) );
            }
            mol.hydrogenize(true);
            for(int i=0; i<mol.getExplicitHcount(); i++) {
                values.add( new Double(0) );
            }
            JFrame frame = ef.createSpaceFrame( mol, values );
            frame.setTitle("Polarizability");
            java.net.URL u = ef.getClass().getResource("/chemaxon/marvin/space/gui/mspace16.gif");
            frame.setIconImage(Toolkit.getDefaultToolkit().createImage((java.awt.image.ImageProducer)u.getContent()));
            frame.setVisible(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a JFrame containing a MarvinSpace panel of two cells.
     * @param mol is the molecule to display in both cells
     * @param resultValues contains value to each atom of the molecule
     */
    public JFrame createSpaceFrame(Molecule mol, ArrayList resultValues) throws Exception {
        MSpaceEasy mspace = new MSpaceEasy(1, 2, true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mspace.addCanvas( frame.getContentPane() );
        mspace.setSize( 800, 600 );
        mspace.setProperty("SynchronousMode", "true");

        MoleculeComponent mc1 = mspace.addMoleculeTo( mol, 0 );
        mspace.getEventHandler().createAtomLabels(mc1, resultValues);

        MoleculeComponent mc2 = mspace.addMoleculeTo(mol, 1);
        mspace.computeSurface( mc2 );

        frame.pack();
        return frame;
    }

}
