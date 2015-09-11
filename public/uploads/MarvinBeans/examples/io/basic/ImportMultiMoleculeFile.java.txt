package io.basic;
/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import chemaxon.marvin.beans.MViewPane;
import chemaxon.marvin.io.MRecordImporter;
import chemaxon.marvin.io.MRecordParseException;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolInputStream;
import chemaxon.struc.Molecule;
import chemaxon.struc.MDocument;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import java.io.IOException;

/**
 * ImportMultiMoleculeFile
 *
 * @author Judit Vasko-Szedlar
 * @version 2009.11.23.
 * @since Marvin 5.3
 */

public class ImportMultiMoleculeFile extends JPanel {

    /** The MarvinView bean is used to display the imported molecule. */
    private MViewPane viewPane;

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setTitle("Basic Import Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImportMultiMoleculeFile sample = new ImportMultiMoleculeFile();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sample, BorderLayout.CENTER);

        try {
            URL url = new URL("http://www.chemaxon.com/marvin/mols-2d/mols.sdf");
            sample.importMoleculeWithMRecordImporter(url);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public ImportMultiMoleculeFile() {
        // Creating the MarvinView JavaBean component.
        // The component is ready and can simply added to
        // any Swing container.
        viewPane = new MViewPane();
        setLayout(new BorderLayout());
        add(viewPane, BorderLayout.CENTER);
        viewPane.setParams(
                // set total row count
                "rows="+3+"\n"
                // set visible row count
                + "visibleRows="+2+"\n"
                // set column count
                + "cols="+4+"\n"
                // visible column count
                + "visibleCols="+4+"\n"
                // 2:1 -> 2 rows and 1 columns per cell;
                // L:0:0:1:1:c:n:0:1 -> first row and col (0:0:1:1) of the
                //     cell is a label (L), centered (c) ...
                // M:1:0:1:1:c:b:1:1 -> second row in the first (and only)
                //     column is a molecule (M), centered (c) ...
                + "layout=:2:1:L:0:0:1:1:c:n:0:1:M:1:0:1:1:c:b:1:1\n"
                // L:10b -> label is displayed in 10pt bold fonts
                // M:200:200 -> molecule is displayed in a 200x200
                // pixels area
                + "param=:L:10b:M:200:200\n"
        );
        viewPane.setBorderWidth(1);
    }

    public void importMoleculeWithMolImporter(URL url) {
        try {
            MolImporter importer = new MolImporter(url.openStream());
            Molecule mol;
            int i = 0;
            while ((mol = importer.read()) != null) {
                viewPane.setM(i++, mol);
            }
            importer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importMoleculeWithMRecordImporter(URL url) {
        try {
            MolInputStream mis = new MolInputStream(url.openStream(), null, null, null);
            MRecordImporter importer = new MRecordImporter(mis, null);
            int i = 0;
            MDocument mDocument;
            while ((mDocument = importer.readDoc()) != null) {
                Molecule mol = (Molecule) mDocument.getMainMoleculeGraph();
                viewPane.setM(i++, mol);
            }
            importer.close();
        } catch (MRecordParseException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
