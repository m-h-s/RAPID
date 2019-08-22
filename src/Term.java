import java.io.Serializable;
import java.util.*;

/* Each term has a satisfaction value which is either undetermined or is determined 
 * and is in the range  * of satisfaction values.
 * Note that UNDET is not defined in the logic or language 
 * but is required to distinguish between a term whose value is not identified 
 * and one whose value is identified. 
 * */
enum TermSatisfactionValues {
	UNDET, DEN, PDEN, CONF, UNKNOWN, PSAT, SAT,DEN_BY_NO_REl, SAT_BY_NO_REL
}

/*
 * Note that the definition of term data structure is in abstract from; i.e. the
 * way it is used for processing operations. This data structure should be
 * augmented with additional attributes in order to be stored in a database;
 * e.g. TermID.
 */

public class Term {

	/*
	 * Example of general form a term: P [a], P [a, b] or better P (a), and P(a,b)
	 * Example of an instance of a term : Accessibility [API]
	 */

	/*
	 * A term has one predicate of type string; i.e. type part.
	 */
	private String predicate; // The type part of a term

	/*
	 * A term has Zero to Many arguments or parameters; i.e. topic part which are of
	 * type string. ### Maybe the ordering of the arguments should not affect a
	 * term. If this is the case, set data type should be used.
	 * 
	 */
	private ArrayList<String> arguments = new ArrayList<String>();

	/*
	 * A term has One satisfaction value which is final.
	 * 
	 */
	private TermSatisfactionValues finalSatValue = TermSatisfactionValues.UNDET;

	/*
	 * A term may receive several sat values during an evaluation procedure.
	 * 
	 */
	private ArrayList<TermSatisfactionValues> ReceivedSatValues = new ArrayList<TermSatisfactionValues>();

	////////////////////////////////////////////////////
	///////////////////////////////////////////////////

	
	public Term ()
	{   //Creating an empty term.
		this.setPredicate("");
		
	}
	public Term(String predicate, ArrayList arguments) {

		this.setPredicate(predicate);
		this.setArguments(arguments);

	}

	public Term(String predicate, String argument) {

		this.setPredicate(predicate);
		ArrayList<String> ArgArray = new ArrayList<String>();
		ArgArray.add(argument);
		this.setArguments(ArgArray);
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public ArrayList<String> getArguments() {
		return arguments;
	}

	public void setArguments(ArrayList<String> arguments) {
		this.arguments = arguments;
	}
	
	public void setArguments(String argument)
	{
		this.arguments.add(argument);
	}

	public TermSatisfactionValues getFinalSatValue() {
		return finalSatValue;
	}

	public void setFinalSatValue(TermSatisfactionValues finalSatValue) {
		this.finalSatValue = finalSatValue;
	}

	public ArrayList<TermSatisfactionValues> getReceivedSatValues() {
		return ReceivedSatValues;
	}

	/*
	 * Received values are added one by one.
	 */
	public void setReceivedSatValue(TermSatisfactionValues ReceivedValue) {
		this.ReceivedSatValues.add(ReceivedValue);
	}

	public String printTerm() {
		String term = "";

		term = term + this.predicate + " ";

		int i = 0;
		for (i = 0; i < this.arguments.size() - 1; i++)
			term = term + "[" + this.arguments.get(i) + "]" + ",";
		term = term + "[" + this.arguments.get(i) + "]";

		return term;

	}
	
	
	public String printTermInEnglish() {
		String term = "";

		term = term + this.predicate;

		
		// I think this part does not work correctly.
			int i = 0;
			
			for (i = 0; i < this.arguments.size() - 1; i++)
				if (this.arguments.get(i) !="")
				    term = term + " of " + this.arguments.get(i) + ",";
			
			if (this.arguments.get(i) !="")
				{ term = term + " of " + this.arguments.get(i) + "";
				  System.out.println("argument is not empty");
				}
		

		return term;

	}
	

	public String printFinalSatStatus() {
		String termFinalSatStat = "";

		termFinalSatStat = this.printTerm();

		termFinalSatStat = termFinalSatStat + ": " + this.finalSatValue;

		return termFinalSatStat;

	}

	public String printReceivedStatus() {
		String termReceivedStatus = "";
		termReceivedStatus = this.printTerm() + ": ";

		int i = 0;

		for (i = 0; i < this.ReceivedSatValues.size() - 1; i++)
			termReceivedStatus = termReceivedStatus + this.ReceivedSatValues.get(i) + ",";

		termReceivedStatus = termReceivedStatus + this.ReceivedSatValues.get(i);

		return termReceivedStatus;
	}

	public boolean isEqual(Term t) {
		//System.out.println("pred1 + pred2");
		//System.out.println(this.getPredicate() + t.getPredicate());
		//boolean HasEqualArg = ;
		//System.out.println(HasEqualArg);
		return Objects.equals(this.predicate.trim().toLowerCase(), t.getPredicate().trim().toLowerCase()) && this.hasEqualArguments(t) ;
		

	}
	
	public boolean hasEqualArguments (Term t)
	{
		//System.out.println("########################");
		boolean isEqual = true;

		//System.out.println("this.length:"+ this.getArguments().size());
		//System.out.println("t.length:"+ this.getArguments().size());
		for (String arg1 : this.getArguments())
			for (String arg2: t.getArguments())
				{
				     //System.out.println("arg1 + arg2");   
				    // System.out.println(arg1 + arg2);
					if (! Objects.equals(arg1.trim().toLowerCase(),arg2.trim().toLowerCase()))
					{   
						 System.out.println("arg1:"+arg1.length());
					     System.out.println("arg2:"+arg2.length());
						isEqual = false;
						break;
					}
				}
		return isEqual;
	}

	public boolean matchString(String TermString) {
		boolean MatchString = false;
		
		//System.out.println("This term :"+ this.printTerm().replaceAll("\\s", "").length());
		//System.out.println("Received  term :"+ TermString.replaceAll("\\s", "").length());
		/*
		for (int i = 0; i< TermString.replaceAll("\\s", "").length(); i++)
			{
			   System.out.println (this.printTerm().replaceAll("\\s", "").toLowerCase().charAt(i) );
			   System.out.println (TermString.replaceAll("\\s", "").charAt(i));
			   System.out.println (this.printTerm().replaceAll("\\s", "").toLowerCase().charAt(i) == TermString.replaceAll("\\s", "").toLowerCase().charAt(i));
			}
			*/
		
		
		if (this.printTerm().replaceAll("\\s", "").toLowerCase().contains(TermString.replaceAll("\\s", "").toLowerCase()))
		//		 /*&& TermString.replaceAll("\\s", "").toLowerCase().contains(this.printTerm().replaceAll("\\s", "").toLowerCase())*/ )
		
		//if (Objects.equals(TermString.replaceAll("\\s", "").toLowerCase(), this.printTerm().replaceAll("\\s", "").toLowerCase()))	
		{ 
			
				
			MatchString = true;
		  
		}
		return MatchString;
	}
   
	
	public boolean hasEqualTermString (String TermString )
	{
		boolean MatchString = false;
		if (Objects.equals(TermString.replaceAll("\\s", "").toLowerCase(), this.printTerm().replaceAll("\\s", "").toLowerCase()))	
		{ 
			
				
			MatchString = true;
		  
		}
		return MatchString;
		
	}
	
	public Term deepCopyTerm() {

		ArrayList<String> ArgumentsCopy = new ArrayList<String>();

		for (int i = 0; i < this.getArguments().size(); i++)
			ArgumentsCopy.add(this.getArguments().get(i).toString());

		Term TermCopy = new Term(this.getPredicate().toString(), ArgumentsCopy);
		return TermCopy;

	}
	
	public void parseTerm (String TermString)
	{
		
		// # This parser does not parse more than one topic. 
		boolean TopicExpected = false;
		boolean TypeExpected = true;
		String Topic ="";
		String Type ="";
		
		TermString = TermString.trim();
		
		for (int i=0; i< TermString.length(); i++)
		{
			char c = TermString.charAt(i); 
			if (c == '[')
			{
				TopicExpected = true;
				TypeExpected = false;
			}
			
			else if (c == ']')
			{
				TopicExpected = false;
			}
			else if (TopicExpected)
			{
				Topic = Topic + c;
				//System.out.println(Type);
			}
			
			else if (TypeExpected)
			{
				Type = Type + c;
			}
			// else if (c == ',')  another topic expected. This topic ends.
		}
		//System.out.println(Type);
		//System.out.println(Topic);
		this.setPredicate(Type.trim());
		this.setArguments(Topic.trim());
	}
}
