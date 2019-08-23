package data_structures;

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

public class Rule {

	/*
	 * A rule is a directed relationship between two or more terms; i.e. term
	 * entities are related using rules. It can be treated as
	 * graph where the nodes are terms and the edges are rules.
	 */

	/*
	 * A rule has one to many left hand side terms and exactly one right hand
	 * side. Examples of rules: general form: A [t] --Help--> B [u] general form: (C
	 * [l], D [k]]) -- and --> F [j] general form: (E [t1], R [t2], S [t3, t4]) --
	 * or --> U [t5, t6]
	 * 
	 * An instance of a real-world rule: Accessibility [API] --Help--> Adoptability
	 * [API]
	 * 
	 * Notes: If the rule type is help, hurt, make, break, somePlus, and someMinus,
	 * then the rule should have exactly one left-hand side term. If the rule type
	 * is AND, or OR, then the rule should have at least two left-hand -side term.
	 * 
	 */

	private static int NumberOfRules = 0;
	private int RuleNumber;

	/*
	 * Each rule has one left hand side that consists of one to many terms.
	 * LeftHandSide of a rule is like a Set since the ordering of the terms should
	 * not influence a rule. 
	 */
	private ArrayList<Term> precedent = new ArrayList<Term>();

	/*
	 * Each rule has exactly one right hand side that consists of (is of type of)
	 * (exactly one term); Terms are nodes in a graph.
	 */
	private Term consequent;

	/*
	 * Each rule has one type of RuleType. *
	 */
	private RuleType RuleType;

	/*
	 * Each rule has a category of type RuleCategory.
	 */
	private RuleCategory RuleCategory;

	/*
	 * A rule can have a set of previous rules. Previous Rule: LHS of the Rule is
	 * RHS in the previous rules.
	 */
	private ArrayList<Rule> PreviousRules = new ArrayList<Rule>();

	/*
	 * A rule can have a set of next rules. Next Rule: RHS of the Rule is LHS in the
	 * previous rules.
	 */
	private ArrayList<Rule> NextRules = new ArrayList<Rule>();


	//------------------------------------------



	/*
	 * Each rule can have other attributes that can be added later. For example,
	 * applicability condition; or degree of validity or confidence; or degree of
	 * satisfaction;
	 */


	//------------------------------------------	



	public Rule() { // The constructor for an empty rule.
		this.NumberOfRules++;
		this.setRuleNumber(this.NumberOfRules);

		this.setConsequent(null);
		this.setRuleType(RuleType.UNDET);
		this.setRuleCategory(RuleCategory.UNDET);
	}

	public Rule(ArrayList<Term> LeftHandSide, Term RightHandSide, RuleType RuleType, RuleCategory RuleCategory) {

		this.NumberOfRules++;
		this.setRuleNumber(this.NumberOfRules);

		this.setPrecedent(LeftHandSide);
		this.setConsequent(RightHandSide);
		this.setRuleType(RuleType);
		this.setRuleCategory(RuleCategory);

		/*
		 * Constraint checking should be done in order to make sure that the constructed
		 * rules are of correct type.
		 */
	}

	public Rule(Term LeftHandSide, Term RightHandSide, RuleType RuleType, RuleCategory RuleCategory) {

		this.NumberOfRules++;
		this.setRuleNumber(this.NumberOfRules);

		ArrayList<Term> LHSArray = new ArrayList<Term>();
		LHSArray.add(LeftHandSide);

		this.setPrecedent(LHSArray);
		this.setConsequent(RightHandSide);
		this.setRuleType(RuleType);
		this.setRuleCategory(RuleCategory);

		/*
		 * Constraint checking should be done in order to make sure that the constructed
		 * rules are of correct type.
		 */
	}

	public int getRuleNumber() {
		return this.RuleNumber;
	}

	public void setRuleNumber(int ruleNumber) {
		this.RuleNumber = ruleNumber;
	}

	public ArrayList<Term> getPrecedent() {
		return this.precedent;
	}

	public void setPrecedent(ArrayList<Term> leftHandSide2) {
		this.precedent = leftHandSide2;
	}

	public Term getConsequent() {
		return this.consequent;
	}

	public void setConsequent(Term rightHandSide) {
		this.consequent = rightHandSide;
	}

	public RuleType getRuleType() {
		return this.RuleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.RuleType = ruleType;
	}

	public RuleCategory getRuleCategory() {
		return this.RuleCategory;
	}

	public void setRuleCategory(RuleCategory ruleCategory) {
		this.RuleCategory = ruleCategory;
	}



	/**
	 * @return the previousRules
	 */
	public ArrayList<Rule> getPreviousRules() {
		return PreviousRules;
	}

	/**
	 * @param previousRules the previousRules to set
	 */
	public void setPreviousRules(ArrayList<Rule> previousRules) {
		this.PreviousRules = previousRules;
	}

	/**
	 * @return the nextRules
	 */
	public ArrayList<Rule> getNextRules() {
		return NextRules;
	}

	/**
	 * @param nextRules the nextRules to set
	 */
	public void setNextRules(ArrayList<Rule> nextRules) {
		this.NextRules = nextRules;
	}

	public boolean hasEqualRightHandSide(Term RHS) {

		return this.consequent.isEqual(RHS);

	}

	public boolean hasEqualLeftHandSide(ArrayList<Term> LHS) {

		boolean IsEqual = true;
		int countofEqualTerms = 0;

		if (this.precedent.size() != LHS.size())
			IsEqual = false;

		else {

			for (Term t1 : LHS)
				for (Term t2 : this.precedent)
					if (t1.isEqual(t2))
						countofEqualTerms++;

			if (countofEqualTerms != LHS.size())
				IsEqual = false;
		}

		return IsEqual;
	}

	public boolean isEqual(Rule r) {
		boolean isEqual = false;
		if (this.hasEqualLeftHandSide(r.getPrecedent()) && this.hasEqualRightHandSide(r.getConsequent())
				&& this.RuleType == r.RuleType)
			isEqual = true;

		return isEqual;
	}

	public boolean containsPrecedent(ArrayList<Term> lhs) {



		boolean matched = true;

		int i = 0;
		while (matched && i < lhs.size()) {
			boolean oneMatch = false;

			for (Term t1 : this.precedent)
				if (t1.isEqual(lhs.get(i)))
					oneMatch = true;

			if (!oneMatch)
				matched = false;
			i++;
		}

		return matched;
	}

	public int findeTermInPrecedent(Term t) {

		int index = -1;
		for (int i = 0; i < this.precedent.size(); i++)
			if (this.precedent.get(i).isEqual(t))
				index = i;
		return index;
	}

	/*
	 * Format Rule and make it ready for printing.
	 */

	public String printPrecedent()

	{
		String LhsPrint = "(";

		for (Term t : this.precedent)
			LhsPrint = LhsPrint + t.printTerm() + ", ";

		LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 2) + ")";

		return LhsPrint;
	}

	public String printPrecedentInEnglish()

	{
		String LhsPrint = "";

		int counter = 0;

		if (this.getPrecedent().size() > 1) {
			char identifier = 'a';
			for (Term t : this.precedent) {
				LhsPrint = LhsPrint + "(" + identifier + ") " + t.printTermInEnglish() + ", " + this.RuleType.toString().toLowerCase()+ " ";
				// Only print three consequent terms in a line.
				identifier++;
				counter++;
				if (counter % 2 == 0)
					LhsPrint = LhsPrint + "\n\n";
			}
		}

		else {
			LhsPrint = LhsPrint + this.precedent.get(0).printTermInEnglish();
		}

		if (Objects.equals(this.RuleType.toString().toLowerCase(),"or"))
		{

			LhsPrint = LhsPrint.trim ();
			LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 4); 


		}

		else if (Objects.equals(this.RuleType.toString().toLowerCase(),"and"))
		{

			LhsPrint = LhsPrint.trim ();
			LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 5); 

		}

		return LhsPrint;
	}

	public String printRightHandSide() {
		String RhsPrint = this.consequent.printTerm();
		return RhsPrint;
	}

	public String printRightHandSideInEnglish() {
		String RhsPrint = this.consequent.printTermInEnglish();
		return RhsPrint;
	}

	public String printRule() {

		String rule = this.printPrecedent() + "\n" + " ---" + this.RuleType + "---> " + this.printRightHandSide()
		+ "\n";

		return rule;

	}

	public String printRuleInEnglish() {


		String rule = this.printPrecedentInEnglish();

		if (this.RuleCategory == RuleCategory.COR)
			if ((this.RuleType == RuleType.HELP))
				rule = rule + " has weak positive impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.HURT))
				rule = rule + "has weak negative impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.MAKE))
				rule = rule + " has strong positive impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.BREAK ))
				rule = rule + " has strong negative impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.SOMEPLUS))
				rule = rule + " has some positive impact on\n\n" + this.printRightHandSideInEnglish() + ". \n\n\n";

			else if ((this.RuleType == RuleType.SOMEMINUS))
				rule = rule + " has some negative impact on\n\n" + this.printRightHandSideInEnglish() + ". \n\n\n";

		// The else part to the if should be written.
		return rule;

	}

	public Term matchStringInLHS(String GivenString) {
		Term matchedTerm = null;

		for (Term t : this.precedent) {

			if (t.matchString(GivenString)) {
				matchedTerm = t;
				break;
			}
		}

		return matchedTerm;

	}

	public Rule deepCopyRule() {
		ArrayList<Term> LHSCopy = new ArrayList<Term>();
		Term RHSCopy;
		Rule RuleCopy;

		for (Term t : this.getPrecedent())

		{
			Term LHSTermCopy = t.deepCopyTerm();

			LHSCopy.add(LHSTermCopy);

		}

		RHSCopy = this.getConsequent().deepCopyTerm();
		RuleCopy = new Rule(LHSCopy, RHSCopy, this.getRuleType(), this.getRuleCategory());

		return RuleCopy;

	}

	public void ParseRule(String RuleString) {
		/*
		 * WARNING: Currently only one topic is parsed. For more than one topic we receive error.
		 */

		Rule r = null;
		ArrayList<Term> LHS = new ArrayList<Term>();
		Term RHS = null;

		String Topic = "";
		String Type = "";
		String RuleTypeString = "";
		String RuleCategoryString = "";
		RuleType RT = RuleType.UNDET;
		RuleCategory RC = RuleCategory.UNDET;

		boolean precedentExpected = false;

		boolean RuleTypeExpected = false;

		boolean cosnequentExpected = false;

		boolean TermExpected = false;

		boolean TypeExpected = false;

		boolean TopicExpected = false;

		boolean RuleCategoryExpected = false;

		for (int i = 0; i < RuleString.length(); i++) {
			char c = RuleString.charAt(i);


			if (c == '(') {
				precedentExpected = true;

				TypeExpected = true;
			} else if (c == '[') {

				Type = Type.trim();
				TopicExpected = true;
				TypeExpected = false;

			} else if (c == ']') {
				TopicExpected = false;

				Topic = Topic.trim();
				Term t = new Term(Type, Topic);

				if (precedentExpected)
					LHS.add(t);
				else if (cosnequentExpected)
					RHS = t;

				Type = "";
				Topic = "";
			}

			else if (c == ',') {
				TypeExpected = true;

			} else if (c == ')') {
				precedentExpected = false;
				TypeExpected = false;

			}

			else if (c == '-' && !precedentExpected) {
				RuleTypeExpected = true;

			}

			else if (c == '>') {

				RuleTypeExpected = false;
				cosnequentExpected = true;
				TypeExpected = true;

				RuleTypeString = RuleTypeString.trim();

				RT = RuleType.valueOf(RuleTypeString);

				RuleTypeString = "";
			}

			else if (c == ':') {
				RuleCategoryExpected = true;
			}

			else if (TypeExpected) {
				Type = Type + c;

			}

			else if (TopicExpected) {
				Topic = Topic + c;
			}

			else if (RuleTypeExpected && c != '-') {

				RuleTypeString = RuleTypeString + c;
			}

			else if (RuleCategoryExpected) {
				RuleCategoryString = RuleCategoryString + c;

			}

		}

		RuleCategoryString = RuleCategoryString.trim();
		RC = RuleCategory.valueOf(RuleCategoryString);


		this.setPrecedent(LHS);
		this.setConsequent(RHS);
		this.setRuleType(RT);
		this.setRuleCategory(RC);

	}


	public int countLHS() {
		int count = 0;

		for (Term t : this.precedent)
			count++;

		return count;
	}

	public String printRuleInReverseOrderAndInEnglish() {
		String ruleInReverseOrder = "";

		if (this.RuleCategory == RuleCategory.NF_REF) {

			if (this.countLHS() > 1)
			{
				ruleInReverseOrder = "Please elaborate on the requirement " + this.printRightHandSideInEnglish() + ". \n\n" +
						this.printRightHandSideInEnglish() + " can be refined into the following requirements: \n \n"+
						this.printPrecedentInEnglish() + ".";
			}
			else if (this.countLHS() == 1)
			{
				ruleInReverseOrder = "Please elaborate on the requirement " + this.printRightHandSideInEnglish() + ". \n\n" +
						this.printRightHandSideInEnglish() + " can be refined into the following requirement: \n \n"+
						this.printPrecedentInEnglish() + ".";
			}

		} else if (this.RuleCategory == RuleCategory.OP_FI_REF) {

			if (this.countLHS() > 1) {

				ruleInReverseOrder = "To design " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nsome possible alternatives are as follows: \n \n" + this.printPrecedentInEnglish()
						+ ".\n\n\n" + "I cannot futher refine the above design alterantives."
						+ "For the next step,\n\nI can analyze the above design alternatives or I can recommend a design solution.";
			}

			else if (this.countLHS() == 1) {
				ruleInReverseOrder = "To design " + this.printRightHandSideInEnglish() + ", "
						+ "\n\none possible alternative is as follows: \n \n" + this.printPrecedentInEnglish() + ".\n\n\n"
						+ "I cannot futher refine the above design alterantives."
						+ "For the next step,\n\nI can analyze the above design alternatives or I can recommend a design solution.";
			}

		}

		else if (this.RuleCategory == RuleCategory.OP_NF_REF) {
			if (this.countLHS() > 1) {
				ruleInReverseOrder = "To address the requirement " + this.printRightHandSideInEnglish() + ", \n\n"
						+ "the following functionalities need to be designed: \n\n" + this.printPrecedentInEnglish()
						+ ".\n\n" + "Please specify which of these functionalities need to be designed.";

			}

			else if (this.countLHS() == 1) {
				ruleInReverseOrder = "To address the requirement " + this.printRightHandSideInEnglish()
				+ ", \n\nthe following functionality needs to be designed: \n \n"
				+ this.printPrecedentInEnglish() + "."
				+ " \n\nPlease specify the functionality that needs to be designed.";

			}
		}

		else if (this.RuleCategory == RuleCategory.OP_FF_REF) {

			if (this.countLHS() > 1) {
				ruleInReverseOrder = "To design the functionality " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nthe following functionalities need to be designed: \n\n" + this.printPrecedentInEnglish()
						+ ".\n\n" + "Please specify which of these functionalities that need to be designed.";

			}

			if (this.countLHS() == 1) {
				ruleInReverseOrder = "To design the functionality " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nthe following functionality need to be designed: \n\n" + this.printPrecedentInEnglish()
						+ ".\n\n" + "Please specify the functionality that needs to be designed.";
			}

		}

		return ruleInReverseOrder;
	}
}
