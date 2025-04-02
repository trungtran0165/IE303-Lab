# Flappy Bird Game

This is a simple implementation of the Flappy Bird game using Java Swing.

## Requirements

-   Java Development Kit (JDK) 8 or higher
-   The following image files in the `images` folder within the project directory:
    -   images/flappybirdbg.png (game background)
    -   images/flappybird.png (bird sprite)
    -   images/toppipe.png (top pipe sprite)
    -   images/bottompipe.png (bottom pipe sprite)

## How to Run

1. Compile all Java files:

    ```
    javac *.java
    ```

2. Run the game:
    ```
    java FlappyBird
    ```

## Game Controls

-   Press SPACE or ENTER to make the bird fly
-   Press R to restart the game after Game Over

## Implementation

The game consists of three main classes:

-   FlappyBird: The main game class that handles the game loop and rendering
-   Bird: Represents the player character with physics
-   Pipe: Represents the obstacles that the bird must avoid

The game features:

-   A bird that falls due to gravity and can fly up when the player presses space
-   Pipes that move from right to left with random heights
-   Score tracking when passing through pipes
-   Game over when hitting pipes or the ground/ceiling
-   Restart functionality
