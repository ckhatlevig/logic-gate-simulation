package gates;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Created by Caleb Hatlevig. This is the heart of the program and defines the properties and behaviors of a gate.
 */
public class Gate {
    private ArrayList<Wire> inputWires;
    private ArrayList<Wire> outputWires;
    private ArrayList<Gate> internalGates;
    private Gate container = null;
    private int x, y;
    private String name;

    public Gate(ArrayList<Wire> inputs, ArrayList<Wire> outputs, ArrayList<Gate> internalGates, String name) {
        this.inputWires = inputs;
        this.outputWires = outputs;
        this.internalGates = internalGates;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<Wire> getInputWires() {
        return inputWires;
    }

    public ArrayList<Wire> getOutputWires() {
        return outputWires;
    }

    public ArrayList<Gate> getInternalGates() {
        return internalGates;
    }

    public Gate getContainer() {
        return container;
    }

    /**
     * Gates contain gates within them. This method returns the highest gate a gate may be contained in.
     * @return The highest gate that includes this gate.
     */
    public Gate getSuperContainer() {
        Gate proposedContainer = this;
        while (proposedContainer.getContainer() != null) {
            proposedContainer = proposedContainer.getContainer();
        }
        return proposedContainer;
    }

    public void setContainer(Gate container) {
        this.container = container;
    }

    public void updateState() {
        for (Wire wire : outputWires) {
            if (wire.getChildGate() != null) {
                wire.getChildGate().updateState();
            }
        }
    }

    /**
     *  Sets the input of this gate to come from a given wire.
     * @param n The index of the input desired to be controlled by the given wire
     * @param wire The wire giving input to this gate
     */
    public void setInput(int n, Wire wire) {
        Gate child = inputWires.get(n).getChildGate();
        int index = child.getInputWires().indexOf(inputWires.get(n));
        inputWires.set(n, wire);
        child.setInput(index, wire);
    }

    /**
     * Sets a wire to be the output of this gate
     * @param n The index of the output desired to control the given wire
     * @param wire The wire to be the output of this gate
     */
    public void setOutput(int n, Wire wire) {
        outputWires.set(n, wire);
        wire.setParentGate(this);
    }

    /**
     * Sets the visual cartesian start point of a given wire to be at the assocaited output node
     * @param wire A wire assumed to be an output of this gate
     */
    public void setStartPoint(Wire wire) {
        int index = this.outputWires.indexOf(wire);
        int total = this.outputWires.size();
        wire.setStartPoint(new Point(x + 100, y + (80 * (index + 1) / (total + 1)) + 5));
    }

    /**
     * Sets the visual cartesian end point of a given wire to be at the associated input node
     * @param wire A wire assumed to be an input of this gate
     */
    public void setEndPoint(Wire wire) {
        int index = this.inputWires.indexOf(wire);
        int total = this.inputWires.size();
        wire.setEndPoint(new Point(x, y + (80 * (index + 1) / (total + 1)) + 5));
    }

    /**
     * Creates a clone of this gate with the same logical behavior through internal gate structure and connections
     * @return A copy of this gate
     */
    public Gate clone() {
        // Creates two lists, one with all the wires of this gate and one that is a list of new empty wires
        ArrayList<Wire> internalWires = new ArrayList<>();
        ArrayList<Wire> referenceWires = new ArrayList<>();
        for (Wire wire : this.inputWires) {
            internalWires.add(wire);
            referenceWires.add(new Wire());
        }
        for (Gate gate : this.internalGates) {
            addToLists(gate, internalWires, referenceWires);
        }

        // Generates the internal gates of the clone with proper connections
        ArrayList<Gate> resultInternals = new ArrayList<>();
        for (Gate gate : this.internalGates) {
            generateInternals(gate, internalWires, referenceWires, resultInternals);
        }

        // Determines the input wires to this gate
        ArrayList<Wire> resultInputs = new ArrayList<>();
        for (Wire wire : this.inputWires) {
            int index = internalWires.indexOf(wire);
            resultInputs.add(referenceWires.get(index));
        }

        // Determines the output wires of this gate
        ArrayList<Wire> resultOutputs = new ArrayList<>();
        for (Wire wire : this.outputWires) {
            System.out.println("Parent " + wire.getParentGate());
            int index = internalWires.indexOf(wire);
            resultOutputs.add(referenceWires.get(index));
        }

        // Initializes the gate and sets its internal gates to have it as their container
        Gate returnGate = new Gate(resultInputs, resultOutputs, resultInternals, this.name);
        for (Gate gate : returnGate.getInternalGates()) {
            gate.setContainer(returnGate);
        }

        return returnGate;
    }

    /**
     * A helper method for the clone method. This adds all the output wires within the gate to to the internalWires and creates all the
     * associated referenceWires.
     * @param inputGate The gate to get the output wires from
     * @param internalWires A list of the internal wires of this gate
     * @param referenceWires A list of new wires associated with the internalWires
     */
    public void addToLists (Gate inputGate, ArrayList<Wire> internalWires, ArrayList<Wire> referenceWires) {
        if (inputGate.getInternalGates().size() == 0) {
                for (Wire wire : inputGate.getOutputWires()) {
                internalWires.add(wire);
                referenceWires.add(new Wire());
                }
            } else {
                for (Gate gate : inputGate.getInternalGates()) {
                    addToLists(gate, internalWires, referenceWires);
                }
            }
    }

    /**
     * A helper method to the clone method. Given an input gate this adds clones of its internal gates to to the clone's internal structure.
     * @param inputGate The gate to get the internal gates of
     * @param internalWires A list of the internal wires of this gate
     * @param referenceWires A list of new wires assocaited with the internal wires
     * @param result The internal gates of the clone of this gate
     */
    public void generateInternals (Gate inputGate, ArrayList<Wire> internalWires, ArrayList<Wire> referenceWires, ArrayList<Gate> result) {
        if (inputGate.getInternalGates().size() == 0) {
            Gate newGate = inputGate.clone();
            for (int i = 0; i < newGate.getInputWires().size(); i++) {
                int index = internalWires.indexOf(inputGate.getInputWires().get(i));
                newGate.setInput(i, referenceWires.get(index));
            }
            for (int i = 0; i < newGate.getOutputWires().size(); i++) {
                int index = internalWires.indexOf(inputGate.getOutputWires().get(i));
                newGate.setOutput(i, referenceWires.get(index));
            }
            result.add(newGate);
        } else {
            for (Gate gate : inputGate.getInternalGates()) {
                generateInternals(gate, internalWires, referenceWires, result);
            }
        }
    }

    /**
     * Disconnects the gate from its associated wires so it can be garbage
     */
    public void disconnect() {
        for (Wire wire : this.inputWires) {
            wire.setChildGate(null);
        }
        for (Wire wire : this.outputWires) {
            wire.setParentGate(null);
        }
    }

    /**
     * A debugging method to see what this gate is, what it contains, and how its internal environment is structured. This is not an override
     * of the Object toString() method as being able to see the gate object's individualized name currently given by toString() is useful.
     * @return A string representing this gate
     */
    public String convertString() {
        String returnString = this.getName() + " Gate with " + inputWires.size() + " inputs and " + outputWires.size() + " outputs and " + internalGates.size() + " internal gates.\n";
        for (Wire wire : inputWires) {
            returnString += "Input wire has parent: " + wire.getParentGate() + " and child " + wire.getChildGate() + "\n";
        }
        for (Wire wire : outputWires) {
            returnString += "Output wire has parent: " + wire.getParentGate() + " and child " + wire.getChildGate() + "\n";
        }
        for (Gate gate : internalGates) {
            returnString += gate.convertString();
        }
        return returnString;
    }
}
