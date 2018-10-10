package com.gheregames.gridedit;

import java.awt.Color;

public class Animator implements Runnable {
	private boolean run = true;
	private Animation animation;
	private LEDGrid ledGrid;
	
	private int currentFrame;
	private int endFrame;
	
	public Animator(Animation anim, LEDGrid grid, int cFrame)
	{
		animation = anim;
		ledGrid = grid;
		currentFrame = cFrame;
		endFrame = anim.GetAnimation().size();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(run)
		{
			//System.out.println("Thread");
			try{
			currentFrame++;
			if(currentFrame > endFrame-1)
				currentFrame = 0;
			SetLEDGrid();
			Thread.sleep(100);}catch(Exception e){}
		}
	}

	public void SetRun(boolean value) { run = value; }
	public boolean GetRun() { return run; }

	public int GetLastFrame() { return currentFrame; }
	
	private void SetLEDGrid() {
		for(int r = 0; r < animation.GetFrame(currentFrame).GetNumRows(); r++)
		{
			for(int c = 0; c < animation.GetFrame(currentFrame).GetNumCols(); c++)
			{
				AnimFrame currFrame = animation.GetFrame(currentFrame);
				int dataValue = currFrame.GetRowCol(r, c);
				ledGrid.setLED(r,  c, new Color(dataValue));
				
			}
		}
	}
}
