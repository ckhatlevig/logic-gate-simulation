package gates;

import java.util.ArrayList;

public class Split extends Gate {
    public Split(ArrayList<Wire> inputs, ArrayList<Wire> outputs) {
        super(inputs, outputs, new ArrayList<Gate>());
    }

    @Override
    public void setInput(int n, Wire wire) {
        this.getInputWires().set(n, wire);
        wire.setChildGate(this);
    }

    @Override
    public void updateState() {
        boolean inputState = this.getInputWires().get(0).isFlowing();
        Wire outputWire1 = this.getOutputWires().get(0);
        outputWire1.setFlow(inputState);
        Wire outputWire2 = this.getOutputWires().get(1);
        outputWire2.setFlow(inputState);
        if (outputWire1.getChildGate() != null) {
            outputWire1.getChildGate().updateState();
        }
        if (outputWire2.getChildGate() != null) {
            outputWire2.getChildGate().updateState();
        }
    }

    @Override
    public Gate clone() {
        return new Split(new ArrayList<Wire>() {{add(new Wire());}}, new ArrayList<Wire>() {{add(new Wire()); add(new Wire());}});
    }
}
