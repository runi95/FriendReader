package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.util.LinkedList;

import javax.swing.JComponent;

public class DrawComponent extends JComponent {
	private static final long serialVersionUID = -2647338112456598948L;
	LinkedList<ComponentProperties> list = null;
	
	public DrawComponent(LinkedList<ComponentProperties> list) {
		this.list = list;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getColor() != null)
				g2.setColor(list.get(i).getColor());
			else
				g2.setColor(new Color(255,255,255));
			if(list.get(i).getComponent() instanceof Image)
				g2.drawImage((Image) list.get(i).getComponent(), list.get(i).getX(), list.get(i).getY(), list.get(i).getWidth(), list.get(i).getHeight(), this);
			else if(list.get(i).getComponent() instanceof Shape) {
				g2.draw((Shape) list.get(i).getComponent());
				if(list.get(i).getFill())
					g2.fill((Shape) list.get(i).getComponent());
			}else if (list.get(i).getComponent() instanceof String) {
				if(list.get(i).getFont() != null)
					g2.setFont(list.get(i).getFont());
				g2.drawString((String) list.get(i).getComponent(), list.get(i).getX(), list.get(i).getY());
		}
	}}
	
	
}
