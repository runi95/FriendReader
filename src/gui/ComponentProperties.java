package gui;

import java.awt.Color;
import java.awt.Font;

public class ComponentProperties {
	private Color color = null;
	private Object component = null;
	private int x, y, width, height;
	private Font font = null;
	private boolean fill = false;
	
	public ComponentProperties(Object component) { this.component = component; }
	public ComponentProperties(Object component, Color clr) { this.component = component; this.color = clr; }
	public ComponentProperties(Object component, Color clr, boolean fill) { this.component = component; this.color = clr; this.fill = fill; }
	public ComponentProperties(Object component, Color clr, int x, int y) { this.component = component; this.color = clr; this.x = x; this.y = y; }
	public ComponentProperties(Object component, boolean fill, int x, int y) { this.component = component; this.fill = fill; this.x = x; this.y = y; }
	public ComponentProperties(Object component, Color clr, int x, int y, Font font) { this.component = component; this.color = clr; this.x = x; this.y = y; this.font = font; }
	public ComponentProperties(Object component, Color clr, boolean fill, int x, int y) { this.component = component; this.color = clr; this.fill = fill; this.x = x; this.y = y;}
	public ComponentProperties(Object component, Color clr, int x, int y, int width, int height) { this.component = component; this.color = clr; this.x = x; this.y = y; this.width = width; this.height = height; }
	
	public Color getColor() { return color; }
	public Object getComponent() { return component; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Font getFont() { return font; }
	public boolean getFill() { return fill; }
}
