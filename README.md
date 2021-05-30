# TicTacToe with MiniMaxAlgorithm
<h3>Programming project for the lecture Advanced Programming</h3>
<p>Daniel Jauß, Gianluca Pierro, Lea Mayer, Lukas Lange, Sophia Bühl, Jonathan Baß</p>

## Preliminary Remarks
You have to set up IntelliJ IDE for JavaFX to work. A short tutorial can be found at the end. <br>
There you will also find a link for the JavaFX sdk download. Please make sure to download the correct version! <br>
In the original projekt, music is played in the background. It changes depending on the scene and difficulty. <br>
Because of copyright reasons we removed the original music a replaced it with a temporary dummy. <br>
To use the music-feature you just have to copy your music files into res/game/ and change the constants in line<br>
76 to 79 to the file name (not the URL or URI! These are generated automatically.).

## TicTacToe
TicTacToe is a paper-and-pencil game for two players, X and O, who takt turns marking the spaces in a 3x3 grid. <br>
The player who succeeds in placing three of their marks in a diagonal, horizontal, or vertical row is the winner. <br>
The game UI is build using JavaFX and follows a Model-View-Controller structure. <br>
The main game consists of three individual scenes:

### MainMenu
The main menu is the scene shown at startup and enables the user to choose a difficulty for the game. <br>
Furthermore you can open the settings scene by clicking on the corresponding button. <br>
And of course, you can exit the game.

### Game
The game-scene consists out of nine tiles (If the boardWidth is set to 3). The Tile class is a child of the button class. <br>
Each Tile can be clicked to set the "O" when it is your turn. <br>
Depending on the chosen gameMode, you will be playing against different "AIs". <br>
- Easy: The AI places the "X" on a random Tile. <br>
- Medium: The "X" is placed with a probability of 50% from the MiniMax "AI". <br>
- Hard: The AI places the "X" on a Tile based of the algorithm. <br>

### Settings
In the settings' scene, there is a short description of the game and how to play it. <br>
In addition you can adjust the volume of the music. <br>
<b>NOTE: Do not press the button on the bottom!</b>

## The MiniMax Algorithm
The minimax algorithm used in this project is a variation form the vanilla version. <br>
It improves the speed of a win or a tie by adding the amount of moves it would take to reach a certain constellation into consideration. <br>
This way, wins are targeted faster and the human player has no possibility to win while playing the hard mode. <br>
The best achievable outcome is a tie.

## Setting up IntelliJ for JavaFX
Instructions on how to set up the IntelliJ IDE:<br>
NOTE: This project was build using Java16. For best compatibility use JavaFX sdk 16! <br>
[1. Download JavaFX packages] https://gluonhq.com/products/javafx/ <br>
[2. Adding JavaFX as library] https://www.youtube.com/watch?v=WtOgoomDewo <br>
[Troubleshooting] https://www.jetbrains.com/help/idea/javafx.html <br>
