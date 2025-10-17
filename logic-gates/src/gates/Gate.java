package gates;

import java.awt.Point;
import java.util.ArrayList;

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

    public void setInput(int n, Wire wire) {
        System.out.println("Here!");
        Gate child = inputWires.get(n).getChildGate();
        int index = child.getInputWires().indexOf(inputWires.get(n));
        inputWires.set(n, wire);
        child.setInput(index, wire);
    }

    public void setOutput(int n, Wire wire) {
        outputWires.set(n, wire);
        wire.setParentGate(this);
    }

    public void setStartPoint(Wire wire) {
        int index = this.outputWires.indexOf(wire);
        int total = this.outputWires.size();
        wire.setStartPoint(new Point(x + 100, y + (80 * (index + 1) / (total + 1)) + 5));
    }

    public void setEndPoint(Wire wire) {
        int index = this.inputWires.indexOf(wire);
        int total = this.inputWires.size();
        wire.setEndPoint(new Point(x, y + (80 * (index + 1) / (total + 1)) + 5));
    }

    public Gate clone() {
        ArrayList<Wire> internalWires = new ArrayList<>();
        ArrayList<Wire> referenceWires = new ArrayList<>();

        for (Wire wire : this.inputWires) {
            internalWires.add(wire);
            referenceWires.add(new Wire());
        }

        for (Gate gate : this.internalGates) {
            for (Wire wire : gate.outputWires) {
                internalWires.add(wire);
                referenceWires.add(new Wire());
            }
        }

        ArrayList<Gate> resultInternals = new ArrayList<>();
        for (Gate gate : this.internalGates) {
            Gate newGate = gate.clone();
            for (int i = 0; i < newGate.getInputWires().size(); i++) {
                int index = internalWires.indexOf(gate.getInputWires().get(i));
                newGate.setInput(i, referenceWires.get(index));
            }
            for (int i = 0; i < newGate.getOutputWires().size(); i++) {
                int index = internalWires.indexOf(gate.getOutputWires().get(i));
                newGate.setOutput(i, referenceWires.get(index));
            }
            resultInternals.add(newGate);
        }

        ArrayList<Wire> resultInputs = new ArrayList<>();
        for (Wire wire : this.inputWires) {
            int index = internalWires.indexOf(wire);
            resultInputs.add(referenceWires.get(index));
        }

        ArrayList<Wire> resultOutputs = new ArrayList<>();
        for (Wire wire : this.outputWires) {
            int index = internalWires.indexOf(wire);
            resultOutputs.add(referenceWires.get(index));
        }

        Gate returnGate = new Gate(resultInputs, resultOutputs, resultInternals, this.name);
        for (Gate gate : returnGate.getInternalGates()) {
            gate.setContainer(returnGate);
        }

        return returnGate;
    }

    public void disconnect() {
        for (Wire wire : this.inputWires) {
            wire.setChildGate(null);
        }
        for (Wire wire : this.outputWires) {
            wire.setParentGate(null);
        }
    }

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
