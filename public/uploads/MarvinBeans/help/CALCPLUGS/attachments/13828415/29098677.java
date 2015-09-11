/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.util.Properties;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import chemaxon.marvin.beans.*;
import chemaxon.marvin.plugin.*;
import chemaxon.marvin.plugin.gui.*;
import chemaxon.struc.Molecule;
import chemaxon.license.LicenseException;
import chemaxon.license.LicenseManager;

/**
 * SelectionPlugin tester.
 * Shows how to insert a plugin into a customized sketcher application.
 * This application adds two plugin-buttons: one for showing the 
 * parameter panel and another one for running the plugin and
 * showing the result component.
 * The plugin is simple, it is designed mainly to show the way to write
 * calculator plugins.
 *
 * @see SelectionPlugin
 * @see SelectionPluginDisplay
 * @see chemaxon.marvin.plugin.CalculatorPlugin
 * @see chemaxon.marvin.plugin.CalculatorPluginDisplay
 * @version Marvin 4.0.2, 10/08/2005
 * @author Nora Mate
 * @since Marvin 4.0
 */
public class SelectionPluginTest extends JFrame
    implements WindowListener, ActionListener
{
    private static String PLUGIN_KEY = "selection";
    private static String PLUGIN_CONFIG = "$SelectionPlugin$SelectionPlugin.jar";
    private MSketchPane sketchPane;
    private JButton theParameterButton;
    private JButton theRunButton;
    private Molecule mol = null;
    private String molString = null;
    private PluginFactory thePluginFactory = null;
    private OptionsPane optionsPane = null;

    /** Initialize MarvinSketch. */
    private SelectionPluginTest() {

	// create PluginFactory, load plugin config 
	initPlugin();

	setTitle("Selection plugin test");

	sketchPane = new MSketchPane();
	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	Container contentPane = getContentPane();
	contentPane.setLayout(gbl);

	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.fill = GridBagConstraints.BOTH;
	gbl.setConstraints(sketchPane, gbc);
	contentPane.add(sketchPane);
	sketchPane.setPreferredSize(new Dimension(430, 400));

	gbc.gridy = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	theParameterButton = new JButton("Set parameters");
	theParameterButton.addActionListener(this);
	theRunButton = new JButton("Run plugin");
	theRunButton.addActionListener(this);
	JPanel panel = new JPanel();
	panel.add(theParameterButton);
	panel.add(theRunButton);
	gbl.setConstraints(panel, gbc);
	contentPane.add(panel);

	addWindowListener(this);
    }

    /**
     * Creates {@link chemaxon.marvin.plugin.PluginFactory} object.
     * Loads plugin config (plugin name and JAR info).
     */
    private void initPlugin() {
	thePluginFactory = new PluginFactory();

	// set this object for handling "OK button pressed" events
	thePluginFactory.setOKActionListener(this);

	// set parent component for positioning options pane
	thePluginFactory.setParentComponent(this);

	Properties pluginProps = new Properties();
	pluginProps.put(PLUGIN_KEY, PLUGIN_CONFIG);
	try {
	    thePluginFactory.load(pluginProps);
	} catch (PluginException e) {
	    System.err.println("Could not load plugin config: "+PLUGIN_CONFIG);
	    System.err.println(e);
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    /** Does nothing */
    public void windowOpened(WindowEvent ev) { }

    /** Does nothing */
    public void windowClosed(WindowEvent ev) { }

    /** Closes the window. */
    public void windowClosing(WindowEvent ev) {
	thePluginFactory.saveParameterSettings();
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

    /** Menu action performed. */
    public void actionPerformed(ActionEvent ev) {
	Object t = ev.getSource();
	if (t == theParameterButton) {
	    showOptionsPane();
	} else if (t == theRunButton) {
	    runPlugin();
	} else if (ev.getActionCommand().equals("plugin_"+PLUGIN_KEY)) {
	    runPlugin();
	    optionsPane.setActionsEnabled(true);
	}
    }

    /**
     * Shows plugin parameters in a "OK/Cancel" options pane.
     */
    private void showOptionsPane() {

	// now index will be 0 - just added to show how to fetch the 
	// plugin index in case of more than one plugins
	int index = thePluginFactory.getPluginIndex(PLUGIN_KEY);

	// get options pane
	try {
	    optionsPane = thePluginFactory.getOptionsPane(index);
	} catch (PluginException e) {
	    sketchPane.showErrorDialog(e.getMessage(), e);
	}

	// show options pane
	if (optionsPane != null) {
	    optionsPane.setVisible(true);
	}
    }

    /**
     * Runs plugin and shows result component.
     */
    private void runPlugin() {

	CalculatorPluginDisplay display = null;
	try {
	    Molecule mol = sketchPane.getMol();
	    if (mol != null) {	

		// now index will be 0 - just added to show how to fetch the 
		// plugin index in case of more than one plugins
		int index = thePluginFactory.getPluginIndex(PLUGIN_KEY);

		// get plugin and display objects
		CalculatorPlugin plugin = thePluginFactory.getPlugin(index);
		display = thePluginFactory.getDisplay(index);

		// load options pane if not already loaded
		if (optionsPane == null) {
		    optionsPane = thePluginFactory.getOptionsPane(index);
		}

		// fetch plugin parameters from options pane
		Properties parameters = new Properties();
		optionsPane.getParameters(parameters);

		// set parameters
		if (parameters != null) {
		    plugin.setParameters(parameters);
		    display.setParameters(parameters);
		}

		// check if plugin can handle the input molecule
		plugin.checkMolecule(mol);

		// set molecule: standardize if needed, 
		// use standardized version for display
		plugin.setMolecule(mol, true, false);

		// run the plugin
		boolean ok = plugin.run();

		// store results in display
		display.store();

		// get result component
		Component component = ok ? 
		    display.getResultComponent() : 
		    display.getErrorComponent();

		// display component
		displayComponent(component, "Results");
	    }
	} catch (PluginException e) {
            if(e.getException() instanceof LicenseException) {
                JOptionPane.showMessageDialog(this, LicenseManager.getLicenseExceptionPanel(
                        (LicenseException)e.getException()
                ));
            }
            else {
		sketchPane.showErrorDialog(e.getMessage(), e);
	    }
	} finally {
	    if (display != null) {
		display.clear();
	    }
	}
    }

    /**
     * Displays result component in frame.
     * @param component the component
     * @param title the frame title
     */
    private void displayComponent(Component component, String title) {
	if (component instanceof JOptionPane) {
	    JDialog dialog = ((JOptionPane) component).createDialog(this, title);
	    dialog.setVisible(true);
	    return;
	}

	JFrame frame = null;
	if (component instanceof JFrame) {
	    frame = (JFrame) component;
	} else {
	    frame = new JFrame(title);
	    frame.getContentPane().add(component);
	}
	frame.pack();
        frame.setLocationRelativeTo(this);
	frame.setVisible(true);
    }

    /** Run the application. */
    public static void main(String[] args) {
	final JFrame w = new SelectionPluginTest();

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
