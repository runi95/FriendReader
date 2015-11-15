package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

public class SteamWindow extends JWindow {
	
	private static final long serialVersionUID = 5252175026468127008L;
	public enum  FrameCase {
		NEUTRAL, ADDED, REMOVED
	}
	
	public enum KillState {
		FADE, MOVEOUT, FLYDOWN
	}
	
	private final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private final int screenWidth = gd.getDisplayMode().getWidth(), screenHeight = gd.getDisplayMode().getHeight();
	private final int windowWidth = 150 + (int) (screenWidth*0.08), windowHeight = 50 + (int) (screenHeight*0.08);
	private final KillState killState = KillState.FLYDOWN;
	public final static int initiationTime = 100;
	private int life = 800, initiation = initiationTime;
	private boolean alive = true;
	private int x, y, queuePos, targetQueue;
	private LinkedList<ComponentProperties> list = new LinkedList<ComponentProperties>();
	
	Image img = null;
	
	public SteamWindow(String titletxt, String info, String name, FrameCase fcase, Image img, URI uri) {
		super();
		init(titletxt, info, name, fcase, img, uri);
	}
	
	public SteamWindow(String titletxt, String info, FrameCase fcase, Image img, URI uri) {
		super();
		init(titletxt, info, null, fcase, img, uri);
	}
	
	private void init(String titletxt, String info, String name, FrameCase fcase, Image img, URI uri) {
		this.img = img;
		this.setSize(windowWidth, windowHeight);
		ComponentProperties background = null;
		switch(fcase) {
		case NEUTRAL:
			background = new ComponentProperties(new Rectangle(windowWidth, windowHeight), new Color(225,225,225), true);
			break;
		case ADDED:
			background = new ComponentProperties(new Rectangle(windowWidth, windowHeight), new Color(25,255,25), true);
			break;
		case REMOVED:
			background = new ComponentProperties(new Rectangle(windowWidth, windowHeight), new Color(255,25,25), true);
			break;
		}
		Rectangle boxRec = new Rectangle(windowWidth-10, windowHeight-7);
		boxRec.setLocation(5, 3);
		ComponentProperties box = new ComponentProperties(boxRec, new Color(25,25,25), true);
		ComponentProperties pic = new ComponentProperties(img, null, (int) (windowWidth*0.1), (int)(windowHeight*0.5) - 25, 50, 50);
		ComponentProperties title = new ComponentProperties(titletxt, new Color(255,255,255),(int) (windowWidth*0.1) , (int) (windowHeight*0.2), new Font("Lao UI", Font.BOLD, 15));
		list.add(background);
		list.add(box);
		String[] split = info.split(" ");
		String string = "";
		int lineCounter = 1;
		for(int i = 0; i < split.length; ++i) {
			if(string.length() + split[i].length() + i < 35)
				string = string + " " + split[i];
			else {
				list.add(new ComponentProperties(string, new Color(255,255,255),(int) (windowWidth*0.15) + 50 , 15*lineCounter + (int)(windowHeight*0.4), new Font("Lao UI", Font.BOLD, 13)));
				lineCounter++;
				string = split[i];
				}
			if(i == split.length - 1) {
				list.add(new ComponentProperties(string, new Color(255,255,255),(int) (windowWidth*0.15) + 50 ,15*lineCounter + (int)(windowHeight*0.4), new Font("Lao UI", Font.BOLD, 13))); lineCounter++; }
		}
		list.add(new ComponentProperties(name, new Color(225,225,75),(int) (windowWidth*0.15) + 50 ,15*lineCounter + (int)(windowHeight*0.4), new Font("Lao UI", Font.BOLD + Font.ITALIC, 13)));
		list.add(pic);
		list.add(title);
		this.add(new DrawComponent(list));
		this.setAlwaysOnTop(true);
		this.validate();
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1) {
					try {
						java.awt.Desktop.getDesktop().browse(uri);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}}});
	}
	
	/*public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(img, 25, 50, 50, 50, this);
	}*/

	public Image getImage(String urlPath) {
		Image tempImage = null;
		try {
			tempImage = ImageIO.read(new URL(urlPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tempImage;
	}
	
	public void run() {	
		if(initiation > 0)
			initiate();
		else if(initiation == 0 && life > 0)
			mainEvent();
		else if(life <= 0)
			kill();
	}
	
	private void mainEvent() {
		if(queuePos > 0)
			queueMove();
		if(life > 0)
			life--;
	}
	
	private void initiate() {
		int target = screenWidth - windowWidth;
		if(initiation == initiationTime)
			first();
		this.setLocation(getX() + (target - screenWidth)/100, y);
		initiation--;
	}
	
	private void kill() {
		if(life > -199) {
			switch(killState) {
			case FADE:
				this.setOpacity((float)(this.getOpacity() - 0.005));
				break;
			case MOVEOUT:
				this.setLocation(x + (int)(Math.ceil(windowWidth*0.01)), y);
				break;
			case FLYDOWN:
				this.setLocation(x, y + (int)(Math.ceil(windowHeight*0.04)));
			}
			life--;
		}else
			destroy();
	}
	
	private void destroy() {
		this.setVisible(false);
		alive = false;
		this.dispose();
	}
	
	private void first() {
		this.alive = true;
		this.setVisible(true);
		this.setLocation(screenWidth, screenHeight - (int)(screenHeight*0.7));
	}
	
	private void queueMove() {
		this.setLocation(getX(),(int) (getY() + ((targetQueue - getY())/queuePos)));
		//this.setLocationRelative(0,0.01);
		queuePos--;
	}
	
	public void setLocationRelative(double x, double y) {
		setLocation(getX() - (int)(windowWidth*x), getY() + (int)(windowHeight*y));
	}
	
	@Override
	public void setLocation(int x, int y) { super.setLocation(x, y); this.x = x; this.y = y; }
	public void setQueuePos() { this.targetQueue = this.getY() + windowHeight; this.queuePos += 100; }
	public int getX() { return x; }
	public int getY() { return y; }
	public boolean isAlive() { return alive; }
	
}
