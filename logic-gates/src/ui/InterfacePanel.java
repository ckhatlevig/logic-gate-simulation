package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import gates.*;

public class InterfacePanel extends JPanel {
    // Declare a GatesFrame to store information
    private GatesFrame frame = new GatesFrame();
    private ArrayList<Input> inputs = new ArrayList<>();

    // Rectangle position and size
    private final int gateWidth = 100, gateHeight = 80;

    // Offset to track where the mouse grabs the rectangle
    private int offsetX, offsetY;

    // Currently dragging gate or wire
    private Gate draggingGate;
    private Wire draggingWire;

    // Line to represent wire being drawn
    private Point wireEndPoint = new Point(0, 0);

    public InterfacePanel() {
        // Add mouse listeners
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if click is on Remove Input
                if (e.getX() >= getWidth() - 110 && e.getX() <= getWidth() - 10 &&
                    e.getY() >= getHeight() - 190 && e.getY() <= getHeight() - 140) {
                        if (inputs.size() > 0) {
                            Input removed = inputs.removeLast();
                            removed.disconnect();
                            repaint();
                        }
                    return;
                }

                // Check if click is on "NEW INPUT" button
                if (e.getX() >= getWidth() - 110 && e.getX() <= getWidth() - 10 &&
                    e.getY() >= getHeight() - 130 && e.getY() <= getHeight() - 80) {
                    Input newInput = new Input(new ArrayList<Wire>() {{add(new Wire());}});
                    newInput.getOutputWires().get(0).setParentGate(newInput);
                    inputs.add(newInput);
                    // System.out.println("Added new input");
                    repaint();
                    return;
                }

                // Check if click is on "SAVE" button
                if (e.getX() >= getWidth() - 110 && e.getX() <= getWidth() - 10 &&
                    e.getY() >= getHeight() - 70 && e.getY() <= getHeight() - 20) {
                    frame.save();
                    inputs.clear();
                    // System.out.println("Saved current circuit");
                    repaint();
                    return;
                }

                // Check if click is inside a gate
                for (Gate gate : frame.getInternalGates()) {
                    
                    // Check if click is on an output
                    for (Wire wire : gate.getOutputWires()) {
                        int index = gate.getOutputWires().indexOf(wire);
                        int size = gate.getOutputWires().size();
                        if (e.getX() >= gate.getX() + gateWidth - 10 && e.getX() <= gate.getX() + gateWidth &&
                            e.getY() >= gate.getY() + (gateHeight*(index+1)/(size+1)) && e.getY() <= gate.getY() + (gateHeight*(index+1)/(size+1)) + 10) {
                        draggingWire = wire;
                        draggingWire.getParentGate().getSuperContainer().setStartPoint(draggingWire);
                        wireEndPoint = e.getPoint();
                        return;
                        }
                    }
                    if (e.getX() >= gate.getX() && e.getX() <= gate.getX() + gateWidth &&
                        e.getY() >= gate.getY() && e.getY() <= gate.getY() + gateHeight) {
                        draggingGate = gate;
                        offsetX = e.getX() - gate.getX();
                        offsetY = e.getY() - gate.getY();
                        return;
                    }
                }

                //Check if click is on a saved gate
                for (Gate gate : GatesFrame.savedGates) {
                    if (e.getX() >= gate.getX() && e.getX() <= gate.getX() + gateWidth &&
                        e.getY() >= gate.getY() && e.getY() <= gate.getY() + gateHeight) {
                        Gate newGate = gate.clone();
                        newGate.setX(e.getX() - gateWidth / 2);
                        newGate.setY(e.getY() - gateHeight / 2);
                        frame.addGate(newGate);
                        draggingGate = newGate;
                        // System.out.println("Added new gate: " + gate.convertString());
                        offsetX = e.getX() - gate.getX();
                        offsetY = e.getY() - gate.getY();
                        return;
                    }
                }

                // Check if click is on an input
                for (Input input : inputs) {
                    if (e.getX() >= input.getX() - 10 && e.getX() <= input.getX() + 10 &&
                        e.getY() >= input.getY() && e.getY() <= input.getY() + 20) {
                        draggingWire = input.getOutputWires().get(0);
                        draggingWire.getParentGate().getSuperContainer().setStartPoint(draggingWire);
                        wireEndPoint = e.getPoint();
                        return;
                    }
                    if (e.getX() >= input.getX() - 20 && e.getX() <= input.getX() &&
                        e.getY() >= input.getY() && e.getY() <= input.getY() + 20) {
                            input.setState(!input.getOutputWires().get(0).isFlowing());
                            repaint();
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingGate != null) {
                    // Update rectangle position
                    draggingGate.setX(e.getX() - offsetX);
                    draggingGate.setY(e.getY() - offsetY);
                    repaint();
                }
                if (draggingWire != null) {
                    // Update wire end point to follow mouse
                    wireEndPoint = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingGate != null) {
                    if (e.getX() < 10 || e.getX() > getWidth() - 30 ||
                        e.getY() < 10 || e.getY() > getHeight() - 200) {
                            frame.removeGate(draggingGate);
                        }
                    draggingGate = null;
                    repaint();
                }
                if (draggingWire != null) {
                    // Check if released over an input of another gate
                    for (Gate gate : frame.getInternalGates()) {
                        for (Wire wire : gate.getInputWires()) {
                            int index = gate.getInputWires().indexOf(wire);
                            int size = gate.getInputWires().size();
                            if (e.getX() >= gate.getX() - 5 && e.getX() <= gate.getX() + 5 &&
                                e.getY() >= gate.getY() + (gateHeight*(index+1)/(size+1)) && e.getY() <= gate.getY() + (gateHeight*(index+1)/(size+1)) + 10) {
                                // Connect the wire to this input
                                frame.connect(draggingWire.getParentGate(), draggingWire.getParentGate().getOutputWires().indexOf(draggingWire), gate, index);
                                draggingWire.setFlow(draggingWire.getParentGate().getOutputWires().get(0).isFlowing());
                                draggingWire.getParentGate().updateState();
                                break;
                            }
                        }
                    }
                    repaint();
                }
                draggingWire = null;
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void drawGate(Graphics2D g2d, Gate gate) {
        int x = gate.getX();
        int y = gate.getY();
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(x, y, gateWidth, gateHeight);
        g2d.setColor(Color.WHITE);
        g2d.drawString(gate.getName(), x + 10, y + 20);
        int inputs = gate.getInputWires() != null ? gate.getInputWires().size() : 0;
        int outputs = gate.getOutputWires() != null ? gate.getOutputWires().size() : 0;
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < inputs; i++) {
            g2d.fillOval(x - 5, y + (gateHeight*(i+1)/(inputs+1)), 10, 10);
        }
        for (int i = 0; i < outputs; i++) {
            g2d.fillOval(x + gateWidth - 5, y + (gateHeight*(i+1)/(outputs+1)), 10, 10);
        }
    }

    public void drawWire(Graphics2D g2d, Wire wire) {
        wire.getParentGate().getSuperContainer().setStartPoint(wire);
        Point start = wire.getStartPoint();
        wire.getChildGate().getSuperContainer().setEndPoint(wire);
        Point end = wire.getEndPoint();
        g2d.setColor(wire.isFlowing() ? Color.RED : Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(start.x, start.y, end.x, end.y);
    }

    public void drawInput(Graphics2D g2d, Input input) {
        int x = input.getX();
        int y = input.getY();
        g2d.setColor(Color.BLUE.darker());
        g2d.fillOval(x - 10, y, 20, 20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Clear everything
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //Draw background for gates
        g2d.setColor(Color.GRAY.brighter());
        g2d.fillRect(30, 10, getWidth() - 30, getHeight() - 200);

        // Draw gates and wires
        for (Gate gate : frame.getInternalGates()) {
            drawGate(g2d, gate);
            for (Wire wire : gate.getOutputWires()) {
                if (wire.isConnected()) {
                    drawWire(g2d, wire);
                }
            }
        }

        // Draw saved gates
        for (Gate gate : GatesFrame.savedGates) {
            int index = GatesFrame.savedGates.indexOf(gate);
            gate.setX(10 + (index % 5) * (gateWidth + 10));
            gate.setY(getHeight() - 180 + (index / 5) * (gateHeight + 10));
            drawGate(g2d, gate);
        }
        
        // Draw inputs and their output wires
        for (Input input : inputs) {
            int index = inputs.indexOf(input);
            int boxHeight = getHeight() - 180;
            input.setX(30);
            input.setY(boxHeight/inputs.size() * index + 10);
            drawInput(g2d, input);
            for (Wire wire : input.getOutputWires()) {
                if (wire.isConnected()) {
                    drawWire(g2d, wire);
                    g2d.setColor(wire.isFlowing() ? Color.RED : Color.BLACK);
                    g2d.fillRect(10, input.getY() + 5, 10, 10);
                }
            }
        }

        // Draw dragging wire if exists
        if (draggingWire != null) {
            // System.out.println("Wire X: " + draggingWire.getStartPoint().x + " Y: " + draggingWire.getStartPoint().y + " Super Container X: " + draggingWire.getParentGate().getSuperContainer().getX() + " Y: " + draggingWire.getParentGate().getSuperContainer().getY());
            g2d.setColor(draggingWire.isFlowing() ? Color.RED : Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(draggingWire.getStartPoint().x, draggingWire.getStartPoint().y, wireEndPoint.x, wireEndPoint.y);
        }

        //Draw debug button
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(getWidth() - 110, getHeight() - 190, 100, 50);
        g2d.setColor(Color.BLACK);
        g2d.drawString("REMOVE INPUT", getWidth() - 100, getHeight() - 160);

        //Draw add input button
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(getWidth() - 110, getHeight() - 130, 100, 50);
        g2d.setColor(Color.BLACK);
        g2d.drawString("NEW INPUT", getWidth() - 90, getHeight() - 100);

        //Draw save button
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(getWidth() - 110, getHeight() - 70, 100, 50);
        g2d.setColor(Color.BLACK);
        g2d.drawString("SAVE", getWidth() - 70, getHeight() - 40);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Logic Gates Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.add(new InterfacePanel());
        frame.setVisible(true);
    }
}
