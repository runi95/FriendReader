package main;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import SteamReader.Friend;
import gui.SteamWindow;
import gui.SteamWindow.FrameCase;
import gui.WindowQueue;

public class Main {
	private static ArrayList<Friend> profileList = new ArrayList<Friend>();
	public final static String saveLocation = System.getProperty("user.home") + System.getProperty("file.separator") + "Documents" + System.getProperty("file.separator") + "SteamInfo";
	private final static File profiles = new File(saveLocation + System.getProperty("file.separator") + "profiles.txt");
	
	public static void main(String[] args) {
		System.out.println("Starting...");
		WindowQueue q = new WindowQueue();

		if((profileList = Manager.getProfiles(profiles, profileList)).isEmpty())
			throw new IllegalArgumentException("ERROR: Could not find any users to check, please add users @" + profiles.getAbsolutePath() + "!");
		
		//while(!checkConnection())
			
		System.out.println("Connection is fine!");
			
		for(Friend f : profileList) {
			ArrayList<Friend> gainedFriends = Manager.gainedFriends(f);
			ArrayList<Friend> lostFriends = Manager.lostFriends(f);
		 	for(Friend fr : gainedFriends) {
				q.add(new SteamWindow(fr.getName(), "has been added by ", f.getName(), FrameCase.ADDED, fr.getImage(), fr.getLink()));
			}
			for(Friend fr : lostFriends) {
				q.add(new SteamWindow(fr.getName(), "has been removed by ", f.getName(), FrameCase.REMOVED, fr.getImage(), fr.getLink()));
			}
			Manager.saveChanges(f, lostFriends, gainedFriends, Manager.unchanged(f));
		}

		while(q.getQueueSize() > 0) {
			q.run();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}// End of Main
	
	private static boolean checkConnection() {
		System.out.println("Checking connection...");
		Enumeration<NetworkInterface> eni = null;
		try {
			eni = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(eni.hasMoreElements()) {
            Enumeration<InetAddress> eia = eni.nextElement().getInetAddresses();
            while(eia.hasMoreElements()) {
                InetAddress ia = eia.nextElement();
                if (!ia.isAnyLocalAddress() && !ia.isLoopbackAddress() && !ia.isSiteLocalAddress()) {
                    if (!ia.getHostName().equals(ia.getHostAddress()))
                        return true;
                }
            }
        }
		return false;
	}
	
} //Main
