package br.com.finan.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Wesley Luiz
 */
public class IntegerDocument extends FixedLengthDocument {

    public IntegerDocument(int maxlen) {
        super(maxlen);
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return;
        }

        super.insertString(offset, str, attr);
    }
}
