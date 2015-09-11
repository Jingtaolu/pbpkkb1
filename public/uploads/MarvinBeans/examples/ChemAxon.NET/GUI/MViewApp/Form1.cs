using System;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using java.awt.@event;
using java.beans;
using javax.swing;
using chemaxon.struc;
using chemaxon.marvin.calculations;
using chemaxon.marvin.common;
using chemaxon.marvin.beans;

namespace chemaxon.jnbridge.marvin.example
{
    public class MView : Form, PropertyChangeListener, MouseListener
    {
        private MViewControl mviewControl = null;
        private Molecule currentMol = null;

        public void propertyChange(PropertyChangeEvent pcEvent)
        {
            String name = pcEvent.getPropertyName();
            if (name.Equals("mol"))
            {
                currentMol = (Molecule) pcEvent.getNewValue();
                updateMolProperties();
            }
        }

        /** Do nothing. */
        public void mouseEntered(MouseEvent e) {
        }

        /** Do nothing. */
        public void mouseExited(MouseEvent e) { }

        /** Do nothing. */
        public void mousePressed(MouseEvent e) { }

        /** Do nothing. */
        public void mouseReleased(MouseEvent e) { }

        public void mouseClicked(MouseEvent e) {
            MViewPane mViewPane = mviewControl.getMViewPane();
            int selectedIndex = mViewPane.getSelectedIndex();
            currentMol = mViewPane.getM(selectedIndex);
            updateMolProperties();
        }

        public void updateMolProperties() {
            textBoxFormula.Text = currentMol.getFormula();
            textBoxSmiles.Text = currentMol.toFormat("smiles");
            textBoxWeight.Text = Convert.ToString(currentMol.getMass());
        }

        public MView()
        {
            InitializeComponent();
	    textBoxWeight.Enabled = false;
	    textBoxSmiles.Enabled = false;
	    textBoxFormula.Enabled = false;
            Text = "MViewApp - .NET runtime version: " + Environment.Version;
            setMViewPanel();

        }

        private void setMViewPanel()
        {
            mviewControl = new MViewControl();
            initMViewControl(mviewControl);

            mviewControl.Anchor = ((System.Windows.Forms.AnchorStyles)((((
                System.Windows.Forms.AnchorStyles.Top 
                | System.Windows.Forms.AnchorStyles.Bottom)
                | System.Windows.Forms.AnchorStyles.Left)
                | System.Windows.Forms.AnchorStyles.Right)));
            mviewControl.Location = new System.Drawing.Point(1, 0);
            mviewControl.Name = "mViewPanel";
            mviewControl.Size = new System.Drawing.Size(293, 273);
            mviewControl.TabIndex = 0;
            mviewControl.Size = new Size(mviewPanel.Width, this.mviewPanel.Height);
            this.mviewPanel.Controls.Add(mviewControl);

            mviewControl.addPropertyChangeListener(this);
            mviewControl.addMouseListener(this);
        }

        private void initMViewControl(MViewControl mviewControl)
        {
            MViewPane mViewPane = mviewControl.getMViewPane();

            // set layout
            mViewPane.setParams("rows=3\n" +
            "cols=2\n" +
            "border=1\n" +
            "visibleRows=2\n" +
            "layout=:2:1:M:1:0:1:1:c:b:1:1:L:0:0:1:1:c:n:0:1\n" +
            "param=:M:100:100:L:10b\n");
            UserSettings settings = mViewPane.getUserSettings();
            settings.setViewNavmode2d("rotZ");

            // create main menu
            JMenuBar menubar = new JMenuBar();
            mViewPane.setJMenuBar(menubar);
            JMenu menu = new JMenu("File");
            menubar.add(menu);
            JMenu submenu = new JMenu("Save As Image...");
            mViewPane.makeSaveImageMenu(submenu);                
            menu.add(submenu);
            
            JMenuItem mi;
            mi = createMenuItem("Save All as Image", "saveAllImage", mViewPane);
            menu.add(mi);

            mi = createMenuItem("Exit","exit", mViewPane);
            menu.add(mi);
            
            menu = new JMenu("View");
            mViewPane.makeViewMenu(menu);
            menubar.add(menu);
        
            mViewPane.addHelpMenu(menubar);

            mViewPane.setM(0, "../../../view/mols-2d/caffeine.mol");
            mViewPane.setL(0, "Caffeine");
            mViewPane.setM(1, "../../../view/mols-2d/lysergide.csmol");
            mViewPane.setL(1, "Lysergide");
            mViewPane.setM(2, "../../../view/mols-2d/vitaminc.csmol");
            mViewPane.setL(2, "Vitamin C");
            mViewPane.setM(3, "../../../view/mols-2d/aspirin.csmol");
            mViewPane.setL(3, "Aspirin");

        }

        /**
         * Creates a new menu item.
         * @param label the label
         * @param cmd the action command
         * @param l the action listener
         */
        private JMenuItem createMenuItem(String label, String cmd, ActionListener l)
        {
            JMenuItem mi = new JMenuItem(label);
            mi.setActionCommand(cmd);
            mi.addActionListener(l);
            return mi;
        }

        private void buttonCalcPredict_Click(object sender, EventArgs e)
        {
            textBoxLogPResult.Text = "";
            textBoxLogDResult.Text = "";
            if (currentMol != null)
            { 
                try {
                    {
                        logPPlugin plugin = new logPPlugin();
                        plugin.setMolecule(currentMol);
                        plugin.run();
                        double logP = plugin.getlogPTrue();
                        textBoxLogPResult.Text = "" + logP;
                    }

                    {
                        logDPlugin plugin = new logDPlugin();
                        plugin.setMolecule(currentMol);
                        plugin.setpH(Convert.ToDouble(textBoxLogDPh.Text));
                        plugin.run();
                        double logD = plugin.getlogD();
                        textBoxLogDResult.Text = "" + logD;
                    }
                }
                catch (Exception exc)
                {
                    Program.debug("exception: " + exc.GetType().FullName + "\n" + exc.StackTrace);
                }
            }
        }

        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.panelMolProperties = new System.Windows.Forms.Panel();
            this.groupBoxPredict = new System.Windows.Forms.GroupBox();
            this.buttonCalcChemTerm = new System.Windows.Forms.Button();
            this.groupBoxDynProperties = new System.Windows.Forms.GroupBox();
            this.textBoxWeight = new System.Windows.Forms.TextBox();
            this.labelWeight = new System.Windows.Forms.Label();
            this.textBoxSmiles = new System.Windows.Forms.TextBox();
            this.labelSmiles = new System.Windows.Forms.Label();
            this.textBoxFormula = new System.Windows.Forms.TextBox();
            this.labelFormula = new System.Windows.Forms.Label();
            this.mviewPanel = new System.Windows.Forms.Panel();
            this.textBoxLogPResult = new System.Windows.Forms.TextBox();
            this.textBoxLogDResult = new System.Windows.Forms.TextBox();
            this.textBoxLogDPh = new System.Windows.Forms.TextBox();
            this.groupBoxLogD = new System.Windows.Forms.GroupBox();
            this.labelLogDPh = new System.Windows.Forms.Label();
            this.labelLogDResult = new System.Windows.Forms.Label();
            this.groupBoxLogP = new System.Windows.Forms.GroupBox();
            this.labelLogPResult = new System.Windows.Forms.Label();
            this.panelMolProperties.SuspendLayout();
            this.groupBoxPredict.SuspendLayout();
            this.groupBoxDynProperties.SuspendLayout();
            this.groupBoxLogD.SuspendLayout();
            this.groupBoxLogP.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelMolProperties
            // 
            this.panelMolProperties.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.panelMolProperties.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.panelMolProperties.Controls.Add(this.groupBoxPredict);
            this.panelMolProperties.Controls.Add(this.groupBoxDynProperties);
            this.panelMolProperties.Location = new System.Drawing.Point(399, 5);
            this.panelMolProperties.Name = "panelMolProperties";
            this.panelMolProperties.Size = new System.Drawing.Size(259, 526);
            this.panelMolProperties.TabIndex = 1;
            // 
            // groupBoxPredict
            // 
            this.groupBoxPredict.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBoxPredict.Controls.Add(this.groupBoxLogP);
            this.groupBoxPredict.Controls.Add(this.groupBoxLogD);
            this.groupBoxPredict.Controls.Add(this.buttonCalcChemTerm);
            this.groupBoxPredict.Location = new System.Drawing.Point(2, 249);
            this.groupBoxPredict.Name = "groupBoxPredict";
            this.groupBoxPredict.Size = new System.Drawing.Size(251, 272);
            this.groupBoxPredict.TabIndex = 1;
            this.groupBoxPredict.TabStop = false;
            this.groupBoxPredict.Text = "On Demand Calculated Properties";
            // 
            // buttonCalcChemTerm
            // 
            this.buttonCalcChemTerm.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonCalcChemTerm.Location = new System.Drawing.Point(10, 19);
            this.buttonCalcChemTerm.Name = "buttonCalcChemTerm";
            this.buttonCalcChemTerm.Size = new System.Drawing.Size(235, 20);
            this.buttonCalcChemTerm.TabIndex = 1;
            this.buttonCalcChemTerm.Text = "Calculate";
//            this.buttonCalcChemTerm.UseVisualStyleBackColor = true;
            this.buttonCalcChemTerm.Click += new System.EventHandler(this.buttonCalcPredict_Click);
            // 
            // groupBoxDynProperties
            // 
            this.groupBoxDynProperties.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBoxDynProperties.Controls.Add(this.textBoxWeight);
            this.groupBoxDynProperties.Controls.Add(this.labelWeight);
            this.groupBoxDynProperties.Controls.Add(this.textBoxSmiles);
            this.groupBoxDynProperties.Controls.Add(this.labelSmiles);
            this.groupBoxDynProperties.Controls.Add(this.textBoxFormula);
            this.groupBoxDynProperties.Controls.Add(this.labelFormula);
            this.groupBoxDynProperties.Location = new System.Drawing.Point(3, 7);
            this.groupBoxDynProperties.Name = "groupBoxDynProperties";
            this.groupBoxDynProperties.Size = new System.Drawing.Size(251, 236);
            this.groupBoxDynProperties.TabIndex = 0;
            this.groupBoxDynProperties.TabStop = false;
            this.groupBoxDynProperties.Text = "Dynamically Calculated Properties";
            // 
            // textBoxWeight
            // 
            this.textBoxWeight.Location = new System.Drawing.Point(9, 181);
            this.textBoxWeight.Name = "textBoxWeight";
            this.textBoxWeight.Size = new System.Drawing.Size(235, 20);
            this.textBoxWeight.TabIndex = 5;
            // 
            // labelWeight
            // 
            this.labelWeight.AutoSize = true;
            this.labelWeight.Location = new System.Drawing.Point(10, 165);
            this.labelWeight.Name = "labelWeight";
            this.labelWeight.Size = new System.Drawing.Size(44, 13);
            this.labelWeight.TabIndex = 4;
            this.labelWeight.Text = "Weight:";
            // 
            // textBoxSmiles
            // 
            this.textBoxSmiles.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxSmiles.Location = new System.Drawing.Point(9, 118);
            this.textBoxSmiles.Multiline = true;
            this.textBoxSmiles.Name = "textBoxSmiles";
            this.textBoxSmiles.Size = new System.Drawing.Size(234, 31);
            this.textBoxSmiles.TabIndex = 3;
            // 
            // labelSmiles
            // 
            this.labelSmiles.AutoSize = true;
            this.labelSmiles.Location = new System.Drawing.Point(10, 102);
            this.labelSmiles.Name = "labelSmiles";
            this.labelSmiles.Size = new System.Drawing.Size(49, 13);
            this.labelSmiles.TabIndex = 2;
            this.labelSmiles.Text = "SMILES:";
            // 
            // textBoxFormula
            // 
            this.textBoxFormula.Location = new System.Drawing.Point(9, 40);
            this.textBoxFormula.Multiline = true;
            this.textBoxFormula.Name = "textBoxFormula";
            this.textBoxFormula.Size = new System.Drawing.Size(233, 46);
            this.textBoxFormula.TabIndex = 1;
            // 
            // labelFormula
            // 
            this.labelFormula.AutoSize = true;
            this.labelFormula.Location = new System.Drawing.Point(10, 21);
            this.labelFormula.Name = "labelFormula";
            this.labelFormula.Size = new System.Drawing.Size(47, 13);
            this.labelFormula.TabIndex = 0;
            this.labelFormula.Text = "Formula:";
            // 
            // msketchPanel
            // 
            this.mviewPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top
                            | System.Windows.Forms.AnchorStyles.Bottom)
                            | System.Windows.Forms.AnchorStyles.Left)
                            | System.Windows.Forms.AnchorStyles.Right)));

            this.mviewPanel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.mviewPanel.Location = new System.Drawing.Point(1, 5);
            this.mviewPanel.Name = "msketchPanel";
            this.mviewPanel.Size = new System.Drawing.Size(392, 526);
            this.mviewPanel.TabIndex = 0;
            // 
            // textBoxLogPResult
            // 
            this.textBoxLogPResult.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxLogPResult.Enabled = false;
            this.textBoxLogPResult.Location = new System.Drawing.Point(54, 30);
            this.textBoxLogPResult.Name = "textBoxLogPResult";
            this.textBoxLogPResult.Size = new System.Drawing.Size(161, 20);
            this.textBoxLogPResult.TabIndex = 3;
            // 
            // textBoxLogDResult
            // 
            this.textBoxLogDResult.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxLogDResult.Enabled = false;
            this.textBoxLogDResult.Location = new System.Drawing.Point(55, 61);
            this.textBoxLogDResult.Name = "textBoxLogDResult";
            this.textBoxLogDResult.Size = new System.Drawing.Size(161, 20);
            this.textBoxLogDResult.TabIndex = 6;
            // 
            // textBoxLogDPh
            // 
            this.textBoxLogDPh.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.textBoxLogDPh.Location = new System.Drawing.Point(55, 24);
            this.textBoxLogDPh.Name = "textBoxLogDPh";
            this.textBoxLogDPh.Size = new System.Drawing.Size(161, 20);
            this.textBoxLogDPh.TabIndex = 8;
            this.textBoxLogDPh.Text = "3.0";
            // 
            // groupBoxLogD
            // 
            this.groupBoxLogD.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBoxLogD.Controls.Add(this.labelLogDResult);
            this.groupBoxLogD.Controls.Add(this.textBoxLogDResult);
            this.groupBoxLogD.Controls.Add(this.labelLogDPh);
            this.groupBoxLogD.Controls.Add(this.textBoxLogDPh);
            this.groupBoxLogD.Location = new System.Drawing.Point(10, 151);
            this.groupBoxLogD.Name = "groupBoxLogD";
            this.groupBoxLogD.Size = new System.Drawing.Size(232, 99);
            this.groupBoxLogD.TabIndex = 2;
            this.groupBoxLogD.TabStop = false;
            this.groupBoxLogD.Text = "LogD";
            // 
            // labelLogDPh
            // 
            this.labelLogDPh.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.labelLogDPh.AutoSize = true;
            this.labelLogDPh.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.labelLogDPh.Location = new System.Drawing.Point(11, 27);
            this.labelLogDPh.Name = "labelLogDPh";
            this.labelLogDPh.Size = new System.Drawing.Size(25, 13);
            this.labelLogDPh.TabIndex = 9;
            this.labelLogDPh.Text = "PH:";
            // 
            // labelLogDResult
            // 
            this.labelLogDResult.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.labelLogDResult.AutoSize = true;
            this.labelLogDResult.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.labelLogDResult.Location = new System.Drawing.Point(11, 65);
            this.labelLogDResult.Name = "labelLogDResult";
            this.labelLogDResult.Size = new System.Drawing.Size(40, 13);
            this.labelLogDResult.TabIndex = 10;
            this.labelLogDResult.Text = "Result:";
            // 
            // groupBoxLogP
            // 
            this.groupBoxLogP.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.groupBoxLogP.Controls.Add(this.labelLogPResult);
            this.groupBoxLogP.Controls.Add(this.textBoxLogPResult);
            this.groupBoxLogP.Location = new System.Drawing.Point(10, 57);
            this.groupBoxLogP.Name = "groupBoxLogP";
            this.groupBoxLogP.Size = new System.Drawing.Size(230, 72);
            this.groupBoxLogP.TabIndex = 3;
            this.groupBoxLogP.TabStop = false;
            this.groupBoxLogP.Text = "LogP";
            // 
            // labelLogPResult
            // 
            this.labelLogPResult.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.labelLogPResult.AutoSize = true;
            this.labelLogPResult.ImageAlign = System.Drawing.ContentAlignment.MiddleLeft;
            this.labelLogPResult.Location = new System.Drawing.Point(10, 33);
            this.labelLogPResult.Name = "labelLogPResult";
            this.labelLogPResult.Size = new System.Drawing.Size(40, 13);
            this.labelLogPResult.TabIndex = 11;
            this.labelLogPResult.Text = "Result:";
            // 
            // MSketch
            // 
//            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
//            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(659, 531);
            this.Controls.Add(this.panelMolProperties);
            this.Controls.Add(this.mviewPanel);
            this.Name = "MSketch";
            this.Text = "MSketchApp";
            this.panelMolProperties.ResumeLayout(false);
            this.groupBoxPredict.ResumeLayout(false);
            this.groupBoxDynProperties.ResumeLayout(false);
            this.groupBoxDynProperties.PerformLayout();
            this.groupBoxLogD.ResumeLayout(false);
            this.groupBoxLogD.PerformLayout();
            this.groupBoxLogP.ResumeLayout(false);
            this.groupBoxLogP.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel panelMolProperties;
        private System.Windows.Forms.GroupBox groupBoxDynProperties;
        private System.Windows.Forms.GroupBox groupBoxPredict;
        private System.Windows.Forms.Button buttonCalcChemTerm;
        private System.Windows.Forms.Label labelSmiles;
        private System.Windows.Forms.TextBox textBoxFormula;
        private System.Windows.Forms.Label labelFormula;
        private System.Windows.Forms.Label labelWeight;
        private System.Windows.Forms.TextBox textBoxSmiles;
        private System.Windows.Forms.TextBox textBoxWeight;
        private Panel mviewPanel;
        private TextBox textBoxLogPResult;
        private TextBox textBoxLogDResult;
        private GroupBox groupBoxLogD;
        private TextBox textBoxLogDPh;
        private Label labelLogDPh;
        private Label labelLogDResult;
        private GroupBox groupBoxLogP;
        private Label labelLogPResult;

    }
} 
