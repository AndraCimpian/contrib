/*
 * AccentRemover.java
 *
 * Created on 4 de Junho de 2003, 23:28
 */

package pt.utl.ist.characters;

/**
 *
 * @author  Nuno Freire
 */
public class AccentRemover {
    
	
	
    public static char unaccentedUppercaseChar(char c){
        c=Character.toLowerCase(c);
        switch (c){
            case '�':
            case '�':
            case '�':
            case '�':
            case '�':
                return 'A';
            case '�':
            case '�':
            case 'e':
            case '�':
                return 'E';
            case '�':
            case '�':
            case '�':                
            case '�':
                return 'I';
            case '�':
            case '�':
            case '�':
            case '�':
            case '�':
                return 'O';
            case '�':
            case '�':
            case '�':
            case '�':
                return 'U';
            case '�':
            case '�':
                return 'Y';
            case '�':
                return 'C';
            case '�':
                return 'N';
            default:
                return Character.toUpperCase(c);
        }
    }
	
    public static String convertToUnaccentedUppercaseChars(String s){
		StringBuffer ret=new StringBuffer(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			ret.append(unaccentedUppercaseChar(c));
		}		
		return ret.toString();
    }
	
	
}
