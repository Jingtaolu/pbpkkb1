/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.util.Arrays;
import java.util.Properties;

import chemaxon.struc.*;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.marvin.plugin.CalculatorPlugin;
import chemaxon.license.LicenseHandler;

/**
 * Test plugin.
 * Creates molecule from selection.
 *
 * @author Nora Mate
 */
public class SelectionPlugin extends CalculatorPlugin {

    /** 
     * True if original atom indexes should be set
     * in atom extra lables in selection molecule.
     */
    private boolean showIndexMap = true;

    /** The input molecule. */
    private Molecule mol = null;

    /** The selected part of the input molecule. */
    private Molecule sel = null;

    /** Maps atom indexes in 'sel' to corresponding atom indexes in 'mol'. */
    private int[] selind2molind = null;

    /** 
     * Maps atom indexes in 'mol' to corresponding atom indexes in 'sel',
     * sets <code>-1</code> for unselected atoms. 
     */
    private int[] molind2selind = null;

    /**
     * Constructor.
     */
    public SelectionPlugin() {
    }

    /**
     * Returns the name of the plugin to be used by the License Manager.
     * @return identifier of the product
     */
    public String getProductName() {
	return LicenseHandler.FREE_PLUGIN;
    }

    /**
     * Returns <code>true</code> if the plugin handles multifragment molecules,
     * <code>false</code> otherwise. 
     * @return <code>true</code>
     */
    public boolean handlesMultiFragmentMolecules() {
	return true;
    }

    /**
     * Checks the input molecule.
     * Throws exception if the molecule molecule contains R-groups.
     * @param mol is the input molecule
     * @throws PluginException with error message for the user if the molecule is refused
     */
    public void checkMolecule(Molecule mol) throws PluginException {
	if (isRgrouped(mol)) {
	    throw new PluginException("Selection plugin is not implemented for molecules with R-groups.");
	}
    }

    /**
     * Sets the input parameters for the plugin:
     * <ul>
     * <li>display: selection,data
     * <li>showIndexMap: true or false (true if original atom indexes should be set
     * in atom extra lables in selection molecule)
     * <li>precision: 0-8 or inf (for data display only)
     * (number of displayed fractional digits, inf for unrounded value)
     * </ul>
     * @param params is the parameter table
     * @throws PluginException on error
     */
    public void setParameters(Properties params) throws PluginException {
	showIndexMap = !"false".equalsIgnoreCase(params.getProperty("showIndexMap"));
	String pr = params.getProperty("precision");
	setDoublePrecision(pr);
    }

    /**
     * Sets the input molecule.
     * @param mol is the input molecule
     * @throws PluginException on error
     */
    protected void setInputMolecule(Molecule mol) throws PluginException {
	this.mol = mol.getSimplifiedMolecule();
	this.sel = null;
    }

    /**
     * Runs the plugin. 
     * Extracts and stores the selection.
     * @return <code>true</code> 
     * @throws PluginException on error
     */
    public boolean run() 
	throws PluginException {

	// check license - commented because LicenseHandler would not allow
        // running with this sample "Selection Plugin" license
	// checkLicense();

	int selcount = getSelectedAtomCount();
	if (selcount == 0) {
	    return false;
	}

	sel = (Molecule) mol.newInstance();
	int count = mol.getAtomCount();

	MolAtom[] selatoms = new MolAtom[count];	
	for (int i=0; i < count; ++i) {
	    MolAtom atom = mol.getAtom(i);
	    if (atom.isSelected()) {
		MolAtom selatom = (MolAtom) atom.clone();
		selatom.setSelected(false);
		sel.add(selatom);
		selatoms[i] = selatom;
	    } 
	}

	for (int j=mol.getBondCount()-1; j >= 0; --j) {
	    MolBond bond = mol.getBond(j);
	    MolAtom atom1 = bond.getAtom1();
	    MolAtom atom2 = bond.getAtom2();
	    if (atom1.isSelected() && atom2.isSelected()) {
		int i1 = mol.indexOf(atom1);
		int i2 = mol.indexOf(atom2);
		MolBond selbond = new MolBond(selatoms[i1], selatoms[i2]);
		selbond.setType(bond.getType());
		sel.add(selbond);
	    }
	}

	molind2selind = new int[count];
	selind2molind = new int[selcount];
	Arrays.fill(molind2selind, -1);
	for (int i=0; i < count; ++i) {
	    if (selatoms[i] != null) {
		int selind = sel.indexOf(selatoms[i]);
		selind2molind[selind] = i;
		molind2selind[i] = selind;
	    }
	}

	return true;
    }

    /**
     * Returns the calculation warning information message. 
     * @return the calculation warning information message
     */
    public String getWarningMessage() {
	return (sel == null) ? "No selection." : "";
    }

    /**
     * Returns the number of selected atoms in the input molecule.
     * @return the number of selected atoms
     */
    private int getSelectedAtomCount() {
	int selcount = 0;
	for (int i=mol.getAtomCount()-1; i >= 0; --i) {
	    if (mol.getAtom(i).isSelected()) {
		++selcount;
	    }
	}
	return selcount;
    }

    /**
     * Returns the input molecule.
     * @return the input molecule
     */
    public Molecule getMolecule() {
	return mol;
    }

    /**
     * Returns the selection molecule.
     * @return the selection molecule
     */
    public Molecule getSelection() {
	return sel;
    }

    /**
     * Returns the original atom index corresponding to the given atom index 
     * in the selection.
     * @param selind is an atom index in the selection
     * @return the corresponding atom index in the original molecule
     */
    public int getMoleculeAtomIndex(int selind) {
	return selind2molind[selind];
    }

    /**
     * Returns the selection atom index corresponding to the given atom index 
     * in the original molecule, <code>-1</code> if the aotm is not selected.
     * @param molind is an atom index in the original molecule
     * @return the corresponding atom index in the selection
     */
    public int getSelectionAtomIndex(int molind) {
	return molind2selind[molind];
    }

    /**
     * Returns the result molecule for display.
     * If parameter 'showIndexMap' is set then sets original atom indexes 
     * in extra atom labels: see
     * ({@link chemaxon.struc.MolAtom#getExtraLabel()}).
     * @return the result molecule
     * @throws PluginException on error
     */
    public Molecule getResultMolecule() throws PluginException {
	if (sel == null) {
	    return null;
	}
	Molecule m = (Molecule) sel.clone();
	if (showIndexMap) {
	    for (int i=m.getAtomCount()-1; i >= 0; --i) {
		int molind = selind2molind[i];
		m.getAtom(i).setExtraLabel(""+(molind+1));
		m.getAtom(i).setExtraLabelColor(BLUE);
	    }
	}
	return m;
    }

    /**
     * Prevents default standardization: does nothing.
     * @param mol is the molecule to be standardized
     */
    public void standardize(Molecule mol) {
    }
}
