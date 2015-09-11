/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MViewPane;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Loads multiple molecules from a chemical structure file and displays
 * them in MarvinView using its multi-cell molecule table mode.
 * Along with the structure the index of the molecule is also displayed
 * as a label (the index is simply an integer number from 1 to the number
 * of structures).
 * The approach in MarvinView is to lay out all structures in a large
 * virtual table with a given number of rows and columns of which a given
 * number of rows and columns are visible. Thus visible rows and all rows,
 * as well as visible columns and all columns are distinguished.
 * As a consequence, the table allows scrolling its content
 * if there are more molecules than table cells (more rows than visible
 * rows, or more columns than visible columns). 
 * In this simple example the number of columns is the same as the
 * number of visible columns (2) and the number of visible rows is 2,
 * while the total number of rows is unlimited (thus the table can be
 * scrolled vertically).
 * The program takes one input file (given in the command line as the
 * first argument by the name of the file), reads all structures and
 * displays them in the table.
 *
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/examples/beans/view-table-params/index.html
 *
 * @author  Judit Vasko-Szedlar, Miklos Vargyas
 * @version 5.2.2 05/15/2009
 * @since   Marvin 5.0.3 04/11/2008 
 */
public class ViewTableParams {

    /** grid table to display molecules */
    private MViewPane viewPane= new MViewPane(); 
    /** application window that contains only the viewPane */
    private JFrame mainWindow = new JFrame();    

    /** number of table rows */
    private int visibleRowCount;
    /** number of columns displayed */
    private int visibleColumnCount;
    /** total number of molecules to be displayed */
    private int molCount;

    /** index of the next molecule to be added*/ 
    private int currentMoleculeIndex = 0;

    /** 
     * Creates and displays a molecule view table of given size.
     * @param molCount total number of molecules to be visualized
     * @param visibleRowCount number of visible table rows
     * @param visibleColumnCount number of visible columns
     */
    public ViewTableParams(int molCount, int visibleRowCount,
                     int visibleColumnCount){

        this.visibleRowCount = visibleRowCount;
        this.visibleColumnCount = visibleColumnCount;
        this.molCount = molCount;
        // Swing components should be created, queried, and manipulated on
        // the event-dispatching thread according to Sun's recommendations.
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        mainWindow.setTitle("MarvinView Table Layout Example");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.getContentPane().setLayout(new BorderLayout());
        mainWindow.getContentPane().add(viewPane, BorderLayout.CENTER);

        initTable();

        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    /**
     * Places the given molecule in the next empty cell. Cells are
     * filled from top to down, and from left to right in each row.
     * The label of the cell is also set, numbered from 1.
     * @param structure the molecule to add to the table
     */
    public void addMolecule( Molecule structure ) {
        viewPane.setM(currentMoleculeIndex, structure);
        viewPane.setL(currentMoleculeIndex,
                Integer.toString(++currentMoleculeIndex));
    }

    /**
     * Sets the table layout and some parameters that affect
     * the appereance of the table.
     */
    private void initTable() {
        // calculate total table rows, making sure that no input molecules
        // is lost, this is why floor (rounding up) is used, the float
        // division instead of an integer division is also important
	int totalRows = (int) Math.floor(
                (double) molCount / visibleColumnCount);

        viewPane.setParams(
                // set total row count
                "rows="+totalRows+"\n"
                // set visible row count
                + "visibleRows="+visibleRowCount+"\n"
                // set column count
                + "cols="+visibleColumnCount+"\n"
                // visible column count
                + "visibleCols="+visibleColumnCount+"\n"
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

        // border around each cell
        viewPane.setBorderWidth(1);
        // table and cell background
        viewPane.setBackground(Color.LIGHT_GRAY);
        // molecule background
        viewPane.setMolbg(Color.WHITE);
    }



    /**
     * Imports Molecules from a named structure file.
     * @param fileName name of the chemical structure file
     * @return ArrayList of Molecule objects in the same order as
     *         read from the file
     * @throws IOException if anything went wrong with reading the
     *                     structures, or opening the file
     */
    private static ArrayList<Molecule> loadMolecules(String fileName)
            throws IOException {

        ArrayList<Molecule> molecules = new ArrayList<Molecule>();
        MolImporter mi = new MolImporter(fileName);

        Molecule m = mi.read();
        while (m != null) {
            molecules.add(m);
            m = mi.read();
        }

        mi.close();
        return molecules;
    }

    /**
     * Takes one command line argument, the name of the input file.
     * @param args the first element is the input file name
     */
    public static void main(String[] args) {

        String fileName = args[0];
        try {
            final ArrayList<Molecule> molecules = loadMolecules(fileName);
            final ViewTableParams mv = new ViewTableParams(molecules.size(), 2, 2);
            // invokeLater is necessary because of the GUI manipulations
            // of setM and setL methods
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for (Molecule mol : molecules) {
                        mv.addMolecule(mol);
                    }
                }
            });
        }
        catch (FileNotFoundException ex) {
            System.err.println("File " + fileName + " not found");
        }
        catch (MolFormatException ex) {
            System.err.println("File " + fileName
                    + " is corrupted or not a structure file.\n"
                    + ex.getMessage());
        }
        catch (IOException e) {
            System.err.println("Error reading file " + fileName + "\n");
        }
    }

}
