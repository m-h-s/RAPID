import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;

enum UserMode {
	Exploration, Query, ExploreOneByOne, FindBestAnswer
}

enum ExpectedQueryItem {
	Req, Type, Topic, PRIORITY, SatVal
}

public class UserInteractionManager {

	private UserInterface UI;
	private RuleExplorer RuleExplorer;
	private QueryManager QueryManager;

	private UserMode UserMode;

	private ExpectedQueryItem ExpectedItem;

	private Query ReqSet;

	private String UserInputForReqType;
	private String UserInputForReqTopic;
	private String UserInputForReq;

	private Term userInputforTerm;
	private TermPriority userInputForPriority;
	private TermSatisfactionValues userInputforExpectedSatsifaction;

	List<ArrayListRuleGraph> ExploredRuleGraphs;
	ArrayListRuleGraph ExpandedRuleGraph;

	private String TheMostRecentUSerInput;

	static int conversationNumber;

	private String greetingMessage = "Hello! I am a design assistant.\n\n";

	private String commandMessage = "Please tell me how I can help:\n\n"
			+ "(a) to figure out a design solution, (b) to analyze a design solution, "
			+ "\n\n(c) to evaluate a design solution, (d) to recommend a design solution,"
			+ "\n\n(e) to show my knowledge, or (f) to explain what I am doing.";

	private String designMessage = "I am in the design mode.\n\n";
	private String requirementSpecificationMessage = "Please specify the design requirements:";

	private String analysisMessage = "I am in the analysis mode.\n\n";
	private String analysisSpecificationMessage = "Please specify the design solution that I should analyze:";

	private String noAnswerMessage = " I cannot respond to this query.\n\n";
	private String RequirementsSpecificationMessage = "Please identify the important non-fucntioanl requirements \n\n"
			+ "and their priority one by one. \n\n";

	public UserInteractionManager() {
		UI = new UserInterface();
		RuleExplorer = new RuleExplorer(this);
		QueryManager = new QueryManager(this);

		UI.InitializeUI();
		UI.setController(this);

		this.ExpectedItem = ExpectedQueryItem.Req;
		this.ReqSet = new Query();

		this.TheMostRecentUSerInput = "";

		resetConversationNumber();

		showStartMessage();
	}

	private void resetConversationNumber() {
		conversationNumber = 0;
	}

	private void showStartMessage() {

		printInConversationHistory("Question", greetingMessage + commandMessage);
	}

	public void processUserInput(String UserInput) {

		/*
		 * Right now the switch statements do not work correctly. The logic of exit is
		 * not correct.
		 */

		if (Objects.equals(UserInput.toLowerCase().trim(), "exit") && this.UserMode != UserMode.Query) {
			printInConversationHistory("Other", UserInput);
			printInConversationHistory("Question", commandMessage);
		}

		else if (Objects.equals(UserInput.toLowerCase().trim(), "exit") && this.UserMode == UserMode.Query) {
			processRequirements(UserInput);
			printInConversationHistory("Question", commandMessage);
		}

		else if (Objects.equals(UserInput.toLowerCase().trim(), "figure out")) {
			this.UserMode = UserMode.Exploration;
			printInConversationHistory("Other", UserInput);
			printInConversationHistory("Question", designMessage + requirementSpecificationMessage);
		}

		else if (Objects.equals(UserInput.toLowerCase().trim(), "find"))
			this.UserMode = UserMode.Query;

		else if (Objects.equals(UserInput.toLowerCase().trim(), "analyze")) {
			printInConversationHistory("Other", UserInput);
			this.UserMode = UserMode.ExploreOneByOne;
			printInConversationHistory("Question", analysisSpecificationMessage);
		}

		else if (Objects.equals(UserInput.toLowerCase().trim(), "recommend")) {
			printInConversationHistory("Other", UserInput);
			this.UserMode = UserMode.Query;
			RuleExplorer.exploreExpandedRuleGraphOneByOne();
			ExploredRuleGraphs = RuleExplorer.getExploredRuleGraphs();
			ExpandedRuleGraph = RuleExplorer.getExpandedRuleGraph();
			printInConversationHistory("Question", RequirementsSpecificationMessage);
			getRequirementsSetFromUser();
		} else if (this.UserMode == UserMode.Exploration)
			exploreDesigns(UserInput);

		else if (this.UserMode == UserMode.Query)
			processRequirements(UserInput);

		else if (this.UserMode == UserMode.ExploreOneByOne) {
			printInConversationHistory("Other", UserInput);
			// printInConversationHistory ("Answer", "\n----------------Exploration
			// begins---------------\n");
			RuleExplorer.analyzeSourceTermsByUser(UserInput);
			printInConversationHistory("Question", analysisMessage + analysisSpecificationMessage);
		}

		else {
			printInConversationHistory("Other", UserInput);
			printInConversationHistory("Answer", noAnswerMessage);
		}

	}

	public void exploreDesigns(String UserInput) {
		if (!Objects.equals(UserInput.toLowerCase().replaceAll("\\s", ""), "exit")) {
			TheMostRecentUSerInput = UserInput;
			sendUserInputToRuleExplorer(UserInput);
		}

		else {

			printInConversationHistory("other", "exit");

			UI.appendToInfoPane("\n ***********************Expanded Graph************************\n"
					+ RuleExplorer.ExpandedRuleGraphtoString()
					+ "\n ####################Expansion Done###################### \n");

			printInConversationHistory("Question",
					"Do you want to explore answers one by one or find the best answer? ");

			/*
			 * RuleExplorer.exploreExpandedRuleGraphOneByOne();
			 * 
			 * ExploredRuleGraphs = RuleExplorer.getExploredRuleGraphs(); ExpandedRuleGraph
			 * = RuleExplorer.getExpandedRuleGraph();
			 * 
			 * getRequirementsSetFromUser();
			 * 
			 */
		}
	}
	/*
	 * public void getUserInputFromUI(String UserInput) {
	 * 
	 * if (this.UserMode == UserMode.Exploration) exploreDesigns(UserInput);
	 * 
	 * else if (this.UserMode == UserMode.Query) processRequirements(UserInput);
	 * 
	 * }
	 */

	public void sendUserInputToRuleExplorer(String UserInput) {
		RuleExplorer.getUserInputFromController(UserInput);
	}

	public void receiveOutputFromModel(String AnswerName, String Answer) {
		if (AnswerName == "Answer") {
			printInConversationHistory("Answer", Answer);
		}

		else if (AnswerName == "EmptyAnswer") {

			printInConversationHistory("Other", this.TheMostRecentUSerInput);
			printInConversationHistory("Answer", Answer);

		}

		else {
			UI.appendToInfoPane(Answer);
		}
	}

	public void receiveOutputFromModel(String AnswerName, ArrayList<String> Answer) {
		if (AnswerName == "RuleGraphExpansionHistory" && !Answer.isEmpty()) {
			for (int i = 0; i < Answer.size(); i++)
				if (i % 2 == 0)
					// printInConversationHistory("Other", Answer.get(i), i);
					printInConversationHistory("Other", Answer.get(i));
				/*
				 * else if (i == Answer.size() - 1) { printInConversationHistory("Answer",
				 * Answer.get(i), -1); System.out.println("ZZZZZZZZZZZZZZZZ"+ Answer.get(i)); }
				 */
				else
					// printInConversationHistory("Answer", Answer.get(i), i);
					printInConversationHistory("Answer", Answer.get(i));
		}

		else if (AnswerName == "Answer") {
			for (int i = 0; i < Answer.size(); i++)
				if (i % 2 == 0)
					printInConversationHistory("Other", Answer.get(i), i);

				else if (i == Answer.size() - 1)
					printInConversationHistory("Answer", Answer.get(i), -1);
				else
					printInConversationHistory("Answer", Answer.get(i), i);
		} else {
			printInConversationHistory("Other", this.TheMostRecentUSerInput);
			printInConversationHistory("Answer", noAnswerMessage);
		}
	}

	public void processRequirements(String UserInput) {

		this.TheMostRecentUSerInput = UserInput;

		if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			if (this.ExpectedItem == ExpectedQueryItem.Req) {
				UserInputForReq = UserInput;
				String[] SplitUserInput = UserInput.toLowerCase().split(" is ");
				userInputforTerm = new Term();
				String[] splitInputTerm;
				splitInputTerm = SplitUserInput[0].split(" of the ");
				userInputforTerm = new Term(splitInputTerm[0], splitInputTerm[1]);
				// userInputforTerm.parseTerm();
				System.out.println(userInputforTerm.printTerm());

				String[] splitInputPriority = SplitUserInput[1].toLowerCase().split("\\.");
				// String inputPriroity = SplitUserInput[1].toLowerCase();

				System.out.println(splitInputPriority[0]);

				if (Objects.equals(splitInputPriority[0], "very critical"))

				{
					userInputForPriority = TermPriority.HIGH;
					System.out.println("here");
				}

				else if (Objects.equals(splitInputPriority[0], "critical"))
					userInputForPriority = TermPriority.MEDIUM;

				else if (Objects.equals(splitInputPriority[0], "important"))
					userInputForPriority = TermPriority.LOW;

				System.out.println(userInputForPriority.toString());

				printInConversationHistory("Other",
						UserInput + "\n\n" + userInputforTerm.printTerm() + ", " + userInputForPriority.toString());

				// UI.appendToConversationHistory ("magenta", userInputforTerm.printTerm()
				// +"\n");
				// printInConversationHistory("Other", userInputforTerm.printTerm());

				this.ReqSet.addQueriedTerm(userInputforTerm, userInputForPriority, TermSatisfactionValues.SAT);

				this.ExpectedItem = ExpectedQueryItem.Req;
				getRequirementsSetFromUser();
			}

		} else {

			printInConversationHistory("Other", UserInput);
			// printInConversationHistory("Other", "exit");
			for (QueriedTerm q : this.ReqSet.getQuerySet())
				printInConversationHistory("Answer", q.printQueriedTerm());

			printInConversationHistory("Answer", "End Of Requirements Specification");
			QueryManager.executeQuery(this.ReqSet, ExploredRuleGraphs);
			QueryManager.findBestMatches(ExpandedRuleGraph);
		}

	}

	/*
	 * 
	 * ################ This version got the keywords for the requirements one by
	 * one. ################ It did not take a complete sentence. public void
	 * processRequirementsOldVersion(String UserInput) {
	 * 
	 * this.TheMostRecentUSerInput = UserInput;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {
	 * 
	 * 
	 * if (this.ExpectedItem == ExpectedQueryItem.Req) { UserInputForReq =
	 * UserInput; userInputforTerm = new Term();
	 * userInputforTerm.parseTerm(UserInputForReq); //
	 * UI.appendToConversationHistory ("magenta", userInputforTerm.printTerm()
	 * +"\n"); printInConversationHistory("Other", userInputforTerm.printTerm());
	 * this.ExpectedItem = ExpectedQueryItem.PRIORITY; getRequirementsSetFromUser();
	 * }
	 * 
	 * 
	 * 
	 * else if (this.ExpectedItem == ExpectedQueryItem.Type) {
	 * 
	 * UserInputForReqType = UserInput;
	 * 
	 * this.ExpectedItem = ExpectedQueryItem.Topic; //
	 * UI.appendToConversationHistory ("magenta", UserInputForReqType+"\n");
	 * printInConversationHistory("Other", UserInputForReqType);
	 * getRequirementsSetFromUser();
	 * 
	 * }
	 * 
	 * else if (this.ExpectedItem == ExpectedQueryItem.Topic) { UserInputForReqTopic
	 * = UserInput; userInputforTerm = new Term(UserInputForReqType,
	 * UserInputForReqTopic); // UI.appendToConversationHistory ("magenta",
	 * userInputforTerm.printTerm()+"\n"); printInConversationHistory("Other",
	 * userInputforTerm.printTerm()); this.ExpectedItem =
	 * ExpectedQueryItem.PRIORITY; getRequirementsSetFromUser();
	 * 
	 * }
	 * 
	 * else if (this.ExpectedItem == ExpectedQueryItem.PRIORITY) {
	 * userInputForPriority = TermPriority.valueOf(UserInput.toUpperCase());
	 * this.ExpectedItem = ExpectedQueryItem.SatVal;
	 * printInConversationHistory("Other", userInputForPriority.toString());
	 * getRequirementsSetFromUser(); }
	 * 
	 * else if (this.ExpectedItem == ExpectedQueryItem.SatVal) { this.ExpectedItem =
	 * ExpectedQueryItem.Req; userInputforExpectedSatsifaction =
	 * TermSatisfactionValues.valueOf(UserInput.toUpperCase());
	 * //userInputforExpectedSatsifaction = TermSatisfactionValues.SAT;
	 * this.ReqSet.addQueriedTerm(userInputforTerm, userInputForPriority,
	 * userInputforExpectedSatsifaction); // printInConversationHistory("Other",
	 * userInputforExpectedSatsifaction.toString()); getRequirementsSetFromUser(); }
	 * } else { printInConversationHistory("Other", "exit"); for (QueriedTerm q :
	 * this.ReqSet.getQuerySet()) printInConversationHistory("Answer",
	 * q.printQueriedTerm());
	 * 
	 * printInConversationHistory("Answer", "End Of Requirements Specification");
	 * QueryManager.executeQuery(this.ReqSet, ExploredRuleGraphs);
	 * QueryManager.findBestMatches(ExpandedRuleGraph); }
	 * 
	 * }
	 */

	public void getRequirementsSetFromUser() {
		/*
		 * 1- Get Requirements (Term) 2- Get priority of Requirement 3- Get Expected
		 * Satisfaction Value 4-Iterate until user enters exit
		 * 
		 * Assumption 1: User inputs are correct and they exist in the requirement list
		 * existing in the rule set.
		 */

		if (this.UserMode == UserMode.Exploration) {
			this.UserMode = UserMode.Query;
			printInConversationHistory("Question", "Please Specify Expected Requirements");
		}

		if (this.ExpectedItem == ExpectedQueryItem.Req)

		{
			printInConversationHistory("Question", "Requirement : ?");
		} else if (this.ExpectedItem == ExpectedQueryItem.Type)

		{
			UserInputForReqType = "";
			UserInputForReqTopic = "";
			userInputforTerm = new Term("", "");

			printInConversationHistory("Question", "Requirement Type: ?");
		}

		else if (this.ExpectedItem == ExpectedQueryItem.Topic)

			printInConversationHistory("Question", "Requirement Topic: ?");

		else if (this.ExpectedItem == ExpectedQueryItem.PRIORITY)
			printInConversationHistory("Question", "priority: ?");

		else if (this.ExpectedItem == ExpectedQueryItem.SatVal)
			// printInConversationHistory("Question", "Expected Satisfaction level: ?");
			processRequirements("SAT");

	}

	public void printInConversationHistory(String OutputType, String Output) {

		conversationNumber++;
		int conversationNumberCopy = conversationNumber;

		Color DarkBlueColor = new Color(0, 0, 204);
		Color LightGrayColor = new Color(110, 110, 110);

		if (OutputType == "Answer" || OutputType == "Question") {
			// Timer userInputforTerm = new Timer(1100, null);
			Timer t = new Timer(0, null);
			t.start();

			t.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					UI.appendToConversationHistory(DarkBlueColor,
							"\n" + conversationNumberCopy + ":  >>  RAPID:\n\n" + Output + "\n");
					t.stop();

				}
			});
		} else
			UI.appendToConversationHistory(LightGrayColor,
					"\n" + conversationNumber + ":  >>  Designer:\n\n" + Output + "\n");

		// conversationNumber++;

	}

	/*
	 * public void printInConversationHistory(String OutputType, String Output, int
	 * i) { if ((OutputType == "Answer" || OutputType == "Question") && i == -1) {
	 * 
	 * Timer userInputforTerm = new Timer(1000, null); userInputforTerm.start();
	 * 
	 * userInputforTerm.addActionListener(new ActionListener() {
	 * 
	 * public void actionPerformed(ActionEvent e) {
	 * UI.appendToConversationHistory("blue", "\n" + ">>  " + Output + "\n");
	 * userInputforTerm.stop();
	 * 
	 * } });
	 * 
	 * }
	 * 
	 * else if ((OutputType == "Answer" || OutputType == "Question") && i != -1)
	 * UI.appendToConversationHistory("blue", "\n" + ">>  " + Output + "\n");
	 * 
	 * else if (i == 0) { UI.showInConversationHistory("");
	 * UI.appendToConversationHistory("red", "\n" + "<<  " + Output + "\n"); }
	 * 
	 * else UI.appendToConversationHistory("red", "\n" + "<<  " + Output + "\n"); }
	 * }
	 */
	public void printInConversationHistory(String OutputType, String Output, int i) {

		conversationNumber++;
		int conversationNumberCopy = conversationNumber;

		Color LightGrayColor = new Color(110, 110, 110);

		Color DarkBlueColor = new Color(0, 0, 204);

		if ((OutputType == "Answer" || OutputType == "Question") && i == -1) {

			// Timer userInputforTerm = new Timer(1100, null);
			Timer t = new Timer(0, null);
			t.start();

			t.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					System.out.println(">>>>>>>>>>>>>>>" + Output);
					UI.appendToConversationHistory(DarkBlueColor,
							"\n" + conversationNumberCopy + ":  >>  RAPID: \n\n" + Output + "\n");
					t.stop();

				}
			});

		}

		else if ((OutputType == "Answer" || OutputType == "Question") && i != -1)
			UI.appendToConversationHistory(DarkBlueColor,
					"\n" + conversationNumber + ":  >>  RAPID:\n\n" + Output + "\n");

		else
			UI.appendToConversationHistory(LightGrayColor,
					"\n" + conversationNumber + ":  >>  Designer:\n\n" + Output + "\n");

	}

}
