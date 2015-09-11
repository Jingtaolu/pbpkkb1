/**
 * Copyright (c) 1998-2015 ChemAxon Ltd. All Rights Reserved.
 */

import chemaxon.formats.MolExporter;
import chemaxon.marvin.beans.MSketchPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * In this image creation example a java.awt.Image is created
 * from the contents of the MarvinSketch canvas and is shown
 * on a sample GUI.
 *
 * For the detailed description of this example, please visit:
 * http://www.chemaxon.com/marvin/examples/beans/sketch-images/index.html
 *
 * @version 5.2.2, 05/15/2009
 * @author  Judit Vasko-Szedlar
 * @author  Peter Csizmadia
 * @since   Marvin 4.0, 07/09/2005
 */
public class SketchImages extends JPanel {
	private MSketchPane sketchPane;
	private JLabel imageArea, imageSize;

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety,
	 * this method should be invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		JFrame sketch = new JFrame();
		sketch.setTitle("MarvinSketch Images Example");
		sketch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SketchImages sketchImages = new SketchImages();

		sketch.getContentPane().setLayout(new BorderLayout());
		sketch.getContentPane().add(sketchImages, BorderLayout.CENTER);

		sketch.pack();
		sketch.setLocationRelativeTo(null);
		sketch.setVisible(true);
	}

	public SketchImages() {
		// Creating the MarvinSketch JavaBean component.
		sketchPane = new MSketchPane();
		sketchPane.setPreferredSize(new Dimension(430, 400));
		JPanel importExportPanel = createGetImagePanel();

		imageSize = new JLabel();
		updateImageSizeLabel(0, 0);

		setLayout(new BorderLayout());
		add(sketchPane, BorderLayout.NORTH);
		add(importExportPanel, BorderLayout.CENTER);
		add(imageSize, BorderLayout.SOUTH);
	}

	//Sets the contents of the canvas with the current
	// scale factor on the label.
	public void getSketchImagePerformed() {
		if(sketchPane.getMol()==null) {
			imageArea.setIcon(null);
			updateImageSizeLabel(0, 0);
		}
		else {
			// getting the actual scale factor of the canvas
			double currentScale = sketchPane.getScale();
			// getting the image of the canvas with
			// the current scale factor
			Image canvasImage = sketchPane.getImage(currentScale);
			// setting the image on the label
			imageArea.setIcon(new ImageIcon(canvasImage));
			// updating image sizes
			updateImageSizeLabel(canvasImage.getWidth(null),
					canvasImage.getHeight(null));
		}
		imageArea.invalidate();
		imageArea.repaint();
	}

	//Converts the current molecule of the canvas to a 200x200 jpeg image
	//and sets it on the label.
	public void getMolImagePerformed() {
		if(sketchPane.getMol()==null) {
			imageArea.setIcon(null);
			updateImageSizeLabel(0, 0);
		}
		else {
			// The image format along with additional options should be set
			// as a String. The options are separated from the format
			// identifier with a colon, and from each other
			// with commas.
			// The maxscale parameter prevents overscaling of structures.

			// Transparent background of images is supported in
			// PNG, SVG, PDF and EMF formats.
			String format = "png:w200,h200,maxscale28,transbg,#ffffffff";

			// Creating a jpeg image without transparent background
			// String format = "jpeg:w200,h200,maxscale28";

			// Converting the molecule shown on the canvas to binary
			// image representation.
			byte[] imageData = null;
			try {
				imageData = MolExporter.exportToBinFormat(sketchPane.getMol(),format);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon icon = new ImageIcon(imageData);
			imageArea.setIcon(icon);
			// updating image sizes
			updateImageSizeLabel(icon.getIconWidth(), icon.getIconHeight());
		}
		imageArea.invalidate();
		imageArea.repaint();
	}

	private void updateImageSizeLabel(int w, int h) {
		imageSize.setText("Image dimensions: ["+w+", "+h+"]");
	}

	// Creating and laying out GUI components.
	// This part is not documented in details, since it is not directly
	// related to using Marvin Beans.
	private JPanel createGetImagePanel() {
		JPanel getImagePanel = new JPanel();
		getImagePanel.setLayout(new GridBagLayout());
		((GridBagLayout)getImagePanel.getLayout()).columnWidths =
				new int[] {0, 0};
		((GridBagLayout)getImagePanel.getLayout()).rowHeights =
				new int[] {5, 0, 5, 0, 0};
		((GridBagLayout)getImagePanel.getLayout()).columnWeights =
				new double[] {1.0, 1.0E-4};
		((GridBagLayout)getImagePanel.getLayout()).rowWeights =
				new double[] {0.0, 0.0, 0.0, 1.0, 1.0E-4};

		JButton getSketchImageButton = new JButton();
		getSketchImageButton.setText("Get Sketch Image");
		getSketchImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				getSketchImagePerformed();
			}
		});

		JButton getMolImageButton = new JButton();
		getMolImageButton.setText("Get Molecule Image");
		getMolImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				getMolImagePerformed();
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(getSketchImageButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(getMolImageButton);
		buttonPanel.add(Box.createHorizontalGlue());

		imageArea = new JLabel();
		imageArea.setBackground(new Color(237, 210, 184));
		imageArea.setOpaque(true);
		imageArea.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(imageArea);
		scrollPane.setPreferredSize(new Dimension(200, 200));

		getImagePanel.add(buttonPanel,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0), 0, 0));

		getImagePanel.add(scrollPane,
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

		return getImagePanel;
	}

}
