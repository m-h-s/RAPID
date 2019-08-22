package user_interaction_component;
import java.awt.*;
import javax.swing.*;



public class GraphDrawer extends JFrame {
	
	
	public void InitializeFrame ()
	{
		setSize (1000,1000);
		setVisible(true);
		
	}
	
	public void drawNode (Graphics g, int x, int y, int nodeWidth, int nodeHeight, String text, int textAnchor)
	{
		g.drawOval(x, y, nodeWidth, nodeHeight);
		g.fillOval(x, y, nodeWidth+1, nodeHeight+1);
		g.drawString(text, x+nodeWidth+textAnchor, y + nodeHeight);
	}
	
	public void drawUpwardVerticalEdge (Graphics g, int x1, int y1, int length, int delta, String text, int textAnchor )
	{
		g.drawLine(x1, y1, x1, y1-length);
		g.drawLine(x1, y1-length, x1+delta, y1-length+delta);
		g.drawLine(x1-delta, y1-length+delta, x1, y1-length);
		g.drawString(text,  (x1+x1) / 2 + textAnchor, (y1+y1-length) / 2+ textAnchor);
	}
	
	public void drawUnaryRule (Graphics g, int startXPositionNode, int startYPositionNode, String LHS, String RHS, String RuleType)
	{
		int NodeWidth = 10;
		int NodeHeight = 10;
		int EdgeLength = 60;
		int ArrowSize =10;
		int SpaceBeforeText = 5;
		Font font = new Font("Arial", Font.BOLD, 14);
		g.setFont(font);
		drawNode(g, startXPositionNode,startYPositionNode,NodeWidth,NodeHeight,LHS,SpaceBeforeText);
		drawUpwardVerticalEdge (g, startXPositionNode+ (NodeWidth/2),startYPositionNode,EdgeLength, ArrowSize , RuleType,SpaceBeforeText);
		drawNode(g, startXPositionNode,startYPositionNode-EdgeLength-NodeHeight,NodeWidth,NodeHeight,RHS,SpaceBeforeText);
	}
	
	public void paint (Graphics g)
	{
		super.paint(g);
		
		int startXPositionNode= 500;
		int startYPositionNode =900;
		drawUnaryRule (g, startXPositionNode, startYPositionNode, "API-key", "Confidentiality", "Help");
		
		
		
		
		
		
		
	}
	

}
