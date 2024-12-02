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
	private boolean inParagraph;
		
	public HTMLRender() {
		// Initialize token array
		tokens = new String[TOKENS_SIZE];
				
		// Initialize Simple Browser
		render = new SimpleHtmlRenderer();
		browser = render.getHtmlPrinter();
		util = new HTMLUtilities();
		inParagraph = false;
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
			if(token.equalsIgnoreCase("<pre>"))
			{
				String preformatted = "";
				while(!token.equalsIgnoreCase("</pre>"))
				{
					token = tokens[count];
					browser.printPreformattedText(" " + token);
					count++;
				}
			}
			if(token.equalsIgnoreCase("<b>"))
			{
				String boldText = "";
				while(!token.equalsIgnoreCase("</b>"));
				{
					boldText += " " + tokens[count];
					count++;
				}
				browser.printBold(boldText);
			}
			if(token.equalsIgnoreCase("<i>"))
			{
				String italicText = "";
				while(!token.equalsIgnoreCase("</i>"));
				{
					italicText += " " + tokens[count];
					count++;
				}
				browser.printItalic(italicText);
			}
			if(token.equalsIgnoreCase("<p>"))
			{
				//String paragraph = "";
				browser.println();
				while(!token.equalsIgnoreCase("</p>"))
				{
					//paragraph += " " + tokens[count];
					browser.print(" " + tokens[count]);
					count++;
				}
				//System.out.println(paragraph);
			}
			else if(token.indexOf('<') == -1 && token.indexOf('>') == -1)
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
}
