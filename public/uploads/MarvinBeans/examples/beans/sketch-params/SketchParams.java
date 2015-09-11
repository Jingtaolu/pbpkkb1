/**
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import chemaxon.marvin.beans.MSketchPane;
import chemaxon.marvin.common.ParameterConstants;
import chemaxon.marvin.sketch.SketchParameterConstants;
import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;

/**
 * Example of hiding R-group definitions and showing only the
 * root structure of an R-group structure. 
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/examples/beans/sketch-params/index.html
 * 
 *
 * @author  Erika Biro
 * @version 5.2.2 04/29/2009
 * @since   Marvin 5.0
 */


public class SketchParams extends JPanel {

    MSketchPane msketchPane;
    private static final String molString =
        "$MDL  REV  1 0424081105\n$MOL\n$HDR\n\n  Marvin  04240811052D          \n\n"+
        "$END HDR\n$CTAB\n  8  8  0  0  0  0            999 V2000\n"+
        "   -4.0661    2.6518    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -4.7805    2.2393    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -4.7805    1.4143    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -4.0661    1.0018    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -3.3516    1.4143    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -3.3516    2.2393    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -4.0661    3.4768    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "   -2.6371    2.6518    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  1  0  0  0  0\n  1  6  1  0  0  0  0\n  2  3  1  0  0  0  0\n"+
        "  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  5  6  1  0  0  0  0\n"+
        "  1  7  2  0  0  0  0\n  6  8  1  0  0  0  0\nM  LOG  1   1   0   0   >0\n"+
        "M  LOG  1   2   0   0   >0\nM  RGP  2   7   2   8   1\nM  END\n$END CTAB\n"+
        "$RGP\n  1\n$CTAB\n  2  1  0  0  0  0            999 V2000\n"+
        "    0.5304    3.1821    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    0.5304    2.3571    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  2  0  0  0  0\nM  APO  1   2   1\nM  END\n$END CTAB\n$CTAB\n"+
        "  2  1  0  0  0  0            999 V2000\n"+
        "    3.9777    3.2116    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    3.9777    2.3866    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  1  0  0  0  0\nM  APO  1   2   1\nM  END\n$END CTAB\n$CTAB\n"+
        "  3  2  0  0  0  0            999 V2000\n"+
        "    2.1804    3.2705    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    2.1804    2.4455    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    1.5970    1.8622    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  2  0  0  0  0\n  2  3  1  0  0  0  0\nM  APO  1   3   1\nM  END\n"+
        "$END CTAB\n$END RGP\n$RGP\n  2\n$CTAB\n"+
        "  6  6  0  0  0  0            999 V2000\n"+
        "    1.0312   -0.3536    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    0.3168   -0.7661    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    0.3168   -1.5911    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    1.0312   -2.0036    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    1.7457   -1.5911    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    1.7457   -0.7661    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  1  0  0  0  0\n  1  6  2  0  0  0  0\n  2  3  2  0  0  0  0\n"+
        "  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\n  5  6  1  0  0  0  0\n"+
        "M  APO  1   2   1\nM  END\n$END CTAB\n$CTAB\n"+
        "  5  5  0  0  0  0            999 V2000\n"+
        "    3.9188   -0.3884    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    3.2513   -0.8733    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    3.5062   -1.6579    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    4.3313   -1.6579    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    4.5862   -0.8733    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\n  2  3  2  0  0  0  0\n"+
        "  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\nM  APO  1   2   1\nM  END\n"+
        "$END CTAB\n$CTAB\n  2  1  0  0  0  0            999 V2000\n"+
        "    5.8929   -0.6482    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "    5.8929   -1.4732    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
        "  1  2  2  0  0  0  0\nM  APO  1   1   1\nM  END\n$END CTAB\n$END RGP\n"+
        "$END MOL\n";

    private SketchParams() {
	// Creating the MSketchPane JavaBean component
	msketchPane = new MSketchPane();
	
	// Set parameter to hide R-group definitions 
	// and show only the scaffold.
	msketchPane.setParams(ParameterConstants.R_GROUPS_VISIBLE +"=false");
	msketchPane.setParams(ParameterConstants.VALENCE_PROPERTY_VISIBLE +"=false");
	msketchPane.setParams(SketchParameterConstants.SKETCH_LIGAND_ORDER_VISIBILITY +"=on");
	msketchPane.setParams(ParameterConstants.LIGAND_ERROR_VISIBLE +"=false");
	msketchPane.setMol(molString);

	// Adding the bean to the panel
        setLayout(new BorderLayout());
        add(msketchPane, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setTitle("MarvinSketch Structure Display Parameter Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final SketchParams sketchParams = new SketchParams();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sketchParams, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
