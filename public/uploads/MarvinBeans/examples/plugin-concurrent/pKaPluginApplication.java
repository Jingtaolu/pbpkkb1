/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.text.DecimalFormat;

import java.util.concurrent.ExecutionException;

import chemaxon.util.concurrent.WorkUnit;
import chemaxon.util.concurrent.WorkUnitFactory;
import chemaxon.util.concurrent.workunitmgmt.WorkUnitManager;
import chemaxon.util.concurrent.marvin.MolInputProducer;
import chemaxon.marvin.plugin.concurrent.PluginWorkUnit;
import chemaxon.marvin.calculations.pKaPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

/**
 * Sample application for concurrent {@link chemaxon.marvin.calculations.pKaPlugin}   
 * calculation processing.
 * The pKa result is stored in a dedicated result object, the plugin is
 * reused within one work unit for different input molecules.
 *
 * @author Nora Mate
 * @since Marvin 5.0
 */
public class pKaPluginApplication extends ConcurrentPluginApplication {

    /**
     * Stores pKa results. 
     */
    static class pKaResult {

	double[] acidicpKa = null;
	int[] acidicIndexes = null;

	double[] basicpKa = null;
	int[] basicIndexes = null;

	/**
	 * Constructor.
	 * @param count the number of strongest pKa values to be computed
	 */
	pKaResult(int count) {
	    acidicpKa = new double[count];
	    acidicIndexes = new int[count];
	    basicpKa = new double[count];
	    basicIndexes = new int[count];
	}

	/**
	 * Provides string representation.
	 * @return the string representation
	 */
	public String toString() {
	    DecimalFormat df = new DecimalFormat();
	    df.setMaximumFractionDigits(2);
	    StringBuffer sb = new StringBuffer();
	    sb.append("\nAcidic: ");
	    for (int i=0; i < acidicpKa.length; ++i) {
		if (acidicIndexes[i] == -1) {
		    break;
		}
		// [acidic pKa value] : [1-based atom index] ;
		sb.append(df.format(acidicpKa[i])+":"+(acidicIndexes[i]+1)+"; ");
	    }
	    sb.append("\nBasic:  ");
	    for (int i=0; i < basicpKa.length; ++i) {
		if (basicIndexes[i] == -1) {
		    break;
		}
		// [basic pKa value] : [1-based atom index] ;
		sb.append(df.format(basicpKa[i])+":"+(basicIndexes[i]+1)+"; ");
	    }
	    return new String(sb);
	}
    }

    /**
     * {@link chemaxon.util.concurrent.WorkUnit} implementation for pKa calculation.
     * This {@link chemaxon.marvin.calculations.pKaPlugin} wrapper is a work unit 
     * used for parallel execution in the ChemAxon concurrent framework. 
     * Refer to the {@link chemaxon.util.concurrent} package for details.
     */
    static class pKaWorkUnit extends PluginWorkUnit {

	private int count = 0;
	
	/**
	 * Constructor.
	 * Creates the {@link chemaxon.marvin.calculations.pKaPlugin} object.
	 * @param count the number of strongest pKa values to be computed,
	 *              <code>0</code> for all values
	 */
	public pKaWorkUnit(int count) {
	    setPlugin(new pKaPlugin());
	    this.count = count;
	}

	/** 
	 * Returns the pKa result.
	 * @return the pKa results in a result object
	 */
	public Object getResult() throws Exception {
	    pKaPlugin p = (pKaPlugin) plugin;
	    pKaResult result = new pKaResult((count == 0) ? p.getAtomCount() : count);
	    p.getMacropKaValues(pKaPlugin.ACIDIC, result.acidicpKa, result.acidicIndexes);
	    p.getMacropKaValues(pKaPlugin.BASIC, result.basicpKa, result.basicIndexes);
	    return result;
	}
    }

    /**
     * Factory that creates {@link pKaWorkUnit} objects.
     */
    static class pKaWorkUnitFactory implements WorkUnitFactory {

	private int count = 0;

	/**
	 * Constructor.
	 */
	public pKaWorkUnitFactory() {
	}

	/**
	 * Constructor. Sets the number of strongest pKa values to be computed.
	 * @param count the number of pKa values to be computed, <code>0</code> for all
	 */
	public pKaWorkUnitFactory(int count) {
	    setCount(count);
	}

	/**
	 * Sets the number of strongest pKa values to be computed.
	 * @param pH the number of pKa values to be computed, <code>0</code> for all
	 */
	public void setCount(int count) {
	    this.count = count;
	}

	/**
	 * Creates a {@link pKaWorkUnit} object.
	 * @return the created {@link pKaWorkUnit} object
	 */
	public WorkUnit createWorkUnit() throws Exception {
	    return new pKaWorkUnit(count);
	}
    }


    /**
     * Consumes the result.
     * @param result the result object returned by {@link pKaWorkUnit#call()}
     */
    protected void consume(Object result) {
	if (result instanceof Exception) {
	    System.err.println("Error during pKa calculation.");
	    ((Exception) result).printStackTrace();
	    return;
	}
	System.out.println((pKaResult) result);
    }

    /**
     * The main pKa calculation process.
     * @param importer the molecule importer
     * @param count the number of strongest pKa values to be computed, <code>0</code> for all
     */
    public void process(MolImporter importer, int count) throws Exception {
	MolInputProducer inputProducer = new MolInputProducer(importer);
	try {
	    process(inputProducer, new pKaWorkUnitFactory(count));
	} finally {
	    inputProducer.close();
	    WorkUnitManager.getInstance().shutdownNow();
	}
    }

    /**
     * Usage: <code>java pKaPluginApplication [count] [molFile]</code>
     * The count sets the number of strongest pKa values to be computed.
     * Omit the count value to get all values.
     */
    static public void main(String[] args) throws Exception {

	int count = 0;
	String molFile = null;
	if (args.length > 0) {
	    try {
		count = Integer.parseInt(args[0]);
	    } catch (NumberFormatException e) {
		molFile = args[0];
	    }
	    if (args.length > 1) {
		try {
		    count = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
		    if (molFile == null) {
			molFile = args[1];
		    } else {
			System.err.println("Usage:\n  java pKaPluginApplication [count] [molFile]");
			return;
		    }
		}
	    }	    
	} else {
	    System.err.println("Usage:\n  java pKaPluginApplication [count] [molFile]");
	    return;
	}

	pKaPluginApplication app = new pKaPluginApplication();
	MolImporter importer = (molFile == null) ? new MolImporter(System.in) : new MolImporter(molFile);

	app.process(importer, count);
    }
}
