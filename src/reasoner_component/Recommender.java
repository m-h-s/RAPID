package reasoner_component;
import java.util.*;
import data_structures.ListRuleSet;
import data_structures.RuleSet;
import data_structures.TermPriority;
import data_structures.TermSatisfactionValues;
import user_interaction_component.UserInteractionManager;
import java.math.*;

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

public class Recommender {

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



	private static final Map<TermPriority, Integer> PriorityWeight = new HashMap<TermPriority, Integer>();

	{
		PriorityWeight.put(TermPriority.HIGH, 4);
		PriorityWeight.put(TermPriority.MEDIUM, 2);
		PriorityWeight.put(TermPriority.LOW, 1);

		Collections.unmodifiableMap(PriorityWeight);

	}



	private List<ConstraintSet> QueryResults;

	List<Double> queryResultsScore;

	private UserInteractionManager Cont;

	public Recommender (UserInteractionManager Cont)
	{
		this.Cont = Cont;
	}

	public  void matchConstraints(ConstraintSet ReqSet, List<ListRuleSet> ExploredRuleGraphs) {
		/*
		 * 0- Form a set containing the rule graph related to each answer (design technique).
		 * 1- Evaluate each rule graph. 
		 * 2- Find each of the query items in each of the rule graph in the rule graph sets.
		 * 3- Return the query item and its value for each graph and each query. 
		 * ###The results can be used for exact matching and close matching.
		 */


		QueryResults = new ArrayList<ConstraintSet>();

		for (RuleSet RuleGraph : ExploredRuleGraphs) {
			ReqSet.print();
			ConstraintSet MatchedQuery = RuleGraph.matchConstraints(ReqSet);
			QueryResults.add(MatchedQuery);
		}

		for (ConstraintSet mq : QueryResults) {
			mq.print();


		}



	}

	public void calculateScores() {
		/*
		  
		 * The reason for setting the initial value of score to 0.01 and having flag.
		 * it is possible that the sum of two sat values becomes zero.
		 * This case should be different from the case that the query related to a term is empty;
		 * i.e. the item identified in the query does not exist in the rule graph.
		 */ 
		String QueryScore = "";

		queryResultsScore = new ArrayList<Double>();
		double score = 0.5;
		boolean flag = false;

		for (ConstraintSet q : QueryResults) {
			flag = false;
			score = 0.5;
			for (Constraint qe : q.getConstraints())

			{

				score = score + TermScore.get(qe.getAttribute().getFinalSatValue())
				* PriorityWeight.get(qe.getAttributePriority());

				flag = true;

			}

			if (score != 0.5)
				score =score - 0.5;

			else if (flag && score == 0.5)
				score -= 0.5;

			queryResultsScore.add(score);
		}

		for (int i = 0; i < queryResultsScore.size(); i++) 
			QueryScore = QueryScore + "Score: " + queryResultsScore.get(i) +"\n";


		this.Cont.receiveOutputFromModel("other", QueryScore);
	}


	public void findBestMatches (ListRuleSet ExpandedRuleGraph)
	{

		/*
		 *  Find the set of highest scores results and print them.
		 */

		String BestMatches ="I recommend either of the following designs:\n\n";
		char counter = 'a';

		this.calculateScores();

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

			if (queryResultsScore.get(i) == max && i != MaxIndex ) 
				MaxResults.add(i);





		Collections.sort(MaxResults);



		for (int i = 0; i < MaxResults.size(); i++) 
		{
			{
				BestMatches = BestMatches + '(' + counter+ ')' + ' ' + 
						ExpandedRuleGraph.getSourceRules().get(0).getPrecedent().get(MaxResults.get(i)).printTermInEnglish() + "\n\n";

				counter++;

			}

			if (queryResultsScore.get(MaxResults.get(i)) == 0.5)
				BestMatches = BestMatches + "Note: The above design alternative has no correlation with the specified requirements. \n\n";

		}
		Cont.receiveOutputFromModel("Answer",  BestMatches +"\n");

	}


}
