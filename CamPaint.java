import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 * @author Anand McCoole (TA: Jason Pak), Dartmouth CS 10, Winter 2022
 * @author Ryan Kim (TA: Caroline Hall), Dartmouth CS 10, Winter 2022
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE

		// Draws accordingly based on displayMode
		if (displayMode == 'w') { g.drawImage(image, 0, 0, null); }			// shows webcam

		if (displayMode == 'r') { g.drawImage(finder.getRecoloredImage(), 0, 0, null); } // shows recoloredImage

		if (displayMode == 'p') { g.drawImage(painting, 0, 0, null); } 		// shows painting
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */
	@Override
	public void processImage(){
		// Webcam Mode – clears painting (shows webcam but that happens in draw)
		if (displayMode == 'w') { clearPainting(); }

		// Painting Mode
		if (displayMode == 'p') {
			// Gets image using finder object and finds regions using RegionFinder methods
			finder.setImage(image);
			finder.findRegions(targetColor);

			// Error checking for if there are no regions
			if (finder.largestRegion() == null) {
				return;
			}

			ArrayList<Point> paintBrush = finder.largestRegion();		// Holds all points in largest region

			/*
			Draws painting by getting points in paintBrush (from image)
			and drawing the same points in painting using color paintColor
			 */
			for (Point pixel : paintBrush) {
				painting.setRGB(pixel.x, pixel.y, paintColor.getRGB());
			}
		}

		// Recolored Image Mode – clears painting and uses RegionFinder object to find and color regions
		if (displayMode == 'r') {
			clearPainting();
			finder.setImage(image);
			finder.findRegions(targetColor);
			finder.recolorImage();
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 * @param x,y coordinates of where mouse was pressed
	 */
	@Override
	public void handleMousePress(int x, int y) {
		this.targetColor = new Color(image.getRGB(x, y));
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 * @param k char of key pressed
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
