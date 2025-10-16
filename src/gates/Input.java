package gates;

import java.util.ArrayList;

public class Input extends Gate {
    public Input(ArrayList<Wire> outputs) {
        super(null, outputs, new ArrayList<Gate>());
    }

    public void setState(boolean state) {
        Wire outputWire = this.getOutputWires().get(0);
        outputWire.setFlow(state);
        if (outputWire.getChildGate() != null) {
            outputWire.getChildGate().updateState();
        }
    }

    @Override
    public Gate clone() {
        // This shoudld never be called
        return null;
    }
}
