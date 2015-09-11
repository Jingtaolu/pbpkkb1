/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.MolPrinter;
import chemaxon.marvin.sketch.swing.SketchPanel;
import chemaxon.struc.Molecule;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * MolRenderer is a simple TableCellRenderer component based on MolPrinter
 * that can be used to render Molecule objects in JTables.
 *
 * @author Judit Vasko-Szedlar
 * @version Marvin 5.2.2 05/18/2009
 * @since Marvin 5.2.2
 */
public class MolRenderer  extends JPanel implements TableCellRenderer {
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private MolPrinter printer;

    public MolRenderer() {
        setOpaque(true);
        printer=new MolPrinter();
    }

    /**
     * The implementation of this method sets up the rendering component to
     * display the passed-in molecule, and then returns the component.
     * @param	table		the <code>JTable</code> that is asking the
     *				renderer to draw; can be <code>null</code>
     * @param	value		the value of the cell to be rendered; it is
     *				considered to be a
     *                          {@link chemaxon.struc.Molecule} instance
     * @param	isSelected	true if the cell is to be rendered with the
     *				selection highlighted; otherwise false
     * @param	hasFocus	if true, a special border is put on the cell,
     *                          if the cell can be edited, it is rendered in
     *                          the color used to indicate editing
     * @param	row	        the row index of the cell being drawn.  When
     *				drawing the header, the value of
     *				<code>row</code> is -1
     * @param	column	        the column index of the cell being drawn
     * @return MolRenderer component that is configured to draw the molecule.
     */
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        setBackgroundColor(table, isSelected, hasFocus, row, column);
        setBorder(table, isSelected, hasFocus, row, column);

        // Passing the current molecule to MolPrinter.
        printer.setMol((Molecule)value);
        return this;
    }

    public void paintComponent(Graphics g) {
        // It is very important to set the scale factor of MolPrinter,
        // otherwise the image will not appear.
        // The scale factor is computed by MolPrinter from
        // the current size.
        double scale = printer.maxScale(getSize());
        if( scale > SketchPanel.DEFAULT_SCALE ) {
            scale = SketchPanel.DEFAULT_SCALE;
        }
        printer.setScale(scale);
        // When MolPrinter is properly initialized, it can paint the
        // molecule.
        printer.paint((Graphics2D) g, getSize());
    }

    /**
     * Sets the background color for the current cell based on selection.
     * The default implementation sets the same color for the renderer as
     * the colors of the table itself.
     * The method is called by
     * {@link #getTableCellRendererComponent(
     * javax.swing.JTable, Object, boolean, boolean, int, int)},
     * overwrite to change the default behavior.
     * @param	table		the <code>JTable</code> that is asking the
     *				renderer to draw; can be <code>null</code>
     * @param	isSelected	true if the cell is to be rendered with the
     *				selection highlighted; otherwise false
     * @param	hasFocus	indicates if the cell is focused
     * @param	row	        the row index of the cell being drawn.  When
     *				drawing the header, the value of
     *				<code>row</code> is -1
     * @param	column	        the column index of the cell being drawn
     */
    public void setBackgroundColor(JTable table, boolean isSelected,
                                   boolean hasFocus, int row, int column) {
        super.setBackground(isSelected
                ? table.getSelectionBackground()
                : table.getBackground());
        printer.setBackgroundColor( isSelected
                ? table.getSelectionBackground()
                : table.getBackground());
    }

    /**
     * Sets the border for the current cell based on selection.
     * The default implementation sets the default border of the table
     * on the renderer. The method is called by
     * {@link #getTableCellRendererComponent(
     * javax.swing.JTable, Object, boolean, boolean, int, int)},
     * overwrite to change the default behavior.
     * @param	table		the <code>JTable</code> that is asking the
     *				renderer to draw; can be <code>null</code>
     * @param	isSelected	true if the cell is to be rendered with the
     *				selection highlighted; otherwise false
     * @param	hasFocus	if true, a special border is put on the cell,
     *				if the cell can be edited, it is rendered in
     *                          the color used to indicate editing
     * @param	row	        the row index of the cell being drawn.  When
     *				drawing the header, the value of
     *				<code>row</code> is -1
     * @param	column	        the column index of the cell being drawn
     */
    public void setBorder(JTable table, boolean isSelected,
                          boolean hasFocus, int row, int column) {
        if(hasFocus) {
            setBorder( UIManager.getBorder(
                    "Table.focusCellHighlightBorder") );
            if(table.isCellEditable(row, column)) {
                setForeground( UIManager.getColor(
                        "Table.focusCellForeground") );
                setBackground( UIManager.getColor(
                        "Table.focusCellBackground") );
            }
        } else {
            setBorder(noFocusBorder);
        }
    }
}
