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
package com.pump.plaf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.Constructor;

import com.pump.blog.ResourceSample;
import com.pump.swing.MultiThumbSlider;

/**
 * A <code>MultiThumbSliderUI</code> that provides a simple (drab)
 * implementation for subclasses to fine-tune.
 *
 * @param <T>
 *            the parameter for the <code>MultiThumbSlider</code>.
 * 
 *            <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 *            <p>
 *            Here are samples demonstrating different possible thumbs:
 *            <table summary="Resource&#160;Samples&#160;for&#160;com.pump.plaf.DefaultMultiThumbSliderUI">
 *            <tr>
 *            <td>Thumb.Circle</td>
 *            <td><img src=
 *            "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/DefaultMultiThumbSliderUI/sample.png"
 *            alt=
 *            "com.pump.plaf.DefaultMultiThumbSliderUI.createDemo(&#160;com.pump.plaf.DefaultMultiThumbSliderUI.class,&#160;com.pump.plaf.MultiThumbSliderUI$Thumb.Circle&#160;)"
 *            ></td>
 *            </tr>
 *            <tr>
 *            <td>Thumb.Triangle</td>
 *            <td><img src=
 *            "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/DefaultMultiThumbSliderUI/sample2.png"
 *            alt=
 *            "com.pump.plaf.DefaultMultiThumbSliderUI.createDemo(&#160;com.pump.plaf.DefaultMultiThumbSliderUI.class,&#160;com.pump.plaf.MultiThumbSliderUI$Thumb.Triangle&#160;)"
 *            ></td>
 *            </tr>
 *            <tr>
 *            <td>Thumb.Rectangle</td>
 *            <td><img src=
 *            "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/DefaultMultiThumbSliderUI/sample3.png"
 *            alt=
 *            "com.pump.plaf.DefaultMultiThumbSliderUI.createDemo(&#160;com.pump.plaf.DefaultMultiThumbSliderUI.class,&#160;com.pump.plaf.MultiThumbSliderUI$Thumb.Rectangle&#160;)"
 *            ></td>
 *            </tr>
 *            <tr>
 *            <td>Thumb.Hourglass</td>
 *            <td><img src=
 *            "https://raw.githubusercontent.com/mickleness/pumpernickel/master/pump-release/resources/samples/DefaultMultiThumbSliderUI/sample4.png"
 *            alt=
 *            "com.pump.plaf.DefaultMultiThumbSliderUI.createDemo(&#160;com.pump.plaf.DefaultMultiThumbSliderUI.class,&#160;com.pump.plaf.MultiThumbSliderUI$Thumb.Hourglass&#160;)"
 *            ></td>
 *            </tr>
 *            <tr>
 *            </tr>
 *            </table>
 *            <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@ResourceSample(columnCount = 1, tableIntroduction = "Here are samples demonstrating different possible thumbs:", rowNames = {
		"Thumb.Circle", "Thumb.Triangle", "Thumb.Rectangle", "Thumb.Hourglass" }, sample = {
		"com.pump.plaf.DefaultMultiThumbSliderUI.createDemo( com.pump.plaf.DefaultMultiThumbSliderUI.class, com.pump.plaf.MultiThumbSliderUI$Thumb.Circle )",
		"com.pump.plaf.DefaultMultiThumbSliderUI.createDemo( com.pump.plaf.DefaultMultiThumbSliderUI.class, com.pump.plaf.MultiThumbSliderUI$Thumb.Triangle )",
		"com.pump.plaf.DefaultMultiThumbSliderUI.createDemo( com.pump.plaf.DefaultMultiThumbSliderUI.class, com.pump.plaf.MultiThumbSliderUI$Thumb.Rectangle )",
		"com.pump.plaf.DefaultMultiThumbSliderUI.createDemo( com.pump.plaf.DefaultMultiThumbSliderUI.class, com.pump.plaf.MultiThumbSliderUI$Thumb.Hourglass )" })
public class DefaultMultiThumbSliderUI<T> extends MultiThumbSliderUI<T> {

	/**
	 * Create a <code>MultiThumbSlider</code> that uses a particular
	 * MultiThumbSliderUI.
	 * 
	 * @param uiClass
	 *            the MultiThumbSliderUI to apply
	 * @return a sample component to render for documentation.
	 */
	public static MultiThumbSlider<?> createDemo(Class<?> uiClass, Thumb thumb) {
		try {
			MultiThumbSlider<Character> slider = new MultiThumbSlider<Character>(
					new float[] { .25f, .75f }, new Character[] { 'A', 'B' });
			slider.putClientProperty(THUMB_SHAPE_PROPERTY, thumb);
			Constructor constructor = uiClass
					.getConstructor(new Class[] { MultiThumbSlider.class });
			slider.setUI((MultiThumbSliderUI) constructor
					.newInstance(new Object[] { slider }));
			return slider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected int FOCUS_PADDING = 3;
	protected Color trackHighlightColor = new Color(0, 0, 0, 140);

	public DefaultMultiThumbSliderUI(MultiThumbSlider<T> slider) {
		super(slider);
		DEPTH = 10;
	}

	protected boolean isTrackHighlightActive() {
		return slider.getThumbCount() == 2;
	}

	@Override
	protected int getPreferredComponentDepth() {
		return 20;
	}

	@Override
	protected Dimension getThumbSize(int thumbIndex) {
		Thumb thumb = getThumb(thumbIndex);
		if (Thumb.Hourglass.equals(thumb)) {
			return new Dimension(8, 16);
		} else if (Thumb.Triangle.equals(thumb)) {
			return new Dimension(10, 18);
		} else if (Thumb.Rectangle.equals(thumb)) {
			return new Dimension(10, 20);
		} else {
			return new Dimension(16, 16);
		}
	}

	@Override
	protected void paintTrack(Graphics2D g) {
		Shape trackOutline = getTrackOutline();
		g = (Graphics2D) g.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0xBBBBBB));
		g.fill(trackOutline);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.clip(trackOutline);
		g2.setColor(new Color(0xAAAAAA));
		g2.setStroke(new BasicStroke(2));
		for (float y = 0; y < .5f; y += .1f) {
			g2.draw(trackOutline);
			g2.translate(0, .1f);
		}
		g2.dispose();

		paintTrackHighlight(g);

		g.setColor(new Color(0x888888));
		g.setStroke(new BasicStroke(1));
		g.draw(trackOutline);

		if (slider.isPaintTicks()) {
			g.setColor(new Color(0x777777));
			g.setStroke(new BasicStroke(1));
			paintTick(g, .25f, 0, 4, true);
			paintTick(g, .5f, 0, 4, true);
			paintTick(g, .75f, 0, 4, true);
			paintTick(g, 0f, 0, 4, true);
			;
			paintTick(g, 1f, 0, 4, true);
		}
		g.dispose();
	}

	/**
	 * This optional method highlights the space on the track (by simply adding
	 * a shadow) between two thumbs.
	 * 
	 * @param g
	 */
	protected void paintTrackHighlight(Graphics2D g) {
		if (!isTrackHighlightActive())
			return;
		g = (Graphics2D) g.create();
		Point2D p1 = getThumbCenter(0);
		Point2D p2 = getThumbCenter(1);
		Shape outline;
		if (slider.getOrientation() == MultiThumbSlider.HORIZONTAL) {
			float minX = (float) Math.min(p1.getX(), p2.getX());
			float maxX = (float) Math.max(p1.getX(), p2.getX());
			outline = new Rectangle2D.Float(minX, trackRect.y, maxX - minX,
					trackRect.height);
		} else {
			float minY = (float) Math.min(p1.getY(), p2.getY());
			float maxY = (float) Math.max(p1.getY(), p2.getY());
			outline = new Rectangle2D.Float(trackRect.x, minY, trackRect.width,
					maxY - minY);
		}
		g.setColor(trackHighlightColor);
		g.fill(outline);
		g.dispose();
	}

	protected void paintTick(Graphics2D g, float f, int d1, int d2,
			boolean mirror) {
		if (slider.getOrientation() == MultiThumbSlider.HORIZONTAL) {
			int x = (int) (trackRect.x + trackRect.width * f + .5f);
			int y = trackRect.y + trackRect.height;
			g.drawLine(x, y + d1, x, y + d2);
			if (mirror) {
				y = trackRect.y;
				g.drawLine(x, y - d1, x, y - d2);
			}
		} else {
			int y = (int) (trackRect.y + trackRect.height * f + .5f);
			int x = trackRect.x + trackRect.width;
			g.drawLine(x + d1, y, x + d2, y);
			if (mirror) {
				x = trackRect.x;
				g.drawLine(x - d1, y, x - d2, y);
			}
		}
	}

	@Override
	protected void paintFocus(Graphics2D g) {
		Shape trackOutline = getTrackOutline();
		g = (Graphics2D) g.create();
		PlafPaintUtils.paintFocus(g, trackOutline, FOCUS_PADDING);
		g.dispose();
	}

	@Override
	protected Rectangle calculateTrackRect() {
		int k = (int) (10 + FOCUS_PADDING + .5);
		if (slider.getOrientation() == MultiThumbSlider.HORIZONTAL) {
			return new Rectangle(k, slider.getHeight() / 2 - DEPTH / 2,
					slider.getWidth() - 2 * k - 1, DEPTH);
		} else {
			return new Rectangle(slider.getWidth() / 2 - DEPTH / 2, k, DEPTH,
					slider.getHeight() - 2 * k - 1);
		}
	}

	protected Shape getTrackOutline() {
		trackRect = calculateTrackRect();
		float k = Math.max(10, FOCUS_PADDING) + 1;
		int z = 3;
		if (slider.getOrientation() == MultiThumbSlider.VERTICAL) {
			return new RoundRectangle2D.Float(trackRect.x, trackRect.y - z,
					trackRect.width, trackRect.height + 2 * z, k, k);
		}
		return new RoundRectangle2D.Float(trackRect.x - z, trackRect.y,
				trackRect.width + 2 * z, trackRect.height, k, k);
	}

	@Override
	protected void paintThumbs(Graphics2D g) {
		float[] values = slider.getThumbPositions();
		for (int a = 0; a < values.length; a++) {
			float darkness = a == slider.getSelectedThumb() ? 1
					: thumbIndications[a] * .5f;
			Graphics2D g2 = (Graphics2D) g.create();
			paintThumb(g2, a, darkness);
			g2.dispose();
		}
	}

	protected void paintThumb(Graphics2D g, int thumbIndex, float selected) {
		g = (Graphics2D) g.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Shape outline = getThumbShape(thumbIndex);
		int gray = (int) ((1 - selected) * 100 + 30);
		g.setColor(new Color(gray, gray, gray));
		g.fill(outline);
		gray = (int) ((1 - selected) * 100);
		g.setColor(new Color(gray, gray, gray));
		g.draw(outline);
		g.dispose();
	}
}