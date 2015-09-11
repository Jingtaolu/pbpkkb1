/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MViewPane;

import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

/**
 * Embeds the MViewPane bean component in a Swing container.
 * MViewPane has a single cell to which a molecule can be loaded by
 * the created open menu item.
 * The built-in View menu and Pop-up menus of MarvinView are used.
 *
 * @author  Judit Vasko-Szedlar
 * @author  Tamas Vertse
 * @author  Peter Csizmadia
 * @version 5.2.5, 08/28/2009
 * @since   Marvin 3.5.2, 12/21/2004
 */
public class ViewSimple extends JPanel {

    /** The MarvinView bean. */
    private MViewPane viewPane;
    private JFileChooser fileChooser = null;

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
        frame.setTitle("Simple MarvinView Bean Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ViewSimple viewSimple = new ViewSimple();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(viewSimple, BorderLayout.CENTER);

        viewSimple.addMenuBar(frame);

        frame.pack();
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public ViewSimple() {
        // Creating the MarvinView JavaBean component.
        // The component is ready and can simply added to
        // any Swing container.
        viewPane = new MViewPane();
        setLayout(new BorderLayout());
        add(viewPane, BorderLayout.CENTER);
    }

    public void addMenuBar(JFrame frame) {
        // Creates the menu bar
        JMenuBar menubar = new JMenuBar();
	frame.setJMenuBar(menubar);
        // The File menu will contain the Open and Exit items.
	JMenu fileMenu = new JMenu("File");
        fileMenu.add(createOpenMenuItem());
        fileMenu.add(createExitMenuItem(frame));
        menubar.add(fileMenu);
        // The built-in View menu of the bean is added to the menubar.
        JMenu viewMenu = new JMenu("View");
        viewPane.makeViewMenu(viewMenu);
        menubar.add(viewMenu);
    }

    private JMenuItem createOpenMenuItem() {
        JMenuItem openMI = new JMenuItem("Open");
        openMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(fileChooser == null) {
                    fileChooser = new JFileChooser();
                }
                if((fileChooser.showOpenDialog(viewPane)
                        == JFileChooser.APPROVE_OPTION)
                        && (fileChooser.getSelectedFile().canRead())) {
		    // Load a molecule from the chosen file.
		    Molecule m = null;
		    try {
			m = readMol(fileChooser.getSelectedFile());
		    } catch(IOException ex) {
		    	JOptionPane.showMessageDialog(ViewSimple.this,
				ex.toString(), "Cannot read file",
				JOptionPane.ERROR_MESSAGE);
		    }
                    // With this method a molecule can be set to MViewPane.
                    // The advantage of using this is its simplicity.
                    // The disadvantage is the limitation in funtionality,
                    // because additional cells are not created in case of
                    // setting a multi-molecule file.
		    viewPane.setM(0, m);
                }
            }
        });
        return openMI;
    }

    private static Molecule readMol(File f) throws IOException {
	MolImporter mi = new MolImporter(f, null);
	return mi.read();
    }

    private JMenuItem createExitMenuItem(final JFrame frame) {
        JMenuItem exitMI = new JMenuItem("Exit");
        exitMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                windowClosing(frame);
            }
        });
        return exitMI;
    }

    // Saves the changes made by using the View menu and closes the window.
    private synchronized void windowClosing(final JFrame frame) {
        // Save settings of SimpleViewer using UserSettings
        try {
            viewPane.getUserSettings().save("Settings of SimpleViewer");
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
	viewPane.exit();
	frame.dispose();
    }

}
