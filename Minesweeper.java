

import javax.swing.JOptionPane;

/**
 * Class with main method, starts the board size window, difficulty dialog and
 * creates a new Game object.
 * 
 */
public class Minesweeper {
	private static Minesweeper minesweeper;



	/**
	 * Method which opens the difficulty dialog and then starts the game.
	 * each difficulty will assigned with specified size	 
      0
  	 */
	public void proceed() {
      int size = 8;
		int difficulty = 1;
		Object[] options = { "Easy", "Medium", "Hard" };
		difficulty = JOptionPane.showOptionDialog(null, "Please select difficulty", "Difficulty",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);//an integer indicating the option chosen by the user, or CLOSED_OPTION if the user closed the dialog
      if (difficulty == -1)
			System.exit(0);
      else if(difficulty == 0){
         size = 8;
      }
      else if(difficulty == 1){
         size = 12;
      }
      else if(difficulty == 2){
         size = 16;
      }
		// start the game itself
		Game game = new Game(size, difficulty);
		game.start(false);
	}

	public static void main(String[] args) {
		minesweeper = new Minesweeper();
		minesweeper.proceed();
	}
}