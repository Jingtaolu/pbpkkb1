/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.beans.MViewPane;
import chemaxon.marvin.common.UserSettings;
import chemaxon.marvin.view.ViewParameterConstants;
import chemaxon.marvin.sketch.SketchParameterConstants;
import chemaxon.util.DotfileUtil;

/**
 * MarvinView example for demonstrating loading and saving Marvin properties.
 * They stored in "marvin.properties". The location of this file depends from
 * the platform. Call <code>chemaxon.util.DotfileUtil.getDotDir()</code> to
 * determine its location.
 *
 * @version 5.2.5, 08/28/2009
 * @since   Marvin 4.0, 07/26/2005
 * @author  Tamas Vertse
 */
public class MViewSaveProperties extends JFrame implements WindowListener,
    ActionListener {
    
    // get the location of marvin.properties to print it as debug message
    private static String propfilepath = 
                DotfileUtil.getDotDir().toString() + 
                System.getProperty("file.separator") + "marvin.properties";
                                
    /** The MarvinView bean. */
    private MViewPane viewPane;

    /** Create the frame. */
    private MViewSaveProperties() {
        setTitle("MViewSaveProperties");
        getContentPane().setLayout(new GridLayout(1, 1));
        // import settings from marvin.properties
        UserSettings settings = new UserSettings();
        // create a new MarvinView bean
        getContentPane().add(viewPane = new MViewPane(settings));
        
	JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	JMenu menu = new JMenu("File");
	menubar.add(menu);
	JMenuItem mi;
       	menu.add(mi = new JMenuItem("Save Properties"));
	mi.setActionCommand("saveProperties");
        mi.addActionListener(this);
	menu.add(mi = new JMenuItem("Exit"));
	mi.setActionCommand("exit");
	mi.addActionListener(this);

        menu = new JMenu("Edit");
        viewPane.makeEditMenu(menu);
        menubar.add(menu);
        menu = new JMenu("View");
        viewPane.makeViewMenu(menu);
        menubar.add(menu);        
        
	// Keyboard events are received by the JFrame but processed by the
	// bean.
	// Warning: order is important! This function call must be *after*
	// the addition of viewPane to the content pane.
	addKeyListener(viewPane);

        // We are listening to our own window closing events.
	addWindowListener(this);

	// print debug messages
//	viewPane.setDebug(1);

	// set basic parameters
	viewPane.setParams(
"#\n"+
"# MarvinView properties\n"+
"#\n"+
ViewParameterConstants.ROWS+"=3\n"+
ViewParameterConstants.COLS+"=2\n"+
ViewParameterConstants.LAYOUT+"0=:2:1:M:1:0:1:1:c:b:1:1:L:0:0:1:1:c:n:0:1\n"+
ViewParameterConstants.PARAMETERS +"0=:M:100:100:L:10b\n"+
//        UserSettings.ATOMSYMBOLS_VISIBLE+"=false\n"+
        ViewParameterConstants.VIEW_CARBON_VISIBILITY +"=off\n"+
"\n"+
"#\n"+
"# MarvinSketch properties also have to be specified here for the sketcher\n"+
"# windows that are launched from the viewer (using Edit/Structure).\n"+
"#\n"+
SketchParameterConstants.EXTRA_BONDS+"=arom,wedge\n");

	// set properties that can be modified runtime
	viewPane.setBorderWidth(1);
	viewPane.setEditable(2);
	viewPane.setM(0, readMol("CN1C=NC2=C1C(=O)N(C)C(=O)N2C"));
	viewPane.setL(0, "Caffeine");
	viewPane.setM(1, readMol("CCN(CC)C(=O)C1CN(C)C2CC3=CNC4=C3C(=CC=C4)C2=C1"));
	viewPane.setL(1, "Lysergide");
	viewPane.setM(2, readMol("OCC(O)C1OC(=O)C(O)=C1O"));
	viewPane.setL(2, "Vitamin C");
	viewPane.setM(3, readMol("CC(=O)Oc1ccccc1C(O)=O"));
	viewPane.setL(3, "Aspirin");
        viewPane.setM(4, readMol("[H]C(F)(Cl)Br"));
       
    }

    private Molecule readMol(String str) {
	try {
	    return MolImporter.importMol(str);
	} catch(IOException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(),
		    "Cannot read file", JOptionPane.ERROR_MESSAGE);
	    return null;
	}
    }

    /** Do nothing. */
    public void mouseEntered(MouseEvent e) { }

    /** Do nothing. */
    public void mouseExited(MouseEvent e) { }

    /** Do nothing. */
    public void mouseClicked(MouseEvent e) { }

    /** Do nothing. */
    public void windowOpened(WindowEvent e) { }

    /** Close the window. */
    public synchronized void windowClosing(WindowEvent e) {
        // save settings at exit if you answer yes
        String sep = System.getProperty("file.separator");
        int res = JOptionPane.showConfirmDialog(this, 
                    "Would you like to save settings before exit?",
                    "",
                    JOptionPane.YES_NO_OPTION);
        viewPane.setSaveIniEnabled(res == JOptionPane.YES_OPTION);
        // close all Marvin window (and save properties if saveIniEnabled 
        //is not false
        viewPane.exit();
        if(res == JOptionPane.YES_OPTION) {
            System.err.println("saved properties into "+propfilepath);
        }
        System.err.println("viewer is destroyed");
	setVisible(false);
	getContentPane().removeAll();
	dispose();
	System.exit(0);
    }

    /** Do nothing. */
    public void windowClosed(WindowEvent e) { }

    /** Do nothing. */
    public void windowIconified(WindowEvent e) { }

    /** Do nothing. */
    public void windowDeiconified(WindowEvent e) { }

    /** Do nothing. */
    public void windowActivated(WindowEvent e) { }

    /** Do nothing. */
    public void windowDeactivated(WindowEvent e) { }

    /** Handle menu events. */
    public void actionPerformed(ActionEvent ev) {
	String cmd = ev.getActionCommand();
	if(cmd.equals("exit")) { 
	    windowClosing(null);
	} else if(cmd.equals("saveProperties")) {
            saveProperties();
        }
    }
    
    private void saveProperties() {
        // save properties into marvin.properties
        UserSettings settings = viewPane.getUserSettings();
        viewPane.setSaveIniEnabled(true);
        try {
            settings.save("Created by MViewSaveProperties at "+
                new java.util.Date());
            System.err.println("saved properties into "+propfilepath);
        } catch(java.io.IOException ioe) {
            System.err.println("Save preferences was failed.");
        }
    }

    /** Show the MarvinView demo window. */
    public static void main(String[] args) {
	final JFrame w = new MViewSaveProperties();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
}
