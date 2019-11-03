package com.pump.icon;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;

import com.pump.plaf.AnimationManager;
import com.pump.plaf.PlafPaintUtils;
import com.pump.plaf.SwitchButtonUI;

/**
 * This is the icon used by SwitchButtonUIs.
 */
public abstract class AbstractSwitchButtonIcon implements Icon {

	static class ButtonTheme {
		public Color outlineTop, outlineBottom, backgroundTop,
				backgroundBottom, backgroundTopArmed, backgroundBottomArmed,
				handleFill, handleFillArmed;

		ButtonTheme(int outlineTop, int outlineBottom, int backgroundTop,
				int backgroundBottom, int backgroundTopArmed,
				int backgroundBottomArmed, int handleFill, int handleFillArmed) {
			this(new Color(outlineTop), new Color(outlineBottom), new Color(
					backgroundTop), new Color(backgroundBottom), new Color(
					backgroundTopArmed), new Color(backgroundBottomArmed),
					new Color(handleFill), new Color(handleFillArmed));
		}

		ButtonTheme(Color outlineTop, Color outlineBottom, Color backgroundTop,
				Color backgroundBottom, Color backgroundTopArmed,
				Color backgroundBottomArmed, Color handleFill,
				Color handleFillArmed) {
			this.outlineTop = outlineTop;
			this.outlineBottom = outlineTop;
			this.backgroundTop = backgroundTop;
			this.backgroundBottom = backgroundBottom;
			this.backgroundTopArmed = backgroundTopArmed;
			this.backgroundBottomArmed = backgroundBottomArmed;
			this.handleFill = handleFill;
			this.handleFillArmed = handleFillArmed;
		}

		ButtonTheme tween(ButtonTheme other, double fraction) {
			fraction = Math.max(0, Math.min(1, fraction));
			return new ButtonTheme(AnimationManager.tween(outlineTop,
					other.outlineTop, fraction), AnimationManager.tween(
					outlineBottom, other.outlineBottom, fraction),
					AnimationManager.tween(backgroundTop, other.backgroundTop,
							fraction), AnimationManager.tween(backgroundBottom,
							other.backgroundBottom, fraction),
					AnimationManager.tween(backgroundTopArmed,
							other.backgroundTopArmed, fraction),
					AnimationManager.tween(backgroundBottomArmed,
							other.backgroundBottomArmed, fraction),
					AnimationManager.tween(handleFill, other.handleFill,
							fraction), AnimationManager.tween(handleFillArmed,
							other.handleFillArmed, fraction));
		}
	}

	static final String PROPERTY_SELECTED_STATE = AbstractSwitchButtonIcon.class
			.getName() + "#selectedState";
	static final String PROPERTY_ARMED_STATE = AbstractSwitchButtonIcon.class
			.getName() + "#armedState";

	ButtonTheme selectedColors, unselectedColors;
	int focusOffset = 3;
	int handleWidth, trackWidth, trackHeight;

	public AbstractSwitchButtonIcon(int trackWidth, int trackHeight,
			int handleWidth, ButtonTheme unselectedColors,
			ButtonTheme selectedColors) {
		Objects.requireNonNull(unselectedColors);
		Objects.requireNonNull(selectedColors);
		this.unselectedColors = unselectedColors;
		this.selectedColors = selectedColors;
		this.trackWidth = trackWidth;
		this.handleWidth = handleWidth;
		this.trackHeight = trackHeight;
	}

	@Override
	public void paintIcon(Component c0, Graphics g0, int x, int y) {
		JComponent c = (JComponent) c0;
		Graphics2D g = (Graphics2D) g0.create();
		float opacity = c.isEnabled() ? 1 : .5f;

		Rectangle iconRect = new Rectangle(x, y, getIconWidth(),
				getIconHeight());
		c.putClientProperty(SwitchButtonUI.PROPERTY_ICON_RECT, iconRect);

		if (opacity != 1) {
			BufferedImage bi = new BufferedImage(getIconWidth(),
					getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bi.createGraphics();
			doPaintIcon(c, g2, 0, 0);
			g2.dispose();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					opacity));
			g.drawImage(bi, x, y, null);
		} else {
			doPaintIcon(c, g, x, y);
		}
	}

	protected void doPaintIcon(JComponent c, Graphics2D g, int x, int y) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getIconWidth() - 2 * focusOffset;
		int h = getIconHeight() - 2 * focusOffset;
		g.translate(focusOffset, focusOffset);
		RoundRectangle2D r = new RoundRectangle2D.Float(x, y + h / 2
				- trackHeight / 2, w, trackHeight, trackHeight, trackHeight);
		boolean isSelected = isSelected(c);
		boolean isArmed = isArmed(c);
		double selectedState = AnimationManager.setTargetProperty(c,
				PROPERTY_SELECTED_STATE, isSelected ? 1 : 0, 20, .2);
		double armedState = AnimationManager.setTargetProperty(c,
				PROPERTY_ARMED_STATE, isArmed ? 1 : 0, 20, .2);

		ButtonTheme colors = unselectedColors.tween(selectedColors,
				selectedState);
		Color handleColor = AnimationManager.tween(colors.handleFill,
				colors.handleFillArmed, armedState);

		Shape handle = new Ellipse2D.Float(x, y + h / 2 - handleWidth / 2,
				handleWidth, handleWidth);

		double dx = selectedState * (r.getWidth() - (h - 1));
		AffineTransform tx = AffineTransform.getTranslateInstance(dx, 0);
		handle = tx.createTransformedShape(handle);

		g.setStroke(new BasicStroke(1));

		if (c.isFocusOwner()) {
			Area area = new Area();
			area.add(new Area(flatten(r)));
			area.add(new Area(flatten(handle)));
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_PURE);
			PlafPaintUtils.paintFocus(g2, area, focusOffset);
			g2.dispose();
		}

		Color trackFillTop = AnimationManager.tween(colors.backgroundTop,
				colors.backgroundTopArmed, armedState);
		Color trackFillBottom = AnimationManager.tween(colors.backgroundBottom,
				colors.backgroundBottomArmed, armedState);
		g.setPaint(new GradientPaint(x, y, trackFillTop, x, y + h,
				trackFillBottom));
		g.fill(r);

		Paint outlinePaint = new GradientPaint(x, y, colors.outlineTop, x, y
				+ h, colors.outlineBottom);
		g.setPaint(outlinePaint);
		g.draw(r);

		g.setPaint(handleColor);
		g.fill(handle);

		g.setPaint(outlinePaint);
		g.draw(handle);

		g.dispose();
	}

	private Shape flatten(Shape shape) {
		GeneralPath p = new GeneralPath();
		p.append(shape.getPathIterator(null, .01f), false);
		return p;
	}

	/**
	 * Return true if this icon should render as if the component is a selected
	 * button.
	 */
	protected boolean isSelected(Component c) {
		return c instanceof AbstractButton && ((AbstractButton) c).isSelected();
	}

	/**
	 * Return true if this icon should render as if the component is an armed
	 * button.
	 */
	protected boolean isArmed(Component c) {
		return c instanceof AbstractButton
				&& ((AbstractButton) c).getModel().isArmed();
	}

	@Override
	public int getIconWidth() {
		return trackWidth + 2 * focusOffset;
	}

	@Override
	public int getIconHeight() {
		return handleWidth + 2 * focusOffset;
	}

}