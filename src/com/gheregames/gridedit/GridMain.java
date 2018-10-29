package com.gheregames.gridedit;
// v1.01 - 10.22
import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GridMain implements ActionListener {
	
	private static int NUM_ROWS = 8;
	private static int NUM_COLS = 16;
	
	Animator an;
	// Declare class member variables to keep track of the UI controls on the JFrame
	private Animation animation;
	private int currentFrame = 0;
	private int maxFrames = 1;
	private int[][] copyBuffer;
	
	
	private JFrame myFrame = null;
	private JPanel mainPanel = null;
	private JPanel boardPanel = null;
	
	private JButton loadButton;
	private JButton saveButton;
	private JButton clearButton;
	private JTextField pathTextField;
	private JTextField filenameTextField;
	private JCheckBox reverseCheckBox;
	
	private JButton copyButton;
	private JButton pasteButton;
	private JButton shiftLeftButton;
	private JButton shiftRightButton;
	
	private JPanel animControlPanel;
	private JCheckBox animationCheckBox;
	private JButton playButton;
	private JButton stopButton;
	private JButton forwardButton;
	private JButton backButton;
	private JButton addFrameButton;
	private JLabel frameNumberLabel;
	private JLabel maxFramesLabel;
	
	private LEDGrid ledGrid;
	private JButton sampleButton;
	
	private JTextField redTextField;
	private JTextField greenTextField;
	private JTextField blueTextField;
	
	private JColorChooser colorChooser;
	
	private String drive = "E:";
	private String defaultFilename = "ScrollGhost16x4.dat";
	
	//private BorderFactory raisedetched;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GridMain();
	}
	public GridMain()
	{
		// Create new JFrame and set the title. 
		myFrame = new JFrame();
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setTitle("LED Grid Editor");
		myFrame.setResizable(false);	// don't let the user resize or board
				
		// The main panel will contain a vertical box layout 
		mainPanel = (JPanel)myFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		// The top panel just contains the player turn label
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		mainPanel.add(topPanel);
		
		JPanel filePanel = new JPanel();
		filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.PAGE_AXIS));
		filePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "File Options"));
		JPanel controlButtons = new JPanel();
		loadButton = new JButton("Load");
		loadButton.addActionListener(this);
		controlButtons.add(loadButton);
		saveButton = new JButton("save");
		saveButton.addActionListener(this);
		controlButtons.add(saveButton);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		controlButtons.add(clearButton);
		filePanel.add(controlButtons);
		
		JPanel pathTextBoxPanel = new JPanel();
		pathTextBoxPanel.add(new JLabel("Path:"));
		pathTextField = new JTextField(drive + "\\animFiles\\", 15);
		pathTextBoxPanel.add(pathTextField);
		pathTextBoxPanel.add(new JLabel("Filename:"));
		filenameTextField = new JTextField(defaultFilename, 10);
		pathTextBoxPanel.add(filenameTextField);
		pathTextBoxPanel.add(new JLabel("Load/Save LED Format: "));
		reverseCheckBox = new JCheckBox();
		reverseCheckBox.setSelected(true);
		pathTextBoxPanel.add(reverseCheckBox);
		filePanel.add(pathTextBoxPanel);
		
		mainPanel.add(filePanel);
		
		JPanel animPanel = new JPanel();
		//filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.PAGE_AXIS));
		animPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Animation"));
		animPanel.add(new JLabel("Animatied"));
		animationCheckBox = new JCheckBox();
		animationCheckBox.setSelected(true);;
		animationCheckBox.addActionListener(this);
		animPanel.add(animationCheckBox);
		animControlPanel = new JPanel();
		animControlPanel.setVisible(animationCheckBox.isSelected());
		
		backButton = new JButton("<<");
		backButton.addActionListener(this);
		//backButton.setEnabled(animationCheckBox.isSelected());
		animControlPanel.add(backButton);
		stopButton = new JButton("[]");
		stopButton.addActionListener(this);
		animControlPanel.add(stopButton);
		playButton = new JButton(">");
		playButton.addActionListener(this);
		//playButton.setEnabled(animationCheckBox.isSelected());
		animControlPanel.add(playButton);
		forwardButton = new JButton(">>");
		forwardButton.addActionListener(this);
		//forwardButton.setEnabled(animationCheckBox.isSelected());
		animControlPanel.add(forwardButton);
		
		animControlPanel.add(new JLabel("Frame"));
		frameNumberLabel = new JLabel(""+ (currentFrame + 1) + " of ");
		animControlPanel.add(frameNumberLabel);
		maxFramesLabel = new JLabel("" + maxFrames);
		animControlPanel.add(maxFramesLabel);

		addFrameButton = new JButton("+");
		addFrameButton.addActionListener(this);
		animControlPanel.add(addFrameButton);
		animControlPanel.add(new JLabel("Add Frame"));
		
		animPanel.add(animControlPanel);

		mainPanel.add(animPanel);
		
		boardPanel = new JPanel();
		animation = new Animation(NUM_ROWS, NUM_COLS);
		for(int i = 0; i < maxFrames; i++)
			animation.AddFrame();
		System.out.println(animation.GetFrame(currentFrame).GetAnimFrameData(false));
		ledGrid = new LEDGrid(NUM_ROWS, NUM_COLS, boardPanel,this);
		mainPanel.add(boardPanel);
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.PAGE_AXIS));
		editPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Edit Tools"));
		JPanel copyPastePanel = new JPanel();
		copyButton = new JButton("Copy Frame");
		copyButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent ev)
    		{
    			CopyFrame();
    			System.out.println("Copy");
    			
    		}
    	});
		copyPastePanel.add(copyButton);
		pasteButton = new JButton("Paste Frame");
		pasteButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent ev)
    		{
    			PasteFrame();
    			System.out.println("Paste");
    			
    		}
    	});
		copyPastePanel.add(pasteButton);
		editPanel.add(copyPastePanel);
		JPanel shiftPanel = new JPanel();
		shiftLeftButton = new JButton("Shift Left");
		shiftLeftButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent ev)
    		{
    			//shiftLeft
    			ShiftFrameLeft();
    			System.out.println("Shift Left");
    			
    		}
    	});
		shiftPanel.add(shiftLeftButton);
		shiftRightButton = new JButton("Shift Right");
		shiftRightButton.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent ev)
    		{
    			//shiftRight
    			ShiftFrameRight();
    			System.out.println("Shift Right");
    			
    		}
    	});
		shiftPanel.add(shiftRightButton);
		editPanel.add(shiftPanel);
		mainPanel.add(editPanel);
		
		JPanel colorSelector = new JPanel();
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(new ColorSelection());
		mainPanel.add(colorChooser);
		
		sampleButton = new JButton();
		sampleButton.setBackground(new Color(255, 255, 255));
		colorSelector.add(sampleButton);
		
		colorSelector.add(new JLabel("Red"));
		redTextField = new JTextField("255", 3);
		redTextField.addActionListener(this);
		colorSelector.add(redTextField);
		colorSelector.add(new JLabel("Green"));
		greenTextField = new JTextField("255", 3);
		greenTextField.addActionListener(this);
		colorSelector.add(greenTextField);
		colorSelector.add(new JLabel("Blue"));
		blueTextField = new JTextField("255", 3);
		blueTextField.addActionListener(this);
		colorSelector.add(blueTextField);
		
		mainPanel.add(colorSelector);
		
		// we're ready, so pack and show the screen!
		myFrame.pack();
		myFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent source)
	{
		// TODO Auto-generated method stub
		Object src = source.getSource();
		Color newColor;
		if(src == redTextField || src == greenTextField || src == blueTextField)
		{
			int red = Math.min(255, Integer.parseInt(redTextField.getText()));
			int green = Math.min(255, Integer.parseInt(greenTextField.getText()));
			int blue = Math.min(255, Integer.parseInt(blueTextField.getText()));
			
			red = Math.max(0,  red);
			green = Math.max(0,  green);
			blue = Math.max(0,  blue);
			
			redTextField.setText("" + red);
			greenTextField.setText("" + green);
			blueTextField.setText("" + blue);
			
			newColor = new Color(red, green, blue);
			sampleButton.setBackground(newColor);
			colorChooser.setColor(newColor);
		}
		else if(src == loadButton)
		{
			LoadFile();
		}
		else if(src == saveButton)
		{
			SaveFile();
		}
		else if(src == clearButton)
		{
			ledGrid.clearAll();
			animation.GetFrame(currentFrame).Clear();
		}
		else if(src == animationCheckBox)
		{
			System.out.println("Animation: " + animationCheckBox.isSelected());
			animControlPanel.setVisible(animationCheckBox.isSelected());
		}
		else if(src == backButton)
		{
			System.out.println("Back");
			if(currentFrame > 0)
				currentFrame--;
			setFrameLabel();
			SetLEDGrid();
		}
		else if(src == stopButton)
		{
			if(an != null)
			{
				an.SetRun(false);
				currentFrame = an.GetLastFrame();
				an = null;
				SetLEDGrid();
			}
		}
		else if(src == playButton)
		{
			if(an == null)
			{
				an = new Animator(animation, ledGrid, currentFrame, frameNumberLabel);
				Thread t = new Thread(an);
				t.start();
			}
		}
		else if(src == forwardButton)
		{
			System.out.println("Forward");
			//if(currentFrame < maxFrames - 1)
				currentFrame++;
				currentFrame %= maxFrames;
			setFrameLabel();
			
			//  get current frame from animFrame
			//  and set to ledGrid
			SetLEDGrid();
		}
		else if(src == addFrameButton)
		{
			maxFrames++;
			maxFramesLabel.setText("" + maxFrames);
			animation.AddFrame(currentFrame + 1);
			incrementFrame();
			SetLEDGrid();
			
		}
		else 
		{
			Color color = colorChooser.getColor();
			LED led = ledGrid.getClickedSquare(src);
			led.setLED(color);
			animation.GetFrame(currentFrame).SetRowCol(led.GetRow(), led.GetCol(), color.getRGB() & 0x00ffffff);
			//System.out.println(animation.GetFrame(currentFrame).GetAnimFrameData(false));
			//System.out.println("Col: " + ledGrid.getClickedSquare(src).getHexColorRGB());
		}
	}
	private void SaveFile() {
		String fullPath = pathTextField.getText() + filenameTextField.getText();
		System.out.println("Save: " + fullPath);
		//System.out.print(ledGrid.getHexGrid(reverseCheckBox.isSelected()));
		try
		{
			PrintWriter out = new PrintWriter(fullPath);
			//  gethexGrid gets the entire grid as a string...
			//  So, the equivalent for animFrame will be used instead.
			//out.println(ledGrid.getHexGrid(reverseCheckBox.isSelected()));
			System.out.println(maxFrames);
			out.println(maxFrames + " " + NUM_ROWS + " " + NUM_COLS);
			for(int frame = 0; frame < maxFrames; frame++)
			{
				System.out.println(animation.GetFrame(frame).GetAnimFrameData());
				out.println(animation.GetFrame(frame).GetAnimFrameData(reverseCheckBox.isSelected()));
			}
			out.flush();
			out.close();
			
		} catch(IOException e) { e.toString(); }
	}
	private void LoadFile() {
		try
		{
			String fullPath = pathTextField.getText()  + filenameTextField.getText();
			System.out.println("Load: " + fullPath);
			Scanner infile = new Scanner(new File(fullPath));
			
			//  Get number of frames, rows and columns
			maxFrames = infile.nextInt();
			NUM_ROWS = infile.nextInt();
			NUM_COLS = infile.nextInt();
			
			System.out.println("fra: " + maxFrames);
			System.out.println("rows: " + NUM_ROWS);
			System.out.println("cols: " + NUM_COLS);
			animation = new Animation(NUM_ROWS, NUM_COLS);
			for(int i = 0; i < maxFrames; i++)
				animation.AddFrame();
			System.out.println("Animatiom frames length: " + animation.GetAnimation().size());
			//ledGrid = new LEDGrid(NUM_ROWS, NUM_COLS, boardPanel,this);
			//int frame =0;
			infile.nextLine();
			for(int frame = 0; frame < maxFrames; frame++)
			{
				System.out.println("Reading Frame: " + frame + " :: Rows: " + NUM_ROWS);
			for(int i = 0; i < NUM_ROWS; i++)
			{
				String dataIn = infile.nextLine();
				System.out.println("row: " + i + " :: " + dataIn);
				Scanner chop = new Scanner(dataIn);
				int col = 0;
				List<Integer> rowList = new ArrayList<Integer>();
				while(chop.hasNext())
				{
					String data = chop.next();
					//System.out.print(data + " ");
					if(reverseCheckBox.isSelected() && i % 2 == 1)
						rowList.add(0, Integer.decode(data));
					else
						rowList.add(Integer.decode(data));
											
					col++;
				}
				col = 0;
				//  data is now in a row set the current led grid.
				//  also, set the appropriate row in the anim frame.
				for(int number : rowList)
				{
					
					animation.GetFrame(frame).SetRowCol(i, col, number);
					col++;
				}
				//infile.nextLine();
				
				//System.out.println();
				
			}
			if(infile.hasNextLine())
				infile.nextLine();
			//frame++;
			}
			infile.close();
			maxFramesLabel.setText(""+maxFrames);
			SetLEDGrid();
			copyBuffer = null;
			//System.out.println(animation.GetFrame(currentFrame).GetAnimFrameData());
		} catch(Exception e) { e.toString(); }
	}
	private void SetLEDGrid() {
		for(int r = 0; r < NUM_ROWS; r++)
		{
			for(int c = 0; c < NUM_COLS; c++)
			{
				AnimFrame currFrame = animation.GetFrame(currentFrame);
				int dataValue = currFrame.GetRowCol(r, c);
				ledGrid.setLED(r,  c, new Color(dataValue));
				
			}
		}
	}
	
	private void CopyFrame()
	{
		copyBuffer = new int[NUM_ROWS][NUM_COLS];
		for(int r = 0; r < NUM_ROWS; r++)
		{
			for(int c = 0; c < NUM_COLS; c++)
			{
				AnimFrame currFrame = animation.GetFrame(currentFrame);
				copyBuffer[r][c] = currFrame.GetRowCol(r, c);
			}
		}
	}
	
	private void PasteFrame()
	{
		//  Check for same size matrix
		//...
		if(copyBuffer != null)
		{
			for(int r = 0; r < NUM_ROWS; r++)
			{
				for(int c = 0; c < NUM_COLS; c++)
				{
					AnimFrame currFrame = animation.GetFrame(currentFrame);
					currFrame.SetRowCol(r, c, copyBuffer[r][c]);
					ledGrid.setLED(r,  c, new Color(copyBuffer[r][c]));
				}
			}
		}
		
	}
	
	private void ShiftFrameLeft()
	{
		for(int r = 0; r < NUM_ROWS; r++)
		{
			for(int c = 0; c < NUM_COLS - 1; c++)
			{
				AnimFrame currFrame = animation.GetFrame(currentFrame);
				currFrame.SetRowCol(r, c, currFrame.GetRowCol(r, c + 1));
				ledGrid.setLED(r,  c, new Color(currFrame.GetRowCol(r, c)));
			}
		}
	}
	
	private void ShiftFrameRight()
	{
		for(int r = 0; r < NUM_ROWS; r++)
		{
			for(int c = NUM_COLS - 1; c > 0; c--)
			{
				AnimFrame currFrame = animation.GetFrame(currentFrame);
				currFrame.SetRowCol(r, c, currFrame.GetRowCol(r, c - 1));
				ledGrid.setLED(r,  c, new Color(currFrame.GetRowCol(r, c)));
			}
		}
	}
	
	private void incrementFrame()
	{
		currentFrame++;
		if(currentFrame > maxFrames)
			currentFrame = 0;
		setFrameLabel();
		SetLEDGrid();
	}
	
	private void setFrameLabel()
	{
		frameNumberLabel.setText(""+ (currentFrame + 1) + " of ");
	}

	//  Changelistener for the ColorChooser control
	class ColorSelection implements ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e) {
			Color color = colorChooser.getColor();
			redTextField.setText(""+color.getRed());
			greenTextField.setText(""+color.getGreen());
			blueTextField.setText(""+color.getBlue());
			sampleButton.setBackground(color);
		}

	}

}