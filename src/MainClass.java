

/**
 * @author Mahsa Sadi
 *
 */
import java.util.*;

import user_interaction_component.UserInteractionManager;

/*
 * Why enum is known although it is not defined here?
 * 
 */

public class MainClass {

	/**
	 * @param args
	 */


	
	public static void main(String[] args) {

		// The initiating term or terms should be taken from the user. however, the user
		// should know how to initiate graph expansion.
		/*
		 * Current usage Scenario of the system: 1- User expands a rule graph 2- User
		 * explores design options one by one. 3- User identifies the important
		 * requirements and their priority. 4- User asks for the best design option.
		 * 
		 */
		
		
		
		 UserInteractionManager Cont = new UserInteractionManager ();
		 
		 
		
		/*
		
		GraphRuleGraph RG = new GraphRuleGraph();
		Term t1 = new Term ("G1", "");
		Term t4 = new Term ("G2", "");
		Term t2 = new Term ("G2", "");
		ArrayList <Term> LHS = new ArrayList <Term> ();
		LHS.add(t1);
		LHS.add (t4);
		Rule r1= new Rule (LHS, t2, RuleType.AND, RuleCategory.UNDET);
		
		Term t3 = new Term ("G3", "");
		
		Rule r2= new Rule (t1, t3, RuleType.HURT, RuleCategory.UNDET);
		
		RG.addRule(r1);
		RG.addRule(r2);
		RG.printRuleGraph();
		RG.deleteRule(r1);
		System.out.println("**********");
		RG.printRuleGraph();
		
		
		*/ 
		
		/*
		
		UserInterface UI = new UserInterface (); 
		Recommender Recommender = new Recommender ();
		rule_explorer_component rule_explorer_component = new rule_explorer_component (UI); 
		
		
		
		rule_explorer_component.expandRuleGraphTopDownByUser();
		//rule_explorer_component.exploreSourceTermsByUser();
		rule_explorer_component.exploreExpandedRuleGraphOneByOne();
		
		
		List <ArrayListRuleGraph> ExploredRuleGraphs = rule_explorer_component.getExploredRuleGraphs();
		ArrayListRuleGraph ExpandedRuleGraph = rule_explorer_component.getExpandedRuleGraph();
		
		
		Recommender.getRequirementsSetFromUser();
		Recommender.executeQuery(ExploredRuleGraphs);
		Recommender.findBestMatches(ExpandedRuleGraph);
		
		
		
		UI.InitializeUI();
		UI.setRuleExplorer(rule_explorer_component);
		*/
		
		
		
		
	     
		
		
		
		
		

		/*
		 * 
		 * GraphDrawer g = new GraphDrawer(); g.InitializeFrame();
		 * 
		 */

		/*
		 * RuleDataBaseManager ruleBaseManager = new RuleDataBaseManager ();
		 * ruleBaseManager.connecttToRuleBase(); //ruleManager.addRule();
		 * ruleBaseManager.updateRule(); ruleBaseManager.showRuleBaseContent();
		 * ruleBaseManager.deteleRule(); //deleteRule doesno't work
		 * ruleBaseManager.showRuleBaseContent();
		 */

	}

}
