package com.gheregames.gridedit;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class LEDGrid {
	// define the number of rows and columns as constants
	private int NUM_ROWS;// = 6;
	private int NUM_COLS;// = 10;
		
	private LED[][] grid;
	
	public LEDGrid(int rows, int cols, JPanel boardPanel, ActionListener listener)
	{
		NUM_ROWS = rows;
		NUM_COLS = cols;
		// create a grid layout with the 9x9 grid
		GridLayout layout = new GridLayout(NUM_ROWS, NUM_COLS);
		
		// we don't want any gap between the buttons
		layout.setHgap(0);
		layout.setVgap(0);
		
		boardPanel.setLayout(layout);
		
		// create new 2D array of GameSquare objects
		grid = new LED[NUM_ROWS][NUM_COLS];
		
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				// create new GameSquare object for this column and row.  Pass in
				// the panel and action listener so the button can be linked up!
				grid[row][col] = new LED(boardPanel, row, col, listener);
			}
		}
	}
	
	public void clearAll()
	{
		for (int row = 0; row < NUM_ROWS; row++)
		{
			for (int col = 0; col < NUM_COLS; col++)
			{
				// create new GameSquare object for this column and row.  Pass in
				// the panel and action listener so the button can be linked up!
				grid[row][col].setLED(Color.black);
			}
		}
	}
	
	public void setLED(int row, int col, Color c)
	{
		grid[row][col].setLEDColor(c);
	}
	
	public String getHexGrid(boolean reverse)
	{
		String s = "";
		for(int row = 0; row < NUM_ROWS; row++)
		{
			s += getHexRow(row, reverse);
			s += "\n";
		}
		
		return s;
	}
	
	public String getHexRow(int row, boolean reverse)
	{
		String s = "";
		
		for(int col = 0; col < NUM_COLS; col++)
		{
			//  Grab the row one at a time and add to string
			if(reverse && row % 2 == 1)
				s += grid[row][(NUM_COLS-1)-col].getHexColorRGB() + " ";
			else
				s += grid[row][col].getHexColorRGB() + " ";
			
				
				
		}
		//s += "\n";
		
		return s;
	}
	
	// find which game square, if any, matches the click event object (button)
	public LED getClickedSquare(Object source)
	{
		// check every game square in the array
		for (int col = 0; col < NUM_COLS; col++)
		{
			for (int row = 0; row < NUM_ROWS; row++)
			{
				LED square = grid[row][col];
				
				// if this square was the one that was clicked
				if (square.isClicked(source))
				{	
					return square;	// return the clicked square!
				}
			}
		}
			
		return null;	// could not find clicked square
	}
		
		

}