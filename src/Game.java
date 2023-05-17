

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.*;

/**
 * Game class, containing all the necessary data and GUI for the game
 * This is the frame of the game which contains all component for the game
 */
public class Game extends JFrame {

	private JButton[][] buttons; // The Grid buttons
	private JPanel topPanel; // Top panel containing labels
	private JPanel boardPanel; // Panel containing the grid of buttons
	private JLabel flagsLabel; // Flags remaining label
	private JLabel timeLabel; // Timer label
   private JMenu menu1;
   private JMenuBar menuBar1;
   public static JMenuItem menuItem1;//menu item for New Game
   public static JMenuItem menuItem2;//menu item for Rules of the Game
   public static JMenuItem menuItem3;//menu item for exit the program
	private int numberOfMines = 0; // The no. of mines in the field
	private int[][] mineField; // 2-D array containing info for each field
	private boolean[][] revealed; // 2-D array of all revealed fields
	private int numberOfRevealed; // Number of fields revealed
	private boolean[][] flagged; // 2-D array of all flagged field
	private Image flag; //The Image of the flag
	private Image mine; //The Image of the mine
	public static final int CELL_SIZE = 30;
	private final int size; //The size of the board 
	private final int difficulty;//The difficulty of the game
   public static Thread timer;
   public static int timePassed;
   public static boolean stopTimer;      
   public static int begn; 
   public static int timer1;
    

	public Game(int size, int difficulty) {
		numberOfMines = size * (1 + difficulty / 2);
		this.setSize(size * CELL_SIZE, size * CELL_SIZE + 50);
		this.setTitle("Minesweeper");
		this.size = size;
		this.difficulty = difficulty;
      this.begn =0;
		setLocationRelativeTo(null);///The window is placed in the center of the screen
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	}

	/**
	 * Initialize mine field, places mines and numbers around mines
	 * 
	 * @param size
	 */
	private void initializeMineField(int size) {
		Random rand = new Random();

		// initialize mine field 2d array with all zeroes
		this.mineField = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.mineField[i][j] = 0;
			}
		}
      
		int minesCounter = 0;
		int xPoint;
		int yPoint;
		// while minesCounter is less than total number of mines, keep adding mines
		while (minesCounter < numberOfMines) {
			// get random integers for mines coordinates
			xPoint = rand.nextInt(size);
			yPoint = rand.nextInt(size);
			// check if there's already a mine on this field
			if (this.mineField[xPoint][yPoint] != -1) {
				this.mineField[xPoint][yPoint] = -1; // -1 represents bomb
				minesCounter++;
			}
		}

		// Fill fields adjacent to mine fields with numbers
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// loop until a bomb field is found
				if (this.mineField[i][j] == -1) {
					// loop all the adjacent fields
					for (int k = -1; k <= 1; k++) {
						for (int l = -1; l <= 1; l++) {
							// if the field is out of bounds, continue
							if ((i + k) < 0 || (i + k) >= this.size || (j + l) < 0 || (j + l) >= this.size) {
								continue;
								// if the field is not a bomb field, increment its number
							} else if (this.mineField[i + k][j + l] != -1) {
								this.mineField[i + k][j + l] += 1;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Initializes all the data and starts a new game
	 * 
	 * @param Boolean value restart showing if the user restarted the game
	 */
	public void start(boolean restart) {
		numberOfRevealed = 0;

		// set sizes for revealed and flagged 2d arrays
		revealed = new boolean[this.size][this.size];
		flagged = new boolean[this.size][this.size];

		// initialize their values to false
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				revealed[i][j] = false;
				flagged[i][j] = false;
			}
		}
		// Add mines to the board
		initializeMineField(this.size);

		Gui();

		
	}

	/**
	 * Initializes all the GUI components
	 */
	private void Gui() {
		FieldClickListener myMouseListener = new FieldClickListener(this);
		JPanel mainPanel = new JPanel();

		this.topPanel = new JPanel();
		this.boardPanel = new JPanel();

		// Images
		images();

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		this.topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		flagsLabel = new JLabel("Flags = " + this.numberOfMines);

		this.timeLabel = new JLabel(" Time : 0");
		this.timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		this.timeLabel.setHorizontalAlignment(JLabel.RIGHT);

		this.topPanel.add(flagsLabel);
		this.topPanel.add(Box.createRigidArea(new Dimension((this.size - 1) * 15 - 80, 50)));
		this.topPanel.add(Box.createRigidArea(new Dimension((this.size - 1) * 15 - 85, 50)));
		this.topPanel.add(this.timeLabel);
      
      menuBar1 = new JMenuBar();
      menu1 = new JMenu();
      menuItem1 = new JMenuItem();
      menuItem2 = new JMenuItem();
      menuItem3 = new JMenuItem();
      menu1.setText("Game");
      // the keystroke combination which will invoke the JMenuItem's actionlisteners without navigating the menu hierarchy
      menuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
      menuItem1.setText("New game");
      menuItem2.setText("Rules");
      menuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
      menuItem3.setText("Exit");   
      menuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
      menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuItem1ActionPerformed(evt);
            }
        });
      menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuItem1ActionPerformed(evt);
            }
        });
     menuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuItem1ActionPerformed(evt);
            }
        }); 
     menu1.add(menuItem1);
     menu1.add(menuItem2);
     menu1.add(menuItem3);
     menuBar1.add(menu1);

     setJMenuBar(menuBar1);
	  this.boardPanel.setLayout(new GridLayout(this.size, this.size));
	  this.buttons = new JButton[this.size][this.size];

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.buttons[i][j] = new JButton();
				this.buttons[i][j].setBorder(new LineBorder(Color.BLACK));
				this.buttons[i][j].setBorderPainted(true);//If the button has a border, the border is painted. 
				this.buttons[i][j].setName(i + " " + j);
				this.buttons[i][j].addMouseListener(myMouseListener);
				this.boardPanel.add(this.buttons[i][j]);
			}
		}
		// Both panels done

		mainPanel.add(this.topPanel);
		mainPanel.add(this.boardPanel);
		this.setContentPane(mainPanel);
		this.setVisible(true);
	}

	/**
	 * Read images and scale them to the cell size
	 */
	private void images() {
		try {
      //Finds a resource with a given name
      //Creates a scaled version of this image.
      //Choose an image-scaling algorithm that gives higher priority to image smoothness than scaling speed.
			flag = ImageIO.read(getClass().getResource("images/flag.png")).getScaledInstance(CELL_SIZE - 10,
					CELL_SIZE - 10, Image.SCALE_SMOOTH);

			mine = ImageIO.read(getClass().getResource("images/mine.png")).getScaledInstance(CELL_SIZE - 10,
					CELL_SIZE - 10, Image.SCALE_SMOOTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * Checks if the user has won the game. numberOfRevealed + numberOfMines must be
	 * equal to the total number of fields
	 * 
	 * @return Boolean value representing if the user has won the game
	 */
	private boolean userWon() {

		return this.numberOfRevealed == (Math.pow(this.mineField.length, 2) - this.numberOfMines);
	}

	/**
	 * Right click handler method.
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	public void rightButtonClicked(int x, int y) {
		// if the field is not revealed
		if (!revealed[x][y]) {
			// if it's already flagged, remove the flag
			if (flagged[x][y]) {
				buttons[x][y].setIcon(null);
				flagged[x][y] = false;
				int old = Integer.parseInt(this.flagsLabel.getText().substring(8));
				this.flagsLabel.setText("Flags = " + ++old);
				// else, flag the field
			} else {
				if (Integer.parseInt(this.flagsLabel.getText().substring(8)) > 0) {
					buttons[x][y].setIcon(new ImageIcon(flag));
					flagged[x][y] = true;
					int old = Integer.parseInt(this.flagsLabel.getText().substring(8));
					this.flagsLabel.setText("Flags = " + --old);
				}
			}
		}
	}
     public void menuItem1ActionPerformed(ActionEvent evt) 
         {
        if(evt.getSource()==menuItem1){
		      restartGame(false);
            JOptionPane.showMessageDialog(this, "New Game" );
       }
        
        else if(evt.getSource() == menuItem2){
            JOptionPane.showMessageDialog(this, "The objective in Minesweeper is to find and mark all the mines \n hidden under the grey squares, in the shortest time possible. \n This is done by clicking on the squares to open them. \n 1. A mine, and if you click on it you'll lose the game.  \n 2. A number, which tells you how many of its adjacent squares have mines in them. \n  3. Nothing. In this case you know that none of the adjacent squares have mines, and they will be automatically opened as well. " );
        
        }
        else if(evt.getSource() == menuItem3){
            System.exit(0);
        }
        
          }
          
	/**
	 * Button click handler method
	 * 
	 * @param x coordinate of clicked field
	 * @param y coordinate of clicked field
	 */
	public void leftButtonClicked(int x, int y) {
		JButton clickedButton = buttons[x][y];
		int currentField = mineField[x][y];

		if (!revealed[x][y] && !flagged[x][y]) {

			revealed[x][y] = true;

			/**
			 * currentField possible values: -1 : mine field, 0 : empty field, 1 : field
			 * with mine next to it
			 */

			switch (currentField) {

			// user clicked on mine
			case -1:
            interruptTimer();
				clickedButton.setIcon(new ImageIcon(mine));
				clickedButton.setBackground(Color.RED);
				restartGame(false);
				break;

			// user clicked on field with 0 mines around
			case 0:
            if(begn == 0){
               startTimer();
               begn++;
            }
				clickedButton.setBackground(Color.lightGray);
 				++this.numberOfRevealed;

				if (userWon()) {
					restartGame(true);
					return;
				}

				// Else recurse around
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						// check if next field is out of bounds
						if ((x + i) < 0 || (x + i) >= this.size || (y + j) < 0 || (y + j) >= this.size) {
							continue;
						}
						// else, recursively call the buttonClicked method
						leftButtonClicked(x + i, y + j);
					}
				}
				return;

			// user clicked on any field with a number on it
			default:
            if(begn == 0){
               startTimer();
               begn++;
            }
				clickedButton.setText(Integer.toString(currentField));
				clickedButton.setBackground(Color.LIGHT_GRAY);
				++this.numberOfRevealed;
				if (userWon()) {
					restartGame(true);
					return;
				}

				break;
			}
		}

	}

	/**
	 * Restarts the game with current size and difficulty
	 */
	private void restartGame(boolean hasWon) {
		if (!hasWon) {
			JOptionPane.showMessageDialog(this, "Game Over!", null, JOptionPane.ERROR_MESSAGE);
         resetTimer();
		} else {
         resetTimer();
			JOptionPane.showMessageDialog(this, "Congratulations! You've Won!");
         
		}
		this.dispose();//The dispose-window default window close operation.
		Game game = new Game(this.size, this.difficulty);
		game.setLocationRelativeTo(this);
		game.start(false);
	}
   
  public void startTimer()
    {        
        stopTimer = false;
        
        timer = new Thread() {
                @Override
                public void run()
                {
                    while(!stopTimer)
                    {
                        timePassed++;

                        // Update the time passed label.
                        timeLabel .setText(" Timer: " + timePassed + "  ");

                        // Wait 1 second.
                        try{
                            
                            sleep(1000); 
                        }
                        catch(InterruptedException ex){}
                    }
                }
        };                

       timer.start();
    }

 ///////////////////////////////////////////////////////   
    public void interruptTimer()
    {
        stopTimer = true;
                
        try 
        {
            if (timer!= null)
                timer.join();
        } 
        catch (InterruptedException ex) 
        {

        }      
        
          
    }
/////////////////////////////////////////////////// /   
    public int getTimePassed(){
      return timePassed;
    }
 ////////////////////////////////////////////////////////   
    public void resetTimer()
    {
        timePassed = 0;
        timeLabel .setText("Timer: " + timePassed + "  ");        
    }
   
   
}

class FieldClickListener implements MouseListener {
	Game parent;

	FieldClickListener(Game parent) {
		this.parent = parent;
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		JButton clickedButton = (JButton) mouseEvent.getSource();
      //Returns true if the mouse event specifies the right mouse button.
		if (SwingUtilities.isRightMouseButton(mouseEvent)) {
			// get the name of the button
			String[] xy = clickedButton.getName().split(" ", 2);
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			// pass the x and y coordinates to the click handler method
			parent.rightButtonClicked(x, y);
		} else {
			String[] xy = clickedButton.getName().split(" ", 2);
			int x = Integer.parseInt(xy[0]);
			int y = Integer.parseInt(xy[1]);
			parent.leftButtonClicked(x, y);
		}
	}
}



