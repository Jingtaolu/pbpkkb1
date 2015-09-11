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
import chemaxon.marvin.view.ViewParameterConstants;
import chemaxon.marvin.common.UserSettings;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

/**
 * Example for customize MarvinView menu.
 *
 * @author  Tamas Vertse
 * @author  Peter Csizmadia
 * @version 5.2.5, 08/28/2009
 * @since Marvin 4.1 05/29/2006
 */
public class CustomMenuExample extends JFrame implements ActionListener, 
    MouseListener 
{

    private MViewPane viewpanel;
    private JPopupMenu custompopup;
    
    /** Creates a new instance of CustomMenuExample */
    public CustomMenuExample() {
        viewpanel = new MViewPane();
        getContentPane().add(viewpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set layout
        viewpanel.setParams(ViewParameterConstants.ROWS+"=3\n"+
            ViewParameterConstants.COLS+"=2\n"+
            ViewParameterConstants.BORDER+"=1\n"+
            ViewParameterConstants.VISIBLE_ROWS+"=2\n"+
            ViewParameterConstants.LAYOUT+"=:2:1:M:1:0:1:1:c:b:1:1:L:0:0:1:1:c:n:0:1\n"+
            ViewParameterConstants.PARAMETERS +"=:M:100:100:L:10b\n");
        UserSettings settings = viewpanel.getUserSettings();
        settings.setViewNavmode2d("rotZ");
        
        // create main menu
        JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	JMenu menu = new JMenu("File");
	menubar.add(menu);
        
	JMenuItem mi;
        mi = createMenuItem("Save All as Image", "saveAllImage", this);
        menu.add(mi);

        mi = createMenuItem("Exit","exit",this);
        menu.add(mi);
        
        menu = new JMenu("View");
	viewpanel.makeViewMenu(menu);
	menubar.add(menu);
        
        viewpanel.addHelpMenu(menubar);
        
        // fill cells
        viewpanel.setM(0, readMol("CN1C=NC2=C1C(=O)N(C)C(=O)N2C"));
	viewpanel.setL(0, "Caffeine");
	viewpanel.setM(1, readMol("CCN(CC)C(=O)C1CN(C)C2CC3=CNC4=C3C(=CC=C4)C2=C1"));
	viewpanel.setL(1, "Lysergide");
	viewpanel.setM(2, readMol("OCC(O)C1OC(=O)C(O)=C1O"));
	viewpanel.setL(2, "Vitamin C");
	viewpanel.setM(3, readMol("CC(=O)Oc1ccccc1C(O)=O"));
	viewpanel.setL(3, "Aspirin");
	viewpanel.setM(4, readMol("[H][C@@]12C=C[C@H](O)[C@@H]3OC4=C(O)C=CC5=C4[C@]13CCN(C)[C@@H]2C5"));
	viewpanel.setL(4, "Morphine");
	viewpanel.setM(5, readMol("CC(=O)C1=CCCC2CCC1N2"));
	viewpanel.setL(5, "Anatoxin");
        
        //create new custom popup menu
	custompopup = createPopupMenu();
	viewpanel.setPopupMenusEnabled(false); // set Marvin menu to disabled
	for(int i=0;i<viewpanel.getVisibleCellCount();i++) {
	    JComponent comp = viewpanel.getVisibleCellComponent(i);
	    comp.addMouseListener(this); // catch mouse events in all cells
	    comp.add(custompopup); // add new popup menu to each cell
	}
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
    
    /**
     * Creates a new menu item.
     * @param label the label
     * @param cmd the action command
     * @param l the action listener
     */
    private JMenuItem createMenuItem(String label, String cmd, ActionListener l)
    {
        JMenuItem mi = new JMenuItem(label);
        mi.setActionCommand(cmd);
        mi.addActionListener(l);
        return mi;
    }
    
    /** Creates a popup menu. */
    private JPopupMenu createPopupMenu() {        
	JPopupMenu menu = new JPopupMenu();
        JMenuItem mi;
	JMenu mu;
        mi = createMenuItem("Save All as Image", "saveAllImage", this);
        menu.add(mi);
        menu.add(new JSeparator());
        
        mu = new JMenu("View");
	viewpanel.makeViewMenu(mu);
	menu.add(mu);
	menu.add(new JSeparator());
	menu.add(viewpanel.addHelpMenu(menu));
        
	return menu;
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        final CustomMenuExample w = new CustomMenuExample();
        // pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
    
    /** 
     * Exports each molecule to image format and save it to a directory.
     * @param dir saves images into this directory
     */
    private void saveAllImage(File dir) {
        String fmt = "png";
        int n = viewpanel.getCellCount();
        for(int i = 0; i < n; i++) {
            Molecule mol = viewpanel.getM(i);
            String molname = viewpanel.getL(i);
            File f = new File(dir.getAbsolutePath()+ "/" + molname + "." + fmt);
            byte[] data = null;
            try {
                data = mol.toBinFormat(fmt);
            } catch(IllegalArgumentException iae) {
                System.err.println("Illegal argument is given for export");
            } catch(SecurityException se) {
                System.err.println(fmt + " export module cannot be loaded.");
            }
            if(data != null) {                
                try {
                    System.err.println(f.getAbsolutePath());
                    FileOutputStream fout = new FileOutputStream(f);
                    fout.write(data);
                    fout.flush();
                    fout.close();
                } catch(IOException ioe) {
                    System.err.println("I/O exception at file saving");
                }
            }
        }
    }

    /** Invoked when an action occurs. */
    public void actionPerformed(ActionEvent e) {        
        String cmd = e.getActionCommand();
	if(cmd.equals("exit")) {
	    System.exit(0);
	} else if(cmd.equals("saveAllImage")) {
            File dir = new File("myimages");
            if(!dir.exists()) {
                dir.mkdirs();
            }
            saveAllImage(dir);
        }
    }    
    
    /** Invoked when the mouse has been clicked on a component. */
    public void mouseClicked(MouseEvent e) {
    }
    
    /** Invoked when the mouse enters a component. */
    public void mouseEntered(MouseEvent e) {
    }
    
    /** Invoked when the mouse exits a component. */
    public void mouseExited(MouseEvent e) {
    }
    
    /** Invoked when a mouse button has been pressed on a component. */
    public void mousePressed(MouseEvent e) {
        if(e.isPopupTrigger()) {
            custompopup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    /** Invoked when a mouse button has been released on a component. */
    public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()) {
            custompopup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
}
