package com.pump.plaf;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

/**
 * This KeyListener listens for consecutive keystrokes and then calls
 * {@link #changeSelectionUsingText(KeyEvent, String)}.
 * <p>
 * For example: if a list or tree has the keyboard focus, and then you start
 * typing "qua", then this will help you automatically jump to the list/tree
 * element "quarterly".
 */
public abstract class KeyListenerNavigator extends KeyAdapter {
	StringBuffer typedText = new StringBuffer();
	long lastKeyPress = -1;

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getWhen() - lastKeyPress > 500) {
			typedText.delete(0, typedText.length());
		}
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			if (typedText.length() == 0) {
				return;
			}
			typedText.substring(0, typedText.length() - 1);
		} else {
			char ch = e.getKeyChar();
			if (ch != KeyEvent.CHAR_UNDEFINED) {
				typedText.append(ch);
			}
		}

		lastKeyPress = e.getWhen();
		boolean success = changeSelectionUsingText(e, typedText.toString());
		if (success)
			e.consume();
	}

	/**
	 * Change the selection based on the String a user typed.
	 * 
	 * @param e
	 *            the most recent KeyEvent
	 * @param inputStr
	 *            the String the user typed
	 * @return true if the selection was changed, false if it wasn't. For
	 *         example, if you typed "X" but no elements started with the letter
	 *         X, then this should return false. But if you typed "S" and the
	 *         selection changed to the first element starting with "S", then
	 *         this should return true.
	 */
	protected abstract boolean changeSelectionUsingText(KeyEvent e,
			String inputStr);

	/**
	 * Extract the first String that can be identified from a component.
	 * 
	 * @param component
	 *            any component, but ideally this will be from a
	 *            ListCellRenderer or TreeCellRenderer, and it will contain a
	 *            JTextComponent or JLabel.
	 * @return a String that was extracted from the component, or null if no
	 *         String was identified.
	 */
	protected String getText(Component component) {
		if (component instanceof JTextComponent)
			return ((JTextComponent) component).getText();
		if (component instanceof JLabel)
			return ((JLabel) component).getText();
		if (!(component instanceof Container))
			return null;
		Container container = (Container) component;
		for (int a = 0; a < container.getComponentCount(); a++) {
			String tc = getText(container.getComponent(a));
			if (tc != null && tc.trim().length() > 0)
				return tc;
		}
		return null;
	}
}