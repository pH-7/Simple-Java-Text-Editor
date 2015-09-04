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

package simplejavatexteditor;

import javax.swing.JTextPane;

public class SimpleJavaTextEditor extends JTextPane {
	
	private static final long serialVersionUID = 1L;
	public final static String AUTHOR_EMAIL = "pierrehs@hotmail.com";
	public final static String NAME = "NotePad PH";
        public final static String EDITOR_EMAIL = "contact@achinthagunasekara.com";
	public final static double VERSION = 2.0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        new UI().setVisible(true);
	}

}
