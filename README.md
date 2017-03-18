# Reversi

https://en.wikipedia.org/wiki/Reversi

This implementation follows the convention of the starting configuration consisting of 2 black and 2 white pieces forming a square in the middle of the board, with the black pieces on the north-east and south-west corners of the square. 

To run this program, compile all files and execute the Game file with two arguments, each either "HUMAN" or "CPU." The first argument corresponds to the first player (Black pieces) and the second argument to the second player (White pieces). Currently, running with argument "CPU" loads the minimax game tree AI as the CPU. However, if another custom Player class is provided, the code can be changed to load the custom class as the CPU instead.
