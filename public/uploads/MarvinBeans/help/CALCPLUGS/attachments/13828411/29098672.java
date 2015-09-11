/*
 * Copyright (c) 1998-2014 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.util.concurrent.ExecutionException;

import chemaxon.util.concurrent.WorkUnit;
import chemaxon.util.concurrent.WorkUnitFactory;
import chemaxon.util.concurrent.workunitmgmt.WorkUnitManager;
import chemaxon.util.concurrent.marvin.MolInputProducer;
import chemaxon.marvin.plugin.concurrent.PluginWorkUnit;
import chemaxon.marvin.calculations.logDPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

/**
 * Sample application for concurrent {@link chemaxon.marvin.calculations.logDPlugin} 
 * calculation processing.
 * The logD result is stored in a <code>Double</code> object, the plugin
 * is reused within one work unit for different input molecules.
 *
 * @author Nora Mate
 * @since Marvin 5.0
 */
public class logDPluginApplication extends ConcurrentPluginApplication {

    /**
     * {@link chemaxon.util.concurrent.WorkUnit} implementation for logD calculation.
     * This {@link chemaxon.marvin.calculations.logDPlugin} wrapper is a work unit 
     * used for parallel execution in the ChemAxon concurrent framework. 
     * Refer to the {@link chemaxon.util.concurrent} package for details.
     */
    static class logDWorkUnit extends PluginWorkUnit {

	/**
	 * Constructor.
	 * Creates the {@link chemaxon.marvin.calculations.logDPlugin} object.
	 * @param pH the pH value
	 */
	public logDWorkUnit(double pH) {
	    logDPlugin plugin = new logDPlugin();
	    plugin.setpH(pH);
	    setPlugin(plugin);
	}

	/** 
	 * Returns the logD calculation result.
	 * @return the logD value in a Double object
	 */
	public Object getResult() throws Exception {
	    double logD = ((logDPlugin) plugin).getlogD();
	    return new Double(logD);
	}
    }

    /**
     * Factory that creates {@link logDWorkUnit} objects.
     */
    static class logDWorkUnitFactory implements WorkUnitFactory {

	static public final double DEF_PH = 7.4;

	private double pH = DEF_PH;

	/**
	 * Constructor.
	 */
	public logDWorkUnitFactory() {
	    this(DEF_PH);
	}

	/**
	 * Constructor. Sets the pH.
	 * @param pH the pH value
	 */
	public logDWorkUnitFactory(double pH) {
	    setpH(pH);
	}

	/**
	 * Sets the pH.
	 * @param pH the pH value
	 */
	public void setpH(double pH) {
	    this.pH = pH;
	}

	/**
	 * Creates a {@link logDWorkUnit} object.
	 * @return the created {@link logDWorkUnit} object
	 */
	public WorkUnit createWorkUnit() throws Exception {
	    return new logDWorkUnit(pH);
	}
    }

    /**
     * Consumes the result.
     * @param result the result object returned by {@link logDWorkUnit#call()}
     */
    protected void consume(Object result) {
	if (result instanceof Exception) {
	    System.err.println("Error during logD calculation.");
	    ((Exception) result).printStackTrace();
	    return;
	}
	double logD = ((Double) result).doubleValue();
	System.out.println("logD = "+logD);
    }

    /**
     * The main logD calculation process.
     * Writes results to the standard output.
     * @param importer the molecule importer
     * @param pH the pH 
     */
    public void process(MolImporter importer, double pH) throws Exception {
	MolInputProducer inputProducer = new MolInputProducer(importer);
	try {
	    process(inputProducer, new logDWorkUnitFactory(pH));
	} finally {
	    inputProducer.close();
	    WorkUnitManager.getInstance().shutdownNow();
	}
    }

    /**
     * Usage: <code>java logDPluginApplication [pH] [molFile]</code>
     */
    static public void main(String[] args) throws Exception {

	double pH = logDWorkUnitFactory.DEF_PH;
	String molFile = null;
	if (args.length > 0) {
	    try {
		pH = Double.parseDouble(args[0]);
	    } catch (NumberFormatException e) {
		molFile = args[0];
	    }
	    if (args.length > 1) {
		try {
		    pH = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
		    if (molFile == null) {
			molFile = args[1];
		    } else {
			System.err.println("Usage:\n  java logDPluginApplication [pH] [molFile]");
			return;
		    }
		}
	    }	 
	} else {
	    System.err.println("Usage:\n  java logDPluginApplication [pH] [molFile]");
	    return;
	}

	logDPluginApplication app = new logDPluginApplication();
	MolImporter importer = (molFile == null) ? new MolImporter(System.in) : new MolImporter(molFile);

	app.process(importer, pH);
    }
}
