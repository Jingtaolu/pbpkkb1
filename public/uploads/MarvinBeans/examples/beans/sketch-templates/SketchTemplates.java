/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MSketchPane;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Example of customizing structure templates in MarvinSketch.
 * The goal of this example is to customize the template structures of
 * MarvinSketch in the following way:
 *  - Generic and Rings template groups are set to visible on the toolbar
 *  - Two wedge bonds are defined in the Extra Templates group, which will
 *    also be visible on the toolbar
 *  - A new template group, Conformers is defined consisting of
 *    six structures
 *
 * Template files (SDfiles/compressed SDfiles with .t extension) should be
 * put into the JAR file.
 * Each template structure group should have a name. Structures stored
 * in the template files will be available in the Template Library
 * and in the Advanced Template toolbar.
 *
 * For the detailed description of this example, please visit
 * http://www.chemaxon.com/marvin/examples/beans/sketch-templates/index.html
 *
 *
 * @author  Judit Vasko-Szedlar
 * @author  Peter Csizmadia
 * @version 5.0.3 Mar 25, 2008
 * @since   Marvin 2.7
 */
public class SketchTemplates extends JPanel {

    public SketchTemplates() {
        // Creating the MSketchPane JavaBean component
        // The default template groups are set here
        MSketchPane sketchPane = new MSketchPane();

        // Custom definition of template groups:
        //    - Generic and Rings template groups are set to visible on
        //      the toolbar with the ttmpls parameter
        //    - with xtmpls parameter, two wedge bonds are added to
        //      the Extra Templates group
        //    - a new template group, Conformers is defined with the
        //      tmpls11 parameter
        // At this point we have 12 defined template groups apart from
        // the My Templates group
        sketchPane.setParams(
            "ttmpls0=*Generic*chemaxon/marvin/templates/generic.t\n"+
            "ttmpls1=*Rings*chemaxon/marvin/templates/rings.t\n"+
            "xtmpls=chemaxon/marvin/templates/wedgebonds.t\n"+
            "tmpls11=:Conformers:chemaxon/marvin/templates/conformers.t\n"
                + "templateToolbarCustomizable=false"
            //+ "template.3d=true"
        );

        // Adding the bean to the container panel
        setLayout(new BorderLayout());
        add(sketchPane, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setTitle("MarvinSketch Structure Templates Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SketchTemplates sketchTemplates = new SketchTemplates();

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(sketchTemplates, BorderLayout.CENTER);

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
