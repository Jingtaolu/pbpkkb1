/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import chemaxon.util.concurrent.WorkUnitFactory;
import chemaxon.util.concurrent.ConcurrentProcessor;
import chemaxon.util.concurrent.InputProducer;
import chemaxon.util.concurrent.processors.ConcurrentProcessors;

/**
 * Base class for concurrent plugin applications.
 * Collects common concurrent code.
 *
 * @author Nora Mate
 * @since Marvin 5.0
 */
abstract public class ConcurrentPluginApplication {

    /**
     * Sample application of the ChemAxon concurrent framework.
     * See the {@link chemaxon.util.concurrent} package for details.
     *
     * @param inputProducer the input producer
     * @param factory the {@link chemaxon.util.concurrent.WorkUnit} factory
     */
    protected void process(InputProducer inputProducer, WorkUnitFactory factory) 
	throws Exception {

	ConcurrentProcessor processor = ConcurrentProcessors.create(
                                            ConcurrentProcessors.IN_INPUT_ORDER,
					    inputProducer,
					    factory);

	// This sets the number of worker threads.
	// The default is the number of processors, whch is optimal in most cases -
	// therefore we comment this out here.
	//processor.setWorkerThreadCount(3);

	processor.start();
	try {
	    while (processor.hasNext()) {
		consume(processor.getNext());
	    }
	} finally {
	    processor.cleanup();
	}
    }

    /**
     * Consumes the result.
     * @param result the result object returned by {@link chemaxon.util.concurrent.WorkUnit#call()}
     */
    abstract protected void consume(Object result);
}
