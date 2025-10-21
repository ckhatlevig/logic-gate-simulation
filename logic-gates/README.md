## Logic Gate Simulator

## Overview and Motivation

After trying to learn about logic gates by books and videos, I realized that would not be enough to gain a strong intuition for them. I designed this program to allow me to build logic gates from the ground up, starting with simple NOT and AND gates (along with a SPLIT gate to split a signal in two) and being capable of creating full adders and beyond.

## Getting Started

This project was designed and works through VS Code's Java implementation. Set up required you open the repository in VS Code and ensure you have the Java Extension Pack installed. To run the program right click the InterfacePanel.java file under logic-gates/src/ui and run with Java.

## Usage

- The program should launch a window with a large light gray box, three gates shown beneath on the left, and three buttons on the right. Gates can be added by clicking and dragging one of those three original gates into the light gray area.
- Gates can be deleted by clicking and dragging them outside that area.
- An output of a gate can be connected to another gate by clicking and dragging from an output node on a gate and connecting it to an input node on another gate.
- Gates can recieve input from input nodes that can be manually set by the user. Add/remove these with the associated buttons in the bottom right corner.
- Once all gates have their inputs full, the save button can save the gate with a name provided by the user. Unresolved outputs will be considered by the program to be the outputs of the gate and will be listed in the order those gates were added.
- Saved gates will join the original three gates in the bottom of the window and can be added to the workspace in the same ways.

## Future Plans and Known Issues

I built this program primarily to be used by myself, but there are some bugs, quality of life changes, and idiot proofing still to be done.
- If the user tries to save the workspace without having all inputs filled, the program cannot properly clone the space as a gate and breaks.
- If more than ten gates are added, the user cannot access newly saved gates as they are displayed outside the bounds of the window.
- The order of outputs in a saved gate are determined by the order the gates were added to the workspace. I would like to add output nodes that the user can use to set this order.
- Saved gates are only saved for as long as the program is running. I would like to add the ability to keep saved gates between runs of the program and the ability to delete saved gates to keep the space clean.