package user_interaction_component;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.lang.reflect.Field;
import java.awt.event.ActionEvent;
import javax.swing.text.*;

public class UserInterface extends JFrame {

	private UserInteractionManager Controller;

	private JTextField DesignerQuery;
	private JEditorPane ConversationHistory;

	private JEditorPane InfoPane;

	public UserInterface() {

		getContentPane().setLayout(null);

		JLabel lblDesignQuery = new JLabel("Design Query");
		lblDesignQuery.setForeground(new Color(128, 128, 128));
		lblDesignQuery.setFont(new Font("Calibri", Font.BOLD, 22));
		lblDesignQuery.setBounds(35, 747, 164, 34);
		getContentPane().add(lblDesignQuery);

		DesignerQuery = new JTextField();
		DesignerQuery.setBounds(35, 785, 790, 155);
		DesignerQuery.setFont(new Font("Consolas", Font.PLAIN, 18));
		Color color = new Color(0, 0, 0);
		DesignerQuery.setForeground(color);
		getContentPane().add(DesignerQuery);
		DesignerQuery.setColumns(10);

		JLabel lblConversationHistory = new JLabel("Conversation History");
		lblConversationHistory.setForeground(new Color(110, 110, 110));
		lblConversationHistory.setFont(new Font("Calibri", Font.BOLD, 22));
		lblConversationHistory.setBounds(35, 25, 259, 25);
		getContentPane().add(lblConversationHistory);

		ConversationHistory = new JTextPane();
		ConversationHistory.setEditable(false);
		ConversationHistory.setContentType("Text");
		ConversationHistory.setBounds(35, 52, 3000, 3000);
		ConversationHistory.setFont(new Font("Consolas", Font.PLAIN, 17));
		Color DarkBlueColor = new Color(25, 25, 112);
		ConversationHistory.setForeground(DarkBlueColor);
		getContentPane().add(ConversationHistory);

		JScrollPane CVScrollPane = new JScrollPane(ConversationHistory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		CVScrollPane.setVisible(true);
		CVScrollPane.setBounds(35, 58, 890, 675);
		getContentPane().add(CVScrollPane);

		InfoPane = new JEditorPane();
		InfoPane.setEditable(false);
		InfoPane.setBounds(10, 13, 2000, 651);
		getContentPane().add(InfoPane);
		InfoPane.setFont(new Font("Consolas", Font.PLAIN, 17));
		InfoPane.setForeground(Color.BLACK);

		JScrollPane InfoPaneScrollPane = new JScrollPane(InfoPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		InfoPaneScrollPane.setBounds(941, 58, 710, 882);
		InfoPaneScrollPane.setVisible(true);
		getContentPane().add(InfoPaneScrollPane);

		JButton QuerySubmitted = new JButton("Submit");
		QuerySubmitted.setForeground(new Color(70, 70, 70));
		QuerySubmitted.setFont(new Font("Consolas", Font.BOLD, 17));
		QuerySubmitted.setBackground(UIManager.getColor("Button.background"));
		QuerySubmitted.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String UserInput = DesignerQuery.getText();
				DesignerQuery.setText("");

				SendUserInputToController(UserInput);
			}
		});

		QuerySubmitted.setBounds(835, 785, 91, 154);
		getContentPane().add(QuerySubmitted);

		JLabel lblInfoPane = new JLabel("Explanations");
		lblInfoPane.setForeground(new Color(110, 110, 110));
		lblInfoPane.setFont(new Font("Calibri", Font.BOLD, 22));
		lblInfoPane.setBounds(941, 27, 150, 25);
		getContentPane().add(lblInfoPane);

	}

	public void InitializeUI() {

		this.setVisible(true);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.getContentPane().setBackground(new Color(224, 224, 224));
		this.setResizable(false);
	}

	public void setController(UserInteractionManager Cont) {
		this.Controller = Cont;
	}

	public void SendUserInputToController(String UserInput) {
		this.Controller.processUserInput(UserInput);
	}

	public void showInConversationHistory(String ExpansionHistory) {
		ConversationHistory.setText(ExpansionHistory);

	}

	public void appendToInfoPane(String output) {
		InfoPane.setText(InfoPane.getText() + output);
	}

	public void appendToConversationHistory(String ColorString, String output) {
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(ColorString);
			color = (Color) field.get(null);
		} catch (Exception e) {
			color = null;
		}

		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, color);
		Document doc = ConversationHistory.getDocument();
		try {
			doc.insertString(doc.getLength(), output, attributeSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void appendToConversationHistory(Color color, String output) {
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, color);
		Document doc = ConversationHistory.getDocument();
		try {
			doc.insertString(doc.getLength(), output, attributeSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
