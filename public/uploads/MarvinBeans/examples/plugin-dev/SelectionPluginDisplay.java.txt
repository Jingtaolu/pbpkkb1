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
import javax.swing.*;
import java.util.Properties;

import chemaxon.marvin.plugin.CalculatorPlugin;
import chemaxon.marvin.plugin.CalculatorPluginDisplay;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;

/**
 * Plugin display for showing selection with statistical data.
 *
 * @author Nora Mate
 */
public class SelectionPluginDisplay extends CalculatorPluginDisplay {

    /** <code>true</code> if selection molecule display */
    private boolean selectionDisplay = true;

    /** <code>true</code> if data display */
    private boolean dataDisplay = true;

    /**
     * Returns <code>true</code> if results for more molecules can be displayed
     * in a single component, <code>false</code> if each molecule should be displayed
     * in a separate component. This implementation returns <code>false</code>.
     * @return <code>false</code>
     */
    public boolean isMultipleDisplay() {
	return false;
    }

    /**
     * Sets display parameters. 
     * @param params is the parameter table
     */
    public void setParameters(Properties params) {
	String str = params.getProperty("display");
	if (str != null) {
	    str = str.toLowerCase();
	    selectionDisplay = (str.indexOf("selection") != -1);
	    dataDisplay = (str.indexOf("data") != -1);
	} else {
	    selectionDisplay = true;
	    dataDisplay = true;
	}
    }

    /**
     * Returns the result component for display.
     * The component displays pKa values and microspecies distribution chart.
     * @return the result component 
     * @throws PluginException on error
     * @since Marvin 4.0
     */
    public Component getResultComponent() throws PluginException {
	Component comp1 = selectionDisplay ? super.getResultComponent() : null;
	Component comp2 = dataDisplay ? getDataComponent() : null;
	if (comp1 == null) {
	    return comp2;
	} 
	if (comp2 == null) {
	    return comp1;
	}
        JSplitPane spane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, comp1, comp2);
	spane.setOneTouchExpandable(true);
	return spane;
    }

    /**
     * Returns the data component.
     * @return the data component
     */
    private Component getDataComponent() {
	SelectionPlugin plugin = (SelectionPlugin) getPlugin();
	Molecule mol = plugin.getMolecule();
	Molecule sel = plugin.getSelection();
	String header = "\t\tMolecule\tSelection\tPercent (%)";
	int molatomcount = mol.getAtomCount();
	int selatomcount = sel.getAtomCount();
	String data1 = "Atom count\t\t"+
	    molatomcount+"\t"+
	    selatomcount+"\t"+
	    plugin.format(100.0*((double)selatomcount)/((double)molatomcount));
	String data2 = "";
	if (!mol.isReaction() && !sel.isReaction()) {
	    double molmass = mol.getMass();
	    double selmass = sel.getMass();
	    data2 = "Mass\t\t"+
		plugin.format(molmass)+"\t"+
		plugin.format(selmass)+"\t"+
		plugin.format(100.0*selmass/molmass);
	}
	String text = header+"\n"+data1+"\n"+data2;
	JTextArea textArea = new JTextArea(text);
	textArea.setEditable(false);
	return createScrollPane(textArea);
    }

    /**
     * Returns scroll pane containing the given component,
     * sets borders accordingly.
     * @param comp is the component
     * @return the wrapper scroll pane
     */
    static private JScrollPane createScrollPane(JComponent comp) {
	comp.setBackground(Color.white);
	JScrollPane sp = new JScrollPane(comp);
	//sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	int scrollbarwidth = sp.getVerticalScrollBar().getMaximumSize().width;
	comp.setBorder(BorderFactory.createEmptyBorder(0,0,0,scrollbarwidth+10));	
	return sp;
    }
}
