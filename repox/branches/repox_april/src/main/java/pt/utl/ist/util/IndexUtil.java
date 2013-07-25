package pt.utl.ist.util;

import java.util.regex.Pattern;

public class IndexUtil {
	public enum RemoveNonIndexableCharacters {REMOVE, DONT_REMOVE};
	public enum RemoveAllSpaces {REMOVE, DONT_REMOVE};
	public enum RemoveDiacritics {REMOVE, DONT_REMOVE};
	
	
	private IndexUtil() { /* static methods only - hide constructor */
	}

	private static Pattern cleanSupplusSpaces=Pattern.compile("\\s\\s+");
	private static Pattern cleanAllSpaces=Pattern.compile("\\s+");
	
    public static String encode(String s)
    {     
        return encode(s, RemoveDiacritics.REMOVE, RemoveAllSpaces.DONT_REMOVE, RemoveNonIndexableCharacters.DONT_REMOVE);
    }

    public static String encode(String s, RemoveAllSpaces removeAllSpaces)
    {
        return encode(s, RemoveDiacritics.REMOVE, removeAllSpaces, RemoveNonIndexableCharacters.DONT_REMOVE);
    }

    public static String encode(String s, RemoveDiacritics removeDiacritics, RemoveAllSpaces removeAllSpaces, RemoveNonIndexableCharacters removeNonIndexableCharacters)
    {
        StringBuffer sb = new StringBuffer();
        char[] chars = s.toCharArray();
        for (char aChar : chars) {
            if (removeNonIndexableCharacters == RemoveNonIndexableCharacters.REMOVE && !Character.isLetterOrDigit(aChar) && !Character.isWhitespace(aChar))
                sb.append(' ');
            else if (removeDiacritics == RemoveDiacritics.REMOVE)
                sb.append(IndexUtil.getEntity(aChar));
            else
                sb.append(aChar);
        }
        String ret=sb.toString();
        if(removeAllSpaces==RemoveAllSpaces.REMOVE) 
        	ret=cleanAllSpaces.matcher(ret).replaceAll("");      
        else
        	ret=cleanSupplusSpaces.matcher(ret).replaceAll(" ");          	
        return ret.trim();
    }
    
    public static String getEntity(char c)
    {
        switch (c) {
            case '�': return "a";
            case '�': return "a";
            case '�': return "a";
            case '�': return "a";
            case '�': return "a";            
            case '�': return "e";
            case '�': return "e";
            case '�': return "e";
            case '�': return "e";
            case '�': return "i";
            case '�': return "i";
            case '�': return "i";
            case '�': return "i";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "u";
            case '�': return "u";
            case '�': return "u";
            case '�': return "u";
            case '�': return "c";
            case '�': return "n";
            case '�': return "y";

            case '�': return "a";
            case '�': return "a";
            case '�': return "a";
            case '�': return "a";
            case '�': return "a";
            case '�': return "e";
            case '�': return "e";
            case '�': return "e";
            case '�': return "e";
            case '�': return "i";
            case '�': return "i";
            case '�': return "i";
            case '�': return "i";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "o";
            case '�': return "u";
            case '�': return "u";
            case '�': return "u";
            case '�': return "u";
            case '�': return "c";
            case '�': return "n";
            
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': 
            case '0': 
            	return String.valueOf(c);

			// all other characters remain the same.
            default: 
            	if (c >= 'a' && c <= 'z')
            		return String.valueOf(c);
            	else if (c >= 'A' && c <= 'Z')
            		return String.valueOf(c).toLowerCase();
            	else
            		return " ";
        }
    }
	
	
}
