package data_structures;

import java.util.*;



public class Rule {

	/*
	 * A rule is a directed relationship between two or more terms; i.e. term
	 * entities (objects, datatypes) are related using rules. It can be treated
	 * graph where the nodes are terms and the edges are rules. A rule connects two
	 * nodes of type term.
	 */

	/*
	 * ###### A rule has one to many left hand side terms and exactly one right hand
	 * side. Examples of rules: general form: A [t] --Help--> B [u] general form: (C
	 * [l], D [k]]) -- and --> F [j] general form: (E [t1], R [t2], S [t3, t4]) --
	 * or --> U [t5, t6]
	 * 
	 * An instance of a real-world rule: Accessibility [API] --Help--> Adoptability
	 * [API]
	 * 
	 * Notes: If the rule type is help, hurt, make, break, somePlus, and someMinus,
	 * then the rule should have exactly one left-hand side term. If the rule type
	 * is and, or or, then the rule should have at least two left-hand -side term.
	 * 
	 */

	/*
	 * Each rule has a unique name or has a unique number. -- Since rule number
	 * should be unique; its value should be stored somewhere.---
	 */
	private static int NumberOfRules = 0;
	private int RuleNumber;

	/*
	 * Each rule has one left hand side that consists of one to many terms.
	 * LeftHandSide of a rule is like a Set since the ordering of the terms should
	 * not influence a rule. Terms are nodes in a graph. ---I do not know how to
	 * define constraints such as cardinality constraints on data items--.
	 */
	private ArrayList<Term> LeftHandSide = new ArrayList<Term>();

	/*
	 * Each rule has exactly one right hand side that consists of (is of type of)
	 * (exactly one term); Terms are nodes in a graph.
	 */
	private Term RightHandSide;

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

	// ----------------------------------------
	/*
	 * Each rule can have other attributes that can be added later. For example,
	 * applicability condition; or degree of validity or confidence; or degree of
	 * satisfaction;
	 */

	/*
	 * To write a rule in the RuleBase an Id should be assigned to each rule.
	 * private int RuleID;
	 */

//------------------------------------------	

///////////////////////////////////////
///////////////////////////////////////

	// There should be an easier way to check the type of lhs and right one
	// constructor.

	public Rule() { // The constructor for an empty rule.
		this.NumberOfRules++;
		this.setRuleNumber(this.NumberOfRules);

		this.setRightHandSide(null);
		this.setRuleType(RuleType.UNDET);
		this.setRuleCategory(RuleCategory.UNDET);
	}

	public Rule(ArrayList<Term> LeftHandSide, Term RightHandSide, RuleType RuleType, RuleCategory RuleCategory) {

		this.NumberOfRules++;
		this.setRuleNumber(this.NumberOfRules);

		this.setLeftHandSide(LeftHandSide);
		this.setRightHandSide(RightHandSide);
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

		this.setLeftHandSide(LHSArray);
		this.setRightHandSide(RightHandSide);
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

	public ArrayList<Term> getLeftHandSide() {
		return this.LeftHandSide;
	}

	public void setLeftHandSide(ArrayList<Term> leftHandSide2) {
		this.LeftHandSide = leftHandSide2;
	}

	public Term getRightHandSide() {
		return this.RightHandSide;
	}

	public void setRightHandSide(Term rightHandSide) {
		this.RightHandSide = rightHandSide;
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

	/*
	 * Match right-hand-side of this rule with a given RightHandSide term;
	 */

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

		return this.RightHandSide.isEqual(RHS);

	}

	public boolean hasEqualLeftHandSide(ArrayList<Term> LHS) {
		/*
		 * Go through this.lhs to see if it equals the given lhs.
		 */
		boolean IsEqual = true;
		int countofEqualTerms = 0;

		if (this.LeftHandSide.size() != LHS.size())
			IsEqual = false;

		else {

			for (Term t1 : LHS)
				for (Term t2 : this.LeftHandSide)
					if (t1.isEqual(t2))
						countofEqualTerms++;

			if (countofEqualTerms != LHS.size())
				IsEqual = false;
		}

		return IsEqual;
	}

	public boolean isEqual(Rule r) {
		boolean isEqual = false;
		if (this.hasEqualLeftHandSide(r.getLeftHandSide()) && this.hasEqualRightHandSide(r.getRightHandSide())
				&& this.RuleType == r.RuleType)
			isEqual = true;

		return isEqual;
	}

	public boolean containsLeftHandSide(ArrayList<Term> lhs) {

		/*
		 * Go through this.lhs to see if it contains the given lhs.
		 */

		boolean matched = true;

		int i = 0;
		while (matched && i < lhs.size()) {
			boolean oneMatch = false;

			for (Term t1 : this.LeftHandSide)
				if (t1.isEqual(lhs.get(i)))
					oneMatch = true;

			if (!oneMatch)
				matched = false;
			i++;
		}

		return matched;
	}

	public int findTerminLeftHandSide(Term t) {

		int index = -1;
		for (int i = 0; i < this.LeftHandSide.size(); i++)
			if (this.LeftHandSide.get(i).isEqual(t))
				index = i;
		return index;
	}

	/*
	 * Format Rule and make it ready for printing.
	 */

	public String printLeftHandSide()

	{
		String LhsPrint = "(";

		for (Term t : this.LeftHandSide)
			LhsPrint = LhsPrint + t.printTerm() + ", ";

		LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 2) + ")";

		return LhsPrint;
	}

	public String printLeftHandSideInEnglish()

	{
		String LhsPrint = "";

		int counter = 0;

		if (this.getLeftHandSide().size() > 1) {
			char identifier = 'a';
			for (Term t : this.LeftHandSide) {
				LhsPrint = LhsPrint + "(" + identifier + ") " + t.printTermInEnglish() + ", " + this.RuleType.toString().toLowerCase()+ " ";
				// Only print three lhs in a line.
				// Formating print should be probabaly some where else.
				identifier++;
				counter++;
				if (counter % 2 == 0)
					LhsPrint = LhsPrint + "\n\n";
			}
		}

		else {
			LhsPrint = LhsPrint + this.LeftHandSide.get(0).printTermInEnglish();
		}

		if (Objects.equals(this.RuleType.toString().toLowerCase(),"or"))
		{
			/*
			System.out.println("\n QQQQQQQQQQQQQQQQQQQQQQQQQQQ \n");
			if (LhsPrint.contains("\n\n"))
		       {LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 7);
		       System.out.println("\n sssssssssss \n");
		       }
			else 
			{
				LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 5);
				System.out.println("\n WWWWWWWWWWWWWWW \n");
			}
			*/
			System.out.println("\n QQQQQQQQQQQQQQQQQQQQQQQQQQQ \n");
			LhsPrint = LhsPrint.trim ();
			LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 4); 
			
			
		}
		
		else if (Objects.equals(this.RuleType.toString().toLowerCase(),"and"))
		{
			/*
			System.out.println("\n uuuuuuuuuuuuuuuuuuuuuuuuuuuuuu \n");
			System.out.println("sssssssssss"+ LhsPrint.trim());
			if (LhsPrint.contains("\n\n"))
			       { LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 8);
			         System.out.println("\n sssssssssss \n");
			       }
				else 
				{
					LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 6);
					System.out.println("\n WWWWWWWWWWWWWWW \n");
				}
				
				
				*/
			System.out.println("\n uuuuuuuuuuuuuuuuuuuuuuuuuuuuuu \n");
			LhsPrint = LhsPrint.trim ();
			LhsPrint = LhsPrint.substring(0, LhsPrint.length() - 5); 
			
		}
		
		return LhsPrint;
	}

	public String printRightHandSide() {
		String RhsPrint = this.RightHandSide.printTerm();
		return RhsPrint;
	}

	public String printRightHandSideInEnglish() {
		String RhsPrint = this.RightHandSide.printTermInEnglish();
		return RhsPrint;
	}

	public String printRule() {

		String rule = this.printLeftHandSide() + "\n" + " ---" + this.RuleType + "---> " + this.printRightHandSide()
				+ "\n";

		return rule;

	}

	public String printRuleInEnglish() {

		// String rule = "There is evidence that "+ this.printLeftHandSideInEnglish();
		String rule = this.printLeftHandSideInEnglish();

		if (this.RuleCategory == RuleCategory.COR)
			if ((this.RuleType == RuleType.HELP /* && this.RuleCategory == RuleCategory.COR */ ))
				rule = rule + " has weak positive impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.HURT /* && this.RuleCategory == RuleCategory.COR */ ))
				rule = rule + "has weak negative impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.MAKE /* && this.RuleCategory == RuleCategory.COR */))
				rule = rule + " has strong positive impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.BREAK /* && this.RuleCategory == RuleCategory.COR */ ))
				rule = rule + " has strong negative impact on\n\n" + this.printRightHandSideInEnglish() + ".\n\n\n";

			else if ((this.RuleType == RuleType.SOMEPLUS /* && this.RuleCategory == RuleCategory.COR */ ))
				rule = rule + " has some positive impact on\n\n" + this.printRightHandSideInEnglish() + ". \n\n\n";

			else if ((this.RuleType == RuleType.SOMEMINUS /* && this.RuleCategory == RuleCategory.COR */))
				rule = rule + " has some negative impact on\n\n" + this.printRightHandSideInEnglish() + ". \n\n\n";

		// The else part to the if should be written.
		return rule;

	}

	public Term matchStringInLHS(String GivenString) {
		Term matchedTerm = null;

		for (Term t : this.LeftHandSide) {

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

		for (Term t : this.getLeftHandSide())

		{
			Term LHSTermCopy = t.deepCopyTerm();

			LHSCopy.add(LHSTermCopy);

		}

		RHSCopy = this.getRightHandSide().deepCopyTerm();
		RuleCopy = new Rule(LHSCopy, RHSCopy, this.getRuleType(), this.getRuleCategory());

		return RuleCopy;

	}

	public void ParseRule(String RuleString) {
		/*
		 * ### Currently only one topic is parsed. For more than one topic we receive
		 * error.
		 * 
		 * 
		 * #### It should be checked to make sure that the rule is an epty rule.
		 * Otherwise no overriding should take place.
		 * 
		 * ### A term parser has been written. This helper function should be used.
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

		boolean LHSExpected = false;

		boolean RuleTypeExpected = false;

		boolean RHSExpected = false;

		boolean TermExpected = false;

		boolean TypeExpected = false;

		boolean TopicExpected = false;

		boolean RuleCategoryExpected = false;

		for (int i = 0; i < RuleString.length(); i++) {
			char c = RuleString.charAt(i);
			// System.out.println(c);

			if (c == '(') {
				LHSExpected = true;

				TypeExpected = true;
			} else if (c == '[') {

				Type = Type.trim();
				TopicExpected = true;
				TypeExpected = false;

			} else if (c == ']') {
				TopicExpected = false;

				Topic = Topic.trim();
				Term t = new Term(Type, Topic);

				if (LHSExpected)
					LHS.add(t);
				else if (RHSExpected)
					RHS = t;

				Type = "";
				Topic = "";
			}

			else if (c == ',') {
				TypeExpected = true;

			} else if (c == ')') {
				LHSExpected = false;
				TypeExpected = false;

			}

			else if (c == '-' && !LHSExpected) {
				RuleTypeExpected = true;

			}

			else if (c == '>') {

				RuleTypeExpected = false;
				RHSExpected = true;
				TypeExpected = true;

				RuleTypeString = RuleTypeString.trim();
				// System.out.print("RuleTypeString:");
				// System.out.println(RuleTypeString.toString());
				RT = RuleType.valueOf(RuleTypeString);

				// System.out.print("RT.toString()");
				// System.out.print(RT.toString());

				RuleTypeString = "";
			}

			else if (c == ':') {
				RuleCategoryExpected = true;
			}

			else if (TypeExpected) {
				Type = Type + c;
				// System.out.println("Type:");
				// System.out.println(Type);
			}

			else if (TopicExpected) {
				Topic = Topic + c;
				// System.out.println("Topic:");
				// System.out.println(Topic);
			}

			else if (RuleTypeExpected && c != '-') {

				RuleTypeString = RuleTypeString + c;
				// System.out.println("RuleTypeString:");
				// System.out.println(RuleTypeString);
			}

			else if (RuleCategoryExpected) {
				RuleCategoryString = RuleCategoryString + c;
				// System.out.println("RuleCategoryString:");
				// System.out.println(RuleCategoryString);
			}

		}
		System.out.println(RHS.printTerm());
		RuleCategoryString = RuleCategoryString.trim();
		RC = RuleCategory.valueOf(RuleCategoryString);

		// r= new Rule (LHS, RHS, RT, RuleCategory.valueOf(RuleCategoryString.trim()));
		this.setLeftHandSide(LHS);
		this.setRightHandSide(RHS);
		this.setRuleType(RT);
		this.setRuleCategory(RC);

	}
	/*
	 * public String printRuleInReverseOrder () { String ruleInReverseOrder =
	 * this.printRightHandSide() + " <---" + this.RuleType + "--- " +
	 * this.printLeftHandSide() + "\n";
	 * 
	 * return ruleInReverseOrder; }
	 */

	public int countLHS() {
		int count = 0;

		for (Term t : this.LeftHandSide)
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
					             this.printLeftHandSideInEnglish() + ".";
			}
			else if (this.countLHS() == 1)
			{
				ruleInReverseOrder = "Please elaborate on the requirement " + this.printRightHandSideInEnglish() + ". \n\n" +
		                 this.printRightHandSideInEnglish() + " can be refined into the following requirement: \n \n"+
			             this.printLeftHandSideInEnglish() + ".";
			}
			
		} else if (this.RuleCategory == RuleCategory.OP_FI_REF) {

			if (this.countLHS() > 1) {

				ruleInReverseOrder = "To design " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nsome possible alternatives are as follows: \n \n" + this.printLeftHandSideInEnglish()
						+ ".\n\n\n" + "I cannot futher refine the above design alterantives."
						+ "For the next step,\n\nI can analyze the above design alternatives or I can recommend a design solution.";
			}

			else if (this.countLHS() == 1) {
				ruleInReverseOrder = "To design " + this.printRightHandSideInEnglish() + ", "
						+ "\n\none possible alternative is as follows: \n \n" + this.printLeftHandSideInEnglish() + ".\n\n\n"
						+ "I cannot futher refine the above design alterantives."
						+ "For the next step,\n\nI can analyze the above design alternatives or I can recommend a design solution.";
			}

		}

		else if (this.RuleCategory == RuleCategory.OP_NF_REF) {
			if (this.countLHS() > 1) {
				ruleInReverseOrder = "To address the requirement " + this.printRightHandSideInEnglish() + ", \n\n"
						+ "the following functionalities need to be designed: \n\n" + this.printLeftHandSideInEnglish()
						+ ".\n\n" + "Please specify which of these functionalities need to be designed.";

			}

			else if (this.countLHS() == 1) {
				ruleInReverseOrder = "To address the requirement " + this.printRightHandSideInEnglish()
						+ ", \n\nthe following functionality needs to be designed: \n \n"
						+ this.printLeftHandSideInEnglish() + "."
						+ " \n\nPlease specify the functionality that needs to be designed.";

			}
		}

		else if (this.RuleCategory == RuleCategory.OP_FF_REF) {

			if (this.countLHS() > 1) {
				ruleInReverseOrder = "To design the functionality " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nthe following functionalities need to be designed: \n\n" + this.printLeftHandSideInEnglish()
						+ ".\n\n" + "Please specify which of these functionalities that need to be designed.";

			}

			if (this.countLHS() == 1) {
				ruleInReverseOrder = "To design the functionality " + this.printRightHandSideInEnglish() + ", "
						+ "\n\nthe following functionality need to be designed: \n\n" + this.printLeftHandSideInEnglish()
						+ ".\n\n" + "Please specify the functionality that needs to be designed.";
			}

		}

		return ruleInReverseOrder;
	}
}
