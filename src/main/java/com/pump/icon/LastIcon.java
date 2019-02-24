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

import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.pump.blog.ResourceSample;
import com.pump.icon.FirstIcon.BarIcon;

/**
 * An icon to navigate to the end of a timeline or animation.
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p>
 * <img src=
 * "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/LastIcon/sample.png"
 * alt=
 * "new&#160;com.pump.swing.resources.LastIcon(&#160;2,&#160;24,&#160;24,&#160;java.awt.Color.lightGray)"
 * > 
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 * 
 * @see FirstIcon
 * @see PauseIcon
 */
@ResourceSample(sample = { "new com.pump.swing.resources.LastIcon( 2, 24, 24, java.awt.Color.lightGray)" })
public class LastIcon implements Icon {
	TriangleIcon triangleIcon;
	BarIcon barIcon;

	public LastIcon(int barWidth, int width, int height, Color color) {
		triangleIcon = new TriangleIcon(SwingConstants.EAST, width - 2
				* barWidth, height, color);
		barIcon = new BarIcon(barWidth, height, color);
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		triangleIcon.paintIcon(c, g, x, y);
		barIcon.paintIcon(c, g,
				x + triangleIcon.getIconWidth() + barIcon.getIconWidth(), y);
	}

	public int getIconWidth() {
		return barIcon.getIconWidth() * 2 + triangleIcon.getIconWidth();
	}

	public int getIconHeight() {
		return Math.max(barIcon.getIconHeight(), triangleIcon.getIconHeight());
	}
}