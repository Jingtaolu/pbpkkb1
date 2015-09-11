/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MViewPane;
import chemaxon.marvin.io.MDocSource;
import chemaxon.marvin.view.swing.TableSupport;
import chemaxon.marvin.view.swing.TableOptions;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Displays a multiple-molecule file in MarvinView.
 * 
 * @author  Peter Csizmadia
 * @version 5.2.1, 04/14/2009
 * @since   Marvin 5.2, 02/15/2009
 */
public class ViewTable {
    /**
     * Takes one command line argument, the name of the input file.
     */
    public static void main(String[] args) {
	final String fileName = args[0];
	final MDocSource docSource = createDocSource(fileName);
	if(docSource != null) {
	    // Swing components should be created, queried, and manipulated
            // on the event-dispatching thread
            // according to Sun's recommendations.
	    EventQueue.invokeLater( new Runnable() {
		public void run() {
		    createAndShowGUI(docSource, fileName);
		}
	    });
	}
    }

    /**
     * Creates a document source for the given filename.
     * @param fname    the filename
     * @return the document source or null if it could not be created
     */
    static MDocSource createDocSource(String fname) {
	try {
	    return new MolImporter(fname);
	} catch(FileNotFoundException ex) {
	    System.err.println("File " + fname+" not found");
	} catch(MolFormatException ex) {
	    System.err.println("File " + fname
		    + " is corrupted or not a structure file.\n"
		    + ex.getMessage());
	} catch (IOException e) {
	    System.err.println("Error reading file " + fname+"\n");
	}
	return null;
    }

    /**
     * Creates and shows the viewer table.
     * @param docSource    the document source
     * @param inputName    input filename
     */
    static void createAndShowGUI(MDocSource docSource, String inputName) {
	// Create and initialize the MarvinView bean.
	MViewPane viewPane = new MViewPane();
        viewPane.setBorderWidth(1);
        viewPane.setBackground(Color.LIGHT_GRAY);
        viewPane.setMolbg(Color.WHITE);

	// Create and initialize the application window.
	JFrame win = new JFrame();
	JMenuBar menubar = new JMenuBar();
	win.setJMenuBar(menubar);
	win.setTitle("MarvinView Table Layout Example");
	win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	win.getContentPane().setLayout(new GridLayout(1, 1));
	win.getContentPane().add(viewPane);

	// The first viewPane.getTableSupport() call creates the table
        // support object. This call should _precede_ win.pack() because
        // the existence of the table support influences the preferred size
        // of the viewPane component.
	TableSupport tableSupport = viewPane.getTableSupport();

	// Create a Menu for the Table options. (Optional.)
	JMenu menu;
	tableSupport.makeTableMenu(menu = new JMenu("Table"));
	menu.setMnemonic('t');
	menubar.add(menu);

	makeHighlightMenu(menu = new JMenu("Highlight"), viewPane);
	menu.setMnemonic('h');
	menubar.add(menu);

	// Optional table settings.
	TableOptions tblopts = tableSupport.getTableOptions();
	// tblopts.setViewHandlerType(TableOptions.VH_GRIDBAG);
	tblopts.setMaxRows(5);
	tblopts.setMaxCols(5);

	// Pack the window, set its location and make it visible.
	win.pack();
	win.setLocationRelativeTo(null);
	win.setVisible(true);

	// Start loading the molecules.
	tableSupport.start(docSource, inputName);
    }

    static void makeHighlightMenu(JMenu menu, final MViewPane viewPane) {
        final ArrayList<Integer> highlightedCells = new ArrayList<Integer>();
        JMenuItem highlightMenuItem, clearMenuItem;

        //
	// Example for setting the background color of the molecule ID field.
	//
        highlightMenuItem = new JMenuItem("Highlight selected cell");
        menu.add(highlightMenuItem);
        highlightMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                int selected = viewPane.getSelectedIndex();
                if(!highlightedCells.contains(selected)) {
                    highlightedCells.add(selected);
                    viewPane.setRecordIDBackground(selected,
                            SystemColor.textHighlight);
                    viewPane.setRecordIDForeground(selected,
                            SystemColor.textHighlightText);
                }
            }
        });

        clearMenuItem = new JMenuItem("Remove highlight from cells");
        menu.add(clearMenuItem);
        clearMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                for(int i : highlightedCells) {
                    viewPane.setRecordIDBackground(i, null);
                    viewPane.setRecordIDForeground(i, null);
                }
                highlightedCells.clear();
            }
        });
    }
}
