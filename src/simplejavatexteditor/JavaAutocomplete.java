package simplejavatexteditor;

import javax.swing.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import javax.swing.GroupLayout.*;
import simplejavatexteditor.UI;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author patrickslagle
 */
public class JavaAutocomplete
        implements DocumentListener {

    static final String keywords[] = {"abstract", "assert", "boolean",
        "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "extends", "false",
        "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native",
        "new", "null", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super", "switch",
        "synchronized", "this", "throw", "throws", "transient", "true",
        "try", "void", "volatile", "while"};

    private enum Mode {

        INSERT, COMPLETION
    };
    private final Mode mode = Mode.INSERT;
    private final String COMMIT_ACTION = "commit";
    UI ui;

    public JavaAutocomplete() {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

        int pos = e.getOffset();
        String content = null;
        int start;
        String prefix = "";
        int n;

        if (e.getLength() < 1) {
            return;
        }

        content = ui.getContent(0, pos + 1);

        for (start = pos; start >= 0; start--) {
            if (!Character.isLetter(content.charAt(start))) {
                break;
            }
        }
        prefix = content.substring(start + 1);
        
        n = Collections.binarySearch(keywords, prefix);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
/*
So, the way to perform autocomplete is:

Get the position of the latest action through getOffset()
Get the text of the area thus far by a getContent() method
Get the last letter of the word the user typed by the Character.isLetter() and for loop.
Get the prefix by using substring with content and w
Store a binarySearch of prefix and keywords in and int
do a if statement to make sure the search results return a match and handle if not
Then, utilize 2 runnable classes to (1) insert the autocomplete and (2) set the cursor position of user and allow insert 
*/