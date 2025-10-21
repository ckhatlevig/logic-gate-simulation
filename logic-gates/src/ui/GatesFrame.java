package ui;
import gates.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Created by Caleb Hatlevig. This is a gate representing the workspace. The main utility is to reuse the clone method from the superclass
 * to save the gate.
 */
public class GatesFrame extends Gate{
    public static ArrayList<Gate> savedGates = new ArrayList<>();

    public GatesFrame() {
        super(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "GATES_FRAME");

        Not savedNot = new Not(new ArrayList<Wire>() {{add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire());}});
        savedNot.getInputWires().get(0).setChildGate(savedNot);
        savedNot.getOutputWires().get(0).setParentGate(savedNot);
        savedGates.add(savedNot);
        
        And savedAnd = new And(new ArrayList<Wire>() {{add(new Wire()); add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire());}});
        savedAnd.getInputWires().get(0).setChildGate(savedAnd);
        savedAnd.getInputWires().get(1).setChildGate(savedAnd);
        savedAnd.getOutputWires().get(0).setParentGate(savedAnd);
        savedGates.add(savedAnd);

        Split savedSplit = new Split(new ArrayList<Wire>() {{add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire()); add(new Wire());}});
        savedSplit.getInputWires().get(0).setChildGate(savedSplit);
        savedSplit.getOutputWires().get(0).setParentGate(savedSplit);
        savedSplit.getOutputWires().get(1).setParentGate(savedSplit);
        savedGates.add(savedSplit);
    }

    public void save() {
        for (Gate gate : this.getInternalGates()) {
            for (Wire wire : gate.getInputWires()) {
                if (wire.getParentGate().getName() == "INPUT") {
                    this.getInputWires().add(wire);
                }
            }
            for (Wire wire : gate.getOutputWires()) {
                if (wire.getChildGate() == null) {
                    this.getOutputWires().add(wire);
                }
            }
        }
        this.setName(JOptionPane.showInputDialog(null, "Gate name:"));
        savedGates.add(this.clone());
        clear();
    }

    public void addGate(Gate gate) {
        this.getInternalGates().add(gate);
    }

    public void removeGate(Gate gate) {
        gate.disconnect();
        this.getInternalGates().remove(gate);
    }

    public void clear() {
        this.getInternalGates().clear();
        this.getInputWires().clear();
        this.getOutputWires().clear();
    }

    public void connect(Gate parent, int parentOutputIndex, Gate child, int childInputIndex) {
        Wire wire = parent.getOutputWires().get(parentOutputIndex);
        child.setInput(childInputIndex, wire);
    }
}
