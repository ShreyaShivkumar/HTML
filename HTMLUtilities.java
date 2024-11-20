/**
 * Utilities for handling HTML. Deals with tags, words, numbers, 
 * punctuation, comments, and pre-formatted text and stores each token 
 * into an array of tokens.
 *
 * @author Shreya Shivkumar
 * @since November 1, 2024
 */
public class HTMLUtilities {

	// NONE = not nested in a block, COMMENT = inside a comment block
	// PREFORMAT = inside a pre-format block
	private enum TokenState { NONE, COMMENT, PREFORMAT };
	// the current tokenizer state
	private TokenState state; // state of token, whether in comment, 
		// preformatted, or none

	public HTMLUtilities()
	{
		state = TokenState.NONE;
	}

	/**
	* Break the HTML string into tokens. The array returned is
	* exactly the size of the number of tokens in the HTML string.
	* Example: HTML string = "Goodnight moon goodnight stars"
	* returns { "Goodnight", "moon", "goodnight", "stars" }
	* @param str the HTML string
	* @return the String array of tokens
	*/
	public String[] tokenizeHTMLString(String str) {
		// make the size of the array large to start
		String[] result = new String[10000];
		char current = 'p';
		int resultNum = 0;
		boolean punct = false;
		
		String start = "";
		String end = "";
		for(int i = 0; i < str.length(); i++)
		{
			if(str.equals("</pre>") && state == TokenState.PREFORMAT)
				state = TokenState.NONE;
			if(state == TokenState.COMMENT) 
			{
				if(str.indexOf("-->") != -1)
				{
					str = str.substring(str.indexOf("-->") + 2,
					str.length() -1);
					state = TokenState.NONE;
				}
				else
					return result; 
			}
			else if(state == TokenState.PREFORMAT)
			{
				result[resultNum] = str;
				resultNum++;
				return result;
			}
			
			if(str.indexOf("<!--") != -1)
			{
				if(str.indexOf("-->") != -1)
				{
					start = str.substring(0, str.indexOf("<!--"));
					end = str.substring(str.indexOf("-->") + 2, 
						str.length() - 1);
					str = start + end;
					state = TokenState.NONE;
				}
				else
				{
					state = TokenState.COMMENT;
					str = str.substring(0, str.indexOf("<!--"));
				}
			}
			else if(str.equals("<pre>"))
				state = TokenState.PREFORMAT;
			else 
				state = TokenState.NONE; 
				
			String token = "";
			char last = 'p';
			
			if(i < str.length())
				current = str.charAt(i);
					
			if(current == '<') 
			{
				int endTag = str.indexOf('>', i);
				if (endTag != -1) 
				{
					result[resultNum] = str.substring(i, endTag + 1); 
					resultNum++;
					i = endTag;  
				}
			}
			
			char next = 'p';
			if(i + 1 < str.length())
				next = str.charAt(i + 1);
			while(((current >= 'A' && current <= 'Z') || 
				(current >= 'a' && current <= 'z') || 
				(current == '-' && !isNumber(next))) && i < str.length())
			{
				token += "" + current;
				i++;
				result[resultNum] = token;
				if(i < str.length())
					current = str.charAt(i);
				if(current == '.')
					punct = true;
				if(current == '<')
					i--;
			}
		
			while((isNumber(current) || ((current == '-' || current == '.') 
				&& isNumber(next)) || (current == 'e' 
				&& (next == '-' || isNumber(next)))) && i < str.length()) 
			{
				token += "" + current;
				i++;
				result[resultNum] = token;
				if( i < str.length()) 
					current = str.charAt(i);
				if(i + 1 < str.length()) 
					next = str.charAt(i + 1);
			}
			if(!(token.equals("")))
				resultNum++;		
			punct = isPunctuation(current);	
			if(punct)
			{
				result[resultNum] = "" + current;
				resultNum++;
			}
		}
			
		return result;
	}

	/**
	* Returns true if passed in char is equal to any of the cases, or 
	* returns false by default.
	* @param current the current char being checked in tokenizeHTMLString
	* @return whether passed in char is punctuation or not
	*/
	public boolean isPunctuation(char current)
	{
		switch(current)
		{
			case '.':
			case ',':
			case ';':
			case ':':
			case '(':
			case ')':
			case '?':
			case '!':
			case '=':
			case '&':
			case '~':
			case '+':
			case '-':
			return true;
			default: return false;
		}
	}


	/**
	* Returns if passed in char is equal to any of the cases, or returns
	* false by default.
	* @param current char being checked in tokenizeHTMLString
	* @return whether char is a number or not
	*/
	public boolean isNumber(char current)
	{
		switch(current)
		{
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
			return true;
			default: return false;
		}
	}

	/**
	* Print the tokens in the array to the screen
	* Precondition: All elements in the array are valid String objects.
	* (no nulls)
	* @param tokens an array of String tokens
	*/
	public void printTokens(String[] tokens) {
		if (tokens == null) return;
		for (int a = 0; a < tokens.length; a++) {
			if (a % 5 == 0 && tokens[a] != null) 
				System.out.print("\n  ");
			if(tokens[a] != null)
				System.out.print("[token " + a + "]: " + tokens[a] + " ");
		}
		System.out.println();
	}

}
