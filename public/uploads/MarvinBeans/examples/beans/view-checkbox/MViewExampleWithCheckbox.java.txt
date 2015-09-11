/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import chemaxon.marvin.beans.*;
import chemaxon.formats.*;
import chemaxon.struc.Molecule;
import chemaxon.marvin.view.ViewParameterConstants;
import chemaxon.marvin.sketch.SketchParameterConstants;

/**
 * MarvinView example application with customized layout.
 * @version 4.0.2, 10/08/2005
 * @since   Marvin 3.0, 10/25/2002
 * @author  Tamas Vertse
 * @author  Peter Csizmadia
 */
public class MViewExampleWithCheckbox extends JFrame implements WindowListener,
    ActionListener, ItemListener
{
    /** The MarvinView bean. */
    private MViewPane viewPane;
    private String[][] imgFiles = null;
    private JTextArea output = null;
    
    /** Create the frame. */
    private MViewExampleWithCheckbox() {
	setTitle("MarvinView Example With Checkboxes");
	JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	JMenu menu = new JMenu("File");
	menubar.add(menu);
	JMenuItem mi;
	menu.add(mi = new JMenuItem("Exit"));
	mi.setActionCommand("exit");
	mi.addActionListener(this);

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(viewPane = new MViewPane(),BorderLayout.CENTER);
	getContentPane().add(output = new JTextArea(10,40),BorderLayout.SOUTH);

	// Keyboard events are received by the JFrame but processed by the
	// bean.
	// Warning: order is important! This function call must be *after*
	// the addition of viewPane to the content pane.
	addKeyListener(viewPane);

	// We are listening to our own window closing events.
	addWindowListener(this);

	// print debug messages
	viewPane.setDebug(1);

	// set basic parameters
	int rows = 2;
	int cols = 2;
	int molnum = rows*cols;
	String cellparams="";
	for(int i=0; i < molnum; i++) {
	    cellparams += "cell"+i+"=||"+
		"||||-|-"+
		"| Get "+i+". |%|getmol"+i+
		"|easychart"+(i+1)+".gif|easychart"+(i+1)+".gif|-\n";
	}

	String params="#\n"+
	    "# MarvinView properties\n"+
	    "#\n"+
	    ViewParameterConstants.ROWS+"="+rows+"\n"+
	    ViewParameterConstants.COLS+"="+cols+"\n"+
	    ViewParameterConstants.LAYOUT+"=:4:2"+
	    ":M:1:0:3:1"+
	    ":L:0:0:1:2:n"+
	    ":C:1:1:1:1:c"+
	    ":B:2:1:1:1:s"+
	    ":I:3:1:1:1:s\n"+
	    ViewParameterConstants.PARAMETERS +"=:M:150:150"+
	    ":L:10b"+
	    ":C:10:"+
	    ":B:10:\n"+
	    cellparams+
	    "\n"+
	    "#\n"+
	    "# MarvinSketch properties also have to be specified here for the sketcher\n"+
	    "# windows that are launched from the viewer (using Edit/Structure).\n"+
	    "#\n"+
	    SketchParameterConstants.EXTRA_BONDS+"=arom,wedge\n";

	viewPane.setParams(params);

	// set properties that can be modified runtime
	viewPane.setBorderWidth(1);
	viewPane.setEditable(2);
	viewPane.setMolbg(new Color(0xffffff));

	java.util.Random selected = new Random();
	for(int i=0;i<molnum;i++) {
	    viewPane.setM(i,createMolecule("CN1C=NC2=C1C(=O)N(C)C(=O)N2C"));
	    viewPane.setL(i, i+". Caffeine ");
	    viewPane.setC(i, selected.nextBoolean());
	}

	AbstractButton button = null;

	//initalize checkboxes
	int viscellnum = viewPane.getVisibleCellCount();
	for(int i=0; i < viscellnum; i++) {
	    button = viewPane.getVisibleButtonC(i);
	    button.addItemListener(this);
	}

	//initalize buttons
	for(int i=0; i < viscellnum; i++) {
	    button = viewPane.getVisibleButtonB(i);
	    button.setEnabled(true);
	    button.addActionListener(this);
	}
    }
    
    private static Molecule createMolecule(String smi) {
        try {
            return MolImporter.importMol(smi);
        } catch(MolFormatException mfe) {
                System.err.println("Invalide smiles:"+smi);
                return null;
        }
    }
    
    /** Do nothing. */
    public void mouseEntered(MouseEvent ev) { }

    /** Do nothing. */
    public void mouseExited(MouseEvent ev) { }

    /** Do nothing. */
    public void mouseClicked(MouseEvent ev) { }

    /** Do nothing. */
    public void windowOpened(WindowEvent ev) { }

    /** Close the window. */
    public synchronized void windowClosing(WindowEvent ev) {
	viewPane.exit();
	setVisible(false);
	getContentPane().removeAll();
	dispose();
	System.exit(0);
    }

    /** Do nothing. */
    public void windowClosed(WindowEvent ev) { }

    /** Do nothing. */
    public void windowIconified(WindowEvent ev) { }

    /** Do nothing. */
    public void windowDeiconified(WindowEvent ev) { }

    /** Do nothing. */
    public void windowActivated(WindowEvent ev) { }

    /** Do nothing. */
    public void windowDeactivated(WindowEvent ev) { }

    /** Handle menu events. */
    public void actionPerformed(ActionEvent ev) {
	Object src = ev.getSource();
	String cmd = ev.getActionCommand();
	if (src instanceof AbstractButton) {
	    // ActionEvent from a Marvin component
	    AbstractButton button = (AbstractButton)src;
	    String msg = "";
	    int index = -1;  // relative index (not absolute)
	    int abscell = -1;
	    if((index = viewPane.indexOfButtonB(button)) > -1) {
		// onestate button
		abscell = viewPane.getAbsoluteCellIndex(button);
		msg = "ACTIONPERFORMED src=button"+index+
		    " label="+	button.getText();
		output.append(msg+"\n");
	    } 
	    if(index > -1) {
		return;
	    }
	}
	if(cmd != null) {
	    if(cmd.equals("exit")) {
		windowClosing(null);
	    } 
	}
    }

    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     */
    public void itemStateChanged(ItemEvent ev) {
        Object target = ev.getSource();
        if(target instanceof AbstractButton) {
            AbstractButton button = (AbstractButton)target;
            int index = -1; // relative index (not absolute)
	    String msg = "";
	    if((index = viewPane.indexOfButtonC(button)) > -1) {
				// checkbox
                msg = "ITEMSTATECHANGED: src= checkbox" + index
		    + " selected= "+viewPane.getC(index);
            } else if((index = viewPane.indexOfButtonB(button)) > -1) {
				// button
                msg = "ITEMSTATECHANGED: src= button" + index
		    + " label="+button.getText()
		    + " selected= "+button.isSelected();
            } 
	    if(msg.length() > 0) {
		output.append(msg+"\n");
	    }
        }
    }    
    
    /** Show the MarvinView demo window. */
    public static void main(String[] args) {
	final JFrame w = new MViewExampleWithCheckbox();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
}
