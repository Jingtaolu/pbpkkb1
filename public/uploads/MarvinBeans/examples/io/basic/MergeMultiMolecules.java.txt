package io.basic;

/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

import java.io.IOException;

import chemaxon.formats.MolConverter;
import chemaxon.formats.MolExporter;
/**
 * Example to merge more molecule input files into a multi-molecule file
 * 
 * @author Erika Biro
 * @version 2012.08.23.
 * @since Marvin 5.11.
 */
public class MergeMultiMolecules {
    public static void main(String args[]) throws IOException {
	//To merge more molecule input files into a multi-molecule file, use the following settings:
	MolConverter.Builder mcbld = new MolConverter.Builder();
	mcbld.addInput("in1.mol", "");
	mcbld.addInput("in2.mol", "");
	mcbld.addInput("in3.mol", "");
	mcbld.setOutput("out.sdf", "sdf");
	mcbld.setOutputFlags( MolExporter.TEXT );
	MolConverter mc = mcbld.build();
	boolean done = false;
	while (!done){
	    done = mc.convert(); 
	}
	mc.close();
    }
}
