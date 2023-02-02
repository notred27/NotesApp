package src;

import java.awt.Color;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
    static JFrame frame;
    static NotesSpace notesSpace;
    static JPanel contentPanel;
    static JLabel fileName;

    static String saveLocation = null;

    public static void main(String[] args) {

        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Notes App (Title TBD)");

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        // Load previous data

        notesSpace = new NotesSpace();

        JPanel bottomBar = new NodeBar(notesSpace);

        bottomBar.setBorder(BorderFactory.createLineBorder(Color.black));

        container.add(notesSpace);
        container.add(bottomBar);

        JPanel totalContainer = new JPanel();
        totalContainer.setLayout(new BoxLayout(totalContainer, BoxLayout.X_AXIS));

        totalContainer.add(container);

        contentPanel = new ContentSpace(notesSpace);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        totalContainer.add(contentPanel);

        frame.add(totalContainer);
        // frame.setPreferredSize(new Dimension(400, 400));

        JMenuBar bar = new JMenuBar();

        fileName = new JLabel(String.format("  %-20s", "New File"));
        fileName.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JMenu menu = new JMenu("File");
        menu.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JMenuItem newFileItem = new JMenuItem("New File");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        newFileItem.addActionListener(newFile);
        load.addActionListener(loadFile);
        saveAsItem.addActionListener(saveAs);
        saveItem.addActionListener(save);

        menu.add(newFileItem);
        menu.add(saveItem);
        menu.add(saveAsItem);
        menu.add(load);

        bar.add(fileName);
        bar.add(menu);
        frame.setJMenuBar(bar);

        // Load previous data
        String prevSave = (String) readFromFile(new File("src/startup"));
        if (prevSave != null) {
            System.out.println(prevSave);
            NotesSpace loaded = (NotesSpace) readFromFile(new File("saves/" + prevSave));
            if (loaded != null) {
                notesSpace.list = loaded.list;
                notesSpace.web = loaded.web;
                notesSpace.repaint();

                saveLocation = prevSave;
                fileName.setText(
                        String.format("  %-20s", saveLocation.substring(0, saveLocation.indexOf(".naf"))));
            }
        }

        frame.pack();

        frame.addWindowListener(saveOnClose);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

    }

    public static Object readFromFile(File path) {
        Object data;

        try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path))) {
            data = inFile.readObject();
            return data;
        } catch (Exception e) {
            // do something
            e.printStackTrace();
        }
        return null;
    }

    public static void writeToFile(File path, Object data) {
        try (ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(path))) {
            write.writeObject(data);
            System.out.println("Wrote to " + path.getName());
        } catch (Exception e) {
            // do something
            e.printStackTrace();

        }
    }

    static ActionListener loadFile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/saves");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Notes App File (.naf)", "naf");

                chooser.setFileFilter(filter);
                int returnVal = chooser.showDialog(frame, "Load");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    System.out.println("You chose to open this file: " +
                            chooser.getSelectedFile());

                    NotesSpace loaded = (NotesSpace) readFromFile(chooser.getSelectedFile());
                    if (loaded != null) {
                        notesSpace.list = loaded.list;
                        notesSpace.web = loaded.web;
                        notesSpace.repaint();

                        saveLocation = chooser.getSelectedFile().getName();
                        fileName.setText(
                                String.format("  %-20s", saveLocation.substring(0, saveLocation.indexOf(".naf"))));
                    } else {
                        System.out.println("Failed to load file");
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    static ActionListener saveAs = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/saves");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Notes App File (.naf)", "naf");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(frame);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile().exists()) {
                    writeToFile(chooser.getSelectedFile(), (Object) notesSpace);
                    saveLocation = chooser.getSelectedFile().getName();
                } else {
                    System.out.println("HERE LMAO");
                    String add = "saves/" + chooser.getSelectedFile().getName();

                    if (!add.contains(".naf")) {
                        add += ".naf";
                    }
                    writeToFile(new File(add), (Object) notesSpace);
                    saveLocation = add;
                }
            }
        }
    };

    static ActionListener save = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (saveLocation != null) {
                writeToFile(new File("saves/" + saveLocation), (Object) notesSpace);
            } else {
                System.out.println("rUT ROH");

                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/saves");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Notes App File (.naf)", "naf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile().exists()) {
                        writeToFile(chooser.getSelectedFile(), (Object) notesSpace);
                        saveLocation = chooser.getSelectedFile().getName();
                    } else {
                        System.out.println("HERE LMAO");
                        String add = "saves/" + chooser.getSelectedFile().getName();

                        if (!add.contains(".naf")) {
                            add += ".naf";
                        }
                        writeToFile(new File(add), (Object) notesSpace);
                        saveLocation = add;

                    }
                }
            }
        }
    };

    static ActionListener newFile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (saveLocation != null) {
                writeToFile(new File("saves/" + saveLocation), (Object) notesSpace);

            } else {
                System.out.println("rUT ROH");

                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/saves");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Notes App File (.naf)", "naf");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    if (chooser.getSelectedFile().exists()) {
                        writeToFile(chooser.getSelectedFile(), (Object) notesSpace);
                        saveLocation = chooser.getSelectedFile().getName();
                    } else {
                        System.out.println("HERE LMAO");
                        String add = "saves/" + chooser.getSelectedFile().getName();

                        if (!add.contains(".naf")) {
                            add += ".naf";
                        }
                        writeToFile(new File(add), (Object) notesSpace);
                        saveLocation = add;

                    }
                }
            }

            notesSpace.list = new ArrayList<Node>();
            notesSpace.web = new ArrayList<Connection>();
            saveLocation = null;
            notesSpace.repaint();
            fileName.setText(String.format("  %-20s", "New File"));

        }
    };

    /*
     * Window listener to automatically save the current file when the window is
     * closed
     */
    static WindowListener saveOnClose = new WindowListener() {
        @Override
        public void windowClosing(WindowEvent evt) {
            ContentSpace.updateData(notesSpace, null);

            if (saveLocation != null) {
                writeToFile(new File(saveLocation), (Object) notesSpace);
                writeToFile(new File("src/startup"), (Object) saveLocation);
            }
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    };

}