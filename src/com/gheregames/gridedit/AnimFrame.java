package com.gheregames.gridedit;

public class AnimFrame {
	
	private int[][] animFrame;
	private int numRows;
	private int numCols;
	
	public AnimFrame(int r, int c)
	{
		numRows = r;
		numCols = c;
		animFrame = new int[r][c];
	}
	
	public int GetNumRows() { return numRows; }
	public int GetNumCols() { return numCols; }
	
	public void SetRowCol(int r, int c, int value)
	{
		animFrame[r][c] = value;
	}
	
	public int GetRowCol(int r, int c)
	{
		return animFrame[r][c];
	}
	
	public int[][] GetAnimFrame() { return animFrame; }
	
	public String GetAnimFrameData()
	{
		String s = "";
		for(int r = 0; r < numRows; r++)
		{
			for(int c =0; c < numCols; c++)
			{
				s += String.format("0x%06X", animFrame[r][c]) + " ";
			}
			s += "\n";
		}
		return s;
	}
	
	public void Clear()
	{
		for(int r = 0; r < numRows; r++)
		{
			for(int c =0; c < numCols; c++)
			{
				SetRowCol(r, c, 0x000000);
			}
		}
	}
	

}