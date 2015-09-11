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
import chemaxon.marvin.plugin.CalculatorPlugin;
import chemaxon.marvin.space.MSpaceInstaller;
import chemaxon.marvin.space.MSpaceEasy;
import chemaxon.marvin.space.MoleculeComponent;
import chemaxon.marvin.space.MolecularSurfaceComponent;
import chemaxon.marvin.space.monitor.Label;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * MarvinSpace example, see "marvin/doc/dev/space-map.html" for description.
 * @author Judit Papp
 */
public class Example3 {

    CalculatorPlugin plugin = null;

    public static void main(String[] args) {
        Example3 ef = new Example3();
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
            ef.setPlugin(plugin);
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

    public void setPlugin(CalculatorPlugin plugin) {
        this.plugin = plugin;
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

        mspace.setProperty( "BackgroundColor", "#ffffff" );
        mspace.setProperty( "Label.Draw2D", "true" );
        mspace.setProperty( "Label.ForegroundColor", "#000000" );
        mspace.setProperty( "Label.BackgroundColor", "#ffffff" );
        mspace.setProperty( "Label.Size", "small" );

        MoleculeComponent mc1 = mspace.addMoleculeTo( mol, 0 );
        Label[] labels = mspace.getEventHandler().createAtomLabels(mc1, resultValues);
        for(int i=0; i<labels.length; i++) {
            labels[i].setBorderColorMode(Label.BORDER_MODE_BRIGHTER_FOREGROUND);
        }
        displayMolecularResults(mol, mspace);

        MoleculeComponent mc2 = mspace.addMoleculeTo(mol, 1);
        MolecularSurfaceComponent msc = mspace.computeSurface( mc2 );

        byte[][] paletteColors = new byte[][] { {89,89,89}, {69,69,89}, {0,0,127} };
        msc.setPalette( paletteColors );
        msc.setAtomPropertyList( resultValues );
        msc.setDrawProperty( "Surface.ColorType", "AtomProperty" );

        frame.pack();
        return frame;
    }

    protected void displayMolecularResults(Molecule mol, MSpaceEasy mspace) {
        String text = getPropertyText();
        if(text == null) {
            return;
        }
        float[] c = new float[] {10, 20};
        Label label = null;
        Object lo = null;
        String[] texts = text.split("\n");
        if(texts.length > 1) {
            for(int i=0; i<texts.length; i++ ) {
                lo = mspace.getEventHandler().createLabel( texts[i], c );
                if(lo instanceof Label) {
                    label = (Label)lo;
                }
                else {
                    continue;
                }
                c[1] += 20;
                label.ignoreDrawProperties( true );
                label.setBorderColorMode(Label.BORDER_MODE_NONE);
                label.setBackgroundColor( Color.white );
                label.setForegroundColor( Color.black );
            }
        }
        else {
            lo = mspace.getEventHandler().createLabel( text, c );
            if(lo instanceof Label) {
                label = (Label)lo;
                label.ignoreDrawProperties( true );
                label.setBorderColorMode(Label.BORDER_MODE_NONE);
                label.setBackgroundColor( Color.white );
                label.setForegroundColor( Color.black );
            }
        }

    }

    /**
     * Returns molecule properties is text form.
     * @return molecule properties in text form
     */
    protected String getPropertyText() {
        String text = null;
        Object[] types = plugin.getResultTypes();
        try {
           for( int t = 0; t < types.length; t++ ) {
               Object type = types[t];
               if(plugin.getResultDomain(type) != CalculatorPlugin.ATOM) {
                   Object result = plugin.getResult(type, 0);
                   if (text == null) {
                       text = "";
                   }
                   else {
                       text += "\n";
                   }
                   text += type;
                   text += " = " + plugin.getResultAsString(type, 0, result);
               }
           }
        }
        catch(Exception e) {
           e.printStackTrace();
        }
        return text;
    }
}

