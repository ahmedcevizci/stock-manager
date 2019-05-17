package info.alaz.stock.manager.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.text.SimpleDateFormat;

public class IsZonedDateTime extends BaseMatcher {

    private final String pattern;
    private final SimpleDateFormat simpleDateFormat;

    public IsZonedDateTime(String pattern) {
        this.pattern = pattern;
        this.simpleDateFormat = new SimpleDateFormat(pattern);
    }

    @Override
    public boolean matches(Object item) {
        if ((item instanceof String)) {
            String stringItem = (String) item;
            try {
                simpleDateFormat.parse(stringItem).toInstant();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("String is a date");
    }
}
