package cs408.packattk;

/**
 * Created by Matthew on 2/10/2015.
 */
public class string {
    /**
     * Check if a character is alphaNumeric
     */
    public static boolean isAlphaNumeric(char c){
        return ((c >='0' && c<='9') || (c>='A' && c<='Z') || (c>='a' && c<='z'));
    }

    /**
     * Check if character is space
     */
    public static boolean isSpace(char c){
        return (c==' ' || c=='\t' || c=='\n' || c=='\r');
    }
}
