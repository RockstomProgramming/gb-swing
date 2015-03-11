package br.com.finan.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Wesley Luiz
 */
public class FixedLengthDocument extends PlainDocument {

	/** Atributo serialVersionUID. */
	private static final long serialVersionUID = 1L;
	private final int maxLength;

	public FixedLengthDocument(final int maxlen) {
		super();

		if (maxlen <= 0) {
			throw new IllegalArgumentException("You must specify a maximum length!");
		}

		maxLength = maxlen;
	}

	@Override
	public void insertString(final int offset, final String str, final AttributeSet attr) throws BadLocationException {
		if (str == null || getLength() == maxLength) {
			return;
		}

		final int totalLen = (getLength() + str.length());
		if (totalLen <= maxLength) {
			super.insertString(offset, str, attr);
			return;
		}

		final String newStr = str.substring(0, (maxLength - getLength()));
		super.insertString(offset, newStr, attr);
	}
}
