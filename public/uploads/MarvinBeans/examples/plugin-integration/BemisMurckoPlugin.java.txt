/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 */

import chemaxon.marvin.plugin.CalculatorPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;
import chemaxon.license.LicenseHandler;

import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Plugin class to calculate Bemis-Murcko framework of input molecules.
 * 
 * @author Gabor Imre
 * @author Zsolt Mohacsi
 */
public class BemisMurckoPlugin extends CalculatorPlugin {
    
    static private String[] TYPE_RANGE = new String[] {
	"structure", "atomcount", "bondcount"
    };

    private Object[] types = new Object[] { "structure" };

    /** The input molecule. */
    private Molecule mol = null;
    
    /** The output format. */
    private String format = null;

    /**
     * Structural framework will produce single atom instead of empty structure.
     */
    private boolean keepSingleForAcyclic = true;

    /**
     * if structure contains multiple fragments then process only the largest 
     * one after removing explicit H atoms.
     */
    private boolean processOnlyLargestInputFragment = false;

    /**
     * if result contains multiple fragments then keep only the largest one. 
     */
    private boolean keepOnlyLargestResultFragment = false;
    
    /**
     * Returns the name of the plugin to be used by the License Manager.
     * @return identifier of the product
     */
    public String getProductName() {
	return LicenseHandler.FREE_PLUGIN;
    }

    /**
     * Sets the input molecule.
     * @param mol is the input molecule
     * @throws PluginException on error
     */
    protected void setInputMolecule(Molecule mol) throws PluginException {
	this.mol = mol;
    }

    /**
     * Sets the input parameters for the plugin.
     * Accepted parameters:
     * <ul>
     * <li><b>keepsingleatom</b>: [true|false] if set true then a single atom
     * will be assigned to acyclic fragments. if false then acyclic fragments
     * will be projected to empty structures
     * <li><b>lfin</b>: [true|false] if set true then the calculation will run
     * only on the largest input fragment. Note that this necessarily yields the
     * largest scaffold
     * <li><b>lfout</b>: [true|false] if set true then only the largest calculated
     * scaffold will be returned. Note that the returned scaffold not necessarily
     * calculated from the largest fragment.
     * <li><b>format</b>: format to use for string export
     * </ul>
     * @param params    parameter table
     * @throws PluginException on error
     */
    public void setParameters(Properties params) throws PluginException {
	String chtypes = params.getProperty("type");
	if (chtypes != null) {
	    StringTokenizer st = new StringTokenizer(chtypes, ",");
	    types = new Object[st.countTokens()];
	    int i = 0;
	    while (st.hasMoreTokens()) {
		String token = st.nextToken();
		checkType(token, TYPE_RANGE);
		types[i++] = token;
	    }
	}
	format = params.getProperty("format", "smiles");
	setKeepSingleAtomForAcyclic(params.getProperty("keepsingleatom", "true").equalsIgnoreCase("true"));
	setProcessOnlyLargestInputFragment(params.getProperty("lfin", "false").equalsIgnoreCase("true"));
	setKeepOnlyLargestResultFragment(params.getProperty("lfout", "false").equalsIgnoreCase("true"));
    }

    /**
     * Returns <code>true</code> if the plugin handles multifragment molecules,
     * <code>false</code> otherwise. This plugin handles multifragment molecules, 
     * so it returns <code>true</code>.
     * @return true because this plugin handles multifragment molecules
     */
    public boolean handlesMultiFragmentMolecules() {
	return true;
    }

    /**
     * Sets behavior on acyclic input.
     * @param b   if set true then a single atom will be assigned to acyclic 
     *            fragments. if false then acyclic fragments will be projected 
     *            to empty structures
     */
    public void setKeepSingleAtomForAcyclic(boolean b) {
	this.keepSingleForAcyclic = b;
    }

    /**
     * Set handling option for multifragment (disconnected) inputs.
     * @param b   if set true then the calculation will run only on the largest
     *            input fragment. Note that this necessarily yields the largest
     *            scaffold
     */
    public void setProcessOnlyLargestInputFragment(boolean b) {
	this.processOnlyLargestInputFragment = b;
    }

    /**
     * Set handling option for multifragment (disconnected) results. Note that
     * disconnected result can be caused by disconnected input or by further
     * modifications of the scaffold generation algorithm
     * @param b   if set true then only the largest calculated scaffold will be
     *            returned. Note that the returned scaffold not necessarily
     *            calculated from the largest fragment.
     */
    public void setKeepOnlyLargestResultFragment(boolean b) {
	this.keepOnlyLargestResultFragment = b;
    }

    /**
     * Runs the tool.
     * @return <code>true</code> 
     * @throws PluginException on error
     */
    public boolean run() throws PluginException {
	mol = generateBemisMurckoFramework(mol, processOnlyLargestInputFragment,
		keepOnlyLargestResultFragment, keepSingleForAcyclic);
	return true;
    }

    /**
     * Generates the Bemis-Murcko framework of the molecule.
     * @param mol is the input molecule
     * @param processOnlyLargestInputFragment if set true then the calculation 
     *        will run only on the largest input fragment
     * @param keepOnlyLargestResultFragment if set true then only the largest 
     *        calculated scaffold will be returned
     * @param keepSingleForAcyclic if set true then a single atom will be 
     *        assigned to acyclic fragments
     * @return the Bemis-Murcko framework of the molecule
     */
    private static Molecule generateBemisMurckoFramework(Molecule mol,
	    boolean processOnlyLargestInputFragment,
	    boolean keepOnlyLargestResultFragment,
	    boolean keepSingleForAcyclic) {

	if (processOnlyLargestInputFragment) {
	    mol = getLargestFragment(mol);
	}

	for (MolAtom atom : mol.getAtomArray()) {
	    atom.setAtno(6);
	    atom.setFlags(0);
	}

	for (MolBond bond : mol.getBondArray()) {
	    bond.setFlags(0);
	    bond.setType(1);
	}

	// remove side chains
	boolean found = true;
	while (found) {
	    found = false;
	    for (int i = 0; !found && i < mol.getAtomCount(); i++) {
		int bci = mol.getAtom(i).getBondCount();
		if (bci == 1) {
		    mol.removeAtom(i);
		    found = true;
		} else if (!keepSingleForAcyclic && bci == 0) {
		    mol.removeAtom(i);
		    found = true;
		}
	    }
	}

	if (keepOnlyLargestResultFragment) {
	    mol = getLargestFragment(mol);
	}

	return mol;
    }

    /**
     * Returns the largest fragment of a structure
     * {@link chemaxon.struc.Molecule#getAtomCount()} will be used to determine
     * fragment size, so explicit and implicit hydrogenes are distinguished.
     * Explicit hydrogenes count as any other atoms.
     * @param mol the molecule
     * @return the largest fragment.
     */
    private static Molecule getLargestFragment(Molecule mol) {
	Molecule[] frags = mol.cloneMolecule().convertToFrags();

	if (frags.length > 1) {
	    int largestAtomCount = -1;
	    Molecule largestFragment = null;
	    for(int i = 0; i < frags.length; i++) {
		if (frags[i].getAtomCount() > largestAtomCount) {
		    largestFragment = frags[i];
		    largestAtomCount = largestFragment.getAtomCount();
		}
	    }
	    return largestFragment;
	} else {
	    return mol;
	}
    }

    /**
     * Returns the Bemis-Murcko framework.
     * @return the Bemis-Murcko framework
     */
    public Molecule getBemisMurckoFramework() {
	return mol;
    }

    /**
     * Returns the number of atoms in the Bemis-Murcko framework.
     * @return the number of atoms in the Bemis-Murcko framework
     */
    public int getBemisMurckoFrameworkAtomCount() {
	return getBemisMurckoFramework().getAtomCount();
    }

    /**
     * Returns the number of bonds in the Bemis-Murcko framework.
     * @return the number of bonds in the Bemis-Murcko framework
     */
    public int getBemisMurckoFrameworkBondCount() {
	return getBemisMurckoFramework().getBondCount();
    }

    /**
     * Returns the result structure.
     * @return the calculated framework
     * @throws PluginException on error
     */
    public Molecule getResultMolecule() throws PluginException {
	Molecule resultMol = getBemisMurckoFramework();
	resultMol.setProperty("Atom count", Integer.toString(getBemisMurckoFrameworkAtomCount()));
	resultMol.setProperty("Bond count", Integer.toString(getBemisMurckoFrameworkBondCount()));
	return resultMol;
    }
    
    /**
     * Returns the result types.
     * @return the result types
     */
    public Object[] getResultTypes() {
	return types;
    }
    
    /**
     * Returns the result item for the specified key and index.
     * @param type is the result type
     * @param index is the result index
     * @return the result item for the specified key and index
     * @throws PluginException if the result cannot be returned
     */
    public Object getResult(Object type, int index) throws PluginException {
	String typestr = type.toString();
	if (typestr.equalsIgnoreCase("structure")) {
	    return getBemisMurckoFramework();
	} else if (typestr.equalsIgnoreCase("atomcount")) {
	    return new Integer(getBemisMurckoFrameworkAtomCount());
	} else if (typestr.equalsIgnoreCase("bondcount")) {
	    return new Integer(getBemisMurckoFrameworkBondCount());
	} else {
	    throw new PluginException("Unknown type: " + type);
	}
    }
    
    /**
     * Get a calculation result (returned by {@link getResultMolecule()} after 
     * a {@link run()} call in the given format {@link setFormat()}
     * @param type   this parameter is ignored
     * @param index  this patameter is ignored
     * @param result result given by {@link getResultMolecule()}
     * @return the string representatin in the given format
     * @throws PluginException on error
     */
    public String getResultAsString(Object type, int index, Object result)
    throws PluginException {
	if (result instanceof Molecule) {
	    Molecule mol = (Molecule) result;
	    return mol.toFormat(format);
	} else if (result instanceof Integer) {
	    return String.valueOf(((Integer) result).intValue());
	} else {
	    throw new PluginException("Cannot convert result to String: " + result);
	}
    }

    @Override
    public void standardize(Molecule mol) {
	mol.ungroupSgroups();
	mol.implicitizeHydrogens(MolAtom.ALL_H);
    }

}
