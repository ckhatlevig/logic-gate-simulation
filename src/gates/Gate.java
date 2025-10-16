package gates;

import java.awt.*;
import java.util.ArrayList;

public class Gate {
    private ArrayList<Wire> inputWires;
    private ArrayList<Wire> outputWires;
    private ArrayList<Gate> internalGates;

    public Gate(ArrayList<Wire> inputs, ArrayList<Wire> outputs, ArrayList<Gate> internalGates) {
        this.inputWires = inputs;
        this.outputWires = outputs;
        this.internalGates = internalGates;
    }

    public ArrayList<Wire> getInputWires() {
        return inputWires;
    }

    public ArrayList<Wire> getOutputWires() {
        return outputWires;
    }

    public void updateState() {
        for (Wire wire : outputWires) {
            if (wire.getChildGate() != null) {
                wire.getChildGate().updateState();
            }
        }
    }

    public void setInput(int n, Wire wire) {
        Gate Child = inputWires.get(n).getChildGate();
        int index = Child.getInputWires().indexOf(inputWires.get(n));
        inputWires.set(n, wire);
        Child.setInput(index, wire);
    }

    public void setOutput(int n, Wire wire) {
        outputWires.set(n, wire);
        wire.setParentGate(this);
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

        return new Gate(resultInputs, resultOutputs, resultInternals);
    }

    public String convertString() {
        String returnString = "Gate with " + inputWires.size() + " inputs and " + outputWires.size() + " outputs and " + internalGates.size() + " internal gates.\n";
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
