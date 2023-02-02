package src;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeBar extends JPanel implements MouseInputListener {
    private NotesSpace s;
    /* Constant node info */
    private final Node circle = new Node(50, 10, 34, 0);
    private final Node square = new Node(10, 10, 30, 1);
    private final Node triangle = new Node(90, 10, 30, 2);
    private final Color shapeColor = new Color(232, 255, 252);

    public NodeBar(NotesSpace s) {
        this.s = s;

        // Set size for layout preferences
        this.setPreferredSize(new Dimension(700, 50));
        this.setMaximumSize(getPreferredSize());
        addMouseListener(this);

        // Set custom bg color
        setBackground(new Color(253, 242, 233));

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Turn on antialiasing
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw Nodes
        g.drawOval(circle.xPos, circle.yPos, circle.size, circle.size);
        g.setColor(shapeColor);
        g.fillOval(circle.xPos, circle.yPos, circle.size, circle.size);
        g.setColor(Color.black);

        g.drawRect(square.xPos, square.yPos, square.size, square.size);
        g.setColor(shapeColor);
        g.fillRect(square.xPos, square.yPos, square.size, square.size);
        g.setColor(Color.black);

        s.drawUpTriangle(g, triangle.xPos, triangle.yPos, triangle.size);
        g.setColor(shapeColor);
        s.fillUpTriangle(g, triangle.xPos, triangle.yPos, triangle.size);
        g.setColor(Color.black);

    }

    /* Returns if the element at (x,y) is equal to the given Node */
    public boolean equalToObjectAt(int x, int y, Node n) {
        if (x > n.xPos - n.size && x < n.xPos + n.size
                && y > n.yPos - n.size && y < n.yPos + n.size) {
            return true;
        }

        return false;
    }

    /*
     * ==================== MouseEventListener Override Methods ====================
     */
    /* Adds the given shape to the NotesSpace when they are clicked on */
    @Override
    public void mousePressed(MouseEvent e) {
        if (equalToObjectAt(e.getX(), e.getY(), square)) {
            s.list.add(new Node(10, 330, 50, 1, "Square"));

        } else if (equalToObjectAt(e.getX(), e.getY(), circle)) {
            s.list.add(new Node(10, 330, 50, 0, "Circle"));

        } else if (equalToObjectAt(e.getX(), e.getY(), triangle)) {
            s.list.add(new Node(10, 330, 50, 2, "Triangle"));
        }

        s.repaint(); // Repaints to show the new Node
    }

    /* Unimplemented mouse methods */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
