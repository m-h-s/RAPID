
import java.util.*;

public class RuleExplorer {

	// ### User Expanded rule graph should be separated from the graph of applicable
	// rules.
	/*
	 * Rule set and rule base are graphs: terms (LHS or RHS) are nodes, and rules
	 * are edges. Currently, there is no graph data structure. There is an ArrayList
	 * of rules. Therefore, we do not know the source term. The following line
	 * should be replaced by rule-graph data structure.
	 */

	private ArrayListRuleGraph ExpandedRuleGraph;
	private RuleSetManager RuleSetManager;
	private List<ArrayListRuleGraph> ExploredRuleGraphs;
    
	
	private UserInteractionManager Cont;
	
	
	String UserInput = "";
	
	
	public RuleExplorer (UserInteractionManager Cont)
	{
		this.Cont = Cont;
		ExpandedRuleGraph = new ArrayListRuleGraph();
		ExploredRuleGraphs = new ArrayList<ArrayListRuleGraph>();
		RuleSetManager = new RuleSetManager(this.Cont);
		
	}

	public List<ArrayListRuleGraph> getExploredRuleGraphs() {
		return this.ExploredRuleGraphs;
	}

	public ArrayListRuleGraph getExpandedRuleGraph() {
		return this.ExpandedRuleGraph;
	}

	private static <T> void print(T x) {
		System.out.println(x.toString());
	}
	
	
	
	public void getUserInputFromController (String UserInput)
	{
		this.UserInput = UserInput;
		expandRuleGraphTopDownByUser();
	}
	
	
	public void sendExpansionHistorytoController (ArrayList <String> ExpansionHistory)
	{
		// System.out.println("KKKKKKKKKKKKKKKKKK"+ ExpansionHistory );
		Cont.receiveOutputFromModel("RuleGraphExpansionHistory", ExpansionHistory);
	}
   
	
	public void sendEmptyResponseToController ()
	{
		Cont.receiveOutputFromModel("EmptyAnswer", "No response can be provided!");
	}
	
	public void sendResponseToController (ArrayList<String> Answer)
	{
		// System.out.println("heeeeerreeee");
		Cont.receiveOutputFromModel("Answer", Answer);
	}
	
	public void sendResponseToController (String Answer)
	{
		Cont.receiveOutputFromModel("Answer", Answer);
	}
	
	public void expandRuleGraphTopDownByUser() {

		/*
		 * Expand the rule graph starting by a Initiating Term. Match right hand sides
		 * of the rules in the rule set to find matched rules. Ask user for next
		 * expansion if there is more than one way to expand.
		 * 
		 * The initiating term is currently Security [API] and there is only one
		 * initiating point. #######This initiating should be asked from the user. The
		 * user should know the possible initiating points. #######There should be
		 * several initiating points not one. ####If there is only one term then expand
		 * automatically. #### Currently, I only assume that one rule is returned.
		 * 
		 *
		 * 
		 * print("####################Expansion begins######################\n");
		 * 
		 * // 1- Currently, start from security as initiating term. //Term
		 * InitiatingTerm = new Term("Security", "API"); /* Term InitiatingTerm = new
		 * Term();
		 * 
		 * 
		 * Scanner ConsoleScan = new Scanner(System.in); String UserInput = "";
		 * 
		 * System.out.println("Please enter the initiating term:");
		 * 
		 * UserInput = ConsoleScan.nextLine();
		 * 
		 */

		// Scanner ConsoleScan = new Scanner(System.in);

		Term InitiatingTerm = new Term();

		InitiatingTerm.parseTerm(UserInput);
		System.out.println(InitiatingTerm.printTerm());

		// 2-Continue until user enters "exit". USer input is a string.
		// if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

		// 3-Find rules in the rule set that have the initiating term in their
		// right-hand side.
		ArrayList<Rule> MatchedRules = RuleSetManager.matchRightHand(InitiatingTerm);

		if (!MatchedRules.isEmpty()) {
			/*
			 * System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"); for (Rule
			 * r : MatchedRules) System.out.println(r.printRule());
			 */

			/*
			 * 4- Filter Found Rules and add them to the expanded graph: Filtering Rules: 1
			 * - Filter 1: In Top-Down Expansion of rule graph (i.e. refinement),
			 * Correlation rules should be eliminated; They should be added once the
			 * top-down refinement and expansion is done. Filter 1: Rule Category != COR 2 -
			 * Filter 2: Duplicated rules should be eliminated since one instance is enough.
			 * 
			 */

			System.out.println("\nOOOOOOO I am hereOOOOOOOOOOO\n");
			
			ArrayList<String> RecentAnswer = new ArrayList<String>();

			for (Rule r : MatchedRules)
				
			{   System.out.println(r.printRuleInEnglish());
			
				if (r.getRuleCategory() != RuleCategory.COR && !ExpandedRuleGraph.containsRule(r)) {
					
					System.out.println("LLLLLLLLLLLLOLLLLLLLLLLLLLL");
					ExpandedRuleGraph.addRule(r);
					RecentAnswer.add(r.getRightHandSide().printTerm());
					RecentAnswer.add(r.printRuleInReverseOrderAndInEnglish());
			   for (String s: RecentAnswer)
				   System.out.println(s);

				}
			}

			// 5- Print the history of input and outputs of rule matching.
			// ExpandedRuleGraph.getExpansionHistory();
			// sendExpansionHistorytoController
			// (ExpandedRuleGraph.getExpansionHistoryinString());

			System.out.println("XXXXXXXXXXXXXXThis is the matched answer:\n" + RecentAnswer+ "XXXXXXX\n");
			sendResponseToController(RecentAnswer);
		}

		else {
			sendEmptyResponseToController();
		}
			
			

	}
	
	
	public void expandRuleGraphTopDownByUserOldVersion() {

		/*
		 * Expand the rule graph starting by a Initiating Term. Match right hand sides
		 * of the rules in the rule set to find matched rules. Ask user for next
		 * expansion if there is more than one way to expand.
		 * 
		 * The initiating term is currently Security [API] and there is only one
		 * initiating point. #######This initiating should be asked from the user. The
		 * user should know the possible initiating points. #######There should be
		 * several initiating points not one. ####If there is only one term then expand
		 * automatically. #### Currently, I only assume that one rule is returned.
		 * 
		 */

		print("####################Expansion begins######################\n");

		// 1- Currently, start from security as initiating term.
		//Term InitiatingTerm = new Term("Security", "API");
		
		Term InitiatingTerm = new Term(); 

		
		Scanner ConsoleScan = new Scanner(System.in);
		String UserInput = "";
		
		System.out.println("Please enter the initiating term:");
		
		UserInput = ConsoleScan.nextLine();
		
		
		
		//Scanner ConsoleScan = new Scanner(System.in);
				
		
		//Term InitiatingTerm = new Term(); 
		
		InitiatingTerm.parseTerm(UserInput);
		System.out.println(InitiatingTerm.printTerm());

		// 2-Continue until user enters "exit". USer input is a string.
		while (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			// 3-Find rules in the rule set that have the initiating term in their
			// right-hand side.
			ArrayList<Rule> MatchedRules = RuleSetManager.matchRightHand(InitiatingTerm);
			//System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			//for (Rule r : MatchedRules)
				//System.out.println(r.printRule());

			/*
			 * 4- Filter Found Rules and add them to the expanded graph: Filtering Rules: 1
			 * - Filter 1: In Top-Down Expansion of rule graph (i.e. refinement),
			 * Correlation rules should be eliminated; They should be added once the
			 * top-down refinement and expansion is done. Filter 1: Rule Category != COR 2 -
			 * Filter 2: Duplicated rules should be eliminated since one instance is enough.
			 * 
			 */

			for (Rule r : MatchedRules)
				if (r.getRuleCategory() != RuleCategory.COR && !ExpandedRuleGraph.containsRule(r))
					{ //System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
					  ExpandedRuleGraph.addRule(r);
					
					}

			// 5- Print the history of input and outputs of rule matching.
			//ExpandedRuleGraph.getExpansionHistory();
			ExpandedRuleGraph.getExpansionHistoryinString();

			// 6- Ask next term from user.
			System.out.print("\n" + "????????????  Which Term to expand?");
			UserInput = ConsoleScan.next();
			System.out.println("*************************************");

			// 7- Look for the User input in the LHS of the expanded Rule graph.
			// ############### this part of code is problematic and does not work correctly.
			/*
			 * Instead of matchedRules ExpandedRuleGarph can be checked to find a rule
			 * having user input in its right hand side. This gives more flexibility in rule
			 * expansion.
			 */

			if (ExpandedRuleGraph.findInRulesLHS(UserInput) != null)
				InitiatingTerm = ExpandedRuleGraph.findInRulesLHS(UserInput).matchStringInLHS(UserInput);

			else {
				print("No match found in the expanded graph.");
			}

		}

		print("\n ***********************Expanded Graph************************\n");
		// The following line has changed from the first version.
		String PrintOFRuleGraph = ExpandedRuleGraph.toStringRuleGraph();
		print("\n ####################Expansion Done###################### \n");
	}
	


	public void expandRuleGraphBottomUp(ArrayListRuleGraph RuleGraph, Rule SourceRule,
			Term InitiatingLHSTerm) {

		/*
		 * Left-Hand-Side Matching is not yet complete and well defined and needs
		 * iteration. ##### probably here. correlation rules should be added.
		 */

		ArrayList<Term> InitiatingLHS = new ArrayList<Term>();
		InitiatingLHS.add(InitiatingLHSTerm);

		ArrayList<Rule> MatchedRules = RuleSetManager.matchLeftHandSide(InitiatingLHS);

		/*
		 * The following can be implemented as a queue
		 * 
		 */

		/*
		 * ############ A problematic code.#################### for (Rule r :
		 * MatchedRules) if (!ExpandedRuleGraph.contains(r)) ExpandedRuleGraph.add(r);
		 * else { //I am not sure whether java works on dynamic arrays correctly.
		 * MatchedRules.remove(r); }
		 * 
		 */
		// Dynamically remove rules which already exist in expanded tree.
		Iterator<Rule> it = MatchedRules.iterator();

		while (it.hasNext()) {
			Rule nextRule = it.next();
			if (RuleGraph.containsRule(nextRule) || nextRule.getRuleCategory() != RuleCategory.COR)
				it.remove();
		}

		/*
		 * while (it.hasNext()) { Rule nextRule = it.next(); for (Rule r : RuleGraph1)
		 * if (r.isEqual(nextRule)) it.remove(); }
		 */

		/// ###### ArrayListRuleGraph.addAll(MatchedRules);

		for (Rule r : MatchedRules)
			RuleGraph.addRule(r.deepCopyRule());

	/*
		// recursive expansion of rule graph depth first
		for (Rule r : MatchedRules)

		// if (!ExpandedRuleGraph.contains(r))
		{
			expandRuleGraphBottomUpAutomatically(RuleGraph, r, r.getRightHandSide());

		}
		*/ 

	}
	
	
	
	public void expandRuleGraphBottomUpAutomatically(ArrayListRuleGraph RuleGraph, Rule SourceRule,
			Term InitiatingLHSTerm) {

		/*
		 * Left-Hand-Side Matching is not yet complete and well defined and needs
		 * iteration. ##### probably here. correlation rules should be added.
		 */

		ArrayList<Term> InitiatingLHS = new ArrayList<Term>();
		InitiatingLHS.add(InitiatingLHSTerm);

		ArrayList<Rule> MatchedRules = RuleSetManager.matchLeftHandSide(InitiatingLHS);

		/*
		 * The following can be implemented as a queue
		 * 
		 */

		/*
		 * ############ A problematic code.#################### for (Rule r :
		 * MatchedRules) if (!ExpandedRuleGraph.contains(r)) ExpandedRuleGraph.add(r);
		 * else { //I am not sure whether java works on dynamic arrays correctly.
		 * MatchedRules.remove(r); }
		 * 
		 */
		// Dynamically remove rules which already exist in expanded tree.
		Iterator<Rule> it = MatchedRules.iterator();

		while (it.hasNext()) {
			Rule nextRule = it.next();
			if (RuleGraph.containsRule(nextRule))
				it.remove();
		}

		/*
		 * while (it.hasNext()) { Rule nextRule = it.next(); for (Rule r : RuleGraph1)
		 * if (r.isEqual(nextRule)) it.remove(); }
		 */

		/// ###### ArrayListRuleGraph.addAll(MatchedRules);

		for (Rule r : MatchedRules)
			RuleGraph.addRule(r.deepCopyRule());

		// recursive expansion of rule graph depth first
		for (Rule r : MatchedRules)

		// if (!ExpandedRuleGraph.contains(r))
		{
			expandRuleGraphBottomUpAutomatically(RuleGraph, r, r.getRightHandSide());

		}

	}

	
	
	
	public void addOnlyRelatedCorrelations(ArrayListRuleGraph RuleGraph, Rule SourceRule, int IndexOfSourceTerm) {

		Term SourceTerm = SourceRule.getLeftHandSide().get(IndexOfSourceTerm);
		expandRuleGraphBottomUp(RuleGraph, SourceRule, SourceTerm);
		
		
		/*
		Cont.receiveOutputFromModel(
				"Other", 
				"\n--------------------------correlationsadded-----------------------------\n" +
						RuleGraph.PrintRuleGraphInEnglish() +
						"--------------------------correlationsend-----------------------------"
		       
										);
		
		*/

	}
	
	public void addRelatedCorrelations(ArrayListRuleGraph RuleGraph, Rule SourceRule, int IndexOfSourceTerm) {

		Term SourceTerm = SourceRule.getLeftHandSide().get(IndexOfSourceTerm);
		expandRuleGraphBottomUpAutomatically(RuleGraph, SourceRule, SourceTerm);
		
		
		
		Cont.receiveOutputFromModel(
				"Other", 
				"\n--------------------------correlationsadded-----------------------------\n" +
						RuleGraph.toStringRuleGraph() +
						"--------------------------correlationsend-----------------------------"
		       
										);
		
		

	}
	
	

	public void analyzeSourceTermOnebyOne(ArrayListRuleGraph UserExpandedRuleGraph, Term SourceTerm) {
		/*
		 * Usage Scenario: After finishing expanding the rule graph, 1- User selects a
		 * design option (source term) 2-The assistant builds the complete rule graph
		 * (knowledge graph) related to that design option. 3-The complete rule graph or
		 * knowledge graph is shown to the user. --User can explore design options one
		 * by one and check their related rule graphs.
		 * 
		 * Tasks to do: 1-get the source term as input. 2-build the complete graph
		 * related to that source term. 3- show the built graph.
		 */

		// 1- Create a deep Copy of user expanded graph
		//ArrayListRuleGraph RuleGraphCopy = UserExpandedRuleGraph.deepCopyRuleGraph();
		ArrayListRuleGraph AnalysisRuleGraph = new ArrayListRuleGraph();
		
		

		//print("---------RuleGraphCopyBegins-----------------");
		//RuleGraphCopy.printRuleGraph();
		//RuleGraphCopy.toStringRuleGraph();
		//print("-------------RuleGraphEnds--------------------");

		// 2 -Identify and find Source Rule
		/*
		 * Currently it is assumed that source rule is the last rule which is not always
		 * correct.
		 */
		//Rule SourceRule = RuleGraphCopy.getRule(RuleGraphCopy.getSize() - 1);
		
		Rule SourceRule = UserExpandedRuleGraph.getRule(UserExpandedRuleGraph.getSize() - 1);
		//AnalysisRuleGraph.addRule(SourceRule.deepCopyRule());
		

		// 3-Find the index of source term in the source rule
		int IndexOfSourceTerm = SourceRule.findTerminLeftHandSide(SourceTerm);
		//print("index of source term:");
		//print(IndexOfSourceTerm);

		// 4-Add correlations related to the source term and build the complete rule
		// graph (knowledge graph)
		addOnlyRelatedCorrelations(AnalysisRuleGraph, SourceRule, IndexOfSourceTerm);

		// 5-Show the complete rule graph

		//print("\n --------------Complete Rule Graph Begins---------------------\n");
		// RuleGraphCopy.printRuleGraph();
		this.sendResponseToController(AnalysisRuleGraph.PrintRuleGraphInEnglish());
		//print("\n ---------------Complete Rule Graph Ends---------------------------\n");

	}
	
	
	public void analyzeSourceTerm(ArrayListRuleGraph UserExpandedRuleGraph, Term SourceTerm) {
		/*
		 * Usage Scenario: After finishing expanding the rule graph, 1- User selects a
		 * design option (source term) 2-The assistant builds the complete rule graph
		 * (knowledge graph) related to that design option. 3-The complete rule graph or
		 * knowledge graph is shown to the user. --User can explore design options one
		 * by one and check their related rule graphs.
		 * 
		 * Tasks to do: 1-get the source term as input. 2-build the complete graph
		 * related to that source term. 3- show the built graph.
		 */

		// 1- Create a deep Copy of user expanded graph
		//ArrayListRuleGraph RuleGraphCopy = UserExpandedRuleGraph.deepCopyRuleGraph();
		ArrayListRuleGraph AnalysisRuleGraph = new ArrayListRuleGraph();
		
		

		//print("---------RuleGraphCopyBegins-----------------");
		//RuleGraphCopy.printRuleGraph();
		//RuleGraphCopy.toStringRuleGraph();
		//print("-------------RuleGraphEnds--------------------");

		// 2 -Identify and find Source Rule
		/*
		 * Currently it is assumed that source rule is the last rule which is not always
		 * correct.
		 */
		//Rule SourceRule = RuleGraphCopy.getRule(RuleGraphCopy.getSize() - 1);
		
		Rule SourceRule = UserExpandedRuleGraph.getRule(UserExpandedRuleGraph.getSize() - 1);
		//AnalysisRuleGraph.addRule(SourceRule.deepCopyRule());
		

		// 3-Find the index of source term in the source rule
		int IndexOfSourceTerm = SourceRule.findTerminLeftHandSide(SourceTerm);
		//print("index of source term:");
		//print(IndexOfSourceTerm);

		// 4-Add correlations related to the source term and build the complete rule
		// graph (knowledge graph)
		addRelatedCorrelations(AnalysisRuleGraph, SourceRule, IndexOfSourceTerm);

		// 5-Show the complete rule graph

		//print("\n --------------Complete Rule Graph Begins---------------------\n");
		// RuleGraphCopy.printRuleGraph();
		this.sendResponseToController(AnalysisRuleGraph.toStringRuleGraph());
		//print("\n ---------------Complete Rule Graph Ends---------------------------\n");

	}
	
	public void analyzeSourceTermOldVersion(ArrayListRuleGraph UserExpandedRuleGraph, Term SourceTerm) {
		/*
		 * Usage Scenario: After finishing expanding the rule graph, 1- User selects a
		 * design option (source term) 2-The assistant builds the complete rule graph
		 * (knowledge graph) related to that design option. 3-The complete rule graph or
		 * knowledge graph is shown to the user. --User can explore design options one
		 * by one and check their related rule graphs.
		 * 
		 * Tasks to do: 1-get the source term as input. 2-build the complete graph
		 * related to that source term. 3- show the built graph.
		 */

		// 1- Create a deep Copy of user expanded graph
		ArrayListRuleGraph RuleGraphCopy = UserExpandedRuleGraph.deepCopyRuleGraph();

		//print("---------RuleGraphCopyBegins-----------------");
		//RuleGraphCopy.printRuleGraph();
		//RuleGraphCopy.toStringRuleGraph();
		//print("-------------RuleGraphEnds--------------------");

		// 2 -Identify and find Source Rule
		/*
		 * Currently it is assumed that source rule is the last rule which is not always
		 * correct.
		 */
		Rule SourceRule = RuleGraphCopy.getRule(RuleGraphCopy.getSize() - 1);

		// 3-Find the index of source term in the source rule
		int IndexOfSourceTerm = SourceRule.findTerminLeftHandSide(SourceTerm);
		//print("index of source term:");
		//print(IndexOfSourceTerm);

		// 4-Add correlations related to the source term and build the complete rule
		// graph (knowledge graph)
		addRelatedCorrelations(RuleGraphCopy, SourceRule, IndexOfSourceTerm);

		// 5-Show the complete rule graph

		//print("\n --------------Complete Rule Graph Begins---------------------\n");
		// RuleGraphCopy.printRuleGraph();
		this.sendResponseToController(RuleGraphCopy.toStringRuleGraph());
		//print("\n ---------------Complete Rule Graph Ends---------------------------\n");

	}
    
	public void exploreSourceTermOldVersion(ArrayListRuleGraph UserExpandedRuleGraph, Term SourceTerm) {
		/*
		 * Usage Scenario: After finishing expanding the rule graph, 1- User selects a
		 * design option (source term) 2-The assistant builds the complete rule graph
		 * (knowledge graph) related to that design option. 3-The complete rule graph or
		 * knowledge graph is shown to the user. --User can explore design options one
		 * by one and check their related rule graphs.
		 * 
		 * Tasks to do: 1-get the source term as input. 2-build the complete graph
		 * related to that source term. 3- show the built graph.
		 */

		// 1- Create a deep Copy of user expanded graph
		ArrayListRuleGraph RuleGraphCopy = UserExpandedRuleGraph.deepCopyRuleGraph();

		print("---------RuleGraphCopyBegins-----------------");
		//RuleGraphCopy.printRuleGraph();
		RuleGraphCopy.toStringRuleGraph();
		print("-------------RuleGraphEnds--------------------");

		// 2 -Identify and find Source Rule
		/*
		 * Currently it is assumed that source rule is the last rule which is not always
		 * correct.
		 */
		Rule SourceRule = RuleGraphCopy.getRule(RuleGraphCopy.getSize() - 1);

		// 3-Find the index of source term in the source rule
		int IndexOfSourceTerm = SourceRule.findTerminLeftHandSide(SourceTerm);
		print("index of source term:");
		print(IndexOfSourceTerm);

		// 4-Add correlations related to the source term and build the complete rule
		// graph (knowledge graph)
		addRelatedCorrelations(RuleGraphCopy, SourceRule, IndexOfSourceTerm);

		// 5-Show the complete rule graph

		print("\n --------------Complete Rule Graph Begins---------------------\n");
		// RuleGraphCopy.printRuleGraph();
		RuleGraphCopy.toStringRuleGraph();
		print("\n ---------------Complete Rule Graph Ends---------------------------\n");

	}
	
	
	
	public void analyzeSourceTermsByUser(String UserInput) {
		/*
		 * Usage Scenario: 1-User identifies the source term (the design option) to be
		 * explored. 2-user gets the complete rule (knowledge) graph as output. 3-This
		 * exploration continues until user enters "exit".
		 * 
		 * 
		 * Tasks: 1-Ask for user input 2-Build related Term 3-Call explore source term
		 * ---- It is possible that user input is not correct. 4-Continue until user
		 * enters exit.
		 * 
		 * 
		 */

		


	

		// 2- Continue until user inputs "exit".
		if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			// 3 -Identify and find Source Rule
			/*
			 * Currently it is assumed that source rule is the last rule which is not always
			 * correct.
			 */
			Rule SourceRule = ExpandedRuleGraph.getRule(ExpandedRuleGraph.getSize() - 1);

			// 3- Build term related to user input.
			Term TermTobeExplored = SourceRule.matchStringInLHS(UserInput);
			//print("source term:");
			//print(TermTobeExplored.printTerm());
			analyzeSourceTermOnebyOne(ExpandedRuleGraph, TermTobeExplored);

			// 4-Ask for next user input
			/*
			System.out.print("\n" + "????????????  Which Source Term to explore? \n");
			UserInput = ConsoleScan.next();
			System.out.println("*************************************");
            */
			// What if user inputs a wrong term??????########

		}
		else 
		{
			sendResponseToController ("\n --------------------DESIGN ends-----------------------\n");
		}
	}

	public void exploreSourceTermsByUserOldVersion() {
		/*
		 * Usage Scenario: 1-User identifies the source term (the design option) to be
		 * explored. 2-user gets the complete rule (knowledge) graph as output. 3-This
		 * exploration continues until user enters "exit".
		 * 
		 * 
		 * Tasks: 1-Ask for user input 2-Build related Term 3-Call explore source term
		 * ---- It is possible that user input is not correct. 4-Continue until user
		 * enters exit.
		 * 
		 * 
		 */

		

		Scanner ConsoleScan = new Scanner(System.in);
		String UserInput;

		// 1- Ask for user input
		System.out.print("\n" + "????????????  Which Source Term to explore? \n");
		UserInput = ConsoleScan.next();
		System.out.println("*************************************");

		// 2- Continue until user inputs "exit".
		while (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			// 3 -Identify and find Source Rule
			/*
			 * Currently it is assumed that source rule is the last rule which is not always
			 * correct.
			 */
			Rule SourceRule = ExpandedRuleGraph.getRule(ExpandedRuleGraph.getSize() - 1);

			// 3- Build term related to user input.
			Term TermTobeExplored = SourceRule.matchStringInLHS(UserInput);
			print("source term:");
			print(TermTobeExplored.printTerm());
			analyzeSourceTerm(ExpandedRuleGraph, TermTobeExplored);

			// 4-Ask for next user input
			System.out.print("\n" + "????????????  Which Source Term to explore? \n");
			UserInput = ConsoleScan.next();
			System.out.println("*************************************");

			// What if user inputs a wrong term??????########

		}

		print("\n --------------------DESIGN ends-----------------------\n");
	}
	
	
	public void exploreExpandedRuleGraphOneByOne() {

		/*
		 * #########This code does not work for more than one source rules in an
		 * expanded graph. It is not flexible.
		 */
		RuleEvaluator RuleEvaluator = new RuleEvaluator();
		int NumberOfSourceTerms;

		// 1-Identify number of source terms

		NumberOfSourceTerms = ExpandedRuleGraph.getSourceRules().get(0).getLeftHandSide().size();

		for (int IndexOfSourceTerm = 0; IndexOfSourceTerm < NumberOfSourceTerms; IndexOfSourceTerm++)

		{
			// 2- deep copy expanded rule graph
			ArrayListRuleGraph RuleGraphCopy = ExpandedRuleGraph.deepCopyRuleGraph();

			// 3-Identify starting (rule;i.e. source source rule) term (i.e. source term)
			/*
			 * Currently it is assumed that source rule is the last rule which is not always
			 * correct.
			 */
			Rule SourceRule = RuleGraphCopy.getSourceRules().get(0);

			// 4-Add correlations related to the term
			addRelatedCorrelations(RuleGraphCopy, SourceRule, IndexOfSourceTerm);
			
			
			// 5- Newly added pruneDanglingRules - Nov 20 : To eliminate terms in LHS which are not applicable to a deisgn context.
			RuleGraphCopy.pruneDanglingRules();

			// 6-Evaluate resulting rule graph starting from source term
			String EvaluationSteps = RuleEvaluator.evaluateRuleGraph(RuleGraphCopy, SourceRule.getLeftHandSide().get(IndexOfSourceTerm),
					TermSatisfactionValues.SAT);
			
			ExploredRuleGraphs.add(RuleGraphCopy);
			Cont.receiveOutputFromModel("other", EvaluationSteps);

		}

	}
	
	public String ExpandedRuleGraphtoString ()
	{
		return this.ExpandedRuleGraph.toStringRuleGraph();
	}

}
