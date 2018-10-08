package com.gheregames.gridedit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class LED {
	public static Color[] colors = {Color.red, Color.green, Color.blue};
	private Color ledColor;
	private JButton led;
	
	private int row;
	private int col;
	int colorCode = 0;

	// These static borders can be used on any square (selected or unselected)
		static Border selectedBorder = BorderFactory.createLineBorder(Color.yellow,3);
		static Border unselectedBorder = BorderFactory.createLineBorder(Color.white,1);
	
	public LED (JPanel panel, int r, int c, ActionListener listener)
	{
		row = r;
		col = c;
		
		led = new JButton();
		led.setPreferredSize(new Dimension(32,32));
		ledColor = Color.black;
		led.setBackground(ledColor);
		led.setBorder(unselectedBorder);
		led.addActionListener(listener);
		
		panel.add(led);
	}
	
	public void setLEDColor(Color c)
	{
		led.setBackground(c);
	}
	
	public JButton getLED() { return led; }
	public int GetRow() { return row; }
	public int GetCol() { return col; }
	
	public int getColorRGB()
	{
		int argb = led.getBackground().getRGB();
		
		int rgb = argb & 0x00ffffff;
		
		return rgb;
	}
	
	public String getHexColorRGB()
	{
		return String.format("0x%06X", getColorRGB());
	}
	
	// returns true if the source object equals the internal JButton
		public boolean isClicked(Object source)
		{
			if (source == led)
				return true;
			else
				return false;
			
		}
	

		
	public void setLED(Color c)
	{
		led.setBackground(c);
	}
	
	public void cycleColor()
	{
		colorCode++;
		colorCode %= 3;
		led.setBackground(colors[colorCode]);
	}
}