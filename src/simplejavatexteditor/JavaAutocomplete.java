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
    
    //for getting the text area
    UI ui;
    
    //we will need to put the array into an arraylist.
    private ArrayList<String> words = new ArrayList<>();
    private ArrayList<String> chars = new ArrayList<>();

    //to keep track of whether we are showing
    //an autocomplete to the user or not.
    private enum Mode {
        INSERT, COMPLETION
    };
    
    //Starting out in insert mode
    private Mode mode = Mode.INSERT;
    
    //the text area object
    private final JTextArea textArea;

    private static final String COMMIT_ACTION = "commit";
    
    private boolean isKeyword;
    
    private int pos;
    private String content;
    
    public JavaAutocomplete(UI ui) {
        
        //set the UI
        this.ui = ui;
        textArea = ui.getEditor();

        
        //enter key is pressed; fill in the keyword
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
        //get the position of the last action
        pos = e.getOffset();
        content = null;

        try {
            //the text from start of typing to the end thus far
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        
        //we need to check if the key pressed is either 
        //a special character or a keyword.
        //we check for special characters first.
        char c = content.charAt(pos);
        String s = String.valueOf(c);
                 
        //keeps us from getting errors as 
        //the text area contantly updates this method
        if (e.getLength() != 1) {
            return;
        }
            
        //this loop begins at the last
        //user event and ddecrements until it hits a
        //non-letter, thus getting the index of the 1st
        //letter of the word currently being typed
        int start;
        for (start = pos; start >= 0; start--) {
            
            //if we hit a non-letter while decrementing, we come 
            //to the first letter in the word the user is typing.
            //store that index in start variable.
                if (!Character.isLetter(content.charAt(start))) {
                    break;
                }
        }
        
        if(chars.contains(s)) {
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
        
        //too short. make autocomplete start after 2 letters
        //are typed
        if (pos - start < 2) {
            return;
        }

        //the content of the word that has been typed thus far 
        String prefix = content.substring(start + 1);

        //perform a binary search on the prefix and the word list.
        int n = Collections.binarySearch(words, prefix);
                
        //find an absolute match on prefixes of the words.
        if (n < 0 && -n < words.size()) {
            isKeyword = true;
            //the matching word
            String match = words.get(-n - 1);
            
            //match the word containig prefix's contents to
            //the beginning of the word.
            if (match.startsWith(prefix)) {
                
                //perform a substring on the matching word to get 
                //the remainder of the word that the user is typing.
                String completion = match.substring(pos - start);
            
                //then, perform the CompletionTask runnable on the word.
                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1));
        } else {
            //nothing found. stay in insert mode.
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
            //insert the remainder of the word after the user's
            //last inserted text
            textArea.insert(completion, position);
            
            if(isKeyword) {
                //these two method calls draw the caret over the
                //text completion and cause the text 
                //area to highlight the suggested completion
                textArea.setCaretPosition(position + completion.length());
                textArea.moveCaretPosition(position);
                
                //enter completion mode. This 
                //affects the CommitAction class behavior
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
            
            //we are in completion mode at time of action
            if (mode == Mode.COMPLETION) {
                
                //the end of the word
                int pos = textArea.getSelectionEnd();
                
                //handle if the character is a special character
                if (isKeyword) { 
                    
                    //it is a keyword
                    textArea.insert(" ", pos);
                    textArea.setCaretPosition(pos + 1);
                    mode = Mode.INSERT;
                    } else {
                    
                    textArea.setCaretPosition(pos);
                    mode = Mode.INSERT;
                    }
            } else {
                //default for pressing enter
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
            
                switch(e.getKeyChar()) {
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

/*
The completion is highlighted upon bracket click,
but when another button is clicked *except for* the completion itself, 
fill the completion and the button within the brackets.

We are at the place when the completion is highlighted. 
We know when action is a char event. We know 

On type, I want to:

Set the caret inside the brackets

let user type
*/
