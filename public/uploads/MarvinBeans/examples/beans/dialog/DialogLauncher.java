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
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import javax.swing.*;

import chemaxon.struc.*;
import chemaxon.formats.*;
import chemaxon.marvin.beans.*;

/**
 * Dialog launcher.
 * @version 4.0.2, 10/08/2005
 * @since   Marvin 2.6, 09/14/2000
 * @author Peter Csizmadia
 * @author Tamas Vertse
 */
public class DialogLauncher extends JFrame
    implements WindowListener, ActionListener
{
	private static String smiles = "CN1C=NC2=C1C(=O)N(C)C(=O)N2C";
	
	/** The MarvinSketch and MarvinView dialogs are modal if true. */
    private boolean modalDialogs;
    
    public DialogLauncher() {
        setTitle("Marvin Dialog Launcher");
        addWindowListener(this);
	Container contentPane = getContentPane();
	contentPane.setLayout(new GridLayout(4, 1));
	JCheckBox cb;
	JButton b;
	contentPane.add(b = new JButton("MarvinSketch Dialog"));
	b.addActionListener(this);
	b.setActionCommand("sketch");
	contentPane.add(b = new JButton("MarvinView Dialog"));
	b.addActionListener(this);
	b.setActionCommand("view");
	contentPane.add(cb = new JCheckBox("Modal"));
	cb.addActionListener(this);
	cb.setActionCommand("modal");
	contentPane.add(b = new JButton("Exit"));
	b.addActionListener(this);
	b.setActionCommand("exit");
    }

    /** Menu action performed. */
    @Override
	public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
	String cmd = ev.getActionCommand();
        if(cmd.equals("modal")) {
	    modalDialogs = ((JCheckBox)src).isSelected();
	} else if(cmd.equals("sketch")) {
	    JDialog dialog = new JDialog(this, 
	    		((modalDialogs)? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS));
	    dialog.setTitle("MarvinSketch Dialog");
	    MSketchPane sketcher = new MSketchPane();
	    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    // Keyboard events are received by the dialog window but
	    dialog.addKeyListener(sketcher); // processed by the bean
	    dialog.getContentPane().add(sketcher);
	    dialog.pack();
	    dialog.setVisible(true);
	} else if(cmd.equals("view")) {
	    JDialog dialog = new JDialog(this,
	    		((modalDialogs)? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS));
	    dialog.setTitle("MarvinView Dialog");
	    MViewPane viewer = new MViewPane();
	    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    try {
		Molecule mol = MolImporter.importMol(smiles);
		viewer.setM(0, mol);
	    } catch(Exception ex) {
		ex.printStackTrace();
	    }
	    // Keyboard events are received by the dialog window but
	    dialog.addKeyListener(viewer); // processed by the bean
	    dialog.getContentPane().add(viewer);
	    dialog.pack();
	    dialog.setVisible(true);
	} else if(cmd.equals("exit")) {
	    System.exit(0);
	}
    }

    /** Does nothing */
    @Override
	public void windowOpened(WindowEvent ev) { }

    /** Does nothing */
    @Override
	public void windowClosed(WindowEvent ev) { }

    /** Closes the window. */
    @Override
	public void windowClosing(WindowEvent ev) {
	System.exit(0);
    }

    /** Does nothing */
    @Override
	public void windowIconified(WindowEvent ev) { }

    /** Does nothing */
    @Override
	public void windowDeiconified(WindowEvent ev) { }

    /** Does nothing */
    @Override
	public void windowActivated(WindowEvent ev) { }

    /** Does nothing */
    @Override
	public void windowDeactivated(WindowEvent ev) { }

    public static void main(String[] args) {
	final JFrame w = new DialogLauncher();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
		public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
}
