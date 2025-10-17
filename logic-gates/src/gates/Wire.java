package gates;

import java.awt.Point;

public class Wire{
    private boolean flowing;
    private Gate parentGate;
    private Gate childGate;
    private Point startPoint;
    private Point endPoint;
    
    public Wire() {
        this.flowing = false;
        this.parentGate = null;
        this.childGate = null;
    }

    public boolean isFlowing() {
        return flowing;
    }

    public boolean isConnected() {
        return this.parentGate != null && this.childGate != null;
    }

    public void setFlow(boolean flowing) {
        this.flowing = flowing;
    }

    public Gate getParentGate() {
        return parentGate;
    }

    public void setParentGate(Gate parentGate) {
        this.parentGate = parentGate;
        if (parentGate != null) {
            this.parentGate.setStartPoint(this);
        }
    }

    public Gate getChildGate() {
        return childGate;
    }

    public void setChildGate(Gate childGate) {
        this.childGate = childGate;
        if (childGate != null) {
            this.childGate.setEndPoint(this);
        }
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point point) {
        this.startPoint = point;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point point) {
        this.endPoint = point;
    }
}
