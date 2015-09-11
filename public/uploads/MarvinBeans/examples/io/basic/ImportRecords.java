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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import chemaxon.formats.MFileFormatUtil;
import chemaxon.formats.MolFormatException;
import chemaxon.marvin.io.MRecord;
import chemaxon.marvin.io.MRecordParseException;
import chemaxon.marvin.io.MRecordReader;
import chemaxon.struc.MProp;

/**
 * Reads records from an rdf file.
 * 
 * @author Erika Biro
 * @version 2012.09.11.
 * @since Marvin 5.11.
 */
public class ImportRecords {

	public static void main(String args[]) throws MolFormatException,
			FileNotFoundException, IOException, MRecordParseException {
		MRecordReader recordReader = MFileFormatUtil.createRecordReader(
				new FileInputStream(new File("examples/io/basic/mols.rdf")),
				null, null, null);
		MRecord record = null;
		int recordCount = 0;
		while ((record = recordReader.nextRecord()) != null) {
			String[] fields = record.getPropertyContainer().getKeys();
			List<MProp> values = record.getPropertyContainer().getPropList();
			System.out.println("----------------------------------------fields: ");
			for (int x = 0; x < fields.length; x++) {
				System.out.println(fields[x] + ": " + values.get(x).getPropValue());
			}
			System.out.println("----------------------------------------fields ");
			System.out.println(record.getString());
			recordCount++;
		}
	}
}
