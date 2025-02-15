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
import java.beans.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import chemaxon.marvin.beans.*;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

/**
 * MarvinView event tester.
 * @version 5.2.5, 08/28/2009
 * @author  Peter Csizmadia
 * @since Marvin 2.8
 */
public class ViewEventTest extends JFrame
    implements WindowListener, PropertyChangeListener
{
    private JTextArea textArea;

    /** Initialize MarvinView. */
    private ViewEventTest() {
	setTitle("MarvinView");

	MViewPane viewPane = new MViewPane();
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	Container contentPane = getContentPane();
	contentPane.setLayout(gbl);

	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.fill = GridBagConstraints.BOTH;
	gbl.setConstraints(viewPane, gbc);
	contentPane.add(viewPane);
	viewPane.addPropertyChangeListener(this);
	viewPane.setPreferredSize(new Dimension(300, 300));
	viewPane.setM(0, readMol("caffeine.mol"));
        viewPane.setEditable(2);
	gbc.gridy = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	textArea = new JTextArea(10, 80);
	JScrollPane scrollPane = new JScrollPane(textArea,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollPane.setPreferredSize(new Dimension(300, 100));
	gbl.setConstraints(scrollPane, gbc);
	contentPane.add(scrollPane);
       
        // Main menu is required.
        // Only those shortcuts work whose menu items are in the 
        // main menu.
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu menu;
        menu = new JMenu("Edit"); 
        viewPane.makeEditMenu(menu);
        menubar.add(menu);
        addWindowListener(this);
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

    /** Does nothing */
    public void windowOpened(WindowEvent ev) { }

    /** Does nothing */
    public void windowClosed(WindowEvent ev) { }

    /** Closes the window. */
    public void windowClosing(WindowEvent ev) {
	System.exit(0);
    }

    /** Does nothing */
    public void windowIconified(WindowEvent ev) { }

    /** Does nothing */
    public void windowDeiconified(WindowEvent ev) { }

    /** Does nothing */
    public void windowActivated(WindowEvent ev) { }

    /** Does nothing */
    public void windowDeactivated(WindowEvent ev) { }

    /** Change title if the "file" property has been changed. */
    public void propertyChange(PropertyChangeEvent ev) {
	String name = ev.getPropertyName();
	System.out.println("propertyChange: name="+name+
			   " value="+ev.getNewValue());
	if(textArea != null) {
	    textArea.setCaretPosition(textArea.getDocument().getLength());
	    textArea.append("propertyChange: name="+name+
		    " value="+ev.getNewValue()+"\n");
	}
    }

    /** Run the application. */
    public static void main(String[] args) {
	final JFrame w = new ViewEventTest();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
		w.requestFocus();
	    }
	});
    }
}
