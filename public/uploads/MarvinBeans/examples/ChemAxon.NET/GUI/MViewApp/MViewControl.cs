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
using javax.swing;
using com.jnbridge.jnbcore;
using chemaxon.marvin.jnbadapters;
using chemaxon.marvin.beans;

namespace chemaxon.jnbridge.marvin.example
{
    public delegate void myDelegate();
    
    [Callback("java.beans.PropertyChangeListener")]
    [Callback("java.awt.event.ActionListener")]
    [Callback("java.awt.event.MouseListener")]
    public class MViewControl : UserControl , PropertyChangeListener,
                                ActionListener, MouseListener
    {
        private bool withMenues = false;
        private MViewAdapter mView = null;
        private IntPtr childHwnd;
        private int INSET_SIZE = 0;

        private bool childInited = false;

        protected ArrayList propertyChangeListeners = new ArrayList();
        protected ArrayList actionListeners = new ArrayList();
        protected ArrayList mouseListeners = new ArrayList();

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

        public MViewControl()
        {
            InitializeComponent();
        }

        public MViewControl(bool withMenues)
        {
            this.withMenues = withMenues;
            InitializeComponent();
        }

        private void initChild()
        {
            childHwnd = (IntPtr)getMView().fetchHwnd();
            if ((int) childHwnd == 0 || (int) mViewPanel.Handle == 0)
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
                MViewPane mViewPane = getMView().getMViewPane();
                mViewPane.addPropertyChangeListener(this);

                for(int i=0; i < mViewPane.getVisibleCellCount(); i++) {
                    JComponent comp = mViewPane.getVisibleCellComponent(i);
                    comp.addMouseListener(this); // catch mouse events in all cells
                }

                getMView().setVisible(true);

                SetParent(childHwnd, mViewPanel.Handle);

                BeginInvoke(new myDelegate(setChildSize));
            }
            catch (java.lang.Exception e)
            {
                throw new Exception(e.StackTrace);
            }
        }

        protected MViewAdapter getMView() {
            if (mView == null)
            {
                try
                {
                    if (withMenues) {
                        mView = new MViewAdapter();
                    } else {
                        mView = new MViewAdapter(null);
                    }
                    getMView().setFocusable(true);
                }
                catch (java.lang.Exception e1)
                {
                    string message = e1.getMessage();
                    string stackTrace = e1.StackTrace;
                    throw new Exception(message + "\r\n" + stackTrace);
                }
            }
            return mView;
        }

        protected override void OnPaint(System.Windows.Forms.PaintEventArgs e)
        {
            try {
                getMView();
                BeginInvoke(new myDelegate(initChild));
            }
            catch (java.lang.Exception e1)
            {
                throw new Exception(e1.StackTrace);
            }

            base.OnPaint(e);
        }

        protected override void OnResize(EventArgs e)
        {
            base.OnResize(e);
            if (mView != null && (int) childHwnd != 0)
            {
                BeginInvoke(new myDelegate(setChildSize));
            }
        }

        private void setChildSize()
        {
            if (mView != null && (int) childHwnd != 0)
            {
                MoveWindow(childHwnd, 0, 0, mViewPanel.Width - INSET_SIZE,
                    mViewPanel.Height - INSET_SIZE, true);
            }
        }

        public MViewPane getMViewPane()
        {
            return getMView().getMViewPane();
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

        public void addMouseListener(MouseListener mouseListener)
        {
            lock (mouseListeners)
            {
                mouseListeners.Add(mouseListener);
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

        public void mouseEntered(MouseEvent e) {
            lock (mouseListeners)
            {
                for (int ix = 0; ix < mouseListeners.Count; ix++)
                {
                    MouseListener listener = (MouseListener) mouseListeners[ix];
                    listener.mouseEntered(e);
                }
            }
        }

        public void mouseExited(MouseEvent e) {
            lock (mouseListeners)
            {
                for (int ix = 0; ix < mouseListeners.Count; ix++)
                {
                    MouseListener listener = (MouseListener) mouseListeners[ix];
                    listener.mouseExited(e);
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            lock (mouseListeners)
            {
                for (int ix = 0; ix < mouseListeners.Count; ix++)
                {
                    MouseListener listener = (MouseListener) mouseListeners[ix];
                    listener.mousePressed(e);
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            lock (mouseListeners)
            {
                for (int ix = 0; ix < mouseListeners.Count; ix++)
                {
                    MouseListener listener = (MouseListener) mouseListeners[ix];
                    listener.mouseReleased(e);
                }
            }
        }

        public void mouseClicked(MouseEvent e) {
            lock (mouseListeners)
            {
                for (int ix = 0; ix < mouseListeners.Count; ix++)
                {
                    MouseListener listener = (MouseListener) mouseListeners[ix];
                    listener.mouseClicked(e);
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
            this.mViewPanel = new System.Windows.Forms.Panel();
            this.SuspendLayout();
            // 
            // mSketchPanel
            // 
	    this.mViewPanel.Anchor =
		    ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top
		        | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.mViewPanel.Location = new System.Drawing.Point(1, 0);
            this.mViewPanel.Name = "mViewPanel";
            this.mViewPanel.Size = new System.Drawing.Size(293, 273);
            this.mViewPanel.TabIndex = 0;
            // 
            // MSketchForm
            // 
            this.ClientSize = new System.Drawing.Size(295, 273);
            this.Controls.Add(this.mViewPanel);
            this.Name = "MViewForm";
            this.Text = "Form2";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel mViewPanel;
    }
}
