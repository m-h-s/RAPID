package data_structures;



import java.io.Serializable;
import java.util.*;

/**
 * @author Mahsa Sadi
 * 
 * @since 2018 - 11 - 01
 * 
 * License: Creative Commons
 * 
 * Copyright by Mahsa Sadi
 * 
 */

public class Term {

	/*
	 * Example of general form a term: P [a], P [a, b] or better P (a), and P(a,b)
	 * Example of an instance of a term : Accessibility [API]
	 */

	/*
	 * A term has one predicate of type string; i.e. type part.
	 */
	private String predicate;

	/*
	 * A term has Zero to Many arguments or parameters; i.e. topic part which is of
	 * type string. ### Maybe the ordering of the arguments should not affect a
	 * term. If this is the case, set data type should be used.
	 * 
	 */
	private ArrayList<String> arguments = new ArrayList<String>();

	/*
	 * A term has one satisfaction value which is final.
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

	public Term() { // Creating an empty term.
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

	public void setArguments(String argument) {
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

		int i = 0;

		for (i = 0; i < this.arguments.size() - 1; i++)
			if (this.arguments.get(i) != "")
				term = term + " of " + this.arguments.get(i) + ",";

		if (this.arguments.get(i) != "") {
			term = term + " of " + this.arguments.get(i) + "";
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
		
		return Objects.equals(this.predicate.trim().toLowerCase(), t.getPredicate().trim().toLowerCase())
				&& this.hasEqualArguments(t);

	}

	public boolean hasEqualArguments(Term t) {
		
		boolean isEqual = true;

		for (String arg1 : this.getArguments())
			for (String arg2 : t.getArguments()) {
				if (!Objects.equals(arg1.trim().toLowerCase(), arg2.trim().toLowerCase())) {
					isEqual = false;
					break;
				}
			}
		return isEqual;
	}

	public boolean matchString(String TermString) {
		boolean MatchString = false;


		if (this.printTerm().replaceAll("\\s", "").toLowerCase()
				.contains(TermString.replaceAll("\\s", "").toLowerCase()))
		{

			MatchString = true;

		}
		return MatchString;
	}

	public boolean hasEqualTermString(String TermString) {
		boolean MatchString = false;
		if (Objects.equals(TermString.replaceAll("\\s", "").toLowerCase(),
				this.printTerm().replaceAll("\\s", "").toLowerCase())) {

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

	public void parseTerm(String TermString) {

		/*
		 * WARNING: This parser does not parse more than one topic.
		 */
		boolean TopicExpected = false;
		boolean TypeExpected = true;
		String Topic = "";
		String Type = "";

		TermString = TermString.trim();

		for (int i = 0; i < TermString.length(); i++) {
			char c = TermString.charAt(i);
			if (c == '[') {
				TopicExpected = true;
				TypeExpected = false;
			}

			else if (c == ']') {
				TopicExpected = false;
			} 

			else if (TopicExpected) {
				Topic = Topic + c;

			}

			else if (TypeExpected) {
				Type = Type + c;
			}
		}


		this.setPredicate(Type.trim());
		this.setArguments(Topic.trim());
	}
}
