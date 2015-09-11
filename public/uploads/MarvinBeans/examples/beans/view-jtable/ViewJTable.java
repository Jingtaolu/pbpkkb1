/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.struc.Molecule;
import chemaxon.marvin.beans.MViewPane;
import chemaxon.formats.MolImporter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;


/**
 * Application using MViewPane as the cell renderer and the cell 
 * editor of a JTable, and as an alternative approach using
 * MolPrinter as a cell renderer.
 * <p>
 * These renderer and editor classes are also accessible as part of
 * the public API.<br>
 * Renderer: {@link chemaxon.marvin.beans.MViewRenderer}<br>
 * Editor: {@link chemaxon.marvin.beans.MViewEditor}
 * <p>
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/examples/beans/view-jtable/index.html
 *
 * @author  Judit Vasko-Szedlar
 * @author  Gabor Bartha
 * @version 5.2.2 May 16, 2009
 * @since   Marvin 2.10.5, 09/02/2002
 */
public class ViewJTable extends JPanel {

    private JTable table;

    /**
     * Constructor, creates the JTable and its editor and renderer objects
     */
    public ViewJTable() {
        //Creates the data object of the table
	MoleculeTableModel molModel = new MoleculeTableModel();
	//Creates the table
        table = new JTable(molModel);
	//Sets table's dimensions
        table.setPreferredScrollableViewportSize(new Dimension(400, 450));
	table.setRowHeight(150);

	//Sets the cell renderer of the table
	setUpMolRenderer(table);
 	//Sets the cell editor of the table
        setUpMolEditor(table);

        //Creates a scroll pane and adds the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);
        add(scrollPane);
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     * @param mols molecules to be displayed in the table
     */
    private static void createAndShowGUI(ArrayList<Molecule> mols) {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setTitle("MarvinView in JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ViewJTable viewJTable = new ViewJTable();
        viewJTable.setMolecules(mols);

        //Adds a scroll pane to the content pane.
        frame.getContentPane().add(viewJTable, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setMolecules(ArrayList<Molecule> mols) {
        MoleculeTableModel tm = (MoleculeTableModel)table.getModel();
        tm.setMolecules(mols);
        table.setModel(tm);
    }

    /**
     * Initializes an MViewPane instance and assigns it as the cell editor
     * of Molecule objects in the table.
     * @param table The JTable object to which an MViewPane
     * is set as a cell editor
     */
    private void setUpMolEditor(JTable table) {

        MViewEditor me = new MViewEditor();
	//Sets its editable property to MViewPane.SKETCHABLE which launches
        //MarvinSketch when double clicking on MViewPane
        me.setEditable(MViewPane.SKETCHABLE);
        //Sets the moleditor to the cell editor of the table which cells
        // are containing Molecule objects
	table.setDefaultEditor(Molecule.class, me);
    }

    /**
     * Initializes an MViewPane or MolPrinter instance and assigns it
     * as the cell renderer of Molecule objects in the table.
     * It also initializes and sets a renderer component to String objects.
     * @param table The JTable object to which an MViewPane
     * is set as a cell renderer
     */
    private void setUpMolRenderer(JTable table) {
        //using cell renderer with MViewPane
        table.setDefaultRenderer(Molecule.class, new MViewRenderer());

        // using cell renderer with MolPrinter
        //table.setDefaultRenderer(Molecule.class, new MolRenderer());

        table.setDefaultRenderer(String.class, new StringRenderer());
    }

    public static void main(String[] args) {
        final ArrayList<Molecule> mols = new ArrayList<Molecule>();
        if(args==null || args.length==0) {
            try {
                mols.add(MolImporter.importMol("C1CCCC1"));
                mols.add(MolImporter.importMol("C1=CC2=C(C=C1)C=CC=C2"));
                mols.add(MolImporter.importMol(
                        "CN1C=NC2=C1C(=O)N(C)C(=O)N2C"));
                mols.add(MolImporter.importMol("N1C=CC=C1"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                MolImporter mi = new MolImporter(args[0]);
                Molecule mol;
                while((mol = mi.read())!=null) {
                    mols.add(mol);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(mols);
            }
        });
    }

    /**
     * String renderer to align text in cells
     */
    private static class StringRenderer extends DefaultTableCellRenderer {

        public StringRenderer() {
            setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
        }
    }

    /**
     * The table model object manages the actual table data. 
     */
    private static class MoleculeTableModel extends AbstractTableModel {

	//Defines names of columns
        final String[] columnNames = {"Index", "Molecule", "Mass"};

        Object[][] data = {};

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

   	//Gets a molecule located at the defined row and col of the table
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

	// In case of double clicking with the mouse's left button
        // on a cell, this function gets called.
        // It returns true for the molecule column only,
        // thus allowing its cell editor to work.
        public boolean isCellEditable(int row, int col) {
            return col == 1;
        }

	//Sets a molecule at the defined row and col of the table
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            if(col==1) {
                // setting the mass here keeps the column updated
                // upon editing the molecules
                setValueAt(""+((Molecule)value).getMass(), row, 2);
            }
            fireTableCellUpdated(row, col);
        }

        public void setMolecules(ArrayList<Molecule> mols) {
            data = new Object[mols.size()][3];
            for(int i=0; i<mols.size(); i++) {
                setValueAt(""+(i+1), i, 0);
                setValueAt(mols.get(i), i, 1);
                // we set the mass inside the setValueAt method instead
                //setValueAt(""+mols.get(i).getMass(), i, 2);
            }
        }

    }

}
