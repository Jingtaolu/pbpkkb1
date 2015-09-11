using System;
using System.Collections;
using System.Runtime.InteropServices;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using java.awt.@event;
using java.beans;
using com.jnbridge.jnbcore;
using chemaxon.marvin.jnbadapters;

namespace chemaxon.jnbridge.marvin.example
{
    public delegate void myDelegate();
    
    [Callback("java.beans.PropertyChangeListener")]
    [Callback("java.awt.event.ActionListener")]
    public class MSketchControl : UserControl , PropertyChangeListener, ActionListener
    {
        private bool withMenues = true;
        private bool childInited = false;
        private MSketchAdapter mSketch = null;
        private IntPtr childHwnd;
        private int INSET_SIZE = 0;

        protected ArrayList propertyChangeListeners = new ArrayList();
        protected ArrayList actionListeners = new ArrayList();

        [DllImport("user32")]
        private static extern int SetParent(
            IntPtr hWndChild,
            IntPtr hWndNewParent);
        [DllImport("user32")]
        private static extern int MoveWindow(
            IntPtr hWnd,
            int X,
            int Y,
            int nWidth,
            int nHeight,
            bool bRepaint);

        public MSketchControl()
        {
            InitializeComponent();
        }

        public MSketchControl(bool withMenues)
        {
            this.withMenues = withMenues;
            InitializeComponent();
        }

        private void initChild()
        {
            childHwnd = (IntPtr) getMSketch().fetchHwnd();
            if ((int) childHwnd == 0 || (int) mSketchPanel.Handle == 0)
            {
                return;
            }

            if (childInited) {
                return;
            }
            else
            {
                childInited = true;
            }

            try
            {
                getMSketch().addPropertyChangeListener(this);
                getMSketch().setVisible(true);
                childHwnd = (IntPtr)getMSketch().fetchHwnd();
                if ((int) childHwnd != 0 && (int) mSketchPanel.Handle != 0)
                {
                    SetParent(childHwnd, mSketchPanel.Handle);
                }
                BeginInvoke(new myDelegate(setChildSize));
            }
            catch (java.lang.Exception e)
            {
                throw new Exception(e.StackTrace);
            }
        }

        protected override void OnPaint(System.Windows.Forms.PaintEventArgs e)
        {
            try
            {
                getMSketch();
                BeginInvoke(new myDelegate(initChild));
            }
            catch (java.lang.Exception e1)
            {
                throw new Exception(e1.StackTrace);
            }

            base.OnPaint(e);
        }

        protected MSketchAdapter getMSketch()
        {
            if (mSketch == null)
            {
                if (withMenues) {
                    mSketch = new MSketchAdapter();
                } else {
                    mSketch = new MSketchAdapter(null);
                }
                mSketch.setFocusable(true);
            }

            return mSketch;
        }

        protected override void OnResize(EventArgs e)
        {
            base.OnResize(e);
            if (mSketch != null)
            {
                BeginInvoke(new myDelegate(setChildSize));
            }
        }

        private void setChildSize()
        {
            if (mSketch != null && (int) childHwnd != 0)
            {
                MoveWindow(childHwnd, 0, 0, mSketchPanel.Width - INSET_SIZE,
                    mSketchPanel.Height - INSET_SIZE, true);
            }
        }

        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener)
        {
            lock (propertyChangeListeners)
            {
                propertyChangeListeners.Add(propertyChangeListener);
            }
        }

        public void addActionListener(ActionListener actionListener)
        {
            lock (actionListeners)
            {
                actionListeners.Add(actionListener);
            }
        }

        public void propertyChange(PropertyChangeEvent pcEvent)
        {
            lock (propertyChangeListeners)
            {
                for (int ix = 0; ix < propertyChangeListeners.Count; ix++)
                {
                    PropertyChangeListener listener =
                        (PropertyChangeListener)propertyChangeListeners[ix];
                    listener.propertyChange(pcEvent);
                }
            }
        }

        public void actionPerformed(ActionEvent actionEvent)
        {
            lock (actionListeners)
            {
                for (int ix = 0; ix < actionListeners.Count; ix++)
                {
                    ActionListener listener = (ActionListener)actionListeners[ix];
                    listener.actionPerformed(actionEvent);
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
            this.mSketchPanel = new System.Windows.Forms.Panel();
            this.SuspendLayout();
            // 
            // mSketchPanel
            // 
            this.mSketchPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.mSketchPanel.Location = new System.Drawing.Point(1, 0);
            this.mSketchPanel.Name = "mSketchPanel";
            this.mSketchPanel.Size = new System.Drawing.Size(293, 273);
            this.mSketchPanel.TabIndex = 0;
            // 
            // MSketchForm
            // 
            this.ClientSize = new System.Drawing.Size(295, 273);
            this.Controls.Add(this.mSketchPanel);
            this.Name = "MSketchForm";
            this.Text = "Form2";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel mSketchPanel;
    }
}
