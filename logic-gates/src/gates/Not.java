package gates;

import java.util.ArrayList;

/**
 * Created by Caleb Hatlevig. The Gate class works off the assumption of some primitive gates that logically operate themselves. As such
 * this gate is simpler and rigid, outputing the inverse of the input signal.
 */
public class Not extends Gate {
    public Not(ArrayList<Wire> inputs, ArrayList<Wire> outputs) {
        super(inputs, outputs, new ArrayList<Gate>(), "NOT");
    }

    @Override
    public void setInput(int n, Wire wire) {
        this.getInputWires().set(n, wire);
        wire.setChildGate(this);
    }

    @Override
    public void updateState() {
        boolean inputState = this.getInputWires().get(0).isFlowing();
        Wire outputWire = this.getOutputWires().get(0);
        outputWire.setFlow(!inputState);
        if (outputWire.getChildGate() != null) {
            outputWire.getChildGate().updateState();
        }
    }

    @Override
    public Gate clone() {
        Not newNot = new Not(new ArrayList<Wire>() {{add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire());}});
        newNot.getInputWires().get(0).setChildGate(newNot);
        newNot.getOutputWires().get(0).setParentGate(newNot);
        return newNot;
    }
}
