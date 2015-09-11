/*
 *  Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *  This software is the confidential and proprietary information of
 *  ChemAxon. You shall not disclose such Confidential Information
 *  and shall use it only in accordance with the terms of the agreements
 *  you entered into with ChemAxon.
 *  
 */
package core;

import static chemaxon.struc.PeriodicSystem.C;
import chemaxon.struc.PeriodicSystem;

/**
 * Example methods of the PeriodicSystem class.
 * 
 * @author Janos Kendi
 * 
 */
public class PeriodicSystemExample {

    public static void main(String[] args) {

	System.out.println("Atomic number of C: "
		+ PeriodicSystem.findAtomicNumber("C"));

	System.out.println("Mass of C: " + PeriodicSystem.getMass(6));

	System.out.println("Column of C: " + PeriodicSystem.getColumn(C));

	System.out.println("Number of C isotopes: "
		+ PeriodicSystem.getIsotopeCount(C));
    }
}
