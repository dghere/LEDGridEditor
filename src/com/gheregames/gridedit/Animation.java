package com.gheregames.gridedit;

import java.util.ArrayList;
import java.util.List;

public class Animation {

	private List<AnimFrame> animation;
	private int numRows;
	private int numCols;
	
	public Animation(int r, int c)
	{
		animation = new ArrayList<AnimFrame>();
		numRows = r;
		numCols = c;
	}
	
	public void AddFrame()
	{
		animation.add(new AnimFrame(numRows, numCols));
	}
	public void AddFrame(int num)
	{
		animation.add(num, new AnimFrame(numRows, numCols));
	}
	public void AddFrame(AnimFrame frame)
	{
		animation.add(frame);
	}
	
	public AnimFrame GetFrame(int frame) { return animation.get(frame); }
	public List<AnimFrame> GetAnimation() { return animation; };
	
}