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
import java.awt.print.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;
import chemaxon.formats.MolFormatException;
import chemaxon.marvin.beans.MViewPane;
import chemaxon.marvin.beans.MView;
import chemaxon.marvin.MolPrinter;
import chemaxon.marvin.sketch.SketchParameterConstants;
import chemaxon.marvin.view.ViewParameterConstants;
import chemaxon.marvin.common.UserSettings;

/**
 * MarvinView example application.
 * @version 5.2.5, 08/28/2009
 * @author  Peter Csizmadia
 * @author  Tamas Vertse
 */
public class MViewExample extends JFrame implements WindowListener,
    ActionListener, Printable
{
    /** The MarvinView bean. */
    private MViewPane viewPane;

    /** Create the frame. */
    private MViewExample() {
        setTitle("MarvinView Example");
        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(viewPane = new MViewPane());
        
	JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	JMenu menu = new JMenu("File");
	menubar.add(menu);
	JMenuItem mi;
       	menu.add(mi = new JMenuItem("Print All"));
	mi.setActionCommand("printAll");
	mi.addActionListener(this);
       	menu.add(mi = new JMenuItem("Print Molecules"));
	mi.setActionCommand("printMols");
	mi.addActionListener(this);
	JMenu submenu = new JMenu("State");
	menu.add(submenu);
       	submenu.add(mi = new JMenuItem("Serialize"));
	mi.setActionCommand("serializeX");
	mi.addActionListener(this);
       	submenu.add(mi = new JMenuItem("Serialize all GUI components"));
	mi.setActionCommand("serializeAll");
	mi.addActionListener(this);
       	submenu.add(mi = new JMenuItem("Deserialize"));
	mi.setActionCommand("deserialize");
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
	viewPane.setDebug(1);

	// set basic parameters
	viewPane.setParams(
                "#\n"+
                "# MarvinView properties\n"+
                "#\n"+
                ViewParameterConstants.ROWS+"=2\n"+
                ViewParameterConstants.COLS+"=2\n"+
                ViewParameterConstants.LAYOUT+"0=:2:1:M:1:0:1:1:c:b:1:1:L:0:0:1:1:c:n:0:1\n"+
                ViewParameterConstants.PARAMETERS +"0=:M:100:100:L:10b\n"+
                "\n"+
                "#\n"+
                "# MarvinSketch properties also have to be specified here for the sketcher\n"+
                "# windows that are launched from the viewer (using Edit/Structure).\n"+
                "#\n"+
                SketchParameterConstants.EXTRA_BONDS+"=arom,wedge\n");

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
	viewPane.exit();
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
	} else if(cmd.equals("printAll")) {
	    Properties props = new Properties();
	    PrintJob prjob = Toolkit.getDefaultToolkit().getPrintJob(this,
		    "table", props);
	    if(prjob != null) {
		Graphics g = prjob.getGraphics();
		printAll(g, prjob.getPageDimension());
		prjob.end();
	    }
	} else if(cmd.equals("printMols")) {
	    PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintable(this);
	    if(job.printDialog()) {
		try {
		    job.print();
		} catch(Exception ex) {
		    JOptionPane.showMessageDialog(this,
			    "Failed printing process: " + ex.getMessage(),
			    "Failed printing process",
			    JOptionPane.ERROR_MESSAGE);
		}
	    }
	} else if(cmd.equals("serializeX")) {
	    saveBeanState(new File("MViewExample.ser"), true);
	} else if(cmd.equals("serializeAll")) {
	    saveBeanState(new File("MViewExample.ser"), false);
	} else if(cmd.equals("deserialize")) {
	    loadBeanState(new File("MViewExample.ser"));
	}
    }


    /**
     * Print the MViewPane.
     * @param g graphics
     * @param page page dimensions
     */
    private void printAll(Graphics g, Dimension page) {
	g.translate((page.width - viewPane.getSize().width)/2,
		    (page.height - viewPane.getSize().height)/2);
	viewPane.printAll(g);
    }

    /** Implements Printable interface to print the molecules.
     *  Called by PrinterJob.
     *  @param g the context into which the page is drawn
     *  @param pf the size and the orientation
     *  @param pageIndex the zero based index of the page to be drawn
     *  @exception PrinterException thrown when the print job is terminated
     */
    public int print(Graphics g, PageFormat pf, int pageIndex)
            throws PrinterException {
       //print only one page
       if(pageIndex >=1) return Printable.NO_SUCH_PAGE;
       Graphics2D g2 = (Graphics2D)g;
       printMols(g2,pf);
       return Printable.PAGE_EXISTS;
    }

    /**
     * Print molecules with the same magnification.
     * @param g the context into which the page is drawn
     * @param pf  the size and orientation of the page being drawn
     */
    private void printMols(Graphics2D g, PageFormat pf) {
        Dimension page = new Dimension((int)pf.getImageableWidth(),
                (int)pf.getImageableHeight());
        Dimension cell = new Dimension(4*page.width/9, 4*page.height/9);
        String[] mols = new String[4];
        MolPrinter printer = new MolPrinter();
        printer.setDispopts(viewPane.getDispopts());
        double minmag = Double.MAX_VALUE;
        for(int i=0; i<4; ++i) {
            mols[i] = viewPane.getM(i, "mol");
        try {
            printer.setMol(mols[i]);
        } catch(MolFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cannot convert molecule "+i+
            ": "+ex.getMessage(), "Cannot convert molecule",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        double mag = printer.maxScale(cell);
        if(mag < minmag)
            minmag = mag;
        }
        printer.setScale(minmag);
        int x0 = (int)pf.getImageableX() + page.width/2 - cell.width - 1;
        int y0 = (int)pf.getImageableY() + page.height/2 - cell.height - 1;
        g.setColor(Color.black);
        for(int i=0; i<4; ++i) {
            int x = x0 + (i&1)*(cell.width + 1);
            int y = y0 + ((i&2)>>1)*(cell.height + 1);
            g.drawRect(x, y, cell.width + 2, cell.height + 2);
	    try {
		printer.setMol(mols[i]);
	    } catch(MolFormatException ex) {
		ex.printStackTrace();
	    }
            printer.paint(g, new Rectangle(x+1, y+1, cell.width, cell.height));
        }
    }

    private void saveBeanState(File f, boolean usex) {
	try {
	    FileOutputStream out = new FileOutputStream(f);
	    ObjectOutputStream oos = new ObjectOutputStream(out);
	    if(usex) {
		oos.writeObject(new MView(viewPane)); // experimental!
	    } else {
		oos.writeObject(viewPane); // NOT RECOMMENDED!
		// Serializing MViewPane or MSketchPane directly is not
		// recommended:
		// - It saves the state of almost all GUI components used
		//     in Marvin, so you will get an extremely large file.
		// - Basic Swing and AWT components have different
		//     serialVersionUID in different Java versions, thus you
		//     will not be able to deserialize them in Java version n+1.
		//     For example, the Java 1.4 version of JComponent (an
		//     ancestor of MarvinPane!) is incompatible with its
		//     Java 1.5 version.
	    }
	    out.close();
	} catch(Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void loadBeanState(File f) {
	try {
	    FileInputStream in = new FileInputStream(f);
	    ObjectInputStream ois = new ObjectInputStream(in);
	    Object o = ois.readObject();
	    MViewPane newPane;
	    if(o instanceof MView) {
		newPane = ((MView)o).getMViewPane();
	    } else if(o instanceof MViewPane) {
		newPane = (MViewPane)o;
	    } else {
		System.err.println("Cannot deserialize " + o);
		return;
	    }
	    in.close();
	    viewPane.closeWindows();
	    getContentPane().remove(viewPane);
	    getContentPane().add(viewPane = newPane);
	    getContentPane().validate();
	    viewPane.repaint();
	} catch(FileNotFoundException ex) {
	    System.err.println(f.getPath()+" not found");
	} catch(Exception ex) {
	    ex.printStackTrace();
	}
    }

    /** Show the MarvinView demo window. */
    public static void main(String[] args) {
	final JFrame w = new MViewExample();

	// pack and show the window in the Swing thread to avoid deadlocks
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		w.pack();
		w.setVisible(true);
	    }
	});
    }
}
