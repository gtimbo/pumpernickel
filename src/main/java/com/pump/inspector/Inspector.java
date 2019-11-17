package com.pump.inspector;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;

/**
 * This manages a set of controls arranged as a series of rows.
 * <p>
 * There are two columns: the smaller "lead" (or "ID") column on the left which
 * takes up only as much width as it has to, and the "main" column which takes
 * up the remaining width on the right.
 * <p>
 * All lead controls are right-aligned. All main controls should generally be
 * right-aligned.
 * 
 * @see <a
 *      href="https://javagraphics.blogspot.com/2009/06/layouts-designing-inspector.html">Layouts:
 *      Designing an Inspector</a>
 */
public class Inspector {

	/**
	 * These describe the possible position of of a component when insets are
	 * applied.
	 */
	public enum Position {
		LEAD, MAIN_ONLY_STRETCH_TO_FILL, MAIN_ONLY_NO_STRETCH, MAIN_WITH_LEAD_STRETCH_TO_FILL, MAIN_WITH_LEAD_NO_STRETCH, MAIN_WITH_LEAD_FIRST_IN_SERIES, MAIN_WITH_LEAD_MIDDLE_IN_SERIES, MAIN_WITH_LEAD_LAST_IN_SERIES, TRAIL
	}

	private static final String PROPERTY_WRAPPED = Inspector.class.getName()
			+ "#wrapped";

	JPanel panel;
	InspectorLayoutManager layout;

	/**
	 * Create a new Inspector.
	 */
	public Inspector() {
		this(new JPanel());
	}

	/**
	 * Create a new Inspector that populates a specific panel.
	 * 
	 * @param panel
	 *            the panel to populate.
	 */
	public Inspector(JPanel panel) {
		this.panel = panel;
		layout = new InspectorLayoutManager(this);
		panel.setLayout(layout);
		clear();
	}

	/**
	 * Return the panel this Inspector populates.
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Add a new InspectorRow.
	 */
	public InspectorRowPanel addRow(InspectorRow row) {
		InspectorRowPanel p = new InspectorRowPanel(row);
		panel.add(p);
		return p;
	}

	/**
	 * Appends a new row containing only 1 object to this inspector.
	 * 
	 * @param component
	 *            the component to add.
	 * @param alignment
	 *            one of the SwingConstants values: LEFT, CENTER, RIGHT.
	 * @param stretchToFill
	 *            whether to stretch this component to fill the space
	 *            horizontally or not.
	 */
	public InspectorRowPanel addRow(JComponent component, boolean stretchToFill) {
		Position pos = stretchToFill ? Position.MAIN_ONLY_STRETCH_TO_FILL
				: Position.MAIN_ONLY_NO_STRETCH;
		prepare(pos, component);
		return addRow(new InspectorRow(null, component, stretchToFill, 0));
	}

	/**
	 * Appends a new row containing 2 objects to this inspector.
	 * 
	 * The identifier is right-aligned, and the control is left-aligned.
	 * 
	 * @param identifier
	 *            the control on the left. This should usually contain text. A
	 *            <code>JLabel</code> or a <code>JCheckBox</code> is
	 *            recommended.
	 * @param control
	 *            any more complex control on the right.
	 * @param stretchControlToFill
	 *            whether this control should stretch to fit the remaining
	 *            width.
	 */
	public InspectorRowPanel addRow(JComponent identifier, JComponent control,
			boolean stretchControlToFill) {
		prepare(Position.LEAD, identifier);
		Position pos = stretchControlToFill ? Position.MAIN_ONLY_STRETCH_TO_FILL
				: Position.MAIN_ONLY_NO_STRETCH;
		prepare(pos, control);
		return addRow(new InspectorRow(identifier, control,
				stretchControlToFill, 0));
	}

	/**
	 * Append a row containing these elements to this inspector.
	 * 
	 * @param identifier
	 *            the control on the left. This should usually contain text. A
	 *            <code>JLabel</code> or a <code>JCheckBox</code> is
	 *            recommended.
	 * @param controls
	 *            a series of controls to group together from left to right. The
	 *            cluster of components will be anchored on the left.
	 */
	public InspectorRowPanel addRow(JComponent identifier,
			JComponent... controls) {
		prepare(Position.LEAD, identifier);
		JPanel controlPanel = new JPanel(new GridBagLayout());
		controlPanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.BASELINE;
		for (int a = 0; a < controls.length; a++) {
			Position pos;

			if (a == 0 && a == controls.length - 1) {
				pos = Position.MAIN_WITH_LEAD_NO_STRETCH;
			} else if (a == 0) {
				pos = Position.MAIN_WITH_LEAD_FIRST_IN_SERIES;
			} else if (a == controls.length - 1) {
				pos = Position.MAIN_WITH_LEAD_LAST_IN_SERIES;
			} else {
				pos = Position.MAIN_WITH_LEAD_MIDDLE_IN_SERIES;
			}
			prepare(pos, controls[a]);

			c.insets = getInsets(pos, controls[a]);
			controlPanel.add(controls[a], c);
			c.gridx++;
		}
		controlPanel.putClientProperty(PROPERTY_WRAPPED, Boolean.TRUE);
		return addRow(new InspectorRow(identifier, controlPanel, false, 0));
	}

	/**
	 * Appends a new row containing 3 objects to this inspector.
	 * 
	 * The identifier is right-aligned. The leftControl is right-aligned, and
	 * the rightControl is right-aligned against the far right margin of the
	 * inspector.
	 * 
	 * @param identifier
	 *            the control on the left. This should usually contain text. A
	 *            <code>JLabel</code> or a <code>JCheckBox</code> is
	 *            recommended.
	 * @param leftControl
	 *            any other control.
	 * @param stretchToFill
	 *            whether the <code>leftControl</code> should stretch to fit the
	 *            remaining width.
	 * @param rightControl
	 *            the element to add on the right.
	 */
	public InspectorRowPanel addRow(JComponent identifier,
			JComponent leftControl, boolean stretchToFill,
			JComponent rightControl) {
		prepare(Position.LEAD, identifier);
		Position pos = stretchToFill ? Position.MAIN_WITH_LEAD_STRETCH_TO_FILL
				: Position.MAIN_WITH_LEAD_NO_STRETCH;
		prepare(pos, leftControl);
		prepare(Position.TRAIL, rightControl);

		JPanel controlPanel = new JPanel(new GridBagLayout());
		controlPanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.fill = stretchToFill ? GridBagConstraints.BOTH
				: GridBagConstraints.NONE;
		c.insets = getInsets(pos, leftControl);
		controlPanel.add(leftControl, c);
		c.gridx++;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE_TRAILING;
		c.insets = getInsets(Position.TRAIL, rightControl);
		controlPanel.add(rightControl, c);
		controlPanel.putClientProperty(PROPERTY_WRAPPED, Boolean.TRUE);
		return addRow(new InspectorRow(identifier, controlPanel, false, 0));
	}

	/**
	 * Appends a new separator to this inspector.
	 * 
	 */
	public InspectorRowPanel addSeparator() {
		return addRow(new InspectorRow(null, new JSeparator(), true, 0));
	}

	/**
	 * Removes all elements from this inspector, usually so elements can be
	 * re-added.
	 */
	public void clear() {
		panel.removeAll();
	}

	/**
	 * Return the insets a given component should use. This method may be called
	 * every time the inspector panel is revalidated/resized.
	 * 
	 * @param position
	 *            the position of this JComponent.
	 * @param c
	 *            the component to identify the insets for.
	 * @return
	 */
	public Insets getInsets(Position position, JComponent c) {
		Boolean wrapped = (Boolean) c.getClientProperty(PROPERTY_WRAPPED);
		if (wrapped != null && wrapped.booleanValue())
			return new Insets(0, 0, 0, 0);

		Insets i = new Insets(3, 3, 3, 3);
		if (position == Position.LEAD) {
			i.left = 5;
			i.right = 6;
		}
		if (position == Position.TRAIL
				|| position == Position.MAIN_ONLY_NO_STRETCH
				|| position == Position.MAIN_ONLY_STRETCH_TO_FILL
				|| position == Position.MAIN_WITH_LEAD_NO_STRETCH
				|| position == Position.MAIN_WITH_LEAD_STRETCH_TO_FILL
				|| position == Position.MAIN_WITH_LEAD_LAST_IN_SERIES) {
			i.right = 5;
		}
		return i;
	}

	/**
	 * Process new components. This may change opacity, borders, or other
	 * properties. This method should only be called once per component.
	 * 
	 * @param position
	 *            the position of this JComponent.
	 * @param component
	 *            the component to prepare for installation.
	 */
	protected void prepare(Position position, JComponent component) {
		if (component instanceof JSlider || component instanceof JRadioButton
				|| component instanceof JCheckBox)
			component.setOpaque(false);

		if (component instanceof JCheckBox || component instanceof JLabel
				|| component instanceof JRadioButton
				|| component instanceof JSlider) {
			component.setBorder(null);
		}
	}

	/**
	 * If true then hidden components in a row do not affect the widths of the
	 * columns.
	 * <p>
	 * If you think rows will frequently change and a constantly changing column
	 * width will be disorienting for the user: you can set this value to false.
	 * The downside is: sometimes that may make certain columns wider than they
	 * need to be to fir the current visible components.
	 * <p>
	 * But if you always want to only accommodate the controls that are showing
	 * (and nothing else), then you can leave this value as true. The downside
	 * is: if the inspector changes as new components are made visible, the
	 * columns may grow/shrink frequently.
	 */
	public boolean isIgnoreHiddenComponents() {
		return true;
	}
}
