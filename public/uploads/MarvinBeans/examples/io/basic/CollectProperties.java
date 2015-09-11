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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import chemaxon.formats.MolFileHandler;
import chemaxon.marvin.io.MRecordParseException;

/**
 * Collects property keys from an rdf file.
 * 
 * @author Erika Biro
 * @version 2012.09.11.
 * @since Marvin 5.11.
 */
public class CollectProperties {

    public static void main(String args[]) throws IOException,
	    MRecordParseException {
	FileInputStream inputStream = new FileInputStream(new File(
		"examples/io/basic/mols.rdf"));
	Vector<String> collectedFields = new Vector<String>();
	MolFileHandler.collectFileInfo(inputStream, 0, collectedFields);
	java.util.Iterator<String> iterator = collectedFields.iterator();
	while (iterator.hasNext()) {
	    System.out.println(iterator.next());
	}
    }

}
