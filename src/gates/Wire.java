package gates;

import java.util.ArrayList;

public class Wire{
    private boolean flowing;
    private Gate parentGate;
    private Gate childGate;
    
    public Wire() {
        this.flowing = false;
        this.parentGate = null;
        this.childGate = null;
    }

    public boolean isFlowing() {
        return flowing;
    }

    public void setFlow(boolean flowing) {
        this.flowing = flowing;
    }

    public Gate getParentGate() {
        return parentGate;
    }

    public void setParentGate(Gate parentGate) {
        this.parentGate = parentGate;
    }

    public Gate getChildGate() {
        return childGate;
    }

    public void setChildGate(Gate childGate) {
        this.childGate = childGate;
    }
}
