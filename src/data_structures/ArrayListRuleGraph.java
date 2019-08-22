package data_structures;
import java.util.*;

import reasoner_component.QueriedTerm;
import reasoner_component.Query;

public class ArrayListRuleGraph implements RuleGraph {

	private List<Rule> RuleGraph;
	List<Rule> SourceRules = new ArrayList<Rule> ();
	
	
	

	public ArrayListRuleGraph() {
		RuleGraph = new ArrayList<Rule>();
	}

	/* (non-Javadoc)
	 * @see RuleGraph1#addRule(Rule)
	 */
	@Override
	public void addRule(Rule r) {
		RuleGraph.add(r);
	}

	/* (non-Javadoc)
	 * @see RuleGraph1#deleteRule(Rule)
	 */
	@Override
	public void deleteRule(Rule r) {
		RuleGraph.remove(r);
	}

	/* (non-Javadoc)
	 * @see RuleGraph1#findRule(Rule)
	 */
	@Override
	public int findRule(Rule r) {
		/*
		 * The find rule currently only finds the rule object. Maybe other kinds of find
		 * method are required.
		 */

		return RuleGraph.indexOf(r);
	}

	/* (non-Javadoc)
	 * @see RuleGraph1#printRuleGraph()
	 */
	@Override
	public String toStringRuleGraph() {
		
		//ArrayList RuleGraphInString = new ArrayList ();
		String  RuleGraphInString ="";
		for (Rule r : RuleGraph)
			//System.out.println(r.printRule());
			//RuleGraphInString.add(r.printRule());
			RuleGraphInString = RuleGraphInString + "\n"+ r.printRule();
		
		return RuleGraphInString;
	}
	
	public String PrintRuleGraphInEnglish() {

		// ArrayList RuleGraphInString = new ArrayList ();
		String RuleGraphInString = "";
		for (Rule r : RuleGraph)
			// System.out.println(r.printRule());
			// RuleGraphInString.add(r.printRule());
			RuleGraphInString = RuleGraphInString + r.printRuleInEnglish();

		return RuleGraphInString;
	}

	public boolean containsRule (Rule GivenRule)
	{
		//return RuleGraph1.contains(r);
		
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
	/*
	public String getExpansionHistoryinString ()
	{   
		
		String ExpansionHistory = "";
		
		for (Rule r: RuleGraph)

		{
			//System.out.println ("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
			//System.out.println("\n" + RuleGraph.indexOf(r) + ": " + r.printRightHandSide());
			//System.out.println(RuleGraph.indexOf(r)  + ": " + r.printRule());
			String info1 = "\n" + RuleGraph.indexOf(r) + ": " + r.printRightHandSide()+"\n \n";
			String info2 = RuleGraph.indexOf(r)  + ": " + r.printRuleInReverseOrder()+"\n \n";
			ExpansionHistory = ExpansionHistory + info1+info2;
			
			//System.out.println("WWWWWWWWWWWWWWWWWWWW"+ ExpansionHistory);
			

		}
      
		
		return ExpansionHistory;
	}
	*/
	
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
	
	
	public ArrayListRuleGraph deepCopyRuleGraph() {

		ArrayListRuleGraph RuleGraphCopy = new ArrayListRuleGraph();

		for (Rule r : this.RuleGraph) {
			Rule RuleCopy = r.deepCopyRule();
			RuleGraphCopy.addRule(RuleCopy);
		}
		/*
		 * for (int i =0; i< ArrayListRuleGraph.size(); i++)
		 * print(ArrayListRuleGraph.get(i).equals(RuleGraphCopy.get(i)));
		 */
		return RuleGraphCopy;
	}
	
	public Rule findInRulesLHS(String TermString)
	{
		Rule MatchedRule = null;
		
		for (Rule r : this.RuleGraph)

			if (r.matchStringInLHS(TermString) != null)

				MatchedRule = r;
		
		if (MatchedRule == null) {
			System.out.print("No matched rule found in the expanded graph.");
		}
		
		
		return MatchedRule;
	}
	
	public Query matchQuery(Query q) {
		
		
		
		/*
		 * The queried items should be found in RHS.
		 * If they only appear in LHS they are not returned backed.
		 */
		Query MatchedQuery = new Query();

		for (Rule r : this.RuleGraph)
		{ 
			for (QueriedTerm queryItem : q.getQuerySet()) 
			{ 													
				//System.out.println("DDDDDDDDDD:"+ queryItem.printQueriedTerm());
				//System.out.println("EEEEEEEEEEEEEE:"+ r.getRightHandSide().printTerm());
				if (r.getRightHandSide().hasEqualTermString(queryItem.getAttribute().printTerm())
						&& r.getRuleCategory() != RuleCategory.OP_NF_REF) 
				{
					MatchedQuery.addQueriedTerm(r.getRightHandSide(), queryItem.getAttributePriority(),
							r.getRightHandSide().getFinalSatValue());
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

		// ########################matchLeftHandSide does not function
		// correctly.##################
		for (Rule r : this.RuleGraph)
			if (r.containsLeftHandSide(LHSTerms))

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
		/*
		System.out.println("Source Rulessssssssssssssssssssss");
		for (Rule r : SourceRules)
			System.out.println(r.printRule());
			*/
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
		 * Dangling Terms are those terms in the left hand side of an AND or OR rule in a rule garph 
		 * which they do not appear in the right hand side of any rules in the rule graph.
		 * ; i.e. They are not reachable from source rules.
		 * ***** i.e. they have the in degree of zero in the rule graph.
		 * 
		 * 
		 * #### A source rule is dangling. However, it should not be considered dangling.  
		 */
		if (!this.SourceRules.contains(r))
			for (Term t : r.getLeftHandSide())
			{   
				boolean isDanglingTerm = true;
				//System.out.println(t.printTerm());
				for (Rule r1: this.RuleGraph)
				
					
					
					  if (r1.hasEqualRightHandSide(t))
					
						 isDanglingTerm = false;
					  
				//System.out.println(isDanglingTerm);
					
				if (isDanglingTerm)
				{
					if (r.getRuleType() == RuleType.AND)
						t.setFinalSatValue(TermSatisfactionValues.SAT_BY_NO_REL);
						//t.setReceivedSatValue(TermSatisfactionValues.SAT_BY_NO_REL);
					
					else if  (r.getRuleType() == RuleType.OR)
						t.setFinalSatValue(TermSatisfactionValues.DEN_BY_NO_REl);
						//t.setReceivedSatValue(TermSatisfactionValues.DEN_BY_NO_REl);
				}
			}
			
	}
	
	public void pruneDanglingRules ()
	{
		/*
		 * Dangling rules are rules in a rule graph which contains dangling terms.
		 * Dangling terms are terms in the LHS of an AND or OR rule which do not appear
		 * in the RHS of other rules; i.e. They are not reachable from source rules;
		 * ***** i.e. they have the in degree of zero in the rule graph.
		 */
		ArrayList<Rule> SuspeciousRules = new ArrayList<Rule> ();
		
		SuspeciousRules.addAll(getRules (RuleType.AND));
		SuspeciousRules.addAll(getRules (RuleType.OR));
		
		
		
		
		for (Rule r: SuspeciousRules )
			{ 
			  // System.out.println("VVVVVVVVVV Sucpicous rule:"+ r.printRule());
			   pruneDanglingTerms (r);
			 
			}
		
		
	}

}
