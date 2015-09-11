package myio;
/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */


import chemaxon.formats.recognizer.Recognizer;
import chemaxon.formats.recognizer.RecognizerList;
/**
 * Recognizer class for "myformat".
 *
 * @since   Marvin 5.11, 08/23/2012
 * @author  Erika Biro
 */

public class MyFormatRecognizer extends Recognizer {

	private boolean needsMore = true;

	@Override
	public void tryToRecognize(String line, int linenum, RecognizerList reclist) {
		if (linenum == 1) {
			if (line.startsWith("MyFormat")) {
				reclist.removeAllExcept("myformat");
				needsMore = false;
			} else {
				reclist.remove("myformat");
				needsMore = false;
			}
		}
	}
    @Override
    public boolean needsMore() {
    	return needsMore;
    }

   
}
