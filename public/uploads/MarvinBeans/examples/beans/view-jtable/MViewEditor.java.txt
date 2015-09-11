/*
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.struc.Molecule;
import chemaxon.marvin.beans.MViewPane;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import java.awt.Component;

/**
 * MViewEditor is an implementation of AbstractCellEditor and TableCellEditor
 * that can be used to assign a cell editor to Molecule objects in JTables.
 *
 * @author Judit Vasko-Szedlar
 * @author Peter Csizmadia
 * @version 5.2.2 05/18/2009
 * @since Marvin 5.1.2
 */
public class MViewEditor extends AbstractCellEditor implements TableCellEditor {
    //The editor component
    private MViewPane editorComponent;

    /**
     * Creates the cell editor. It can be assigned to JTables for example with
     * <code>table.setDefaultEditor(Molecule.class, new MViewEditor());</code>
     */
    public MViewEditor() {
        editorComponent = new MViewPane();
    }

    /**
     * Returns the editor component.
     * @return the <code>MViewPane</code> editor component
     */
    public MViewPane getEditorComponent() {
        return editorComponent;
    }

    /**
     * Returns the mode that determines if the structure is editable.
     * @return
     * {@link MViewPane#VIEW_ONLY}  if molecules can be viewed only,
     * {@link MViewPane#EDITABLE}   if they are editable with MarvinView,
     * {@link MViewPane#SKETCHABLE} if they are editable with MarvinSketch.
     */
    public int getEditable() {
	return getEditorComponent().getEditable();
    }

    /**
     * Sets the mode that determines if the structure is editable.
     * If the structure is allowed to be edited, the Edit > Structure menu
     * or the double mouse click performs the editing.
     * <p>
     * {@link MViewPane#VIEW_ONLY}: editing is disabled,
     * {@link MViewPane#EDITABLE}: editing is enabled
     *              and launches MarvinView in a new window,
     * {@link MViewPane#SKETCHABLE}: editing is enabled
     *              and launches MarvinSketch in a new window.
     * @param e   identifier of the mode
     */
    public void setEditable(int e) {
	getEditorComponent().setEditable(e);
    }

    /**
     * Returns the edited molecule that is a {@link Molecule} instance.
     */
    public Object getCellEditorValue() {
        return getEditorComponent().getM(0);
    }

    /**
     * Sets up the editor component.
     * @param	table		the <code>JTable</code> that is asking the
     *				editor to edit; can be <code>null</code>
     * @param	value		the value of the cell to be edited; it is
     *				considered to be a {@link Molecule} instance;
     *                          <code>null</code> is a valid value
     * @param	isSelected	true if the cell is to be rendered with
     *				highlighting
     * @param	row     	the row of the cell being edited
     * @param	column  	the column of the cell being edited
     * @return	the component for editing
     */
    public Component getTableCellEditorComponent(JTable table,
            Object value, boolean isSelected, int row, int column) {
        MViewPane mviewPane = getEditorComponent();

        setBackgroundColor(table, mviewPane, isSelected, row, column);
        setBorder(table, mviewPane, isSelected, row, column);

        mviewPane.setM(0, (Molecule) value);
        return editorComponent;
    }

    /**
     * Sets the background color for the current cell based on selection.
     * The default implementation sets the table selection color
     * on the editor component. The method is called by
     * {@link #getTableCellEditorComponent(
     *      javax.swing.JTable, Object, boolean, int, int)},
     * overwrite to change the default behavior.
     * @param	table		the <code>JTable</code> that is asking the
     *				editor to edit; can be <code>null</code>
     * @param   mviewPane       the editor component
     * @param	isSelected	true if the cell is to be rendered with the
     *				selection highlighted; otherwise false
     * @param	row	        the row of the cell being edited
     * @param	column	        the column of the cell being edited
     */
    protected void setBackgroundColor(JTable table, MViewPane mviewPane,
                                   boolean isSelected, int row, int column) {
        mviewPane.setMolbg( table.getSelectionBackground() );
        mviewPane.setForeground( UIManager.getColor(
                "Table.focusCellForeground") );
        mviewPane.setBackground( UIManager.getColor(
                "Table.focusCellBackground") );
    }

    /**
     * Sets the border for the current cell based on selection.
     * The default implementation sets the table selection color
     * on the editor component. The method is called by
     * {@link #getTableCellEditorComponent(
     *      javax.swing.JTable, Object, boolean, int, int)},
     * overwrite to change the default behavior.
     * @param	table		the <code>JTable</code> that is asking the
     *				editor to edit; can be <code>null</code>
     * @param   mviewPane       the editor component
     * @param	isSelected	true if the cell is to be rendered with the
     *				selection highlighted; otherwise false
     * @param	row	        the row of the cell being edited
     * @param	column	        the column of the cell being edited
     */
    protected void setBorder(JTable table, MViewPane mviewPane,
                          boolean isSelected, int row, int column) {
        mviewPane.setBorder( UIManager.getBorder(
                "Table.focusCellHighlightBorder") );
    }
}
