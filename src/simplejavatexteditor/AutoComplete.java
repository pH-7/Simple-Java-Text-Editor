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

/**
 * <h1>Auto complete functionality multiple programming languages, including brackets and
 * parentheses</h1>
 *
 * <p>
 * An ArrayList is created for the keywords and the brackets.
 * Logic for setting the content of the ArrayList is
 * found in UI.java. If the word currently being typed
 * matches a word in the list, a Runnable inner class is
 * implemented to handle the word completion.
 *
 * Two other inner classes are also used. The second one handles when the enter
 * key is pressed in response to an auto complete suggestion. The third one
 * performs additional logic on brackets.
 * </p>
 *
 *
 * @author Patrick Slagle
 * @since 2016-12-03
 */
public class AutoComplete
        implements DocumentListener {

    private ArrayList<String> brackets = new ArrayList<>();
    private ArrayList<String> bracketCompletions = new ArrayList<>();

    private ArrayList<String> words = new ArrayList<>();

    SupportedKeywords kw;

    //Keep track of when code completion
    //has been activated
    private enum Mode {
        INSERT, COMPLETION
    };

    private final UI ui;
    private Mode mode = Mode.INSERT;
    private final JTextArea textArea;
    private static final String COMMIT_ACTION = "commit";
    private boolean isKeyword;
    private int pos;
    private String content;

    public AutoComplete(UI ui, ArrayList<String> al) {
        //Set the keywords
        words = al;
        kw = new SupportedKeywords();
        brackets = kw.getBrackets();
        bracketCompletions = kw.getBracketCompletions();

        //Access the editor
        this.ui = ui;
        textArea = ui.getEditor();

        //Set the handler for the enter key
        InputMap im = textArea.getInputMap();
        ActionMap am = textArea.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER "), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());

        Collections.sort(words);
    }

    /**
     * A character has been typed into the document.
     * This method performs the primary
     * check to find a keyword completion.
     *
     * @param e
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        pos = e.getOffset();
        content = null;

        try {
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

        if (e.getLength() != 1) {
            return;
        }

        //Before checking for a keyword
        checkForBracket();

        //Get the beginning of the word being typed
        int start;
        for (start = pos; start >= 0; start--) {
            if (!Character.isLetter(content.charAt(start))) {
                break;
            }
        }

        //Auto complete will start
        //after two characters are typed
        if (pos - start < 2) {
            return;
        }

        //Search for a match on the word being typed
        //in the keywords ArrayList
        String prefix = content.substring(start + 1);
        int n = Collections.binarySearch(words, prefix);

        if (n < 0 && -n < words.size()) {
            String match = words.get(-n - 1);

            if (match.startsWith(prefix)) {
                String completion = match.substring(pos - start);
                isKeyword = true;
                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1));
            } else {
                mode = Mode.INSERT;
            }
        }
    }

    /**
     * Performs a check to see if the last
     * key typed was one of the supported
     * bracket characters
     */
    private void checkForBracket() {
        //String of the last typed character
        char c = content.charAt(pos);
        String s = String.valueOf(c);

        for (int i = 0; i < brackets.size(); i++) {
            if (brackets.get(i).equals(s)) {
                isKeyword = false;
                SwingUtilities.invokeLater(
                        new CompletionTask(bracketCompletions.get(i), pos + 1));
            }
        }
    }

    /**
     * So that future classes can view the keyword list in the future.
     *
     * @return the keywords
     */
    private ArrayList<String> getKeywords() {
        return words;
    }

    /**
     * So that these keywords can be modified or added to in the future.
     *
     * @param keyword the keyword to set
     */
    private void setKeywords(String keyword) {
        words.add(keyword);
    }

    /**
     * Handles the auto complete suggestion
     * generated when the user is typing a
     * word that matches a keyword.
     */
    private class CompletionTask
            implements Runnable {

        private final String completion;
        private final int position;

        public CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            textArea.insert(completion, position);

            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETION;
            if (!isKeyword) {
                textArea.addKeyListener(new HandleBracketEvent());
            }
        }
    }

    /**
     * Enter key is pressed in response to an
     * auto complete suggestion. Respond
     * appropriately.
     */
    private class CommitAction
            extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (mode == Mode.COMPLETION) {
                int pos = textArea.getSelectionEnd();

                if (isKeyword) {
                    textArea.insert(" ", pos);
                    textArea.setCaretPosition(pos + 1);
                    mode = Mode.INSERT;
                }
            } else {
                mode = Mode.INSERT;
                textArea.replaceSelection("\n");
            }
        }
    }

    /**
     * Additional logic for bracket auto complete
     */
    private class HandleBracketEvent
            implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //Bracket auto complete needs special attention.
            //Multiple possible responses are needed.
            String keyEvent = String.valueOf(e.getKeyChar());
            for (String bracketCompletion : bracketCompletions) {
                if (keyEvent.equals(bracketCompletion)) {
                    textArea.replaceRange("", pos, pos + 1);
                    mode = Mode.INSERT;
                    textArea.removeKeyListener(this);
                }
            }
            int currentPosition = textArea.getCaretPosition();
            switch (e.getKeyChar()) {
                case '\n':
                    textArea.insert("\n\n", currentPosition);
                    textArea.setCaretPosition(currentPosition + 1);
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

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

}
