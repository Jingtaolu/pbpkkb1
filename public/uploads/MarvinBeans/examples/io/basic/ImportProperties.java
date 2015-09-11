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
import chemaxon.marvin.io.MPropHandler;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

/**
 * ImportProperties is a basic example of importing molecular properties.
 *
 * @author Judit Vasko-Szedlar
 * @version 2009.11.24.
 * @since Marvin 5.3
 */

public class ImportProperties extends JPanel {

    private final static String NAME_FIELD = "product_name";
    private final static String LOGP_FIELD = "c_log_p";
    private final static String RBONDS_FIELD = "rotatable_bonds";
    private final static String molSource = "\n"
            + "  Marvin  11240914532D          \n"
            + "\n"
            + " 18 19  0  0  0  0            999 V2000\n"
            + "    1.4242    0.6193    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    0.7087    0.2102    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -0.0045    0.6215    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -1.4242   -1.0285    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -0.7177   -0.6148    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    1.4287    1.4465    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    2.1374    0.2079    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -2.1441   -0.6148    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -0.7177    0.2102    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -2.1441    0.2102    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -1.4242   -1.8535    0.0000 Cl  0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    2.1374   -0.6193    0.0000 F   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    0.7132    1.8557    0.0000 Cl  0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -2.8596   -1.0285    0.0000 F   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "   -1.4242    0.6215    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    2.8573    1.4443    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    2.8551    0.6171    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "    2.1419    1.8535    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
            + "  2  1  1  0  0  0  0\n"
            + "  3  2  2  0  0  0  0\n"
            + "  4  5  2  0  0  0  0\n"
            + "  5  9  1  0  0  0  0\n"
            + "  6  1  1  0  0  0  0\n"
            + "  7  1  2  0  0  0  0\n"
            + "  8 10  2  0  0  0  0\n"
            + "  9  3  1  0  0  0  0\n"
            + " 10 15  1  0  0  0  0\n"
            + " 11  4  1  0  0  0  0\n"
            + " 12  7  1  0  0  0  0\n"
            + " 13  6  1  0  0  0  0\n"
            + " 14  8  1  0  0  0  0\n"
            + " 15  9  2  0  0  0  0\n"
            + " 16 18  1  0  0  0  0\n"
            + " 17  7  1  0  0  0  0\n"
            + " 18  6  2  0  0  0  0\n"
            + " 16 17  2  0  0  0  0\n"
            + "  8  4  1  0  0  0  0\n"
            + "M  END\n"
            + ">  <MOLFORMULA>\n"
            + "C13 H7 Cl2 F2 N\n"
            + "\n"
            + ">  <MOLWEIGHT>\n"
            + "2.861070000000000e+002\n"
            + "\n"
            + ">  <product_name>\n"
            + "N1-(2-chloro-6-fluorobenzylidene)-3-chloro-4-fluoroaniline\n"
            + "\n"
            + ">  <c_log_p>\n"
            + "5.136800000000000e+000\n"
            + "\n"
            + ">  <rotatable_bonds>\n"
            + "3\n"
            + "\n"
            + "$$$$";

    /** The MarvinView bean is used to display the imported molecule. */
    private MViewPane viewPane;
    private JTextField nameTextField;
    private JTextField logPTextField;
    private JTextField rBondsTextField;
    private JTextArea otherProperties;

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

        ImportProperties sample = new ImportProperties();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sample, BorderLayout.CENTER);

        try {
            sample.importMolecule(molSource);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public ImportProperties() {
        // Creating the MarvinView JavaBean component.
        // The component is ready and can simply added to
        // any Swing container.
        viewPane = new MViewPane();
        setLayout(new BorderLayout());
        add(viewPane, BorderLayout.CENTER);

        JPanel propertiesPanel = createPropertiesPanel();

        add(propertiesPanel, BorderLayout.SOUTH);
    }

    private JPanel createPropertiesPanel() {
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridBagLayout());
        ((GridBagLayout)propertiesPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
        ((GridBagLayout)propertiesPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
        ((GridBagLayout)propertiesPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
        ((GridBagLayout)propertiesPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

        JLabel label1 = new JLabel();
        label1.setText("Product name:");
        propertiesPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        nameTextField = new JTextField();
        nameTextField.setEditable(false);
        propertiesPanel.add(nameTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        JLabel label2 = new JLabel();
        label2.setText("logP:");
        propertiesPanel.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

        logPTextField = new JTextField();
        logPTextField.setEditable(false);
        propertiesPanel.add(logPTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        JLabel label3 = new JLabel();
        label3.setText("Rotatable bonds:");
        propertiesPanel.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        rBondsTextField = new JTextField();
        rBondsTextField.setEditable(false);
        propertiesPanel.add(rBondsTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        JLabel label4 = new JLabel("Other properties:");
        propertiesPanel.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

        otherProperties = new JTextArea();
        otherProperties.setEditable(false);
        JScrollPane scp = new JScrollPane();
        scp.setViewportView(otherProperties);
        propertiesPanel.add(scp, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

        return propertiesPanel;
    }

    private void fillProperties(Molecule molecule) {
        nameTextField.setText(MPropHandler.convertToString(molecule.properties(), NAME_FIELD));
        logPTextField.setText(MPropHandler.convertToString(molecule.properties(), LOGP_FIELD));
        rBondsTextField.setText(MPropHandler.convertToString(molecule.properties(), RBONDS_FIELD));

        for (int i=0; i<molecule.getPropertyCount(); i++) {
            String s = molecule.getPropertyKey(i);
            if (!isKnownPropertyKey(s)) {
                otherProperties.setText(otherProperties.getText()+MPropHandler.convertToString(molecule.properties(),s) + "\n");
            }
        }
    }

    private boolean isKnownPropertyKey(String key) {
        return NAME_FIELD.equals(key) || LOGP_FIELD.equals(key) || RBONDS_FIELD.equals(key);
    }

    public void importMolecule(String s) {
        try {
            Molecule mol = MolImporter.importMol(s);
            viewPane.setM(0, mol);
            fillProperties(mol);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
