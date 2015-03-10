package br.com.finan.validator;

/**
 *
 * @author Wesley Luiz
 */
public class IntegerValidator extends MaxLengthValidator {

    public IntegerValidator(int max) {
        super(max);
    }

    @Override
    public Result validate(Object t) {
        String s = t.toString();
        try {
            Integer.valueOf(s);
            return super.validate(t);
        } catch (NumberFormatException ex) {
            getComp().setText(s.substring(0, s.length() - 1));
            return null;
        }
    }
}
