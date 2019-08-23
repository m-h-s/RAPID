package rule_base_component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import data_structures.*;


public class RuleLoader {

	private ArrayList<Rule> RuleSet;
	public String RuleBaseFileName;

	public RuleLoader(ArrayList<Rule> ruleSet) {
		RuleSet = ruleSet;
		ruleSetInitializer();
	}

	public void ruleSetInitializer() {

		/*
		 * #####Is it required to load all the rules? If not, which portion should be
		 * loaded. #####Is it required to load the rule set at all? In this case the
		 * interactions between the rule base and the program increases and the time
		 * increases. #####Should we insert the rules by adding columns about the next
		 * rule and the previous rule?
		 * 
		 */

		importRuleFromFile(Paths.get("").toAbsolutePath().toString() + "\\src\\rule_base_component\\Rule Base");

	}

	public void importRuleFromFile(String InputRulesFileName) {
		File InputFile = new File(InputRulesFileName);
		BufferedReader bf;

		try {
			bf = new BufferedReader(new FileReader(InputFile));

			String RuleString;
			while ((RuleString = bf.readLine()) != null) {
				// System.out.println ("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				// System.out.println("Rule String"+ RuleString);
				// Create an empty rule.
				if (!RuleString.trim().isEmpty() && !RuleString.trim().startsWith("###")) {
					Rule r = new Rule();
					r.ParseRule(RuleString);
					// System.out.println ("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
					// System.out.println(r.printRule());
					addRule(r);
				}
			}

			bf.close();
		}

		catch (IOException Ex) {
			Ex.printStackTrace();
		}

	}

	public void addRule(Rule newRule) {
		RuleSet.add(newRule);
	}

/*
	public void addRule(String lhsType, String lhsTopic, String rhsType, String rhsTopic, RuleType rType,
			RuleCategory rCat) {
		// #### important A problematic point is that lhs and rhs and terms should be in
		// the form of set not list or arraylist.

		Term t1 = new Term(lhsType, lhsTopic);
		Term t2 = new Term(rhsType, rhsTopic);

		Rule newRule = new Rule(t1, t2, rType, rCat);

		RuleSet.add(newRule);

	}

	public void addRule(ArrayList<Term> lhs, String rhsType, String rhsTopic, RuleType rType, RuleCategory rCat) {
		// ####Important A problematic point is that lhs and rhs and terms should be in
		// the form of set not list or arraylist.

		Term t2 = new Term(rhsType, rhsTopic);

		Rule newRule = new Rule(lhs, t2, rType, rCat);

		RuleSet.add(newRule);

	}

	public ArrayList<Term> createLHS(Term t) {
		ArrayList<Term> LHS = new ArrayList<Term>();
		LHS.add(t);
		return LHS;
	}
	*/
}
