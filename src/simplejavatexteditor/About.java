/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2015-09-4
 *
 *
 * @modifiedby  Achintha Gunasekara
 * @modweb      http://www.achinthagunasekara.com
 * @modemail    contact@achinthagunasekara.com
 *
 * @Modifiedby SidaDan
 * @modemail Fschultz@sinf.de
 * Bug fixed. If JTextArea txt not empty and the user will
 * shutdown the Simple Java NotePad, then the Simple Java NotePad
 * is only hidden (still running). So I added a WindowListener
 * an call method dispose() for this frame.
 * Tested with java 8.
 */

package simplejavatexteditor;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class About {

    private final JFrame frame;
    private final JPanel panel;
    private String contentText;
    private final JLabel text;

    public About(UI ui) {
        panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame = new JFrame();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });


        frame.setVisible(true);
        frame.setSize(500,300);
        frame.setLocationRelativeTo(ui);
        text = new JLabel();
    }

    public void me() {
        frame.setTitle("About Me - " + SimpleJavaTextEditor.NAME);

        contentText =
        "<html><body><p>" +
        "Author: Pierre-Henry Soria<br />" +
        "Contact me at: " +
        "<a href='mailto:" + SimpleJavaTextEditor.AUTHOR_EMAIL + "?subject=About the NotePad PH Software'>" + SimpleJavaTextEditor.AUTHOR_EMAIL + "</a>" +
                "<br /><br />" +
                "Modified By: Achintha Gunasekara<br />" +
                "Contact me at: <a href='mailto:" + SimpleJavaTextEditor.EDITOR_EMAIL + "?subject=About the NotePad PH Software'>" + SimpleJavaTextEditor.EDITOR_EMAIL + "</a>" +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

    public void software() {
        frame.setTitle("About Me - " + SimpleJavaTextEditor.NAME);

        contentText =
        "<html><body><p>" +
        "Name: " + SimpleJavaTextEditor.NAME + "<br />" +
        "Version: " + SimpleJavaTextEditor.VERSION +
        "</p></body></html>";

        text.setText(contentText);
        panel.add(text);
        frame.add(panel);
    }

}