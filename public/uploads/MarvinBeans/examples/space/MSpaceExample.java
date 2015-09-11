/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * ChemAxon. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with ChemAxon.
 *
 */

package examples.space;

import chemaxon.marvin.util.CallbackIface;
import chemaxon.marvin.space.MSpaceInstaller;
import chemaxon.marvin.space.MSpaceEasy;
import chemaxon.struc.Molecule;

import javax.swing.*;
import java.lang.reflect.Method;
import java.awt.*;

/**
 * MSpaceExample
 * Created on Feb 13, 2006, 3:22:53 PM
 * @author Judit Papp
 */
public class MSpaceExample {

    public static void main(String[] args) {
        MSpaceExample ms = new MSpaceExample();
        try{
            ms.createSimpleMarvinSpaceFrame();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

//    public void openSimpleMarvinSpaceFrame() throws Exception {
//        Method m = (Class.forName("chemaxon.marvin.space.MSpaceInstaller").getMethod("load", new Class[] {boolean.class}));
//        m.invoke(null, new Object[] { Boolean.FALSE });
//        final CallbackIface mspace = (CallbackIface)Class.forName("chemaxon.marvin.space.MSpaceEasy").newInstance();
//
//        JFrame frame = new JFrame() {
//            public void dispose() {
//                mspace.callback("removeAllComponents", null);
//                super.dispose();
//            }
//        };
//        Class mspaceClass = null;
//        try{
//            mspaceClass = Class.forName("chemaxon.marvin.space.gui.MSpace");
//        }
//        catch(ClassNotFoundException cne) {
//            System.out.println("MarvinSpace is not available");
//            return;
//        }
//        frame.setTitle(mspaceClass.getField("programName").get(null)+" "+mspaceClass.getField("version").get(null));
//        java.net.URL u = getClass().getResource("/chemaxon/marvin/space/gui/mspace32.gif");
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Image img = tk.createImage((java.awt.image.ImageProducer)u.getContent());
//        frame.setIconImage(img);
//        frame.setSize(800, 750);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        mspace.callback("addCanvas", frame.getContentPane());
//        mspace.callback("addPopupMenu", null);
//        mspace.callback("setLicenseKey", null);
//        mspace.callback("setSize", new Object[] { new Integer(600), new Integer(600) });
//
//        frame.pack();
//        frame.setVisible(true);
//    }

    public void createSimpleMarvinSpaceFrame() throws Exception {
        final MSpaceEasy mspace = new MSpaceEasy(true);

        JFrame frame = new JFrame();
        frame.setTitle(chemaxon.marvin.space.gui.MSpace.programName+" "+chemaxon.marvin.space.gui.MSpace.version);
        java.net.URL u = getClass().getResource("/chemaxon/marvin/space/gui/mspace16.gif");
        Image img = Toolkit.getDefaultToolkit().createImage((java.awt.image.ImageProducer)u.getContent());
        frame.setIconImage(img);
        frame.setSize(800, 750);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mspace.addCanvas(frame.getContentPane());
        mspace.addPopupMenu();
        mspace.addMenuBar( frame );
        mspace.setSize(600, 600);
        //mspace.addMoleculeToEmptyCell(mol);

        frame.pack();
        frame.setVisible(true);
    }

    public void openMolecule(CallbackIface mspace, Molecule mol) throws Exception{
        Object o = mspace.callback("addMoleculeToEmptyCell", mol);
        if(o!=null && o instanceof Exception) {
            throw (Exception)o;
        }
    }

    public class AppletExample extends JApplet {
        MSpaceEasy mspace = null;

        public void init() {
            try {
                mspace= new MSpaceEasy(true, false, this.getCodeBase());
                if(getParameter("selectionpanel")!=null && getParameter("selectionpanel").equals("true")) {
                    mspace.addSelectionPanel(this);
                }
                else {
                    mspace.addCanvas(this.getContentPane());
                }
                if(getParameter("menubar")!=null && getParameter("menubar").equals("true")) {
                    mspace.addMenuBar(this);
                }
                if(getParameter("toolbar")!=null && getParameter("toolbar").equals("true")) {
                    mspace.addToolBar(this);
                }
                if(getParameter("popupmenu")!=null && getParameter("popupmenu").equals("true")) {
                    mspace.addPopupMenu();
                }
                this.setVisible(true);

                String rc = getParameter("rowCount");
                String cc = getParameter("columnCount");
                final int rowCount = rc == null ? 1 : Integer.parseInt(rc);
                final int columnCount = cc == null ? 1 : Integer.parseInt(cc);

                if(rowCount > 1 || columnCount > 1) {
                    mspace.setLayout(rowCount, columnCount);
                }

                final String mol = getParameter("molecule");
                if(mol!=null) {
                    mspace.addMolecule(getAbsoluteFileName(mol));
                }

                int n = 0;
                while(n < rowCount * columnCount ) {
                    final String moln = getParameter("cell"+n);
                    if(moln != null) {
                        mspace.addMoleculeToEmptyCell(moln);
                        n++;
                    }
                    else {
                        break;
                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

        private String getAbsoluteFileName(String fileName) {
            java.io.File f = new java.io.File(fileName);
            if(fileName.startsWith("http:/") || fileName.startsWith("ftp:/")) {
                try{
                    new java.net.URL(fileName).openConnection();
                }
                catch(Exception e ) {
                    fileName = getCodeBase() + ((fileName.charAt(0)!='/') ? fileName : fileName.substring(1));
                }
            }
            else if(!f.exists()) {
                fileName = getCodeBase() + ((fileName.charAt(0)!='/') ? fileName : fileName.substring(1));
            }
            return fileName;
        }
    
    }
}