/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        About.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2012-05-05
 */

package ph.notepad;

import javax.swing.*;
import java.awt.FlowLayout;

public class About {

	private JFrame frame;
    private JPanel panel;
    private String contentText;
    private JLabel text;
  
	public About() {
		panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(300,300);
		text = new JLabel();
	}
	
	public void me() {
		frame.setTitle("About Me - " + Main.NAME);
		
		contentText = 
		"<html><body><p>" +
		"Author: Pierre-Henry Soria<br />" +
		"Contact me at: " +
		"<a href='mailto:" + Main.AUTHOR_EMAIL + "?subject=About the NotePad PH Software'>" + Main.AUTHOR_EMAIL + "</a>" +
		"</p></body></html>";
		
		text.setText(contentText);
		panel.add(text);
		frame.add(panel);
	}
    
	public void software() {
		frame.setTitle("About Me - " + Main.NAME);
		
		contentText = 
		"<html><body><p>" +		
		"Name: " + Main.NAME + "<br />" +
		"Version: " + Main.VERSION + 
		"</p></body></html>";
		
		text.setText(contentText);
		panel.add(text);
		frame.add(panel);
	}
	
}
