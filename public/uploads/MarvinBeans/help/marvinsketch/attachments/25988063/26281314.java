/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
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
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.beans.*;
import chemaxon.marvin.view.ViewParameterConstants;
import chemaxon.marvin.sketch.SketchParameterConstants;

/**
 * MarvinView example application with custom popup menu.
 * @version 5.2.5, 08/28/2009
 * @since   Marvin 3.0, 01/13/2003
 * @author  Tamas Vertse
 * @author  Peter Csizmadia
 */
public class MViewPopupExample extends JFrame implements WindowListener,
	ActionListener, MouseListener {

    /** The MarvinView bean. */
    private MViewPane viewPane;
    private JTextArea textinfo;
    private JPopupMenu custompopup;
	private int lastselectedindex = -1;
    /** Create the frame. */
    private MViewPopupExample() {
	setTitle("MarvinView Example with custom popup menu");

	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(viewPane = new MViewPane(),BorderLayout.CENTER);
	getContentPane().add(textinfo = new JTextArea(3,30),BorderLayout.SOUTH);
	textinfo.setBackground(Color.white);
	textinfo.setEditable(false);
		
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
	viewPane.setParams(
"#\n"+
"# MarvinView properties\n"+
"#\n"+
ViewParameterConstants.ROWS+"=2\n"+
ViewParameterConstants.COLS+"=2\n"+
ViewParameterConstants.VISIBLE_ROWS+"=1\n"+
ViewParameterConstants.LAYOUT+"0=:2:1:M:1:0:1:1:c:b:1:1:L:0:0:1:1:c:n:0:1\n"+
ViewParameterConstants.PARAMETERS +"0=:M:100:100:L:10b\n"+
"\n"+
"#\n"+
"# MarvinSketch properties also have to be specified here for the sketcher\n"+
"# windows that are launched from the viewer (using Edit/Structure).\n"+
"#\n"+
SketchParameterConstants.EXTRA_BONDS+"=arom,wedge\n");
 
        // create main menu
        JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	JMenu menu = new JMenu("File");
	menubar.add(menu);
	JMenuItem mi;
	menu.add(mi = new JMenuItem("Exit"));
	mi.setActionCommand("exit");
	mi.addActionListener(this);
        
        menu = new JMenu("View");
	viewPane.makeViewMenu(menu);
	menubar.add(menu);

	// set properties that can be modified runtime
	viewPane.setBorderWidth(1);
	viewPane.setEditable(2);
	viewPane.setBackground(new Color(0xcccccc));
	viewPane.setMolbg(new Color(0xffffff));
	viewPane.setM(0, readMol("caffeine.mol"));
	viewPane.setL(0, "Caffeine");
	viewPane.setM(1, readMol("lysergide.csmol"));
	viewPane.setL(1, "Lysergide");
	viewPane.setM(2, readMol("vitaminc.csmol"));
	viewPane.setL(2, "Vitamin C");
	viewPane.setM(3, readMol("aspirin.csmol"));
	viewPane.setL(3, "Aspirin");
	
	//create new custom popup menu
	custompopup = createPopupMenu();
	viewPane.setPopupMenusEnabled(false);
	for(int i=0;i<viewPane.getVisibleCellCount();i++) {
	    JComponent comp = viewPane.getVisibleCellComponent(i);
	    comp.addMouseListener(this);
	    comp.add(custompopup);
	}
    }

    private Molecule readMol(String file) {
	try {
	    MolImporter mi = new MolImporter(new File(file), null);
	    return mi.read();
	} catch(IOException ex) {
	    JOptionPane.showMessageDialog(this, ex.toString(),
		    "Cannot read file", JOptionPane.ERROR_MESSAGE);
	    return null;
	}
    }

    private JPopupMenu createPopupMenu() {
	JPopupMenu jpop = new JPopupMenu();
	JMenu mu = new JMenu("View");
	viewPane.makeViewMenu(mu);
	jpop.add(mu);
	jpop.add(viewPane.addHelpMenu(jpop));
	return jpop;
    }

    /** Shows custom popup menu. */
    private void maybeShowPopup(MouseEvent ev) {
       if(ev.isPopupTrigger()) {
            custompopup.show(ev.getComponent(), ev.getX(), ev.getY());
        } 
    }
	
    /** Displays a message if selected index was changed. */
    private void checkSelectedIndex() {
	int selectedindex = viewPane.getSelectedIndex();
	if(lastselectedindex != selectedindex) {
	    String msg = "selectedIndex was changed:"+
		"\nold value="+lastselectedindex+
		"\nnew value="+selectedindex;
		lastselectedindex = selectedindex;
	    textinfo.setText(msg);
	}
    }

    public void mouseEntered(MouseEvent ev) {
	JComponent comp = (JComponent)ev.getSource();
	int cellindex = viewPane.getAbsoluteCellIndex(comp);
	if (cellindex == -1) {
	    return;
	}
	String molstr = viewPane.getM(cellindex,"smiles");
	String label = viewPane.getL(cellindex);
	textinfo.setText(cellindex+". molecule:\n"+label+"\n"+molstr);
    }

    /** Do nothing. */
    public void mouseExited(MouseEvent ev) { }

    /** Do nothing. */
    public void mouseClicked(MouseEvent ev) {
	//verify the index of the selected cell
	checkSelectedIndex();
    }
    
    /** Press a mouse button. */
    public void mousePressed(MouseEvent ev) {
	maybeShowPopup(ev);
    }

    /** Release a mouse button. */
    public void mouseReleased(MouseEvent ev) {
	maybeShowPopup(ev);
    }

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
	String cmd = ev.getActionCommand();
	if(cmd.equals("exit")) {
	    windowClosing(null);
	}
    }

    /** Show the MarvinView demo window. */
    public static void main(String[] args) {
	final JFrame w = new MViewPopupExample();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
}