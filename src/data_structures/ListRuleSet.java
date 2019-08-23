package data_structures;
import java.util.*;

import reasoner_component.Constraint;
import reasoner_component.ConstraintSet;

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

public class ListRuleSet implements RuleSet {

	private List<Rule> RuleGraph;
	List<Rule> SourceRules = new ArrayList<Rule> ();




	public ListRuleSet() {
		RuleGraph = new ArrayList<Rule>();
	}


	@Override
	public void addRule(Rule r) {
		RuleGraph.add(r);
	}


	@Override
	public void deleteRule(Rule r) {
		RuleGraph.remove(r);
	}


	@Override
	public int findRule(Rule r) {
		/*
		 * The find rule currently only finds the rule object. Maybe other kinds of find
		 * method are required.
		 */

		return RuleGraph.indexOf(r);
	}


	@Override
	public String toStringRuleGraph() {

		String  RuleGraphInString ="";
		for (Rule r : RuleGraph)
			RuleGraphInString = RuleGraphInString + "\n"+ r.printRule();

		return RuleGraphInString;
	}

	public String PrintRuleGraphInEnglish() {

		String RuleGraphInString = "";
		for (Rule r : RuleGraph)
			RuleGraphInString = RuleGraphInString + r.printRuleInEnglish();

		return RuleGraphInString;
	}

	public boolean containsRule (Rule GivenRule)
	{


		boolean contains = false;


		for (Rule r : this.RuleGraph)
			if (r.isEqual(GivenRule))
				contains = true;

		return contains;
	}

	public int getSize ()
	{
		return RuleGraph.size();
	}

	public ArrayList <String> getExpansionHistoryinString ()
	{   

		ArrayList <String> ExpansionHistory = new ArrayList<String> ();

		for (Rule r: RuleGraph)

		{
			String info1 = RuleGraph.indexOf(r) + ": " + r.printRightHandSide();
			ExpansionHistory.add(info1);
			String info2 = RuleGraph.indexOf(r)  + ": " + r.printRuleInReverseOrderAndInEnglish();
			ExpansionHistory.add(info2);


		}


		return ExpansionHistory;
	}
	public Rule getRule (int RulePosition)
	{
		return RuleGraph.get(RulePosition);
	}


	public ListRuleSet deepCopyRuleGraph() {

		ListRuleSet RuleGraphCopy = new ListRuleSet();

		for (Rule r : this.RuleGraph) {
			Rule RuleCopy = r.deepCopyRule();
			RuleGraphCopy.addRule(RuleCopy);
		}

		return RuleGraphCopy;
	}

	public Rule findInRulesLHS(String TermString)
	{
		Rule MatchedRule = null;

		for (Rule r : this.RuleGraph)

			if (r.matchStringInLHS(TermString) != null)

				MatchedRule = r;

		if (MatchedRule == null) {
			// Print a message in the conversation history.
		}


		return MatchedRule;
	}

	public ConstraintSet matchConstraints(ConstraintSet q) {



		/*
		 * The queried items should be found in RHS.
		 * If they only appear in LHS they are not returned backed.
		 */
		ConstraintSet MatchedQuery = new ConstraintSet();

		for (Rule r : this.RuleGraph)
		{ 
			for (Constraint queryItem : q.getConstraints()) 
			{ 													
				if (r.getConsequent().hasEqualTermString(queryItem.getAttribute().printTerm())
						&& r.getRuleCategory() != RuleCategory.OP_NF_REF) 
				{
					MatchedQuery.addConstraint(r.getConsequent(), queryItem.getAttributePriority(),
							r.getConsequent().getFinalSatValue());
				}


			}
		}

		return MatchedQuery;
	}

	public ArrayList <Rule> findRulesStartingWithSourceTerm (Term SourceTerm)
	{
		ArrayList<Term> LHSTerms = new ArrayList<Term>();
		LHSTerms.add(SourceTerm);

		ArrayList<Rule> Rules = new ArrayList<Rule>();

		for (Rule r : this.RuleGraph)
			if (r.containsPrecedent(LHSTerms))

				Rules.add(r);

		return Rules;
	}

	public List<Rule>  getSourceRules ()
	{

		/*
		 * Source rule is a rule which contains teh refinement of a design objective to design alternatives.
		 * Assumption 1: We can have more than one source rule in an expanded graph.
		 * Assumption 2: There is only one level of functional req refinement and that belong to
		 * the refinement of a design objective to design options.
		 * Assumption3: There is at least one source rule in an expanded graph otherwise the graph is not valid.
		 */

		/*
		 * ###### In general form this should be replaced by rules who have source terms.
		 * 
		 * 
		 * ##### One assumption was that there is only one level of functional refinement.
		 * We have introduced OP_FI_REQ to omit this assumption currently.
		 */


		for (Rule r: this.RuleGraph)
			if (r.getRuleCategory() == RuleCategory.OP_FI_REF)
				SourceRules.add(r);

		return SourceRules;

	}

	public ArrayList<Rule> getRules (RuleType RT)
	{
		ArrayList<Rule>  WantedRules = new ArrayList<Rule> ();

		for (Rule r: this.RuleGraph)
			if (r.getRuleType() == RT)
				WantedRules.add(r);

		return WantedRules;
	}


	public void pruneDanglingTerms (Rule r)
	{
		/*
		 * Dangling Terms are those terms in the left hand side of an AND or OR rule in a rule graph, 
		 * which do not appear in the right hand side of any rules in the rule graph.
		 * i.e. They are not reachable from source rules.
		 * i.e. they have the in degree of zero in the rule graph.
		 * #### A source rule is dangling. However, it should not be considered dangling.  
		 */
		
		
		
		if (!this.SourceRules.contains(r))
			for (Term t : r.getPrecedent())
			{   
				boolean isDanglingTerm = true;

				for (Rule r1: this.RuleGraph)

					if (r1.hasEqualRightHandSide(t))
						isDanglingTerm = false;


				if (isDanglingTerm)
				{
					if (r.getRuleType() == RuleType.AND)
						t.setFinalSatValue(TermSatisfactionValues.SAT_BY_NO_REL);

					else if  (r.getRuleType() == RuleType.OR)
						t.setFinalSatValue(TermSatisfactionValues.DEN_BY_NO_REl);

				}
			}

	}




	public void pruneDanglingRules ()
	{
		/*
		 * Dangling rules are rules in a rule graph which contains dangling terms.
		 * Dangling terms are terms in the LHS of an AND or OR rule which do not appear in the RHS of other rules; 
		 * i.e. They are not reachable from source rules;
		 * i.e. they have the in degree of zero in the rule graph.
		 */
		ArrayList<Rule> SuspeciousRules = new ArrayList<Rule> ();

		SuspeciousRules.addAll(getRules (RuleType.AND));
		SuspeciousRules.addAll(getRules (RuleType.OR));

		for (Rule r: SuspeciousRules)
			pruneDanglingTerms (r);

	}



	public String RuleBaseToString() {
		String RuleBaseInStringForm = "\n##########################Rule Set Begins #########################\n \n";
		for (Rule r : this.RuleGraph)
			RuleBaseInStringForm = RuleBaseInStringForm + r.getRuleNumber() + ": " + r.printRule() + "\n \n";

		RuleBaseInStringForm = RuleBaseInStringForm
				+ "##########################Rule Set Ends ######################### \n ";

		return RuleBaseInStringForm;

	}

	public ArrayList<Rule> findConsequent(Term rhs) {

		// Find and Return the rules in the rule set whose right-hand-side equals to the given term.
		

		ArrayList<Rule> matchedRules = new ArrayList<Rule>();
		for (Rule r : RuleGraph) 
			if (r.hasEqualRightHandSide(rhs)) 
				matchedRules.add(r);


		return matchedRules;
	}

	public ArrayList<Rule> findPrecedent(ArrayList LHS) {

		/*
		 * Find and Return the rules in the rule set whose Left-Hand-Side contains term LHS. 
		 * Example: a---help ---> b contains a as lhs.
		 * Example: (a,b) ----or---> c contains a as lhs. 
		 * Example: (a,b) ---and ---> d contains (a,b) in lhs
		 */

		ArrayList<Rule> matchedRules = new ArrayList<Rule>();
		for (Rule r : RuleGraph) 
			if (r.containsPrecedent((LHS))) 
				matchedRules.add(r);	
		return matchedRules;
	}

}
