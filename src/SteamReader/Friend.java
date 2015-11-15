package SteamReader;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Main;

public class Friend {

protected ArrayList<Friend> friends = new ArrayList<>();
protected ArrayList<Friend> oldfriends = new ArrayList<>();
protected String name, id;
protected Image img = null;
	
	protected final String fileLocation = Main.saveLocation;
	public Friend(String id) {this.id = id;}
	public Friend(String name, String id) {this.name = name;this.id = id;}
	
	public String getName() { if(name != null) return name; else findSteamName(); return name; }
	public String getId() {return id;}
	public Image getImage() {if(img == null) {return getImageFromProfile();}else	return img;}
	public ArrayList<Friend> getFriends() {if(!friends.isEmpty()) return friends; else loadSteamFriendsFromProfile(); return friends;}
	public ArrayList<Friend> getOldFriends() {if(!oldfriends.isEmpty()) return oldfriends; else loadOldFriendsList(); return oldfriends;}
	
	private Image getImageFromProfile() {
		try{
			URL readUrl = new URL("http://steamcommunity.com/profiles/" + id + "/?xml=1");
			BufferedReader r = new BufferedReader(new InputStreamReader(readUrl.openStream()));
			String l;
			int line = 0;
			while((l = r.readLine()) != null) {
				line++;
				if(line == 8)
					return ImageIO.read(new URL(l.substring(22, l.lastIndexOf(']') - 1)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void loadSteamFriendsFromProfile() {
		friends.clear();
		try{
			URL url = new URL("http://steamcommunity.com/profiles/" + id + "/friends/?xml=1");
			BufferedReader r =  new BufferedReader(new InputStreamReader(url.openStream()));
			String l;
			while((l = r.readLine()) != null) {
				if(l.contains("<friend>"))
					friends.add(new Friend(l.substring(10, l.lastIndexOf('<'))));
			}
		}catch (Exception e) {
			
		}
	}
	
	public ArrayList<Friend> loadOldFriendsList() {
		oldfriends.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader( new File(fileLocation + System.getProperty("file.separator") + name + System.getProperty("file.separator") + "friends.txt")));
			
			String l;
			while((l = r.readLine()) != null) {
				if(l.length() > 0)
				if(l.charAt(0) != '#' && l.charAt(0) != '+' && l.charAt(0) != '-') {
					oldfriends.add(new Friend(l.substring(l.indexOf('/') + 1),l.substring(0,l.indexOf('/'))));
				}
			}
			r.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return oldfriends;
	}
	
	/**
	 * Gets the steam name of a user by his / her steamID.
	 * 
	 * @param steamID
	 * @return String - Current steam name
	 * @throws Exception
	 */
	public void findSteamName() {
		try{
		URL url = new URL("http://steamcommunity.com/profiles/" + id + "/?xml=1");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String inputLine;
		int line = 0;
		while((inputLine = in.readLine()) != null) {
			line++;
			if(inputLine.toLowerCase().contains("<steamID>".toLowerCase()))
				name = inputLine.substring(19, inputLine.indexOf(93));
			else if(line == 10) {
				img = ImageIO.read(new URL(inputLine.substring(22, inputLine.lastIndexOf(']') - 1))); break; }
		}
		in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public URI getLink() { try {
		return new URI("http://steamcommunity.com/profiles/" + id);
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	return null;
	}
	
	public String toString() { return "Player[name: " + name + ", id: " + id + "]"; }
	
}
