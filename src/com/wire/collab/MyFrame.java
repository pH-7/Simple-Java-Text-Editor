package com.wire.collab;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MyFrame extends JFrame {

    private final JTextArea textArea;

    MyFrame(String name) {
        // Set the initial size of the window
        setSize(700, 500);

        // Set the title of the window
        setTitle(name);

        // Set the default close operation (exit when it gets closed)
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // center the frame on the monitor
        setLocationRelativeTo(null);

        // Set a default font for the TextArea
        textArea = new JTextArea("", 0, 0);
        textArea.setFont(new Font("Arial", Font.PLAIN, 18));
        textArea.setTabSize(4);

        textArea.setLineWrap(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(textArea);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(99);
        }
    }

    Document getDocument() {
        return textArea.getDocument();
    }
}
