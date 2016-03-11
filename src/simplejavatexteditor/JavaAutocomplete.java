package simplejavatexteditor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class JavaAutocomplete
        implements DocumentListener {

    static final String[] keywords = {"abstract", "assert", "boolean",
        "break", "byte", "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else", "extends", "false",
        "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "System", "out", "print", "println",
        "new", "null", "package", "private", "protected", "public", "interface",
        "long", "native", "return", "short", "static", "strictfp", "super", "switch",
        "synchronized", "this", "throw", "throws", "transient", "true",
        "try", "void", "volatile", "while", "String"};

    static final String[] specialChars = {"{", "("};

    UI ui;

    private ArrayList<String> words = new ArrayList<>();
    private ArrayList<String> chars = new ArrayList<>();

    private enum Mode {

        INSERT, COMPLETION
    };

    private Mode mode = Mode.INSERT;

    private final JTextArea textArea;

    private static final String COMMIT_ACTION = "commit";

    private boolean isKeyword;

    private int pos;
    private String content;

    public JavaAutocomplete(UI ui) {

        this.ui = ui;
        textArea = ui.getEditor();

        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER "), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());

        //set the keywords
        for (String keyList : keywords) {
            words.add(keyList);
        }
        for (String character : specialChars) {
            chars.add(character);
        }
        Collections.sort(words, null);
        Collections.sort(chars, null);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        pos = e.getOffset();
        content = null;

        try {
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

        char c = content.charAt(pos);
        String s = String.valueOf(c);

        if (e.getLength() != 1) {
            return;
        }

        int start;
        for (start = pos; start >= 0; start--) {

            if (!Character.isLetter(content.charAt(start))) {
                break;
            }
        }

        if (chars.contains(s)) {
            for (String str : chars) {
                if (s.equals(str)) {
                    switch (str) {
                        case "{":
                            isKeyword = false;
                            SwingUtilities.invokeLater(
                                    new CompletionTask("}", pos + 1));
                            break;
                        case "(":
                            isKeyword = false;
                            SwingUtilities.invokeLater(
                                    new CompletionTask(")", pos + 1));

                            break;
                    }
                }
            }
        }

        if (pos - start < 2) {
            return;
        }

        String prefix = content.substring(start + 1);

        int n = Collections.binarySearch(words, prefix);

        if (n < 0 && -n < words.size()) {
            isKeyword = true;
            String match = words.get(-n - 1);

            if (match.startsWith(prefix)) {

                String completion = match.substring(pos - start);

                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1));
            } else {
                mode = Mode.INSERT;
            }
        }
    }

    private class CompletionTask
            implements Runnable {

        private final String completion;
        private int position;

        public CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public String getCompletion() {
            return completion;
        }

        @Override
        public void run() {
            System.out.println(isKeyword);

            textArea.insert(completion, position);

            if (isKeyword) {

                textArea.setCaretPosition(position + completion.length());
                textArea.moveCaretPosition(position);

                mode = Mode.COMPLETION;
            } else {
                textArea.setCaretPosition(position + completion.length());
                textArea.moveCaretPosition(position);
                mode = Mode.COMPLETION;
                textArea.addKeyListener(new HandleBracketEvent());

            }
        }
    }

    private class CommitAction
            extends AbstractAction {

        public void actionPerformed(ActionEvent e) {

            if (mode == Mode.COMPLETION) {

                int pos = textArea.getSelectionEnd();

                if (isKeyword) {

                    textArea.insert(" ", pos);
                    textArea.setCaretPosition(pos + 1);
                    mode = Mode.INSERT;
                } else {

                    textArea.setCaretPosition(pos);
                    mode = Mode.INSERT;
                }
            } else {
                textArea.replaceSelection("\n");
            }
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    private class HandleBracketEvent
            implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("Activated");

            switch (e.getKeyChar()) {
                case '}':
                    textArea.replaceRange("", pos, pos);
                    mode = Mode.INSERT;
                    textArea.removeKeyListener(this);

                    break;
                case ')':
                    textArea.replaceRange("", pos, pos);
                    mode = Mode.INSERT;
                    textArea.removeKeyListener(this);
                    break;
                case '\n':
                    textArea.setCaretPosition(pos);
                    textArea.replaceSelection("\n");
                    mode = Mode.INSERT;
                    textArea.removeKeyListener(this);
                    break;
                default:
                    textArea.setCaretPosition(pos);
                    mode = Mode.INSERT;
                    textArea.removeKeyListener(this);
                    break;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }
}
