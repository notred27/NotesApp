package src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ContentSpace extends JPanel implements KeyListener {
    /* Swing elements used to make the GUI */
    private static JTextArea desc;
    private static JScrollPane descContainer;
    private static JTextField title;
    private static JButton remove;
    private static JLabel shape1;
    private static JLabel shape2;
    private static JPanel connectionInfo;
    private static JTextArea conList;

    /* Other member variables */
    private NotesSpace s;
    private static Node currentNode; // This is the currently selected node

    public ContentSpace(NotesSpace s) {
        this.s = s;
        this.setPreferredSize(new Dimension(200, 450));
        this.setMaximumSize(new Dimension(200, 450));
        this.setMinimumSize(new Dimension(200, 450));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        this.setLayout(layout);

        setBackground(new Color(246, 221, 204));

        /* JTextField that acts as the title for the GUI */
        title = new JTextField(14);
        title.setMaximumSize(new Dimension(250, 20));
        title.getCaret().setBlinkRate(0);
        title.addKeyListener(this);

        /*
         * JLabels that act as icons for showing the current shape at the top of the GUI
         */
        shape1 = new JLabel("");
        shape2 = new JLabel("");

        /*
         * JTextArea and JScroll pane that act as the editable info section of the GUI
         */
        desc = new JTextArea(20, 20);
        desc.setBorder(BorderFactory.createLineBorder(Color.black));
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.getCaret().setBlinkRate(0);
        desc.setRows(16);
        descContainer = new JScrollPane(desc);

        /* JButton that is used to remove nodes */
        remove = new JButton("Delete Node");
        remove.addActionListener(deleteNode);
        remove.setHorizontalAlignment(SwingConstants.CENTER);

        /* Make the JPanel to display the connection list info */
        makeConnectionListElement();

        // Add all elements to this JPanel
        this.add(shape1);
        this.add(title);
        this.add(shape2);
        this.add(descContainer);
        this.add(remove);
        this.add(connectionInfo);

        showGUI(false);

    }

    /* Makes a JPanel to display the connection list info */
    public void makeConnectionListElement() {
        connectionInfo = new JPanel();
        connectionInfo.setPreferredSize(new Dimension(180, 110));
        connectionInfo.setAlignmentY(TOP_ALIGNMENT);

        JLabel connectionsList = new JLabel("Connections:");

        conList = new JTextArea(5, 20);
        conList.setLineWrap(true);
        conList.setEditable(false);

        JScrollPane conContainer = new JScrollPane(conList);

        connectionInfo.add(connectionsList);
        connectionInfo.add(conContainer);
        connectionInfo.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /*
     * Action listener that is called by the delete JButton to remove a node. Also
     * deletes that nodes connections.
     */
    ActionListener deleteNode = new ActionListener() {
        // This is called to delete the node that is currently selected, and any
        // connections it may have
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentNode != null) {
                // Remove the current node from the list of nodes in notesSpace
                s.list.remove(currentNode);

                // Check each connection in the notesSpace web, and if it has the current node
                // as one of the endpoints, remove the connection
                for (int i = 0; i < s.web.size(); i++) {
                    if (s.web.get(i).p.equals(currentNode) || s.web.get(i).q.equals(currentNode)) {
                        s.web.remove(i);
                        i--;
                    }
                }

                currentNode = null;
                updateData(s, currentNode); // Repaint the gui to show changes
                s.repaint(); // Repaint the notesSpace to show changes
            }
        }
    };

    /* Used to save node data, and control what to display on the GUI */
    public static void updateData(NotesSpace s, Node selectedNode) {
        if (currentNode != null) { // save previously selected node
            currentNode.text = desc.getText();
            currentNode.title = title.getText();
        }

        if (selectedNode != null) {
            currentNode = selectedNode;

            // Show the node gui
            showGUI(true);

            // Update the gui elements to show the current node
            title.setText(selectedNode.title);
            desc.setText(selectedNode.text);
            shape1.setText(unicodeShape(selectedNode.shape));
            shape2.setText(unicodeShape(selectedNode.shape));

            // Get the connections text for the box
            conList.setText("");
            for (int i = 0; i < s.web.size(); i++) {
                if (s.web.get(i).q.equals(currentNode)) {

                    conList.setText(
                            conList.getText() + unicodeShape(s.web.get(i).p.shape) + s.web.get(i).p.title + "\n");
                } else if (s.web.get(i).p.equals(currentNode)) {

                    conList.setText(
                            conList.getText() + unicodeShape(s.web.get(i).q.shape) + s.web.get(i).q.title + "\n");
                }
            }

        } else { // If no node is selected, hide everything
            showGUI(false);
        }
    }

    /* Helper method to toggle showing the GUI elements */
    public static void showGUI(boolean show) {
        title.setVisible(show);
        shape1.setVisible(show);
        shape2.setVisible(show);
        descContainer.setVisible(show);
        desc.setVisible(show);
        remove.setVisible(show);
        connectionInfo.setVisible(show);
    }

    /* Helper method used to get the unicode character for each shape */
    public static String unicodeShape(int i) {
        String unicode = "";
        switch (i) {
            case 0:
                unicode += "\u25CB ";
                break;
            case 1:
                unicode += "\u25A1 ";
                break;
            case 2:
                unicode += "\u25B3 ";
                break;
        }
        return unicode;
    }

    /*==============     KeyListener Events      ==============*/
    /*Used to repaint the title of a node in the notesSpace when enter is used */
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
            currentNode.title = title.getText();
            s.repaint();
        }
    }

    /* Unimplemented methods */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
