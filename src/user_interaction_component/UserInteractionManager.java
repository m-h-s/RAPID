package user_interaction_component;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;
import reasoner_component.Constraint;
import reasoner_component.ConstraintSet;
import reasoner_component.Recommender;
import rule_explorer_component.RuleExplorer;
import data_structures.*;

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

public class UserInteractionManager {

	private UserInterface UI;
	private RuleExplorer ruleExplorer;
	private Recommender recommender;

	private UserMode userMode;

	private ExpectedConstraintItem expectedItem;

	private ConstraintSet reqSet;

	private String UserInputForReqType;
	private String UserInputForReqTopic;
	private String UserInputForReq;

	private Term userInputforTerm;
	private TermPriority userInputForPriority;
	private TermSatisfactionValues userInputforExpectedSatsifaction;

	List<ListRuleSet> exploredRuleGraphs;
	ListRuleSet workingRuleSet;

	private String TheMostRecentUSerInput;

	static int conversationNumber;

	private String greetingMessage = "Hello! I am a design assistant.\n\n";

	private String commandMessage = "Please tell me how I can help:\n\n"
			+ "(a) to figure out a design solution, (b) to analyze a design solution, "
			+ "\n\n(c) to evaluate a design solution, or (d) to recommend a design solution.";

	private String designMessage = "I am in the design mode.\n\n";

	private String requirementSpecificationMessage = "Please specify the design requirements:";

	private String analysisMessage = "I am in the analysis mode.\n\n";

	private String analysisSpecificationMessage = "Please specify the design solution that I should analyze:";

	private String noAnswerMessage = " I cannot respond to this query.\n\n";

	private String RequirementsSpecificationMessage = "Please identify the important non-fucntioanl requirements \n\n"
			+ "and their priority one by one. \n\n";

	public UserInteractionManager() {
		UI = new UserInterface();
		UI.InitializeUI();
		UI.setController(this);


		ruleExplorer = new RuleExplorer(this);
		recommender = new Recommender(this);



		this.expectedItem = ExpectedConstraintItem.Req;
		this.reqSet = new ConstraintSet();
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
		UserInput = UserInput.toLowerCase().trim();

		if (Objects.equals(UserInput, "exit")) {
			if (this.userMode != userMode.RECOM) {
				printInConversationHistory("Other", UserInput);
				printInConversationHistory("Question", commandMessage);
			}

			else {
				ruleExplorer.resetRuleGraph();
				processPreferences(UserInput);
				printInConversationHistory("Question", commandMessage);
			}
		}

		else if (Objects.equals(UserInput, "figure out")) {
			this.userMode = userMode.EXPLOR;
			printInConversationHistory("Other", UserInput);
			printInConversationHistory("Question", designMessage + requirementSpecificationMessage);
		}


		else if (Objects.equals(UserInput, "analyze")) {
			printInConversationHistory("Other", UserInput);
			this.userMode = userMode.ANALYZ;
			printInConversationHistory("Question", analysisSpecificationMessage);
		}

		else if (Objects.equals(UserInput, "recommend")) {
			printInConversationHistory("Other", UserInput);
			this.userMode = userMode.RECOM;
			ruleExplorer.exploreExpandedRuleGraphOneByOne();
			exploredRuleGraphs = ruleExplorer.getExploredRuleGraphs();
			workingRuleSet = ruleExplorer.getExpandedRuleGraph();
			printInConversationHistory("Question", RequirementsSpecificationMessage);
			getConstraintsSetFromUser();
		} 
		else if (this.userMode == userMode.EXPLOR)
			exploreRuleBase(UserInput);

		else if (this.userMode == userMode.RECOM)
			processPreferences(UserInput);

		else if (this.userMode == userMode.ANALYZ) {
			printInConversationHistory("Other", UserInput);
			ruleExplorer.analyzeSourceTermsByUser(UserInput);
			printInConversationHistory("Question", analysisMessage + analysisSpecificationMessage);
		}

		else {
			printInConversationHistory("Other", UserInput);
			printInConversationHistory("Answer", noAnswerMessage);
		}

	}

	public void exploreRuleBase(String UserInput) {

		TheMostRecentUSerInput = UserInput;
		ruleExplorer.getUserInputFromController(UserInput);

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

	public void receiveAnswerFromExplorer(String AnswerName, ArrayList<String> Answer) {
		if (AnswerName == "RuleGraphExpansionHistory" && !Answer.isEmpty()) {
			for (int i = 0; i < Answer.size(); i++)
				if (i % 2 == 0)
					printInConversationHistory("Other", Answer.get(i));

				else
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

	public void processPreferences(String UserInput) {

		this.TheMostRecentUSerInput = UserInput;

		if (!UserInput.replaceAll("\\s", "").toLowerCase().contains("exit")) {

			if (this.expectedItem == ExpectedConstraintItem.Req) {
				UserInputForReq = UserInput;
				String[] SplitUserInput = UserInput.toLowerCase().split(" is ");
				userInputforTerm = new Term();
				String[] splitInputTerm;
				splitInputTerm = SplitUserInput[0].split(" of the ");
				userInputforTerm = new Term(splitInputTerm[0], splitInputTerm[1]);


				String[] splitInputPriority = SplitUserInput[1].toLowerCase().split("\\.");


				if (Objects.equals(splitInputPriority[0], "very critical"))

				{
					userInputForPriority = TermPriority.HIGH;
				}

				else if (Objects.equals(splitInputPriority[0], "critical"))
					userInputForPriority = TermPriority.MEDIUM;

				else if (Objects.equals(splitInputPriority[0], "important"))
					userInputForPriority = TermPriority.LOW;

				printInConversationHistory("Other",
						UserInput + "\n\n" + userInputforTerm.printTerm() + ", " + userInputForPriority.toString());


				this.reqSet.addConstraint(userInputforTerm, userInputForPriority, TermSatisfactionValues.SAT);

				this.expectedItem = ExpectedConstraintItem.Req;
				getConstraintsSetFromUser();
			}

		} else {

			printInConversationHistory("Other", UserInput);
			
			for (Constraint q : this.reqSet.getConstraints())
				printInConversationHistory("Answer", q.printQueriedTerm());

			printInConversationHistory("Answer", "End Of Requirements Specification");
			recommender.matchConstraints(this.reqSet, exploredRuleGraphs);
			recommender.findBestMatches(workingRuleSet);
		}

	}

	public void getConstraintsSetFromUser() {
		/*
		 * 1- Get Requirements (Term) 2- Get priority of Requirement 3- Get Expected
		 * Satisfaction Value 4-Iterate until user enters exit
		 * 
		 * Assumption 1: User inputs are correct and they exist in the requirement list
		 * existing in the rule set.
		 */

		if (this.userMode == userMode.EXPLOR) {
			this.userMode = userMode.RECOM;
			printInConversationHistory("Question", "Please Specify Expected Requirements");
		}

		if (this.expectedItem == ExpectedConstraintItem.Req)

		{
			printInConversationHistory("Question", "Requirement : ?");
		} else if (this.expectedItem == ExpectedConstraintItem.Type)

		{
			UserInputForReqType = "";
			UserInputForReqTopic = "";
			userInputforTerm = new Term("", "");

			printInConversationHistory("Question", "Requirement Type: ?");
		}

		else if (this.expectedItem == ExpectedConstraintItem.Topic)

			printInConversationHistory("Question", "Requirement Topic: ?");

		else if (this.expectedItem == ExpectedConstraintItem.PRIORITY)
			printInConversationHistory("Question", "priority: ?");

		else if (this.expectedItem == ExpectedConstraintItem.SatVal)
			processPreferences("SAT");

	}

	public void printInConversationHistory(String OutputType, String Output) {

		conversationNumber++;
		int conversationNumberCopy = conversationNumber;

		Color DarkBlueColor = new Color(0, 0, 100);
		Color Black = new Color (1,1,1);
		Color LightGrayColor = new Color(110, 110, 110);

		if (OutputType == "Answer" || OutputType == "Question") {
			Timer userInputforTerm = new Timer(1100, null);
			Timer t = new Timer(1000, null);
			t.start();

			t.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					UI.appendToConversationHistory(DarkBlueColor,
							"\n" + conversationNumberCopy + ":  >>  RAPID:\n\n" + Output + "\n");
					t.stop();

				}
			});
		} else
			UI.appendToConversationHistory(Black,
					"\n" + conversationNumber + ":  >>  Designer:\n\n" + Output + "\n");

	}

	public void printInConversationHistory(String OutputType, String Output, int i) {

		conversationNumber++;
		int conversationNumberCopy = conversationNumber;

		Color LightGrayColor = new Color(110, 110, 110);

		Color Black = new Color (1,1,1);

		Color DarkBlueColor = new Color(0, 0, 100);

		if ((OutputType == "Answer" || OutputType == "Question") && i == -1) {

			Timer userInputforTerm = new Timer(1100, null);
			Timer t = new Timer(1000, null);
			t.start();

			t.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
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
			UI.appendToConversationHistory(Black,
					"\n" + conversationNumber + ":  >>  Designer:\n\n" + Output + "\n");

	}

}
