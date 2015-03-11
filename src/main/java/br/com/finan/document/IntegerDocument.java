package br.com.finan.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Wesley Luiz
 */
public class IntegerDocument extends FixedLengthDocument {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;

	public IntegerDocument(final int maxlen) {
		super(maxlen);
	}

	@Override
	public void insertString(final int offset, final String str, final AttributeSet attr) throws BadLocationException {
		if (str == null) {
			return;
		}

		try {
			Integer.parseInt(str);
		} catch (final Exception e) {
			return;
		}

		super.insertString(offset, str, attr);
	}
}
