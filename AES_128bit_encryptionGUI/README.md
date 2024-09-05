NOTE: This was based off a project I had created for a cryptography class, in which we had to implement 128-bit AES. The catch was that we had to write it by hand, though -- we weren't allowed to use any libraries or anything. (So, we basically had to write all the operations, like substitution byte, Shift row, Mix-column, AddRound key, etc -- we had to do all that from scratch). Also, for the project, I had originally only implemented it in ECB mode, and it only worked by running it in an IDE/using the command line.

Later on (so, after the class ended), I wanted to practice making a simple GUI, so here I edited my previous project so that there is a GUI that a user can interact with in order to encrypt and decrypt their data. I also edited my old code so that the GUI version would work in CBC mode as well (as ECB is not very secure). I did that by hand as well.

(The original code that I had created for the class is in the "originalAESProject" folder for this repo.)
