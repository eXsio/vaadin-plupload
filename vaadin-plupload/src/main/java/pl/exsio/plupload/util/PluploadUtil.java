package pl.exsio.plupload.util;

/**
 *
 * @author exsio
 */
public class PluploadUtil {

    public static String trimTextInTheMiddle(String text, int length) {

        if (text.length() <= length) {
            return text;
        } else {
            String prefix = text.substring(0, (int) Math.floor(length / 2));
            String suffix = text.substring(text.length() - (int) Math.ceil(length / 2), text.length());
            return new StringBuilder(prefix).append("..").append(suffix).toString();
        }

    }
}
