package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import java.awt.RenderingHints;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class NotesSpace extends JPanel implements MouseInputListener {

    // Storage for all Nodes and Connections
    public ArrayList<Node> list = new ArrayList<Node>();
    public ArrayList<Connection> web = new ArrayList<>();

    private final Color shapeColor = new Color(232, 255, 252);

    // Local variables for manipulating what to display
    private Node selectedNode;
    private boolean rightClick;
    private Node tmp;
    private Node last; // Used to reselect node if user dragged it outside the window

    public NotesSpace() {
        this.setPreferredSize(new Dimension(500, 400));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCanvas(g);
    }

    /* Helpper method for drawing to this custom JPanel */
    public void drawCanvas(Graphics g) {
        // Turn on antialiasing
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Connections
        ArrayList<Connection> garbage = new ArrayList<Connection>();
        for (Connection c : web) {
            if (c.p != null && c.q != null) {
                g.setColor(c.color);
                g.drawLine(c.p.xPos + c.p.size / 2, c.p.yPos + c.p.size / 2, c.q.xPos + c.q.size / 2,
                        c.q.yPos + +c.q.size / 2);
                g.setColor(Color.black);
            } else {
                garbage.add(c);
            }
        }
        web.removeAll(garbage); // Saftey for removing invalid nodes

        // Draw Nodes
        for (Node n : list) {
            switch (n.shape) {
                case 0:
                    g.drawOval(n.xPos, n.yPos, n.size, n.size);
                    g.setColor(shapeColor);
                    g.fillOval(n.xPos, n.yPos, n.size, n.size);
                    g.setColor(Color.black);
                    break;
                case 1:
                    g.drawRect(n.xPos, n.yPos, n.size, n.size);
                    g.setColor(shapeColor);
                    g.fillRect(n.xPos, n.yPos, n.size, n.size);
                    g.setColor(Color.black);
                    break;
                case 2:
                    drawUpTriangle(g, n.xPos, n.yPos, n.size);
                    g.setColor(shapeColor);
                    fillUpTriangle(g, n.xPos, n.yPos, n.size);
                    g.setColor(Color.black);
                    break;
            }

            // TODO fix text centering
            JLabel l = new JLabel(n.title); // hack way for getting text size, do I need to remove these?
            g.drawString(n.title, n.xPos + n.size / 2 - l.getPreferredSize().width / 2, n.yPos + n.size / 2);
        }
    }

    /*
     * Methods for implementing a triangle shape (Similar to .drawRect and .fillRect
     */
    public void drawUpTriangle(Graphics g, int x, int y, int size) {
        Path2D myPath = new Path2D.Double();
        myPath.moveTo(x + size / 2, y);
        myPath.lineTo(x + size, y + size);
        myPath.lineTo(x, y + size);
        myPath.closePath();
        ((Graphics2D) g).draw(myPath);
    }

    public void fillUpTriangle(Graphics g, int x, int y, int size) {
        Path2D myPath = new Path2D.Double();
        myPath.moveTo(x + size / 2, y);
        myPath.lineTo(x + size, y + size);
        myPath.lineTo(x, y + size);
        myPath.closePath();

        ((Graphics2D) g).fill(myPath);
    }

    /*
     * ==================== MouseEventListener Override Methods ====================
     */

    /* Returns the node at the given (x,y) mouse position */
    public Node getObjectAt(int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            if (x > list.get(i).xPos && x < list.get(i).xPos + list.get(i).size
                    && y > list.get(i).yPos && y < list.get(i).yPos + list.get(i).size) {
                return list.get(i);
            }
        }
        return null;
    }

    /* Single click on an object displays its info on the GUI */
    @Override
    public void mouseClicked(MouseEvent e) {
        Node n = getObjectAt(e.getX(), e.getY());
        if (n != null) {
            ContentSpace.updateData(this, n);
        }
    }

    /*
     * If left click, gets ready to handle dragging the object on the screen.
     * If right click, gets ready to draw a connection from this node to another.
     * 
     * Both edit the local variables and are resolved through the mouseDragged
     * method.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // test if you have clicked on an object
        selectedNode = getObjectAt(e.getX(), e.getY());

        rightClick = SwingUtilities.isRightMouseButton(e);
        if (rightClick) {
            // Make a temp invisable node so a connection is drawn to the cursor
            tmp = new Node(e.getX(), e.getY(), 0, 0);
            list.add(tmp);
            web.add(new Connection(tmp, selectedNode));
        }
    }

    /* Used to handle selecting a node for the second part of a connection */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedNode != null && rightClick) {
            // Create a new connection if the selected nodes are valid
            Node secondNode = getObjectAt(e.getX(), e.getY());
            if (secondNode != null && !secondNode.equals(selectedNode)) {
                if (!secondNode.equals(tmp) && !selectedNode.equals(tmp)) {

                    if (!web.contains(new Connection(selectedNode, secondNode))) {
                        // Add the conection if it doesn't exist
                        web.add(new Connection(selectedNode, secondNode));

                    } else {
                        // Remove the connection if it already exists
                        web.remove(new Connection(selectedNode, secondNode));
                    }
                }
            }

            // Removes the temporary connection used to draw to the cursor
            web.remove(new Connection(tmp, selectedNode));
            list.remove(tmp);

            // Update the visuals and empty the GUI
            repaint();
            ContentSpace.updateData(this, selectedNode);
        }
    }

    /* Used to reselect a node that was being dragged when re-entering the window */
    @Override
    public void mouseEntered(MouseEvent e) {
        selectedNode = last;
        last = null;
    }

    /* Used to save a node that was being dragged out of the window */
    @Override
    public void mouseExited(MouseEvent e) {
        last = selectedNode;
        selectedNode = null;
    }

    /* Used to move a selected node to the mouse when being dragged */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedNode != null) {
            if (rightClick) {
                tmp.xPos = e.getX();
                tmp.yPos = e.getY();

            } else {
                selectedNode.xPos = e.getX() - selectedNode.size / 2;
                selectedNode.yPos = e.getY() - selectedNode.size / 8;
            }
            repaint();
        }
    }

    /* UNused override */
    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
