# Tower of Hanoi Simulation

A Java desktop app that animates moving discs from the first peg to the last, without placing a larger disc on a smaller one.

![Tower of Hanoi animation](documentation/hanoisimulation.gif)

## What you need

A Java Development Kit (JDK) and a graphical desktop. No extra libraries are needed.

## How to run

From the project folder:

```sh
javac -d build src/*.java
java -cp build Flicker
```

Enter the **number of discs first, then the number of pegs**:

```text
4 3
```

This animates a puzzle with 4 discs and 3 pegs. Use **1–9 discs** and **3–9 pegs**.

Close the window to exit. Run the second command again to try another puzzle.
