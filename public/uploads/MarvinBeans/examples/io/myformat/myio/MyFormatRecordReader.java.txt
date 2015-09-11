/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

package myio;

import java.io.IOException;
import java.io.InputStream;

import chemaxon.marvin.io.MRecord;
import chemaxon.marvin.io.MRecordParseException;
import chemaxon.marvin.io.formats.AbstractMRecordReader;
import chemaxon.struc.MPropertyContainer;
/**
 * Record reader class for "myformat".
 *
 * @version 5.2.3, 05/28/2009
 * @since   Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */

public class MyFormatRecordReader extends AbstractMRecordReader {

    /**
     * Constructs the record reader.
     * @param istr   the input stream
     * @param opts   input option string or <code>null</code>
     */
    public MyFormatRecordReader(InputStream istr, String opts)
	    throws IOException {
	super(istr, opts);
    }

    /**
     * Reads the next record.
     * @return the record
     */
    public MRecord nextRecord() throws MRecordParseException, IOException {
	return nextRecord(false);
    }

    /**
     * Skips the next record.
     * @return incomplete record information, only the record start and end
     *         positions are guaranteed to be set
     */
    public MRecord skipRecord() throws MRecordParseException, IOException {
	return nextRecord(true);
    }

    /**
     * Reads next record.
     * @return the record
     */
    private MRecord nextRecord(boolean skip)
	    throws MRecordParseException, IOException {

	StringBuffer sb = skip? null : new StringBuffer();
	String line;
	long startpos = getFilePointer();
	int lineno = getLineCount();

	// read comment line
	if((line = readLine()) == null) {
	    return null;
	}

	// write comment line
	appendLine(sb, line);

	// read atom count line
	if((line = readLine()) == null) { 
	    return null;
	}
	int nlines = 1;
	int natoms;
	try {
	    natoms = Integer.parseInt(line.trim());
	} catch(NumberFormatException ex) {
	    throw new MRecordParseException(getPosition(),
					    "invalid number of atoms in \""
					    + line + "\"");
	}

	// write atom count line
	appendLine(sb, line);
	++nlines;

	// read and write atom block
	for(int i = 0; i < natoms; ++i) {
	    if((line = readLine()) == null) {
		throw new MRecordParseException(getPosition(),
						"premature end of file");
	    }
	    appendLine(sb, line);
	    ++nlines;
	}

	// read bond count line
	if((line = readLine()) == null) {
	    throw new MRecordParseException(getPosition(),
					    "premature end of file");
	}
	int nbonds;
	try {
	    nbonds = Integer.parseInt(line.trim());
	} catch(NumberFormatException ex) {
	    throw new MRecordParseException(getPosition(),
					    "invalid number of bonds in \""
					    + line + "\"");
	}

	// write bond count line
	appendLine(sb, line);
	++nlines;

	// read and write bond block
	for(int i = 0; i < nbonds; ++i) {
	    if((line = readLine()) == null) {
		throw new MRecordParseException(getPosition(),
						"premature end of file");
	    }
	    appendLine(sb, line);
	    ++nlines;
	}

	// create the record object
	return endMolecule(sb != null? sb.toString() : null, startpos, lineno);
    }

    private MRecord endMolecule(String str, long startpos, int lineno) {
	boolean skip = str == null;
	int[] map = endRecord(skip);
	MPropertyContainer pc = skip? null : new MPropertyContainer();
	long endpos = getFilePointer();
	return new MRecord(startpos, endpos, lineno, str, pc, map);
    }
}
