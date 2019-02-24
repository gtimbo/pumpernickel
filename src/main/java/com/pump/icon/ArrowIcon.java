/**
 * This software is released as part of the Pumpernickel project.
 * 
 * All com.pump resources in the Pumpernickel project are distributed under the
 * MIT License:
 * https://raw.githubusercontent.com/mickleness/pumpernickel/master/License.txt
 * 
 * More information about the Pumpernickel project is available here:
 * https://mickleness.github.io/pumpernickel/
 */
package com.pump.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.pump.blog.ResourceSample;

/**
 * An arrow icon that can point north, south, east or west.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * Here are some samples:
 * <table summary="Resource&#160;Samples&#160;for&#160;com.pump.swing.resources.ArrowIcon">
 * <tr>
 * <td></td>
 * <td>North</td>
 * <td>South</td>
 * <td>East</td>
 * <td>West</td>
 * </tr>
 * <tr>
 * <td>12x12</td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.NORTH,&#160;12,&#160;12)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample2.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.SOUTH,&#160;12,&#160;12)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample3.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.EAST,&#160;12,&#160;12)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample4.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.WEST,&#160;12,&#160;12)"
 * ></td>
 * </tr>
 * <tr>
 * <td>24x24</td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample5.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.NORTH,&#160;24,&#160;24)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample6.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.SOUTH,&#160;24,&#160;24)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample7.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.EAST,&#160;24,&#160;24)"
 * ></td>
 * <td><img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/ArrowIcon/sample8.png"
 * alt=
 * "new&#160;com.pump.swing.resources.ArrowIcon(&#160;javax.swing.SwingConstants.WEST,&#160;24,&#160;24)"
 * ></td>
 * </tr>
 * <tr>
 * </tr>
 * </table>
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(sample = {
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.NORTH, 12, 12)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.SOUTH, 12, 12)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.EAST, 12, 12)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.WEST, 12, 12)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.NORTH, 24, 24)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.SOUTH, 24, 24)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.EAST, 24, 24)",
		"new com.pump.swing.resources.ArrowIcon( javax.swing.SwingConstants.WEST, 24, 24)" }, columnNames = {
		"North", "South", "East", "West" }, rowNames = { "12x12", "24x24" })
public class ArrowIcon implements Icon, SwingConstants {
	final int height, width;
	final GeneralPath arrow;
	String name;

	/**
	 * Creates an arrow.
	 * 
	 * @param direction
	 *            one of the SwingConstants NORTH, SOUTH, EAST or WEST.
	 * @param w
	 *            the width of the arrow.
	 * @param h
	 *            the height of the arrow.
	 * @return an arrow.
	 */
	public static GeneralPath createArrow(int direction, int w, int h) {
		GeneralPath p = new GeneralPath();

		float width = (direction == EAST || direction == WEST) ? w : h;
		float height = (direction == EAST || direction == WEST) ? h : w;
		p.moveTo(0, height / 2 - height / 6);
		p.lineTo(width / 2, height / 2 - height / 6);
		p.lineTo(width / 2, 0);
		p.lineTo(width, height / 2);
		p.lineTo(width / 2, height);
		p.lineTo(width / 2, height / 2 + height / 6);
		p.lineTo(0, height / 2 + height / 6);

		if (direction == EAST) {
			return p;
		} else if (direction == WEST) {
			AffineTransform horizontalFlip = new AffineTransform();
			horizontalFlip.translate(width, 0);
			horizontalFlip.scale(-1, 1);
			p.transform(horizontalFlip);
			return p;
		} else if (direction == NORTH) {
			AffineTransform rotateCounterclockwise = new AffineTransform();
			rotateCounterclockwise.translate(0, width);
			rotateCounterclockwise.rotate(-Math.PI / 2);
			p.transform(rotateCounterclockwise);
			return p;
		} else if (direction == SOUTH) {
			AffineTransform rotateClockwise = new AffineTransform();
			rotateClockwise.translate(height, 0);
			rotateClockwise.rotate(Math.PI / 2);
			p.transform(rotateClockwise);
			return p;
		}

		throw new IllegalArgumentException(
				"direction ("
						+ direction
						+ ") must be one of the SwingConstants: NORTH, SOUTH, EAST or WEST.");
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Creates a new TriangleIcon.
	 * 
	 * @param direction
	 *            one of the SwingConstant constants for NORTH, SOUTH, EAST or
	 *            WEST. For example, if the direction is EAST this arrow will
	 *            point to the right.
	 * @param width
	 *            the width of this icon
	 * @param height
	 *            the height of this icon
	 */
	public ArrowIcon(int direction, int width, int height) {
		this.width = width;
		this.height = height;

		arrow = createArrow(direction, width, height);
		name = "Arrow " + toString(direction) + " " + width + "x" + height;
	}

	private static String toString(int direction) {
		if (SwingConstants.NORTH == direction) {
			return "North";
		} else if (SwingConstants.SOUTH == direction) {
			return "South";
		} else if (SwingConstants.EAST == direction) {
			return "East";
		} else if (SwingConstants.WEST == direction) {
			return "West";
		}
		throw new IllegalArgumentException(
				"direction ("
						+ direction
						+ ") must be one of the SwingConstants: NORTH, SOUTH, EAST or WEST.");
	}

	public int getIconHeight() {
		return height;
	}

	public int getIconWidth() {
		return width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.translate(x, y);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.darkGray);
		g2.fill(arrow);
	}
}