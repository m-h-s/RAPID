package rule_base_component;
import java.util.*;

import data_structures.Rule;
import data_structures.RuleCategory;
import data_structures.RuleType;
import data_structures.Term;
import user_interaction_component.UserInteractionManager;

import java.io.*;
import java.nio.file.Paths;

public class RuleSetManager {
	
	
	
	/* Currently, I assume that the collection of rules are kept in a list. 
	 * However, this implementation is not efficient when the number of rules increases.
	 * This should be replaced by the Rule-Graph Data Structure.
	 */
	private ArrayList<Rule> RuleSet = new ArrayList<Rule>();
	
	private UserInteractionManager Cont;
	
	private String RuleBaseFileName;	

	public RuleSetManager (UserInteractionManager Cont)
	{
		// #####I am not sure whether these should be in the main method or in the constructor of the class.
		// #####I am not sure about the type of operations that a constructor should do.
		this.Cont = Cont;
		ruleSetInitializer();
		Cont.receiveOutputFromModel("other", this.RuleSetToString());
		RuleBaseFileName = "RuleBase";
		//printRuleSet ();
	}
	
	public void addRule(String lhsType, String lhsTopic, String rhsType, String rhsTopic, RuleType rType,
			RuleCategory rCat) {
		// #### important A problematic point is that lhs and rhs and terms should be in
		// the form of set not list or arraylist.

		Term t1 = new Term(lhsType, lhsTopic);
		Term t2 = new Term(rhsType, rhsTopic);

		Rule newRule = new Rule(t1, t2, rType, rCat);

		RuleSet.add(newRule);

	}

	public void addRule(ArrayList<Term> lhs, String rhsType, String rhsTopic, RuleType rType,
			RuleCategory rCat) {
		// ####Important A problematic point is that lhs and rhs and terms should be in
		// the form of set not list or arraylist.

		Term t2 = new Term(rhsType, rhsTopic);

		Rule newRule = new Rule(lhs, t2, rType, rCat);

		RuleSet.add(newRule);

	}
	
	public void addRule (Rule newRule)
	{
		RuleSet.add(newRule);
	}

	/*
	public Term createTerm(String predicate, String argument) {
		
		Term t = new Term (predicate, argument);
		return t;

	}
	*/
	
	public ArrayList<Term> createLHS (Term t)
	{
		ArrayList<Term> LHS = new ArrayList<Term> ();
		LHS.add(t);
		return LHS;
	}
/*	
	public ArrayList <Term> addToLHS (ArrayList <Term> LHS, Term t)
	{
		LHS.add(t);
		return LHS;
	}
	*/
	
	
		
	public void importRuleFromFile (String InputRulesFileName)
	{
		File InputFile = new File (InputRulesFileName);
		BufferedReader bf;
		
		try 
		{
		  bf = new BufferedReader (new FileReader (InputFile));
		  
		  String RuleString;
		  while ((RuleString = bf.readLine()) != null)
		  {
			//System.out.println ("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			//System.out.println("Rule String"+ RuleString);
			 //Create an empty rule.
			 if (! RuleString.trim().isEmpty() && ! RuleString.trim().startsWith("###"))
				{
					Rule r = new Rule();
					r.ParseRule(RuleString);
					// System.out.println ("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
					// System.out.println(r.printRule());
					addRule(r);
				}
		  }
		  
		  bf.close();
		}
		
		
		catch (IOException Ex)
		{
		    Ex.printStackTrace();
		}
		
	}
	
	
	
	public void ruleSetInitializer() {
		
		/*  #####For initializer , a loader is maybe required that loads the rule set from a file or from data base. 
		 *  ##### Initializer can get the rules from the rulebase. 
		 *  #####Is it required to load all the rules? If not, which portion should be loaded.
		 *  #####Is it required to load the rule set at all? In this case the interactions 
		 *  between the rule base and the program increases and the time increases.
		 *  #####Should we insert the rules by adding columns about the next rule and the previous rule? 
		 *  
		 */
		
		importRuleFromFile (Paths.get("").toAbsolutePath().toString() +"\\src\\rule_base_component\\Rule Base");
		
		

	}

	/*
	public void printRuleSet() {
		
		System.out.println("\n ##########################Rule Set Begins ######################### \n");
		
		for (Rule r : RuleSet) {
			System.out.println(r.printRule());
			
		}
		
		System.out.println("\n ##########################Rule Set Ends ######################### \n ");
	}
	*/
	
	public String RuleSetToString ()
	{
		String RuleSetInStringForm = "\n##########################Rule Set Begins #########################\n \n";
		for (Rule r : RuleSet)
				RuleSetInStringForm = RuleSetInStringForm + r.getRuleNumber() + ": "+ r.printRule() + "\n \n";
		
		RuleSetInStringForm= RuleSetInStringForm + "##########################Rule Set Ends ######################### \n ";
		
		return RuleSetInStringForm;
		
	}
	
	
	public ArrayList<Rule> matchRightHand (Term rhs)
	{
		
		// Find and Return the rules in the ruleset whose righthandside equals to the given term.
		/* ########Is it possible to have more than one rule whose left-hand-side is the same?
		 * ######## Example: a --Help --> b and c ---help---> d? or
		 * ######## (a,b) --and--> c , (e,f)----and---> d
		 * ######## It is possible that no match is found in the rule set.
		 * ######## What if the matchedRules is empty? This code does not handle an empty matchedRules.
		 * ######## Current assumption: There is only one rule which matches the right-hand side.
		 */
		
		// I changed the return type of this method so that a rule can be returned instead of only a rule's left hand side.
		
		ArrayList<Rule> matchedRules = new ArrayList<Rule> ();
		for (Rule r : RuleSet)
		{
			// System.out.println(r.printRule());
			//System.out.println(rhs.getPredicate()+ ""+ rhs.getArguments() +"\n");
			//System.out.println(r.getRightHandSide().getPredicate()+ ""+ r.getRightHandSide().getArguments() +"\n");
			//System.out.println(r.matchRightHandSide(rhs));
			if (r.hasEqualRightHandSide(rhs))
			{   
				//System.out.println("**********************"+r.printRule());
				matchedRules.add(r);
				//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
				//System.out.println (r.printRule());
			}
			
			/* 
			 * Add the code for handling an empty matched rule set.
			 */
			
		}
		
		return matchedRules;
	}
	
	
	public ArrayList<Rule> matchLeftHandSide (ArrayList LHS)
	{
		
		/*
		 *  Find and Return the rules in the ruleset whose Left-Hand-Side contains term LHS.
		 *  Example: a---help ---b> contains a as lhs
		 *  Example (a,b) ----or---> c contains a as lhs.
		 *  Example (a,b) ---and ---> d contains (a,b) in lhs
		 *  ############# Currently, I only consider exact matching.
		 *  #####################The implementation of match left hand side is not correct.
		 *  It implements containment not equality of two arrays.
		 */
		
		
		ArrayList<Rule> matchedRules = new ArrayList<Rule> ();
		for (Rule r : RuleSet)
		{
			//System.out.println(rhs.getPredicate()+ ""+ rhs.getArguments() +"\n");
			//System.out.println(r.getRightHandSide().getPredicate()+ ""+ r.getRightHandSide().getArguments() +"\n");
			//System.out.println(r.matchRightHandSide(rhs));
			
			
			/// #####################Currently contains is not correct.
			if (r.containsLeftHandSide((LHS)))
			{   
				//System.out.println("**********************"+r.getRuleNumber());
				matchedRules.add(r);
				//System.out.println (r.printRule());
			}
			
			/* 
			 * Add the code for handling an empty matched rule set.
			 */
			
		}
		
		return matchedRules;
	}
	// Iterative right-hand-side matching. In order to incrementally expand the related portion of the graph.
	// Left-Hand Side Matching.
	
	
	

}
