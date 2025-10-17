package gates;

import java.awt.Point;
import java.util.ArrayList;

public class Input extends Gate {
    public Input(ArrayList<Wire> outputs) {
        super(null, outputs, new ArrayList<Gate>(), "INPUT");
    }

    public void setState(boolean state) {
        Wire outputWire = this.getOutputWires().get(0);
        outputWire.setFlow(state);
        if (outputWire.getChildGate() != null) {
            outputWire.getChildGate().updateState();
        }
    }

    @Override
    public void setStartPoint(Wire wire) {
        wire.setStartPoint(new Point(this.getX(), this.getY() + 10));
    }

    @Override
    public void setEndPoint(Wire wire) {
        // System.out.println("Here!");
    }

    @Override
    public void disconnect() {
        this.getOutputWires().get(0).setParentGate(null);
    }

    @Override
    public Gate clone() {
        // This shoudld never be called
        return null;
    }
}
