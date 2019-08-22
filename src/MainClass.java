
/**
 * @author Mahsa Sadi
 *
 */
import java.util.*;

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
		QueryManager QueryManager = new QueryManager ();
		RuleExplorer RuleExplorer = new RuleExplorer (UI); 
		
		
		
		RuleExplorer.expandRuleGraphTopDownByUser();
		//RuleExplorer.exploreSourceTermsByUser();
		RuleExplorer.exploreExpandedRuleGraphOneByOne();
		
		
		List <ArrayListRuleGraph> ExploredRuleGraphs = RuleExplorer.getExploredRuleGraphs();
		ArrayListRuleGraph ExpandedRuleGraph = RuleExplorer.getExpandedRuleGraph();
		
		
		QueryManager.getRequirementsSetFromUser();
		QueryManager.executeQuery(ExploredRuleGraphs);
		QueryManager.findBestMatches(ExpandedRuleGraph);
		
		
		
		UI.InitializeUI();
		UI.setRuleExplorer(RuleExplorer);
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
