package rule_base_component;

import java.util.*;

import data_structures.Rule;
import data_structures.RuleCategory;
import data_structures.RuleType;
import data_structures.Term;
import user_interaction_component.UserInteractionManager;

import java.io.*;
import java.nio.file.Paths;

public class RuleBaseManager {

	public ArrayList<Rule> RuleSet;
	private UserInteractionManager Cont;
	private RuleLoader RuleLoader;

	public RuleBaseManager(UserInteractionManager Cont) {

		this.Cont = Cont;

		RuleSet = new ArrayList<Rule>();
		RuleLoader = new RuleLoader(RuleSet);
		// printRuleSet ();
		Cont.receiveOutputFromModel("other", this.RuleSetToString());
	}

	public String RuleSetToString() {
		String RuleSetInStringForm = "\n##########################Rule Set Begins #########################\n \n";
		for (Rule r : RuleSet)
			RuleSetInStringForm = RuleSetInStringForm + r.getRuleNumber() + ": " + r.printRule() + "\n \n";

		RuleSetInStringForm = RuleSetInStringForm
				+ "##########################Rule Set Ends ######################### \n ";

		return RuleSetInStringForm;

	}

	public ArrayList<Rule> matchRightHand(Term rhs) {

		// Find and Return the rules in the ruleset whose righthandside equals to the
		// given term.
		/*
		 * ########Is it possible to have more than one rule whose left-hand-side is the
		 * same? ######## Example: a --Help --> b and c ---help---> d? or ######## (a,b)
		 * --and--> c , (e,f)----and---> d ######## It is possible that no match is
		 * found in the rule set. ######## What if the matchedRules is empty? This code
		 * does not handle an empty matchedRules. ######## Current assumption: There is
		 * only one rule which matches the right-hand side.
		 */

		// I changed the return type of this method so that a rule can be returned
		// instead of only a rule's left hand side.

		ArrayList<Rule> matchedRules = new ArrayList<Rule>();
		for (Rule r : RuleSet) {
			// System.out.println(r.printRule());
			// System.out.println(rhs.getPredicate()+ ""+ rhs.getArguments() +"\n");
			// System.out.println(r.getRightHandSide().getPredicate()+ ""+
			// r.getRightHandSide().getArguments() +"\n");
			// System.out.println(r.matchRightHandSide(rhs));
			if (r.hasEqualRightHandSide(rhs)) {
				// System.out.println("**********************"+r.printRule());
				matchedRules.add(r);
				// System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
				// System.out.println (r.printRule());
			}

			/*
			 * Add the code for handling an empty matched rule set.
			 */

		}

		return matchedRules;
	}

	public ArrayList<Rule> matchLeftHandSide(ArrayList LHS) {

		/*
		 * Find and Return the rules in the ruleset whose Left-Hand-Side contains term
		 * LHS. Example: a---help ---b> contains a as lhs Example (a,b) ----or---> c
		 * contains a as lhs. Example (a,b) ---and ---> d contains (a,b) in lhs
		 * ############# Currently, I only consider exact matching.
		 * #####################The implementation of match left hand side is not
		 * correct. It implements containment not equality of two arrays.
		 */

		ArrayList<Rule> matchedRules = new ArrayList<Rule>();
		for (Rule r : RuleSet) {
			// System.out.println(rhs.getPredicate()+ ""+ rhs.getArguments() +"\n");
			// System.out.println(r.getRightHandSide().getPredicate()+ ""+
			// r.getRightHandSide().getArguments() +"\n");
			// System.out.println(r.matchRightHandSide(rhs));

			/// #####################Currently contains is not correct.
			if (r.containsLeftHandSide((LHS))) {
				// System.out.println("**********************"+r.getRuleNumber());
				matchedRules.add(r);
				// System.out.println (r.printRule());
			}

			/*
			 * Add the code for handling an empty matched rule set.
			 */

		}

		return matchedRules;
	}
	// Iterative right-hand-side matching. In order to incrementally expand the
	// related portion of the graph.
	// Left-Hand Side Matching.

}
