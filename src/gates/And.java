package gates;

import java.util.ArrayList;

public class And extends Gate {
    public And(ArrayList<Wire> inputs, ArrayList<Wire> outputs) {
        super(inputs, outputs, new ArrayList<Gate>());
    }

    @Override
    public void setInput(int n, Wire wire) {
        this.getInputWires().set(n, wire);
        wire.setChildGate(this);
    }

    @Override
    public void updateState() {
        boolean inputState1 = this.getInputWires().get(0).isFlowing();
        boolean inputState2 = this.getInputWires().get(1).isFlowing();
        Wire outputWire = this.getOutputWires().get(0);
        outputWire.setFlow(inputState1 && inputState2);
        if (outputWire.getChildGate() != null) {
            outputWire.getChildGate().updateState();
        }
    }

    @Override
    public Gate clone() {
        return new And(new ArrayList<Wire>() {{add(new Wire()); add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire());}});
    }
}
