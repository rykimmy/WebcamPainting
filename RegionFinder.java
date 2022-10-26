import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Winter 2022
 * 
 * @author Ryan Kim (TA: Caroline Hall), Dartmouth CS 10, Winter 2022
 * @author Anand McCoole (TA: Jason Pak), Dartmouth CS 10, Winter 2022
 */
public class RegionFinder {
	private static final int maxColorDiff = 40;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points so the identified regions are in a list of lists of points

	public RegionFinder() { this.image = null; }

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 * @param targetColor the target color that we'll use to compare points (set in CamPaint through mouse)
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE

		// visited – image that allows us to check which points have been visited (1 = checked, 0 = not checked)
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		ArrayList<Point> toVisit = new ArrayList<>(); 	// holds all the points that need to be visited for a particular region
		regions = new ArrayList<>();

		// Loops through all the points to check all pixels for potential regions
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {

				// Holds all the points to a singular region that may or may not be added to ArrayList regions
				ArrayList<Point> potentialRegion = new ArrayList<>();

				// Getting the point and color values of each pixel
				Point point = new Point(x, y);
				Color color = new Color(image.getRGB(x, y));

				// Checking to see if noted point matches our specified targetColor and has not yet
				// been visited (by checking the color of blank image)
				if (colorMatch(targetColor, color) && visited.getRGB(x, y) == 0) {
					toVisit.add(point);

					// Continues looping through all the neighbors and their neighbors to find potential points to add
					// to this specific potential region
					while (toVisit.size() > 0) {

						// Getting the x,y of the point we're checking
						int cx = toVisit.get(0).x;
						int cy = toVisit.get(0).y;


						potentialRegion.add(toVisit.get(0));							// Add point to potential region
						visited.setRGB(toVisit.get(0).x, toVisit.get(0).y, 1);		// Sets color to white on visited image to mark that point has been visited
						toVisit.remove(0);										// Remove checked point from toVisit

						/*
						Loops through all neighbors (depending on whether at edge or not)
						and checks if neighbors are of the same color
						and haven't been visited; if so, add to toVisit and set
						the corresponding coordinates on the visited image to white
						to mark the point as visited
						 */
						for (int i = Math.max(0, cx - 1); i < Math.min(image.getWidth(), cx + 2); i++) {
							for (int j = Math.max(0, cy - 1); j < Math.min(image.getHeight(), cy + 2); j++) {
								Color newColor = new Color(image.getRGB(i, j));
								if (i != cx && j != cy && colorMatch(targetColor, newColor) &&
										visited.getRGB(i, j) == 0) {
									toVisit.add(new Point(i, j));
									visited.setRGB(i, j, 1);
								}
							}
						}
					}
					/*
					After loop stops running, there are no more points to check
					so potentialRegion is complete; if it passes the threshold to be
					a region (minRegion = 50) then gets added to ArrayList regions
					*/
					if (potentialRegion.size() >= minRegion) {
						regions.add(potentialRegion);
					}
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold,
	 * which you can vary).
	 * @param c1, c2; two colors that are being compared
	 * @return boolean; whether or not two colors are close enough
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE

		// Calculates the color difference
		int diff = Math.abs(c1.getRed() - c2.getRed())
				+ Math.abs(c1.getGreen() - c2.getGreen())
				+ Math.abs(c1.getBlue() - c2.getBlue());

		// If color difference is within our parameters (maxColorDiff = 50), returns true
		return diff < maxColorDiff;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE

		// Error checking for if there are no regions
		if (regions.size() == 0) {
			return null;
		}

		ArrayList<Point> biggest = new ArrayList<>(); // the biggest region (most number of points)

		// Runs through each ArrayList in regions and compares sizes; returns the biggest
		for (ArrayList<Point> lists : regions) {
			if (lists.size() > biggest.size()) {
				biggest = lists;
			}
		}
		return biggest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null),
				image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		Color randomColor = new Color((int)(Math.random() * 16777216)); 	// Gets random color

		// For each region, draw each point with random color
		for (ArrayList<Point> region : regions) {
			for (Point point : region) {
				recoloredImage.setRGB(point.x, point.y, randomColor.getRGB());
			}
		}
	}
}

//public sum(elt);
//int x = 0;
//
//if (e.next == null) {
//	return e.data;
//} else {
//	x += sum(e.next);
//}
