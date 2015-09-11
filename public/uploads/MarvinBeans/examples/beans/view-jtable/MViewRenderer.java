/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.marvin.beans.MViewPane;
import chemaxon.struc.Molecule;

import javax.swing.table.TableCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Component;

/**
 * MViewRenderer is a TableCellRenderer component that can be used to render
 * Molecule objects in JTables.
 *
 * @author Judit Vasko-Szedlar
 * @version Marvin 5.2.2 2009.05.18.
 * @since Marvin 5.2.2
 */
public class MViewRenderer implements TableCellRenderer {
    private MViewPane rendererComponent;
    private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    /**
     * Creates the cell renderer. It can be assigned to JTables for example
     * with <code>table.setDefaultRenderer(
     * Molecule.class, new MViewRenderer());</code>
     */
    public MViewRenderer() {
        rendererComponent = new MViewPane();
        rendererComponent.setOpaque(true);
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
     *				if the cell can be edited, it is rendered in
     *				the color used to indicate editing
     * @param	row	        the row index of the cell being drawn.  When
     *				drawing the header, the value of
     *				<code>row</code> is -1
     * @param	column	        the column index of the cell being drawn
     * @return MViewPane component that is configured to draw the molecule.
     */
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setBackgroundColor(table, isSelected, hasFocus, row, column);
        setBorder(table, isSelected, hasFocus, row, column);
        rendererComponent.setM( 0, (Molecule)value );
        return rendererComponent;
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
        rendererComponent.setBackground(isSelected
                ? table.getSelectionBackground()
                : table.getBackground());
        rendererComponent.setMolbg( isSelected
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
     *				the color used to indicate editing
     * @param	row	        the row index of the cell being drawn. When
     *				drawing the header, the value of
     *				<code>row</code> is -1
     * @param	column	        the column index of the cell being drawn
     */
    public void setBorder(JTable table, boolean isSelected,
                                   boolean hasFocus, int row, int column) {
        if(hasFocus) {
            rendererComponent.setBorder( UIManager.getBorder(
                    "Table.focusCellHighlightBorder") );
            if(table.isCellEditable(row, column)) {
                rendererComponent.setForeground( UIManager.getColor(
                        "Table.focusCellForeground") );
                rendererComponent.setBackground( UIManager.getColor(
                        "Table.focusCellBackground") );
            }
        } else {
            rendererComponent.setBorder(noFocusBorder);
        }
    }
}
