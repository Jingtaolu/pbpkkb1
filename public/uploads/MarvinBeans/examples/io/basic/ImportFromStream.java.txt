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
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * ImportFromStream
 *
 * @author Judit Vasko-Szedlar
 * @version 2009.11.23.
 * @since Marvin 5.3
 */

public class ImportFromStream extends JPanel {

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

        ImportFromStream sample = new ImportFromStream();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sample, BorderLayout.CENTER);

        String molSource = "CCC(N)c1cc(Cl)cc(C(N)CC)c1Br";
        InputStream in = new ByteArrayInputStream(molSource.getBytes());
        sample.importMolecule(in);

        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public ImportFromStream() {
        // Creating the MarvinView JavaBean component.
        // The component is ready and can simply added to
        // any Swing container.
        viewPane = new MViewPane();
        setLayout(new BorderLayout());
        add(viewPane, BorderLayout.CENTER);
    }

    public void importMolecule(InputStream stream) {
        try {
            MolImporter mi = new MolImporter(stream);
            Molecule mol = mi.read();
            viewPane.setM(0, mol);
            mi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
