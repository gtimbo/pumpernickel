/*
 * @(#)ClipperDemo.java
 *
 * $Date: 2015-02-28 15:59:45 -0500 (Sat, 28 Feb 2015) $
 *
 * Copyright (c) 2011 by Jeremy Wood.
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Jeremy Wood. For details see accompanying license terms.
 * 
 * This software is probably, but not necessarily, discussed here:
 * https://javagraphics.java.net/
 * 
 * That site should also contain the most recent official version
 * of this software.  (See the SVN repository for more details.)
 */
package com.pump.showcase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.pump.blog.Blurb;
import com.pump.blog.ResourceSample;
import com.pump.geom.Clipper;
import com.pump.swing.BasicConsole;

/** A simple demo program for the Clipper class.
 * <P>This offers both a performance analysis and a GUI-based
 * demo (so you can visually inspect the results).
 * 
 * <!-- ======== START OF AUTOGENERATED SAMPLES ======== -->
 * <p><img src="https://javagraphics.java.net/resources/samples/ClipperDemo/sample.png" alt="new&#160;com.bric.geom.ClipperDemo()">
 * <!-- ======== END OF AUTOGENERATED SAMPLES ======== -->
 */
@Blurb (
filename = "Clipper",
title = "Shapes: Clipping to a Rectangle",
releaseDate = "April 2007",
summary = "What's the most common shape we need to clip to?  A <code>Rectangle</code>.\n"+
"<p>But if we use <code>Areas</code> our performance will tank.  This demonstrates a simple model that iterates over a path exactly once to get the clipped shape.\n"+
"<p>(This does not use Sutherland-Hodgman clipping algorithm or the Weiler-Atherton clipping algorithm.)",
instructions = "This applet demonstrates the <Code>Clipper</code> class clipping a few randomly generated shapes.",
link = "http://javagraphics.blogspot.com/2007/04/shapes-clipping-to-rectangle.html",
sandboxDemo = true
)
@ResourceSample( sample="new com.bric.geom.ClipperDemo()" )
public class ClipperDemo extends JPanel {
	static final GeneralPath[] p = new GeneralPath[5];
	private static final long serialVersionUID = 1L;
	static {
		Random r = new Random(0);
		for(int a = 0; a<p.length; a++) {
			p[a] = new GeneralPath();
			p[a].moveTo( (int)(300*r.nextDouble()),
					(int)(300*r.nextDouble()) );
			int size = 20;
			for(int b = 0; b<size; b++) {
				int t = (int)(3*r.nextDouble());
				if(t==0) {
					p[a].lineTo( (int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()) );
				} else if(t==1) {
					p[a].quadTo( (int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()) );
				} else {
					p[a].curveTo( (int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()),
						(int)(300*r.nextDouble()) );
				}
			}
			p[a].closePath();
		}
	}
	
	/** Measure the time and the memory performance of the Clipper
	 * class vs. the Area class.
	 */
	public static void runNumbers(PrintStream dest) {
		int callsPerTest = 50;
		long[] t1 = new long[p.length];
		long[] t2 = new long[p.length];
		long[] t3 = new long[5];
		long[] m1 = new long[p.length];
		long[] m2 = new long[p.length];
		long[] m3 = new long[5];
		dest.println("Running performance tests...");
		Rectangle2D rect = new Rectangle(100,100,100,100);
		Area rArea = new Area(rect);
		for(int a = 0; a<p.length; a++) {
			Area area = null;
			//calculate the area 5 times, and remember the median time.
			for(int b = 0; b<t3.length; b++) {
				m3[b] = Runtime.getRuntime().freeMemory();
				t3[b] = System.currentTimeMillis();
				for(int z = 0; z<callsPerTest; z++) {
					area = new Area(p[a]);
					area.intersect(rArea);
				}
				t3[b] = System.currentTimeMillis()-t3[b];
				m3[b] = m3[b]-Runtime.getRuntime().freeMemory();
				System.runFinalization();
				System.gc();
			}
			Arrays.sort(t3);
			Arrays.sort(m3);
			t1[a] = t3[t3.length/2];
			m1[a] = m3[m3.length/2];
			
			for(int b = 0; b<t3.length; b++) {
				m3[b] = Runtime.getRuntime().freeMemory();
				t3[b] = System.currentTimeMillis();
				for(int z = 0; z<callsPerTest; z++) {
					Clipper.clipToRect(p[a],rect);
				}
				t3[b] = System.currentTimeMillis()-t3[b];
				m3[b] = m3[b]-Runtime.getRuntime().freeMemory();
			}
			Arrays.sort(t3);
			Arrays.sort(m3);
			t2[a] = t3[t3.length/2];
			m2[a] = m3[m3.length/2];
			
			//since this takes a while, let's mention to the console that we're doing something:
			dest.println("Finished "+(a+1)+" out of "+p.length);
			System.runFinalization();
			System.gc();
		}
		Arrays.sort(t1);
		Arrays.sort(t2);
		dest.println("Using an Area class took: "+t1[t1.length/2]+" ms, "+m1[m1.length/2]+" bytes");
		dest.println("Using clipToRect took: "+t2[t2.length/2]+" ms, "+m2[m2.length/2]+" bytes");
	}

	JButton runButton = new JButton("Run");
	BasicConsole console = new BasicConsole(false, false);
	JComboBox<String> comboBox = new JComboBox<>();
	JPanel panel = new JPanel() {
		private static final long serialVersionUID = 1L;
		
		Rectangle2D r = new Rectangle2D.Float(100,100,100,100);
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			GeneralPath s = p[comboBox.getSelectedIndex()];
			g2.setColor(Color.white);
			g2.fillRect(0,0,300,300);
			g2.setColor(Color.blue);
			g2.fill(s);
			GeneralPath s2 = Clipper.clipToRect(s,null,r);
			g2.setColor(new Color(0,255,0,120));
			g2.fill(s2);
			PathIterator i = s.getPathIterator(null);
			float[] f2 = new float[6];
			g.setColor(Color.red);
			while(i.isDone()==false) {
				int k = i.currentSegment(f2);
				if(k==PathIterator.SEG_MOVETO) {
					g2.fill(new Ellipse2D.Float(f2[0]-2,f2[1]-2,4,4));
				} else if(k==PathIterator.SEG_LINETO) {
					g2.draw(new Ellipse2D.Float(f2[0]-2,f2[1]-2,4,4));
				} else if(k==PathIterator.SEG_QUADTO) {
					g2.draw(new Ellipse2D.Float(f2[2]-2,f2[3]-2,4,4));
				} else if(k==PathIterator.SEG_CUBICTO) {
					g2.draw(new Ellipse2D.Float(f2[4]-2,f2[5]-2,4,4));
				}
				i.next();
			}
			g2.setColor(new Color(0,0,0,120));
			g2.draw(r);
		}
	};
	
	public ClipperDemo() {
		super();

		for(int a = 0; a<p.length; a++) {
			comboBox.addItem("Shape #"+(a+1));
		}
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 0;
		add(comboBox,c);
		c.gridy++;
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.repaint();
			}
		});
		panel.setPreferredSize(new Dimension(300,300));
		add(panel,c);
		c.gridy++;
		add(runButton, c);
		c.gridy++; c.fill = GridBagConstraints.BOTH; c.weighty = 1;
		add(new JScrollPane(console), c);
		
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runButton.setEnabled(false);
				Thread thread = new Thread() {
					public void run() {
						try {
							runNumbers(console.createPrintStream(false));
						} finally {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									runButton.setEnabled(true);
								}
							});
						}
					}
				};
				thread.start();
			}
		});
	}
}