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

import chemaxon.formats.MFileFormat;

/**
 * Creates file format metadata for "myformat".
 *
 * @version 5.2.3, 05/28/2009
 * @since   Marvin 5.0, 06/04/2007
 * @author  Peter Csizmadia
 */
public class Init {
    public static final MFileFormat MYFORMAT;

    static {
	MYFORMAT = new MFileFormat("My Format", "myformat",
	    null, // no MIME type
	    "myformat myf",
	    "myio.MyFormatRecordReader",
	    "myio.MyFormatImport", "myio.MyFormatExport",
	    "myio.MyFormatRecognizer 10",
	    MFileFormat.F_IMPORT | MFileFormat.F_EXPORT | MFileFormat.F_RECOGNIZER
	    | MFileFormat.F_MOLECULE | MFileFormat.F_COORDS
	    | MFileFormat.F_MULTIPLE_RECORDS_LEGAL
	    | MFileFormat.F_MULTIPLE_RECORDS_POSSIBLE);
    }
}
