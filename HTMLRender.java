import java.util.Scanner;

/**
 *	HTMLRender
 *	This program renders HTML code into a JFrame window.
 *	It requires your HTMLUtilities class and
 *	the SimpleHtmlRenderer and HtmlPrinter classes.
 *
 *	The tags supported:
 *		<html>, </html> - start/end of the HTML file
 *		<body>, </body> - start/end of the HTML code
 *		<p>, </p> - Start/end of a paragraph.
 *					Causes a newline before and a blank line after. Lines are restricted
 *					to 80 characters maximum.
 *		<hr>	- Creates a horizontal rule on the following line.
 *		<br>	- newline (break)
 *		<b>, </b> - Start/end of bold font print
 *		<i>, </i> - Start/end of italic font print
 *		<q>, </q> - Start/end of quotations
 *		<hX>, </hX> - Start/end of heading with size X = 1, 2, 3, 4, 5, 6
 *		<pre>, </pre> - Preformatted text
 *
 *	@author Shreya Shivkumar
 *  @since November 20, 2024
 *	@version 
 */
public class HTMLRender {
	
	// the array holding all the tokens of the HTML file
	private String [] tokens;
	private final int TOKENS_SIZE = 100000;	// size of array
	
	// SimpleHtmlRenderer fields
	private int lineCharCount;
	private SimpleHtmlRenderer render;
	private HtmlPrinter browser;
	private HTMLUtilities util;
	private enum TokenState { NONE, BOLD, ITALIC, PREFORMAT, HEADING1, HEADING2, 
		HEADING3, HEADING4, HEADING5, HEADING6 };
	private TokenState state;
	//private boolean inHeading1, inHeading2;
	//private boolean inHeading3;
		
	public HTMLRender() {
		// Initialize token array
		tokens = new String[TOKENS_SIZE];
				
		// Initialize Simple Browser
		render = new SimpleHtmlRenderer();
		browser = render.getHtmlPrinter();
		util = new HTMLUtilities();
		state = TokenState.NONE;
	}
	
	
	public static void main(String[] args) {
		HTMLRender hf = new HTMLRender();
		hf.run(args);
	}
	
	public void run(String[] args) {
		// Sample renderings from HtmlPrinter class
		
		// Print plain text without line feed at end
		/*browser.print("First line");
		
		// Print line feed
		browser.println();
		
		// Print bold words and plain space without line feed at end
		browser.printBold("bold words");
		browser.print(" ");
		
		// Print italic words without line feed at end
		browser.printItalic("italic words");
		
		// Print horizontal rule across window (includes line feed before and after)
		browser.printHorizontalRule();
		
		// Print words, then line feed (printBreak)
		browser.print("A couple of words");
		browser.printBreak();
		browser.printBreak();
		
		// Print a double quote
		browser.print("\"");
		
		// Print Headings 1 through 6 (Largest to smallest)
		browser.printHeading1("Heading1");
		browser.printHeading2("Heading2");
		browser.printHeading3("Heading3");
		browser.printHeading4("Heading4");
		browser.printHeading5("Heading5");
		browser.printHeading6("Heading6");
		
		// Print pre-formatted text (optional)
		browser.printPreformattedText("Preformat Monospace\tfont");
		browser.printBreak();
		browser.print("The end");*/
		
		Scanner input = null;
		String fileName = "";
		if(args.length > 0)
			fileName = args[0];
		else 
		{
			System.out.println("Usage: java -cp \".:SimpleHtmlRenderer.jar\" HTMLRender <htmlFileName>");
			System.exit(0);
		}
		
		input = FileUtils.openToRead(fileName);
		
		while (input.hasNext()) 
		{
			String line = input.nextLine();
			//System.out.println("\n" + line);
			tokens = util.tokenizeHTMLString(line);
			//for(int i = 0; i < tokens.length; i++)
				System.out.println(tokens[0]);
			//util.printTokens(tokens);
			processTokensAndRender(tokens);
		}
		input.close();
		
	}
	
	public void processTokensAndRender(String[] tokens)
	{
		int count = 0;
		String token = "";
		while(tokens[count] != null)
		{
			//charCount = token.length();
			/*if(charCount > 80)
				browser.println();*/
			token = tokens[count];
			System.out.println("HERE " + token);
			/*if(state == TokenState.NONE)
				browser.print(token);*/
				
			breakTag(token);
			horizontalRule(token);
			
			if(token.equalsIgnoreCase("<b>") || state == TokenState.BOLD)
			{
				String boldText = "";
				System.out.println("BOlD: " + token);
				if(token.equalsIgnoreCase("<b>"))
					count++;
				while(!token.equalsIgnoreCase("</b>") && tokens[count] != null)
				{
					System.out.println("STILL BOLD: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					breakTag(token);
					horizontalRule(token);
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 80)
					{
						browser.printBold(boldText);
						browser.println();
						boldText = "";
						lineCharCount = 0;
					}
					boldText += token;
					count++;
				}
				if(boldText.indexOf("</b>") != -1)
				{
					boldText = boldText.substring(0, boldText.indexOf("</b>") - 1);
					state = TokenState.NONE;
				}
				else if(boldText.indexOf("</B>") != -1)
				{
					boldText = boldText.substring(0, boldText.indexOf("</B>") - 1);
					state = TokenState.NONE;
				}
				else
					state = TokenState.BOLD;
				browser.printBold(boldText);
			}
			else if(token.equalsIgnoreCase("<i>") || state == TokenState.ITALIC)
			{
				String italicText = "";
				System.out.println("italic: " + token);
				if(token.equalsIgnoreCase("<i>"))
					count++;
				while(!token.equalsIgnoreCase("</i>") && tokens[count] != null)
				{
					System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					breakTag(token);
					horizontalRule(token);
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 80)
					{
						browser.printItalic(italicText);
						browser.println();
						italicText = "";
						lineCharCount = 0;
					}
					italicText += token;
					count++;
				}
				if(italicText.indexOf("</i>") != -1)
				{
					italicText = italicText.substring(0, italicText.indexOf("</i>") - 1);
					state = TokenState.NONE;
				}
				else if(italicText.indexOf("</I>") != -1)
				{
					italicText = italicText.substring(0, italicText.indexOf("</I>") - 1);
					state = TokenState.NONE;
				}
				else
					state = TokenState.ITALIC;
				browser.printItalic(italicText);
				System.out.println("token: " + token);
			}
			if(token.indexOf("<p>") != -1 || token.indexOf("</p>") != -1 
				|| token.indexOf("<P>") != -1 || token.indexOf("</P>") != -1 )
			{
				browser.println();
				browser.println();
				lineCharCount = 0;
			}
			if(token.equalsIgnoreCase("<pre>") || state == TokenState.PREFORMAT)
			{
				if(token.equalsIgnoreCase("<pre>"))
					count++;
				if(tokens[count] != null)
					token = tokens[count];
				if(token != null && !token.equalsIgnoreCase("<pre>") && !token.equalsIgnoreCase("</pre>"))
					browser.printPreformattedText(token);
				browser.println();
				browser.println();
				System.out.println("AFTER PREFORMAT");
				if(token.equalsIgnoreCase("</pre>"))
					state = TokenState.NONE;
				else
					state = TokenState.PREFORMAT;
				//System.out.println("IN PREFORMAT");
				//String preformatted = "";
				
				//count++;
				
				/*while(!token.equalsIgnoreCase("</pre>") && tokens[count] != null)
				{
					token = tokens[count];
					System.out.println("INSIDE OF LOOP PREFORMAT " + token);
					browser.printPreformattedText(token);
					browser.println();
					count++;
				}*/
			}
			else if(token.equalsIgnoreCase("<q>") || token.equalsIgnoreCase("</q>"))
			{
				browser.print("\" ");
			}
			
			else if(token.equalsIgnoreCase("<h1>") || state == TokenState.HEADING1)
			{
				String heading1 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
					count++;
				while(!token.equalsIgnoreCase("</h1>") && tokens[count] != null)
				{
					if(state == TokenState.NONE)
					{
						browser.println();
						browser.println();
					}
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 120)
					{
						browser.printHeading1(heading1);
						browser.println();
						heading1 = "";
						lineCharCount = 0;
					}
					heading1 += token;
					count++;
				}
				token = token.trim();
				if(token.equalsIgnoreCase("</h1>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h1") && tokens[count] == null)
					state = TokenState.HEADING1;
				
				//System.out.println("token: " + token);
				/*String heading1 = "";
				count++;
				lineCharCount = 0;
				while(!token.equalsIgnoreCase("</h1>"))
				{
					heading1 += " " + tokens[count];
					lineCharCount += tokens[count] + 1;
					count++;
				} */
				browser.printHeading1(heading1);
			}
			else if(token.equalsIgnoreCase("<h2>") || state == TokenState.HEADING2)
			{
				if(state == TokenState.NONE)
				{
					browser.println();
					browser.println();
				}
				String heading2 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
				count++;
				while(!token.equalsIgnoreCase("</h2>") && tokens[count] != null)
				{
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 50)
					{
						browser.printHeading2(heading2);
						browser.println();
						heading2 = "";
						lineCharCount = 0;
					}
					heading2 += token;
					count++;
				}  
				System.out.println("NEVER BEFORE SEEN: " + heading2);
				System.out.println("TOKEN OF NEVER SEEN HEDING 2: " + token);
				token = token.trim();
				if(token.equalsIgnoreCase("</h2>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h2") && tokens[count] == null)
					state = TokenState.HEADING2;
				
				browser.printHeading2(heading2);
				if(state == TokenState.NONE)
				{
					browser.println();
					browser.println();
				}
			}
			else if(token.equalsIgnoreCase("<h3>") || state == TokenState.HEADING3)
			{
				if(state == TokenState.NONE)
				{
					browser.println();
					browser.println();
				}
				String heading3 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
				//count++;
				while(!token.equalsIgnoreCase("</h3>") && tokens[count] != null)
				{
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(tokens[count].equalsIgnoreCase("<h3>"))
						count++;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 60)
					{
						browser.printHeading3(heading3);
						browser.println();
						heading3 = "";
						lineCharCount = 0;
					}
					heading3 += token;
					count++;
				}			
				token = token.trim();	
				if(token.equalsIgnoreCase("</h3>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h3>") && tokens[count] == null)
					state = TokenState.HEADING3;
				
				browser.printHeading3(heading3);
				if(state == TokenState.NONE)
				{
					browser.println();
					System.out.println("OUT OF HEADING 3");
					//browser.println();
				}
			}
			else if(token.equalsIgnoreCase("<h4>") || state == TokenState.HEADING4)
			{
				if(state == TokenState.NONE)
				{
					browser.println();
					browser.println();
				}
				String heading4 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
				count++;
				while(!token.equalsIgnoreCase("</h4>") && tokens[count] != null)
				{
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 80)
					{
						browser.printHeading4(heading4);
						browser.println();
						heading4 = "";
						lineCharCount = 0;
					}
					heading4 += token;
					count++;
				}
				token = token.trim();
				if(token.equalsIgnoreCase("</h4>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h4") && tokens[count] == null)
					state = TokenState.HEADING4;
				
				browser.printHeading4(heading4);
				if(state == TokenState.NONE)
				{
					browser.println();
					browser.println();
				}
			}
		else if(token.equalsIgnoreCase("<h5>") || state == TokenState.HEADING5)
			{
				if(state == TokenState.NONE)
				{
					browser.println();
					//browser.println();
				}
				String heading5 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
				count++;
				while(!token.equalsIgnoreCase("</h5>") && tokens[count] != null)
				{
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 100)
					{
						browser.printHeading5(heading5);
						browser.println();
						heading5 = "";
						lineCharCount = 0;
					}
					heading5 += token;
					count++;
				}
				token = token.trim();
				if(token.equalsIgnoreCase("</h5>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h5") && tokens[count] == null)
					state = TokenState.HEADING5;
				
				browser.printHeading5(heading5);
				if(state == TokenState.NONE)
				{
					browser.println();
					//browser.println();
				}
			}
			else if(token.equalsIgnoreCase("<h6>") || state == TokenState.HEADING6)
			{
				if(state == TokenState.NONE)
				{
					browser.println();
					//browser.println();
				}
				String heading6 = "";
				//System.out.println("italic: " + token);
				//if(token.equalsIgnoreCase("<i>"))
				count++;
				while(!token.equalsIgnoreCase("</h6>") && tokens[count] != null)
				{
					//System.out.println("STILL italic: " + token + " at " + count);
					boolean isPunct = false;
					char p = 'p';
					if(tokens[count].length() == 1)
					{
						p = tokens[count].charAt(0);
						isPunct = util.isPunctuation(p);
					}
					else
						isPunct = false;
					if(!isPunct)
						token =  " " + tokens[count];
					else
						token = tokens[count];
					lineCharCount += token.length();
					System.out.println("LINE CHARCOUNT: " + lineCharCount + " LENGTH: " + token.length());
					if(lineCharCount >= 120)
					{
						browser.printHeading6(heading6);
						browser.println();
						heading6 = "";
						lineCharCount = 0;
					}
					heading6 += token;
					count++;
				}
				token = token.trim();
				if(token.equalsIgnoreCase("</h6>"))
					state = TokenState.NONE;
				else if(!token.equalsIgnoreCase("</h6") && tokens[count] == null)
					state = TokenState.HEADING6;
				
				browser.printHeading6(heading6);
				if(state == TokenState.NONE)
				{
					browser.println();
					//browser.println();
				}
			}
			else if((token.indexOf('<') == -1 && token.indexOf('>') == -1) && state == TokenState.NONE)
			{
				
				boolean isPunct = false;
				if(token.length() == 1)
				{
					char current = token.charAt(0);
					isPunct = util.isPunctuation(current);
				}
				String printed = "";
				if(!isPunct)
					printed = " " + token;
				else if(isPunct)
					printed = token;
				browser.print(printed);
				lineCharCount += printed.length();
				System.out.println("CHARCOUNT " + lineCharCount);
				if(lineCharCount >= 80)
				{
					browser.println();
					lineCharCount = 0;
				}
			}
			count++;
		}
	}
	
	public void breakTag(String token)
	{
		if(token.equalsIgnoreCase("<br>"))
		{
			browser.printBreak();
			lineCharCount = 0;
		}
	}
	
	public void horizontalRule(String token)
	{
		if(token.equalsIgnoreCase("<hr>"))
		{
			browser.printHorizontalRule();
			lineCharCount = 0;
		}
	}
}
