/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        Main.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 */

package ph.notepad;

import javax.swing.JTextPane;

public class Main extends JTextPane {
	
	private static final long serialVersionUID = 1L;
	public final static String AUTHOR_EMAIL = "pierrehs@hotmail.com";
	public final static String NAME = "NotePad PH";
	public final static double VERSION = 1.0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(() -> UI().setVisible(true);)
	}

}
