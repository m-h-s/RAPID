package rule_explorer_component;


import java.util.*;

import data_structures.ListRuleSet;
import data_structures.Rule;
import data_structures.RuleCategory;
import data_structures.Term;
import data_structures.TermSatisfactionValues;
import reasoner_component.Evaluator;
import rule_base_component.RuleBaseManager;
import user_interaction_component.UserInteractionManager;


/**
 * 
 * 
 * 
 * @author: Mahsa Sadi
 * 
 * @since: 2018 - 11 - 01
 * 
 * License: Creative Commons
 * 
 * Copyright by Mahsa Sadi
 * 
 * 
 * 
 * 
 */

public class RuleExplorer {


	/*
	 * Rule Explorer is in charge of the following responsibilities:
	 * 1- Refining a Given Term
	 * 2- Adding Correlation Rules Related to a Given Term
	 * 3- Adding Rule Reachable from Correlation Rules
	 * 
	 */



	private ListRuleSet workingRuleSet;
	private RuleBaseManager ruleSetManager;
	private List<ListRuleSet> copiesOfWorkingRuleSet;


	private UserInteractionManager Cont;


	String inputTerm = "";


	public RuleExplorer (UserInteractionManager Cont)
	{
		this.Cont = Cont;
		workingRuleSet = new ListRuleSet();
		copiesOfWorkingRuleSet = new ArrayList<ListRuleSet>();
		ruleSetManager = new RuleBaseManager(this.Cont);

	}

	public void resetRuleGraph ()
	{
		workingRuleSet = new ListRuleSet();
	}

	public List<ListRuleSet> getExploredRuleGraphs() {
		return this.copiesOfWorkingRuleSet;
	}

	public ListRuleSet getExpandedRuleGraph() {
		return this.workingRuleSet;
	}


	public void getUserInputFromController (String UserInput)
	{
		this.inputTerm = UserInput;
		refineTerm();
	}


	public void sendExpansionHistorytoController (ArrayList <String> ExpansionHistory)
	{
		Cont.receiveAnswerFromExplorer("RuleGraphExpansionHistory", ExpansionHistory);
	}


	public void sendEmptyResponseToController ()
	{
		Cont.receiveOutputFromModel("EmptyAnswer", "No response can be provided!");
	}

	public void sendResponseToController (ArrayList<String> Answer)
	{
		Cont.receiveAnswerFromExplorer("Answer", Answer);
	}

	public void sendResponseToController (String Answer)
	{
		Cont.receiveOutputFromModel("Answer", Answer);
	}


	/* Refine Term  */
	public void refineTerm() {

		
		/*
		 * Expand the rule graph starting by a Initiating Term. 
		 * Match right hand sides of the rules in the rule set to find matched rules. 
		 * Ask user for next expansion if there is more than one way to expand.
		 * 
		 */

		Term InitiatingTerm = new Term();

		InitiatingTerm.parseTerm(inputTerm);

		ArrayList<Rule> MatchedRules = ruleSetManager.matchConsequent(InitiatingTerm);

		if (!MatchedRules.isEmpty()) 
		{

			ArrayList<String> RecentAnswer = new ArrayList<String>();

			for (Rule r : MatchedRules)

			{   
				
				/*
				 * Filter Found Rules and add them to the expanded graph: 
				 * Filtering Rules: 
				 * 1- Filter 1: In Top-Down Expansion of rule graph (i.e. refinement),
				 * Correlation rules should be eliminated; 
				 * They should be added once the top-down refinement and expansion is done. 
				 * 
				 * Filter 1: Rule Category != COR  -
				 * Filter 2: Duplicated rules should be eliminated since one instance is enough.
				 * 
				 */
				

					if (r.getRuleCategory() != RuleCategory.COR && !workingRuleSet.containsRule(r)) 
					{
		
						workingRuleSet.addRule(r);
						RecentAnswer.add(r.getConsequent().printTerm());
						RecentAnswer.add(r.printRuleInReverseOrderAndInEnglish());
		
					}
					
			}
			
			
			sendResponseToController(RecentAnswer);
		}

		else 
		{
			sendEmptyResponseToController();
		}



	}



	public void addOnlyRelatedCorrelations(ListRuleSet RuleGraph, Rule SourceRule, int IndexOfSourceTerm) 

	{
		Term SourceTerm = SourceRule.getPrecedent().get(IndexOfSourceTerm);
		addCorrelationRules(RuleGraph, SourceRule, SourceTerm);
	}




	/* add Correlation Rules */
	public void addCorrelationRules(ListRuleSet workingRuleSet, Rule SourceRule, Term InitiatingLHSTerm) 

	{

		ArrayList<Term> InitiatingLHS = new ArrayList<Term>();
		InitiatingLHS.add(InitiatingLHSTerm);

		ArrayList<Rule> MatchedRules = ruleSetManager.matchPrecedent(InitiatingLHS);


		Iterator<Rule> it = MatchedRules.iterator();

		while (it.hasNext()) {
			Rule nextRule = it.next();
			if (workingRuleSet.containsRule(nextRule) || nextRule.getRuleCategory() != RuleCategory.COR)
				it.remove();
		}


		for (Rule r : MatchedRules)
			workingRuleSet.addRule(r.deepCopyRule());
	}


	/* add Correlation Rules and Rules Reachable from Correlation Rules */
	public void addCorrelationsAndReachableRules(ListRuleSet workingRuleSet, Rule SourceRule, int IndexOfSourceTerm) 

	{

		Term SourceTerm = SourceRule.getPrecedent().get(IndexOfSourceTerm);
		expandWorkingRuleSetBottomUp(workingRuleSet, SourceRule, SourceTerm);



		Cont.receiveOutputFromModel(
				"Other", 
				"\n--------------------------correlationsadded-----------------------------\n" +
						workingRuleSet.toStringRuleGraph() +
						"--------------------------correlationsend-----------------------------"
				);

	}


	public void expandWorkingRuleSetBottomUp(ListRuleSet workingRuleSet, Rule SourceRule, Term InitiatingLHSTerm) 


	{

		ArrayList<Term> InitiatingLHS = new ArrayList<Term>();
		InitiatingLHS.add(InitiatingLHSTerm);

		ArrayList<Rule> MatchedRules = ruleSetManager.matchPrecedent(InitiatingLHS);


		Iterator<Rule> it = MatchedRules.iterator();

		while (it.hasNext()) 
		{
			Rule nextRule = it.next();
			if (workingRuleSet.containsRule(nextRule))
				it.remove();
		}


		for (Rule r : MatchedRules)

			workingRuleSet.addRule(r.deepCopyRule());


		for (Rule r : MatchedRules)

			expandWorkingRuleSetBottomUp(workingRuleSet, r, r.getConsequent());

	}



	public void analyzeSourceTermOnebyOne(ListRuleSet UserExpandedRuleGraph, Term SourceTerm) {
		/*
		 * Usage Scenario: 
		 * 
		 * 1- After finishing expanding the rule graph, user selects a design option (source term).
		 * 
		 * 2-The assistant builds the complete rule graph (knowledge graph) related to that design option. 
		 * 
		 * 3-The complete rule graph (knowledge graph) is shown to the user.
		 * 
		 * 4-User can explore design options one by one and check their related rule graphs.
		 * 
		 * Performed Tasks:
		 * 
		 * 1-get the source term as input. 
		 * 
		 * 2-build the complete graph related to that source term. 
		 * 
		 * 3- show the built graph.
		 */

		ListRuleSet AnalysisRuleGraph = new ListRuleSet();

		Rule SourceRule = UserExpandedRuleGraph.getRule(UserExpandedRuleGraph.getSize() - 1);

		int IndexOfSourceTerm = SourceRule.findeTermInPrecedent(SourceTerm);

		addOnlyRelatedCorrelations(AnalysisRuleGraph, SourceRule, IndexOfSourceTerm);

		this.sendResponseToController(AnalysisRuleGraph.PrintRuleGraphInEnglish());

	}

	public void analyzeSourceTermsByUser(String UserInput) 
	{

		if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			Rule SourceRule = workingRuleSet.getRule(workingRuleSet.getSize() - 1);
			Term TermTobeExplored = SourceRule.matchStringInLHS(UserInput);
			analyzeSourceTermOnebyOne(workingRuleSet, TermTobeExplored);


		}
		else 
		{
			sendResponseToController ("\n --------------------Design ends-----------------------\n");
		}
	}


	public void exploreExpandedRuleGraphOneByOne() {

		/*
		 * WARNING: This code does not work for more than one source rules in an expanded graph.
		 */
		Evaluator RuleEvaluator = new Evaluator();
		int NumberOfSourceTerms;


		NumberOfSourceTerms = workingRuleSet.getSourceRules().get(0).getPrecedent().size();

		for (int IndexOfSourceTerm = 0; IndexOfSourceTerm < NumberOfSourceTerms; IndexOfSourceTerm++)

		{
			ListRuleSet RuleGraphCopy = workingRuleSet.deepCopyRuleGraph();

			/*
			 * WARNING: Currently it is assumed that source rule is the last rule which is not always correct.
			 */
			Rule SourceRule = RuleGraphCopy.getSourceRules().get(0);

			addCorrelationsAndReachableRules(RuleGraphCopy, SourceRule, IndexOfSourceTerm);

			RuleGraphCopy.pruneDanglingRules();

			String EvaluationSteps = RuleEvaluator.evaluateRuleGraph
					(RuleGraphCopy, SourceRule.getPrecedent().get(IndexOfSourceTerm),TermSatisfactionValues.SAT);

			copiesOfWorkingRuleSet.add(RuleGraphCopy);
			
			Cont.receiveOutputFromModel("other", EvaluationSteps);

		}

	}

	public String ExpandedRuleGraphtoString ()
	{
		return this.workingRuleSet.toStringRuleGraph();
	}




	private static <T> void print(T x) 
	{
		System.out.println(x.toString());
	}
}
