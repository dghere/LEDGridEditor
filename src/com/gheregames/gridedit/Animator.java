package com.gheregames.gridedit;
//v1.01
import java.awt.Color;

import javax.swing.JLabel;

public class Animator implements Runnable {
	private boolean run = true;
	private Animation animation;
	private LEDGrid ledGrid;
	private JLabel frameNumberLabel;
	
	private int currentFrame;
	private int endFrame;
	
	public Animator(Animation anim, LEDGrid grid, int cFrame, JLabel fnl)
	{
		animation = anim;
		ledGrid = grid;
		currentFrame = cFrame;
		endFrame = anim.GetAnimation().size();
		frameNumberLabel = fnl;
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
			SetFrameLabel();
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
	
	private void SetFrameLabel()
	{
		frameNumberLabel.setText(""+ (currentFrame + 1) + " of ");
	}
}
