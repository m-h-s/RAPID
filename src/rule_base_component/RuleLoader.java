package rule_base_component;

import java.util.ArrayList;

import data_structures.Rule;
import data_structures.RuleCategory;
import data_structures.RuleType;
import data_structures.Term;

public class RuleLoader {
	
		public ArrayList<Rule> RuleSet;
		public String RuleBaseFileName;

		public RuleLoader(ArrayList<Rule> ruleSet) {
			RuleSet = ruleSet;
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
	

}
