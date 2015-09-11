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

import chemaxon.util.concurrent.InputProducer;
import chemaxon.util.concurrent.WorkUnit;
import chemaxon.util.concurrent.WorkUnitFactory;
import chemaxon.util.concurrent.marvin.CompositeInputProducer;
import chemaxon.util.concurrent.marvin.ReusableInputProducer;
import chemaxon.util.concurrent.marvin.MolInputProducer;
import chemaxon.util.concurrent.workunitmgmt.WorkUnitManager;
import chemaxon.marvin.calculations.TautomerizationPlugin;
import chemaxon.marvin.calculations.logDPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.marvin.plugin.concurrent.ReusablePluginWorkUnit;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolImporter;

/**
 * Sample application for concurrent {@link chemaxon.marvin.calculations.TautomerizationPlugin} 
 * calculation processing.
 * <p>
 * The tautomers are stored in the plugin objects themselves, we do not want
 * to fetch all of the tautomers in advance, since there may be many tautomers 
 * and we do not know which of these will be required by the caller.
 * <p>
 * In this example we write the tautomer with the smallest logD value.
 * <p>
 * The plugin objects are returned for reuse by the caller after the results 
 * have been fetched. The input producer will distribute both the input molecules
 * and the plugin objects for the work units.
 *
 * @author Nora Mate
 * @since Marvin 5.0
 */
public class TautomerizationPluginApplication extends ConcurrentPluginApplication {

    /**
     * Input producer returning a {@link chemaxon.marvin.calculations.TautomerizationPlugin} 
     * object and the next molecule from the standard input.
     */
    static class TautomerizationInputProducer extends CompositeInputProducer {

	static public final int DEF_PLUGIN_COUNT = 10;

	/**
	 * Constructor. Creates a molecule importer which will read the standard input.
	 * Creates the plugin object array.
	 * @param pluginCount the number of plugin objects to be used
	 * @param importer the molecule importer object
	 */
	public TautomerizationInputProducer(int pluginCount, MolImporter importer) {
	    super(new InputProducer[] {
		new PluginInputProducer(pluginCount), 
		new MolInputProducer(importer)
	    });
	}

	/**
	 * Puts a plugin into the plugin queue.
	 * Called by work units after the plugin results have been fetched.
	 * @param plugin the plugin object
	 */
	public void reuse(TautomerizationPlugin plugin) {
	    ((ReusableInputProducer) inputProducers[0]).reuse(plugin);
	}

	/**
	 * Closes the molecule input.
	 */
	public void close() throws Exception {
	    ((MolInputProducer) inputProducers[1]).close();
	}
    }

    /**
     * Input producer for holding and reusing a given number of Tautomerization plugin objects,
     * and also creating new plugin objects when needed.
     */
    static class PluginInputProducer extends ReusableInputProducer {

	static private TautomerizationPlugin[] createPlugins(int pluginCount) {
	    TautomerizationPlugin[] plugins = new TautomerizationPlugin[pluginCount];
	    for (int i=0; i < plugins.length; ++i) {
		plugins[i] = new TautomerizationPlugin();
	    }
	    return plugins;
	}
	
	/**
	 * Constructor. Creates and stores the given number of plugins.
	 * @param pluginCount the number of plugin objects
	 */
	public PluginInputProducer(int pluginCount) {
	    super(createPlugins(pluginCount));
	}

	/**
	 * Creates a plugin object.
	 * Used when a plugin object is required but the input queue is empty.
	 * @return the plugin object
	 */
	protected Object createInput() {
	    return new TautomerizationPlugin();
	}
    }

    /**
     * WorkUnit factory that creates reusable workunits.
     */
    static class ReusablePluginWorkUnitFactory implements WorkUnitFactory { 

	/**
	 * Creates a {@link chemaxon.marvin.plugin.concurrent.ReusablePluginWorkUnit} object.
	 * @return the created work unit object
	 */
	public WorkUnit createWorkUnit() throws Exception {
	    return new ReusablePluginWorkUnit();
	}
    }

    /** The input producer. */
    private TautomerizationInputProducer inputProducer = null;

    /** The logD plugin object to calculate the logD of tautomers. */
    private logDPlugin logDCalc = new logDPlugin();


    /**
     * Consumes the result. Returns the plugin object to the input producer for reuse.
     *
     * This implementation looks for the tautomer with least logD value at default pH=7.4.
     * Note, that the algorithm examines all tautomers but stores only one of them,
     * showing the advantage of having the plugin as the result resource rather than
     * having an array of tautomers (possibly 5000-length molecule array) in memory.
     * 
     * @param result plugin object
     */
    protected void consume(Object result) {

	// if there was an error
	if (result instanceof Exception) {
	    System.err.println("Tautomer calculation failed.");
	    ((Exception) result).printStackTrace();
	    return;
	}

	// the result is now a plugin object
	TautomerizationPlugin plugin = (TautomerizationPlugin) result;

	// perform logD calculation to get the structure with minimal logD
	int count = plugin.getStructureCount();	
	try {
	    Molecule mol = null;
	    double minlogD = Double.POSITIVE_INFINITY;
	    for (int i=0; i < count; ++i) {
		Molecule tautomer = plugin.getStructure(i);
		logDCalc.setMolecule(tautomer);
		logDCalc.run();
		double logD = logDCalc.getlogD();
		if (logD < minlogD) {
		    minlogD = logD;
		    mol = tautomer;
		}
	    }

	    // display the result
	    System.out.println("Tautomer with minimal logD ("+minlogD+"): "+mol.toFormat("smiles"));
	} catch (Exception e) {
	    System.err.println("logD calculation failed.");
	    e.printStackTrace();
	}

	// return the plugin object to the input producer for reuse
	inputProducer.reuse(plugin);
    }

    /**
     * The main tautomer calculation process.
     * Writes the tautomer with minimal logD to the standard output.
     *
     * @param pluginCount the number of plugin objects to be created
     * @param importer the molecule importer
     */
    public void process(int pluginCount, MolImporter importer) throws Exception {
	logDCalc.setpH(7.4);
	inputProducer = new TautomerizationInputProducer(pluginCount, importer);
	try {
	    process(inputProducer, new ReusablePluginWorkUnitFactory());
	} finally {
	    inputProducer.close();
	    WorkUnitManager.getInstance().shutdownNow();
	}
    }

    /**
     * Usage: <code>java TautomerizationPluginApplication [pluginCount] [molFile]</code>
     * The pluginCount sets the number of plugin objects to be created.
     * Omit the count to use the default value (<code>10</code>).
     * If the molecule file is omitted then the standard input is read.
     * This application writes the tautomer with least logD at pH=7.4.
     */
    static public void main(String[] args) throws Exception {

	int pluginCount = TautomerizationInputProducer.DEF_PLUGIN_COUNT;
	String molFile = null;
	if (args.length > 0) {
	    try {
		pluginCount = Integer.parseInt(args[0]);
	    } catch (NumberFormatException e) {
		molFile = args[0];
	    }
	    if (args.length > 1) {
		try {
		    pluginCount = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
		    if (molFile == null) {
			molFile = args[1];
		    } else {
			System.err.println("Usage:\n  java TautomerizationPluginApplication [pluginCount] [molFile]");
			return;
		    }
		}
	    }
	} else {
	    System.err.println("Usage:\n  java TautomerizationPluginApplication [pluginCount] [molFile]");
	    return;
	}

	TautomerizationPluginApplication app = new TautomerizationPluginApplication();
	MolImporter importer = (molFile == null) ? new MolImporter(System.in) : new MolImporter(molFile);
		
	app.process(pluginCount, importer);
    }
}
