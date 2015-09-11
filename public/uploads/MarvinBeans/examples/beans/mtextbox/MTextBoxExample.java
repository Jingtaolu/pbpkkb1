/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MSketchPane;
import chemaxon.struc.CTransform3D;
import chemaxon.struc.MDocument;
import chemaxon.struc.MPoint;
import chemaxon.struc.Molecule;
import chemaxon.struc.graphics.MFont;
import chemaxon.struc.graphics.MRectangle;
import chemaxon.struc.graphics.MTextAttributes;
import chemaxon.struc.graphics.MTextBox;
import chemaxon.struc.graphics.MTextDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main goal of this example is to show how to create formatted text from
 * the Marvin API.
 *
 * For the detailed description of this example, please visit:
 * http://www.chemaxon.com/marvin/examples/beans/mtextbox/index.html
 *
 * @author  Peter Cseh
 * @version 5.2.5 Aug 30, 2009
 * @since   Marvin 5.2.5
 */
public class MTextBoxExample extends JPanel {

    /**
     * Create an empty molecule, add a textbox to it's document, then
     * init the  textbox's content and set the size, then create a MSketchPane
     * to show the result.
     */
    private MTextBoxExample() {

        Molecule mol = new Molecule();
        MTextBox textBox = initTextBox(mol);
        initTextBoxContent(textBox.getTextDocument());

        // Set the MTextBox's coordinates.

        MPoint p1 = new MPoint(0, 0);
        MPoint p2 = new MPoint(10.2, -5);
        textBox.setCorners(p1, p2);

        // Rotate the MTextBox with 45 degrees around the Z axis.

        CTransform3D ctrans=new CTransform3D();
        ctrans.setEuler(0, 0, 45);
        textBox.setTCenter(MRectangle.P_CENTER);
        textBox.transform(ctrans, 0, null);

        // Creating the MSketchPane JavaBean component
        MSketchPane msketchPane = new MSketchPane();
        msketchPane.setMol(mol);

        // Adding the bean to the panel
        setLayout(new BorderLayout());
        add(msketchPane, BorderLayout.CENTER);
    }

    /**
     * Create an empty MTextBox and add it to the Molecule's MDocument.
     * @param mol the molecule
     * @return the created textbox
     */
    private MTextBox initTextBox(Molecule mol) {
        MDocument md = mol.getDocument();
        if (md == null) {
            md = new MDocument(mol);
        }
        MTextBox textBox = new MTextBox();
        md.addObject(textBox);
        return textBox;
    }

    /**
     * Create the MFonts and MTextAttributes that are needed for formatting
     * the text, then add the text to the MTextDocument.
     * @param textDocument the document
     */
    private void initTextBoxContent(MTextDocument textDocument) {
        // Create MFonts for MTextAttributes. The font's name, size and style
        // (Plain, Bold, Italic) can be set here.

        MFont mf = new MFont("SansSerif", MFont.PLAIN, 12);
        MFont italicMf = new MFont("SansSerif", MFont.ITALIC, 12);
        MFont boldMf = new MFont("SansSerif", MFont.BOLD, 12);
        MFont boldItalicMf = new MFont(mf.getFamily(), MFont.ITALIC | MFont.BOLD, mf.getSizeDouble());

        // Create MTextAttributes. The text's color and superscript/subscript
        // information can be set here.

        MTextAttributes defaultMta = new MTextAttributes(0, 0, Color.black, mf, 1, 0, 0);
        MTextAttributes redMta = new MTextAttributes(0, 0, Color.red, mf, 1, 0, 0);
        MTextAttributes italicMta = new MTextAttributes(0, 0, Color.black, italicMf, 1, 0, 0);
        MTextAttributes boldMta = new MTextAttributes(0, 0, Color.black, boldMf, 1, 0, 0);
        MTextAttributes blueBoldMta = new MTextAttributes(0, 0, Color.blue, boldMf, 1, 0, 0);
        MTextAttributes boldItalicMta = new MTextAttributes(0, 0, Color.black, boldItalicMf, 1, 0, 0);
        MTextAttributes superScriptMta = new MTextAttributes(0, MTextAttributes.DEFAULT_SUPERSCRIPT_SUBLEVEL, Color.black, mf, MTextAttributes.DEFAULT_SUPERSCRIPT_SCALE, 0, MTextAttributes.DEFAULT_SUPERSCRIPT_DELTAY);
        MTextAttributes subScriptMta = new MTextAttributes(0, -MTextAttributes.DEFAULT_SUPERSCRIPT_SUBLEVEL, Color.black, mf, MTextAttributes.DEFAULT_SUPERSCRIPT_SCALE, 0, -MTextAttributes.DEFAULT_SUPERSCRIPT_DELTAY);

        // Add text with MTextAttribute.

        textDocument.append("The element", defaultMta);
        textDocument.append(" hydrogen", redMta);
        textDocument.append(" has three isotopes:", boldMta);
        textDocument.append(" H", defaultMta);
        textDocument.append("1 ", superScriptMta);
        textDocument.append("1 ", subScriptMta);
        textDocument.append(", D", defaultMta);
        textDocument.append("2 ", superScriptMta);
        textDocument.append("1 ", subScriptMta);
        textDocument.append("and T", defaultMta);
        textDocument.append("3 ", superScriptMta);
        textDocument.append("1 ", subScriptMta);
        textDocument.append(". They're being called ", defaultMta);
        textDocument.append(" hydrogen,", italicMta);
        textDocument.append(" deuterium", blueBoldMta);
        textDocument.append(" and", defaultMta);
        textDocument.append(" tricium.", boldItalicMta);
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setTitle("MTextBox Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final MTextBoxExample textBoxExample = new MTextBoxExample();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(textBoxExample, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }
}
