import java.util.*;

public class RuleEvaluator {

	/*
	 * Look up tables for evaluating unary relationships. ######## All the tables
	 * should be checked.
	 */

	private final Map<TermSatisfactionValues, Double> OrderofSatValues = new HashMap<TermSatisfactionValues, Double>();

	{  
		/*
		 * The reason for adding DEN-BY-NO-REl and SAT-BY-NO-REl is to neutralize the dangling terms in AND and OR rules 
		 * of a rule grap related to an item.
		 */
		
		OrderofSatValues.put(TermSatisfactionValues.DEN_BY_NO_REl, -2.2);
		OrderofSatValues.put(TermSatisfactionValues.DEN, -2.0);
		OrderofSatValues.put(TermSatisfactionValues.PDEN, -1.0);
		OrderofSatValues.put(TermSatisfactionValues.UNKNOWN, 0.0);
		OrderofSatValues.put(TermSatisfactionValues.CONF, 0.0);
		OrderofSatValues.put(TermSatisfactionValues.PSAT, 1.0);
		OrderofSatValues.put(TermSatisfactionValues.SAT, 2.0);
		OrderofSatValues.put(TermSatisfactionValues.SAT_BY_NO_REL, 2.2);
		
		Collections.unmodifiableMap(OrderofSatValues);
	}
	
	

	private final Map<TermSatisfactionValues, TermSatisfactionValues> HeLpEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		// Currently the initialization is not correct.
		HeLpEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.PSAT);
		HeLpEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PSAT);
		HeLpEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PDEN);
		HeLpEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.PDEN);
		HeLpEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		HeLpEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(HeLpEvaluationTable);
	}

	private final Map<TermSatisfactionValues, TermSatisfactionValues> HurtEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		HurtEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.PDEN);
		HurtEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PDEN);
		HurtEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PSAT);
		HurtEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.PSAT);
		HurtEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		HurtEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(HurtEvaluationTable);
	}

	private final Map<TermSatisfactionValues, TermSatisfactionValues> MakeEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		// Currently the initialization is not correct.
		MakeEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.SAT);
		MakeEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PSAT);
		MakeEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PDEN);
		MakeEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.DEN);
		MakeEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		MakeEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(MakeEvaluationTable);
	}

	private final Map<TermSatisfactionValues, TermSatisfactionValues> BreakEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		// Currently the initialization is not correct.
		BreakEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.DEN);
		BreakEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PDEN);
		BreakEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PSAT);
		BreakEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.SAT);
		BreakEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		BreakEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(BreakEvaluationTable);
	}

	private final Map<TermSatisfactionValues, TermSatisfactionValues> SomePlusEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		// Currently the initialization is not correct.
		SomePlusEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.PSAT);
		SomePlusEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PSAT);
		SomePlusEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PDEN);
		SomePlusEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.PDEN);
		SomePlusEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		SomePlusEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(SomePlusEvaluationTable);
	}

	private final Map<TermSatisfactionValues, TermSatisfactionValues> SomeMinusEvaluationTable = new HashMap<TermSatisfactionValues, TermSatisfactionValues>();
	{
		// This look up table should be checked.
		SomeMinusEvaluationTable.put(TermSatisfactionValues.SAT, TermSatisfactionValues.PDEN);
		SomeMinusEvaluationTable.put(TermSatisfactionValues.PSAT, TermSatisfactionValues.PDEN);
		SomeMinusEvaluationTable.put(TermSatisfactionValues.PDEN, TermSatisfactionValues.PSAT);
		SomeMinusEvaluationTable.put(TermSatisfactionValues.DEN, TermSatisfactionValues.PSAT);
		SomeMinusEvaluationTable.put(TermSatisfactionValues.UNKNOWN, TermSatisfactionValues.UNKNOWN);
		SomeMinusEvaluationTable.put(TermSatisfactionValues.CONF, TermSatisfactionValues.CONF);
		Collections.unmodifiableMap(SomeMinusEvaluationTable);
	}

	private final Map<RuleType, Map<TermSatisfactionValues, TermSatisfactionValues>> EvaluationTable = new HashMap<RuleType, Map<TermSatisfactionValues, TermSatisfactionValues>>();
	{
		EvaluationTable.put(RuleType.HELP, HeLpEvaluationTable);
		EvaluationTable.put(RuleType.HURT, HurtEvaluationTable);
		EvaluationTable.put(RuleType.MAKE, MakeEvaluationTable);
		EvaluationTable.put(RuleType.BREAK, BreakEvaluationTable);
		EvaluationTable.put(RuleType.SOMEPLUS, SomePlusEvaluationTable);
		EvaluationTable.put(RuleType.SOMEMINUS, SomeMinusEvaluationTable);
		Collections.unmodifiableMap(EvaluationTable);

	}

	// I am not sure whether these methods should be static or not.
	public void AndEvaluation(Rule r) {
		/*
		 * Precondition 1: All the terms in the left-hand-side of the given rule have a
		 * final sat value. This precondition has been checked before this method.
		 * 
		 * Precondition 2: There is more that one term in the given rule LHS.
		 * 
		 * Precondition 3: ??? #######How conf and known in lhs should be treated.
		 * 
		 * This implementation of and evaluation is based on page 72 of NFR and maybe
		 * different from my TP summary (I do not have conf and unknown).
		 */
		// #######The problem here is that should conf and unknown values be resolved
		// before evaluation or not?
		Term MinTerm = r.getLeftHandSide().get(0);

		for (Term t : r.getLeftHandSide())
		{   //System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+ OrderofSatValues.get(t.getFinalSatValue()));
			if (OrderofSatValues.get(t.getFinalSatValue()) < OrderofSatValues.get(MinTerm.getFinalSatValue()))
				MinTerm = t;
		}	

		assignReceivedValue(r.getRightHandSide(), MinTerm.getFinalSatValue());
	}

	public void OrEvaluation(Rule r) {
		/*
		 * Precondition 1: All the terms in the left-hand-side of the given rule have a
		 * final sat value. This precondition has been checked before this method.
		 * 
		 * Precondition 2: There is more that one term in the given rule LHS.
		 * 
		 * Precondition 3: ??? #######How conf and known in lhs should be treated.
		 * 
		 * This implementation of and evaluation is based on page 72 of NFR and maybe
		 * different from my TP summary (I do not have conf and unknown).
		 */
		// #######The problem here is that should conf and unknown values be resolved
		// before evaluation or not?

		Term MaxTerm = r.getLeftHandSide().get(0);

		for (Term t : r.getLeftHandSide())
			
			{ //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+OrderofSatValues.get(t.getFinalSatValue()));
			  if (OrderofSatValues.get(t.getFinalSatValue()) > OrderofSatValues.get(MaxTerm.getFinalSatValue()))
				MaxTerm = t;
			}

		assignReceivedValue(r.getRightHandSide(), MaxTerm.getFinalSatValue());
	}

	// I am not sure whether this method should be static or not.

	public void assignReceivedValue(Term t, TermSatisfactionValues SatValue) {
		/*
		 * Set the received value of a given term from a given rule.
		 */
		t.setReceivedSatValue(SatValue);
	}

	public void evaluate(Rule r) {
		/*
		 * This method is the same as propagate value in the evaluation procedure. Set
		 * the received value of the right-hand side term of a rule, having the final
		 * value of all the terms in the left hand side.
		 * 
		 */
		/*
		 * Precondition: All the terms in Left Hand Side must have a final Satisfaction
		 * value.
		 */

		///################## for and rule type one den is enough.
		//#################### for or rule type one sat is enough.
		
		
		
		boolean precondition_1_Met = false;
		boolean precondition_2_Met = false;
		boolean precondition_3_Met = true;
		 

		for (Term t : r.getLeftHandSide())
			
		{
			if (t.getFinalSatValue() == TermSatisfactionValues.SAT && r.getRuleType() == RuleType.OR) 
				{ 
					precondition_1_Met = true;
					break;
				}
			else if (t.getFinalSatValue() == TermSatisfactionValues.DEN && r.getRuleType() == RuleType.AND) 
				{ 
					precondition_2_Met = true;
					break;
				}
			
			else if (t.getFinalSatValue() == TermSatisfactionValues.UNDET) 
				{ 
					precondition_3_Met = false;
					
				}
					
		}

		
		
		
		if (precondition_1_Met)
		{
			assignReceivedValue(r.getRightHandSide(), TermSatisfactionValues.SAT);
		}
		
		else if (precondition_2_Met)
		{
			assignReceivedValue(r.getRightHandSide(), TermSatisfactionValues.DEN);
		}
		
		else if (precondition_3_Met) {

			if (r.getRuleType() == RuleType.AND)
				// Binary relation # maybe it is more appropriate to
				// define a separate data type for binary and unary relations.
				AndEvaluation(r);
			else if (r.getRuleType() == RuleType.OR)
				// Binary relation
				OrEvaluation(r);
			else if (r.getRuleType() == RuleType.UNKNOWN)
				// Unary relation
				// This passing is by reference.
				// Do I use Unknown relation and rule in my rule graphs? Currently not.
				assignReceivedValue(r.getRightHandSide(), TermSatisfactionValues.UNKNOWN);
			else if (r.getRuleType() == RuleType.EQUAL)
				// Unary Relation
				// This passing is by reference.
				// Do I use Unknown relation and rule in my rule graphs? Currently not.
				assignReceivedValue(r.getRightHandSide(), r.getLeftHandSide().get(0).getFinalSatValue());
			else {
				// look up appropriate table and set value.
				assignReceivedValue(r.getRightHandSide(),
						EvaluationTable.get(r.getRuleType()).get(r.getLeftHandSide().get(0).getFinalSatValue()));
			}

		}
		
		else
		{
			System.out.println("There is at least one left-hand-side term "
					+ "which does not have a final satisfaction value");
		}

	}

	public void assignFinalValue(Term t, TermSatisfactionValues SatValue) {
		/*
		 * Set the final value of a given term from a given rule.
		 */
		t.setFinalSatValue(SatValue);
	}

	public void DecideFinalValueRHS(Rule r) {
		/*
		 * 1- Conflict Resolution if there are any 2- Set final Value Precondition 1:
		 * The values of LHS terms are final or the rule has no LHS; i.e. the term is a
		 * source not but not a sink. Precondition 2: The LHS terms do not receive any
		 * further received values.
		 */
	}

	public void clearRecivedValues(Term t) {
		// Maybe required.
	}

	public void clearFinalValues(Term t) {
		// Maybe required.
	}

	public void assignLHSValue(Rule r, Term LHSTerm, TermSatisfactionValues SatVal) {

		int index = r.findTerminLeftHandSide(LHSTerm);

		if (index >= 0) {

			Term t = r.getLeftHandSide().get(index);

			this.assignReceivedValue(t, SatVal);
		}
		
		else 
		{
			print ("Term not found in left hand side when assigning received value.");
		}

	}

	public void finalizeLHSValue(Rule r, Term LHSTerm, TermSatisfactionValues SatVal) {
		int index = r.findTerminLeftHandSide(LHSTerm);

		if (index >= 0) {

			Term t = r.getLeftHandSide().get(index);

			this.assignFinalValue(t, SatVal);
		}
		else 
		{
			print ("Term not found in left hand side when assigning final value.");
		}


	}

	public void PropogateValue(Rule r) {
		this.evaluate(r);
	}

	public void finalizeRHSValue(Rule r, TermSatisfactionValues SatVal)

	{

		this.assignFinalValue(r.getRightHandSide(), SatVal);
	}
  

	

	public String evaluateRuleGraph(ArrayListRuleGraph RuleGraph, Term SourceTerm, TermSatisfactionValues SatVal) {

		/*
		 * Evaluate Expanded Rule Graph by Starting from the given Source Term. The Sat
		 * Value of the source is given as well.
		 * 
		 * Assumption 1- : The given graph is (fully) connected; i.e. the is no term or
		 * no rule which is not accessible from other terms and rules.
		 * 
		 * Assumption 2- There is no term which appears at the right hand side of two
		 * rules. if a --T1--> b and c ---T2---> b then a == c.
		 * 
		 * Assumption 3- The given source term is the selected (design) option and
		 * belongs to a rule of typeFREQ.
		 * 
		 * Assumption 4- Rules related to the given source Term are unary.
		 */

		/*
		 * Algorithm for the evaluation of the rule (term) graph (in a breadth-first
		 * order):
		 * 
		 * 0- Get source node (Term). 0- Get the value of source node (Term).
		 * 
		 * 1- Find related rule to the source node in the rule graph. -- Find the
		 * neighbors of the source node.
		 * 
		 * 
		 * 2- for each found rule Set the value of the source node in the found rules.
		 * 
		 * 3- for each neighbor of the source node: propagate the value of the source
		 * node.
		 * 
		 * 
		 * 4- Identify the neighbors of the source term as new source terms.
		 * 
		 * 
		 * 5- for each neighbor (RHS Term) of the source node: start from 0,1
		 */

		/*
		print("####################Evaluation begins######################\n");
		for (Rule r : ArrayListRuleGraph)
				print (r.printRule());
		print("----------------------------------------//////////////////");
		*/
		
		Queue<Term> QueueofSourceTerms = new LinkedList<Term>();
		QueueofSourceTerms.add(SourceTerm);

		Queue<TermSatisfactionValues> QueueOfSourceTermSatVal = new LinkedList<TermSatisfactionValues>();
		QueueOfSourceTermSatVal.add(SatVal);
		
		String EvaluationSteps = "####################Evaluation begins######################\n";

		while (!QueueofSourceTerms.isEmpty()) {

			// 0- Get new source term (node)
		
			SourceTerm = QueueofSourceTerms.remove();

			// 0- Get the value of source term (node)
			SatVal = QueueOfSourceTermSatVal.remove();

			// 1- Find Rules starting from the source node (the source term)
			 //ArrayList<Rule> RulesStartingfromSourceTerm = findRules(RuleGraph1, SourceTerm);
			ArrayList<Rule> RulesStartingfromSourceTerm = RuleGraph.findRulesStartingWithSourceTerm(SourceTerm);
			
			/*
			print("ppppppppppppppppppppppppppppppppppppppp");
			for (Rule r : RulesStartingfromSourceTerm )
				print(r.printRule());
			
			print("ppppppppppppppppppppppppppppppppppppppp");
			*/
			//SourceTerm = QueueofSourceTerms.remove();

			// 2- Set the value of source term (i.e. LHS term) in the found rules.
			for (Rule r : RulesStartingfromSourceTerm)

			{ // evaluateRule(r1, SourceTerm, SatVal);
				assignLHSValue(r, SourceTerm, SatVal);
				finalizeLHSValue(r, SourceTerm, SatVal);
			}

			// 3- For each rule in the found rules propagate the value of the source term.

			for (Rule r : RulesStartingfromSourceTerm)

			{ ///////////
               // String EvaluationStep = "";
               
               /*
				print("-------------SourceRule------------------------");
				print(r.printRule());
				
				*/
               
               EvaluationSteps = EvaluationSteps + 
            		   "-------------SourceRule------------------------\n" +
            		   r.printRule() +
            		   "\n";

				///////////

				PropogateValue(r);

				if (!r.getRightHandSide().getReceivedSatValues().isEmpty())
					finalizeRHSValue(r, r.getRightHandSide().getReceivedSatValues().get(0));

				////////////

				for (Term t : r.getLeftHandSide()) {
					if (!t.getReceivedSatValues().isEmpty())
						// print("lhs received:" + t.printReceivedStatus());
						EvaluationSteps = EvaluationSteps +
						"lhs received:" + 
								t.printReceivedStatus() +
								"\n";
					else {
						// print("lhs received:[]");
						EvaluationSteps = EvaluationSteps + "lhs received:[]"+ "\n";
					}
					//print("lhs final:" + t.printFinalSatStatus());
					EvaluationSteps = EvaluationSteps + "lhs final:" + t.printFinalSatStatus() + "\n";
				}

				 // print("rhs received:" + r.getRightHandSide().getReceivedSatValues());
				EvaluationSteps = EvaluationSteps + "rhs received:" + r.getRightHandSide().getReceivedSatValues() + "\n";
				// print("rhs final:" + r.getRightHandSide().getFinalSatValue());
				EvaluationSteps = EvaluationSteps + "rhs final:" + r.getRightHandSide().getFinalSatValue() + "\n";

				/////////////
				
			}

			// 4- Identify new Source Terms and add the to the queue.

			for (Rule r : RulesStartingfromSourceTerm) {
				QueueofSourceTerms.add(r.getRightHandSide());
				QueueOfSourceTermSatVal.add(r.getRightHandSide().getFinalSatValue());
			}

			// 5- start from new source node.

		}

		
		EvaluationSteps = EvaluationSteps + "####################Evaluation ends######################\n";
		
		return EvaluationSteps;
		/*
		 * ###################Old way: Recursive and depth first################ //
		 * 4-Old Identify new source terms. ArrayList<Term> newSourceTerms = new
		 * ArrayList<Term>();
		 * 
		 * for (Rule r : RulesStartingfromSourceTerm) {
		 * newSourceTerms.add(r.getRightHandSide()); }
		 * 
		 * // 5- Old Start from 1 // If the iteration is breadth first it works more
		 * efficiently. for (Term newTerm : newSourceTerms) evaluateRuleGraph(newTerm,
		 * newTerm.getFinalSatValue());
		 * 
		 * ######################################
		 */
		
		//print("####################Evaluation ends######################\n");

	}

	private <T> void print(T x) {
		System.out.println(x.toString());
	}

}
