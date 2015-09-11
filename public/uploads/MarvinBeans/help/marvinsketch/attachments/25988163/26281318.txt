/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import chemaxon.marvin.MolPrinter;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * SimpleJFileChooserAccessory
 *
 * @author Attila Szabo, Judit Vasko-Szedlar
 * @version 5.0.3 Apr 26, 2008
 * @since Marvin 5.0.3
 */
public class SimpleJFileChooserAccessory extends JPanel
        implements PropertyChangeListener {
    //MolImporter for reading molecule files
    private MolImporter molImporter;
    //MoleculeRenderer that paints the molecules
    private MoleculeRenderer renderer;

    //Adds propertyChangeListener to the file chooser
    //Initializes the MoleculeRenderer and the MolImporter
    public SimpleJFileChooserAccessory(JFileChooser fileChooser) {
        molImporter = new MolImporter();
        renderer = new MoleculeRenderer();
        fileChooser.addPropertyChangeListener(
                JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, this);
        setLayout(new BorderLayout());
        add(renderer, BorderLayout.CENTER);
        renderer.setPreferredSize(new Dimension(100,100));
    }

    //This method handles file selection change events.
    public void propertyChange(PropertyChangeEvent evt) {
        File selection = (File)(evt.getNewValue());
        if(selection != null) {
            try {
                //Passes the selected molecule file to the importer,
                //or the first molecule in case of a multi-molecule file.
                molImporter.setFileName(selection.getAbsolutePath());
                //Passes the molecule from the importer to the renderer.
                setMolecule();
            } catch (IOException exception) {
                renderer.setMolecule(new Molecule());
            }
        }
    }

    //Passes the molecule from the importer to the renderer.
    private void setMolecule() {
        try {
            renderer.setMolecule(
                    molImporter.nextDoc().getPrimaryMolecule());
            renderer.repaint();
        } catch (IOException exception) {
            renderer.setMolecule(new Molecule());
        }
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Initiate a JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        //Initiate SimpleJFileChooserAccessory with the JFileChooser instance
        //to custumize.
        //This is very important, otherwise the accessory can't handle
        //file change events fired from the JFileChooser instance.
        SimpleJFileChooserAccessory accessory =
                new SimpleJFileChooserAccessory(fileChooser);
        //Adding the accessory instance to file chooser.
        fileChooser.setAccessory(accessory);
        fileChooser.showOpenDialog(null);
        System.exit(0);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static class MoleculeRenderer extends JComponent {
        private MolPrinter mPrinter = new MolPrinter();

        public void setMolecule(Molecule molecule) {
            // Passing the current molecule to MolPrinter.
            mPrinter.setMol(molecule);
        }

        public void paintComponent(Graphics g) {
            // It is very important to set the scale factor of MolPrinter,
            // otherwise the molecule will not appear.
            // The scale factor is computed by MolPrinter from
            // the current size.
            mPrinter.setScale(mPrinter.maxScale(getSize()));
            mPrinter.setBackgroundColor(this.getBackground());
            mPrinter.paint((Graphics2D) g, getSize());
        }
    }
}
