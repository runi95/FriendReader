package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import SteamReader.Friend;

public class Manager {
	
	/**
	 * Checks if there's readable content on the profiles.txt.
	 * 
	 * @return true if it's readable and false if it's not
	 */
	public static ArrayList<Friend> getProfiles(File profiles, ArrayList<Friend> profileList) {
		profilesCheck(profiles);
		if(profiles.exists()) {
			try{
			BufferedReader r = new BufferedReader(new FileReader(profiles));
			String inputLine;
			while((inputLine = r.readLine()) != null) {
				if(inputLine.length() > 0)
				if(inputLine.charAt(0) != '#')
					profileList.add(getProfile(inputLine));
			}
			r.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			if(profileList.isEmpty())
				return profileList;
			else
				return profileList;
		}
		return profileList;
	}
	
	/**
	 * Checks if the profiles.txt file exists.
	 */
	public static void profilesCheck(File profiles) {
		if(!profiles.exists()) {
			try{
				BufferedWriter w = new BufferedWriter(new FileWriter(profiles));
				w.write("#How to add users to this list: Simply add their URL here, ex:\n#http://steamcommunity.com/id/<their id here>\n#Or for those without a custom id:\n#http://steamcommunity.com/profiles/<steam id number here>\n#Disclaimer: This does not work for profiles with the private setting activated!");
				w.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected static Friend getProfile(String link) {
		String id = null, name = null;
		try{
		URL url = new URL(link + "/?xml=1");
		BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
		
		int line = 0;
		String l;
		while((l = r.readLine()) != null) {
			if(line == 1)
				id = l.substring(12, l.lastIndexOf('<'));
			if(line == 2) {
				name = l.substring(19, l.lastIndexOf(']') - 1); break; }
			line++;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new Friend(name, id);
	}
	
	public static ArrayList<Friend> lostFriends(Friend f) {
		ArrayList<Friend> temp = new ArrayList<Friend>();
		boolean flag;
		for(Friend friend : f.getOldFriends()) {
			flag = false;
			for(Friend fr : f.getFriends()) {
				if(fr.getId().equals(friend.getId()))
					flag = true;
			}
			if(!flag)
				temp.add(friend);
		}
		
		return temp;
	}
	
	public static ArrayList<Friend> gainedFriends(Friend f) {
		ArrayList<Friend> temp = new ArrayList<Friend>();
		
		boolean flag;
		for(Friend friend : f.getFriends()) {
			flag = false;
			for(Friend fr : f.getOldFriends()) {
				if(fr.getId().equals(friend.getId()))
					flag = true;
			}
			if(!flag)
				temp.add(friend);
		}
		
		return temp;
	}
	
	public static void saveChanges(Friend f, ArrayList<Friend> lostFriends, ArrayList<Friend> gainedFriends, ArrayList<Friend> unchanged) {
		try{
			BufferedReader r = new BufferedReader(new FileReader(new File(Main.saveLocation + System.getProperty("file.separator") + f.getName() + System.getProperty("file.separator") + "friends.txt")));
			ArrayList<String> readLines = new ArrayList<String>();
			String l;
			while((l = r.readLine()) != null) {
				if(l.length() > 0)
				if(l.charAt(0) == '-' || l.charAt(0) == '+')
					readLines.add(l);
			}
			r.close();
			BufferedWriter w = new BufferedWriter(new FileWriter(new File(Main.saveLocation + System.getProperty("file.separator") + f.getName() + System.getProperty("file.separator") + "friends.txt")));
			w.write("#Current friend's of " + f.getName() + ":\n");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			for(Friend friend : unchanged) {
				w.write(friend.getId() + "/" + friend.getName() + "\n");
			}
			for(Friend friend : gainedFriends) {
				w.write(friend.getId() + "/" + friend.getName() + "\n");
			}
			w.write("\n#Removed friends:\n");
			for(Friend friend : lostFriends) {
				w.write("-" + friend.getId() + "/" + friend.getName() + "\t" + dateFormat.format(date) + "\n");
			}
			w.write("\n#Added friends:\n");
			for(Friend friend : gainedFriends) {
				w.write("+" + friend.getId() + "/" + friend.getName() + "\t" + dateFormat.format(date) + "\n");
			}
			w.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Friend> unchanged(Friend f) {
		ArrayList<Friend> unchanged = new ArrayList<Friend>();
		
		for(Friend friend : f.getOldFriends()) {
			for(Friend fr : f.getFriends()) {
				if(fr.getId().equals(friend.getId())) {
					unchanged.add(friend);
					break;
				}
			}
		}
		
		return unchanged;
	}
}
