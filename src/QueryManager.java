import java.util.*;

import java.math.*;



public class QueryManager {
	
	private static final Map<TermSatisfactionValues, Integer> TermScore = new HashMap<TermSatisfactionValues, Integer>();

	{
		
		TermScore.put(TermSatisfactionValues.SAT, 2);
		TermScore.put(TermSatisfactionValues.PSAT, 1);
		TermScore.put(TermSatisfactionValues.CONF, 0);
		TermScore.put(TermSatisfactionValues.UNKNOWN, 0);
		TermScore.put(TermSatisfactionValues.PDEN, -1);
		TermScore.put(TermSatisfactionValues.DEN, -2);
		Collections.unmodifiableMap(TermScore);
	}

	
	/*
	private static final Map<TermPriority, Double> PriorityWeight = new HashMap<TermPriority, Double>();

	{
		PriorityWeight.put(TermPriority.HIGH, 2.0);
		PriorityWeight.put(TermPriority.MEDIUM, 1.0);
		PriorityWeight.put(TermPriority.LOW, 0.5);

		Collections.unmodifiableMap(PriorityWeight);

	}
	*/
	
	private static final Map<TermPriority, Integer> PriorityWeight = new HashMap<TermPriority, Integer>();

	{
		PriorityWeight.put(TermPriority.HIGH, 4);
		PriorityWeight.put(TermPriority.MEDIUM, 2);
		PriorityWeight.put(TermPriority.LOW, 1);

		Collections.unmodifiableMap(PriorityWeight);

	}
	
	
	//private REQUIREMENTS ReqSet;
	private List<Query> QueryResults;
	List<Double> queryResultsScore;
    
	private UserInteractionManager Cont;
/*	
	public void getRequirementsSetFromUser() {
		/*
		 * 1- Get Requirements (Term) 2- Get priority of Requirement 3- Get Expected
		 * Satisfaction Value 4-Iterate until user enters exit Assumption 1: User inputs
		 * are correct and they exist in the requirement list exitings in the rule set.
		 *

		System.out.println("\n----------------Requirements Specification Begins---------------\n");

		Scanner ConsoleScan = new Scanner(System.in);
		String UserInput = "";
		String UserInputForReqType = "";
		String UserInputForReqTopic = "";

		//String UserInputForExpectedSatsifactionValue = "";

		ReqSet = new REQUIREMENTS();

		while (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {
			System.out.println("\n" + "Requirement Type: ?");
			UserInputForReqType = ConsoleScan.nextLine();
			System.out.println("\n" + "Requirement Topic: ?");
			UserInputForReqTopic = ConsoleScan.nextLine();
			Term t = new Term(UserInputForReqType, UserInputForReqTopic);

			System.out.println("\n" + "priority: ?");
			TermPriority UserInputForPrioirty = TermPriority.valueOf(ConsoleScan.nextLine().toUpperCase());

			System.out.println("\n" + "Expected Satisfaction level: ?");
			TermSatisfactionValues UserInputforExpectedSatsifaction = TermSatisfactionValues
					.valueOf(ConsoleScan.nextLine().toUpperCase());

			ReqSet.addQueriedTerm(t, UserInputForPrioirty, UserInputforExpectedSatsifaction);
			UserInput = ConsoleScan.nextLine();
			System.out.println(UserInput);
		}

		
		ReqSet.printQuery();

		System.out.println("\n----------------Requirements Specification Ends---------------\n");
		

	}
*/
	public QueryManager (UserInteractionManager Cont)
	{
		this.Cont = Cont;
	}
	
	public  void executeQuery(Query ReqSet, List<ArrayListRuleGraph> ExploredRuleGraphs) {
		/*
		 * 0- Form a set containing the rule graph related to each answer (design
		 * option) 0- Evaluate each rule graph. 1- Find each of the query items in each
		 * of the rule graph in the rule graph sets. 2- Return the query item and its
		 * value for each graph and each query. --The results can be used for exact
		 * matching and close matching.
		 */

		
		QueryResults = new ArrayList<Query>();
		
		for (RuleGraph RuleGraph : ExploredRuleGraphs) {
		    ReqSet.printQuery();
			Query MatchedQuery = RuleGraph.matchQuery(ReqSet);
			QueryResults.add(MatchedQuery);
		}

		for (Query mq : QueryResults) {
			mq.printQuery();
			

		}
		
		

	}
	
	public void calculateQueryResultsScores() {
		/*
		 * There was a problem regarding defining the map in the class and then using it
		 * in this static method. I do not know what the problem is, but i moved teh
		 * place of map and now it works correctly.
		 * 
		 * 
		 * 
		 * The reason for setting the initial value of score to 0.01 and having flag.
		 * it is possible that the sum of two sat values becomes zero.
		 * This case should be different from the case that the query related to a term is empty;
		 * i.e. the item identified in the query does not exist in the rule graph.
		*/ 
        String QueryScore = "";
		
		queryResultsScore = new ArrayList<Double>();
		double score = 0.5;
		boolean flag = false;

		for (Query q : QueryResults) {
			flag = false;
			score = 0.5;
			for (QueriedTerm qe : q.getQuerySet())

			{

				//System.out.println("pppppp");
				///System.out.println(qe.getAttributePriority());
				
				//System.out.println ("qe.getAttribute().getFinalSatValue():");
				//System.out.println (qe.getAttribute().getFinalSatValue());
				score = score + TermScore.get(qe.getAttribute().getFinalSatValue())
						* PriorityWeight.get(qe.getAttributePriority());
				//System.out.println("score:");
				//System.out.println(score);
				flag = true;

			}
			
			if (score != 0.5)
				score =score - 0.5;
			
			else if (flag && score == 0.5)
				score -= 0.5;
			
			queryResultsScore.add(score);
		}
       
		for (int i = 0; i < queryResultsScore.size(); i++) {
			//System.out.println("\n hereeeeeeeeeeeeeeeeeee \n");
			QueryScore = QueryScore + "Score: " + queryResultsScore.get(i) +"\n";
			//System.out.println(queryResultsScore.get(i));

		}
		
		this.Cont.receiveOutputFromModel("other", QueryScore);
	}
	
	
	public void findBestMatches (ArrayListRuleGraph ExpandedRuleGraph)
	{

		/// Find the set of highest scores results and print them.
		// ############## The code is not well designed. It should be redesigned.

		String BestMatches ="I recommend either of the following designs:\n\n";
		char counter = 'a';

		this.calculateQueryResultsScores();
		
		double max = queryResultsScore.get(0);
		int MaxIndex = 0;
		
		for (int i = 0; i < queryResultsScore.size(); i++)

			if (queryResultsScore.get(i) > max) {
				max = queryResultsScore.get(i);
				MaxIndex = i;
			}

		List<Integer> MaxResults = new ArrayList<Integer>();
		MaxResults.add(MaxIndex);

		for (int i = 0; i < queryResultsScore.size(); i++)

			if (queryResultsScore.get(i) == max && i != MaxIndex ) {
				MaxResults.add(i);
			}

		Collections.sort(MaxResults);

		for (int i = 0; i < MaxResults.size(); i++) {
			 {
				 BestMatches = BestMatches + '(' + counter+ ')' + ' ' + ExpandedRuleGraph.getSourceRules().get(0).getLeftHandSide().get(MaxResults.get(i))
							.printTermInEnglish() + "\n\n";
				 
				 counter++;
				 /*
				 System.out.println(ExpandedRuleGraph.getSourceRules().get(0).getLeftHandSide().get(MaxResults.get(i))
					.printTerm());
					*/
			 }
			
			if (queryResultsScore.get(MaxResults.get(i)) == 0.5)
			{ // System.out.println("  :  !!!!! This rule has no relation with the specified reqs");
				BestMatches = BestMatches + "Note: The above design alternative has no correlation with the specified requirements. \n\n";
			}
			
			
			
		}
		Cont.receiveOutputFromModel("Answer",  BestMatches +"\n");

		/*
		 * Sorting does not work since the design is not correct. // Return the terms
		 * with the highest design option. Collections.sort(queryResultsScore); int
		 * MaxIndex =queryResultsScore.size()-1;
		 * 
		 * for (int i =0; i<queryResultsScore.size(); i++ ) print
		 * (queryResultsScore.get(i));
		 * 
		 * 
		 * 
		 * print ("the best rules:    "); int IndexOfNextElement = MaxIndex;
		 * 
		 * do { //printRuleGraph (ExploredRuleGraphs.get(MaxIndex));
		 * 
		 * print
		 * (ExpandedRuleGraph.get(ExpandedRuleGraph.size()-1).getLeftHandSide().get(
		 * IndexOfNextElement).printTerm()); IndexOfNextElement --; } while
		 * (queryResultsScore.get(IndexOfNextElement) == queryResultsScore.get(MaxIndex)
		 * && IndexOfNextElement >= 0 ); //print (max);
		 */
		 

	}


}
