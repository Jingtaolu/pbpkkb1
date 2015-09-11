package examples.space;

import javax.swing.*;

public class MSpaceExample1 {

public void createSimpleMarvinSpaceFrame() throws Exception {
    final chemaxon.marvin.space.MSpaceEasy mspace = new chemaxon.marvin.space.MSpaceEasy(true);

    JFrame frame = new JFrame();
    frame.setTitle(chemaxon.marvin.space.gui.MSpace.programName+" "+chemaxon.marvin.space.gui.MSpace.version);
    frame.setSize(800, 750);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    mspace.addCanvas(frame.getContentPane());
    mspace.addPopupMenu();
    mspace.addMenuBar(frame);
    mspace.setSize(600, 600);

    frame.pack();
    frame.setVisible(true);
}

    public static void main(String[] args) {
        MSpaceExample1 e = new MSpaceExample1();
        try{
            e.createSimpleMarvinSpaceFrame();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
