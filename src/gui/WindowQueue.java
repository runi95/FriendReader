package gui;

import java.util.ArrayList;
import java.util.LinkedList;

public class WindowQueue {
	private ArrayList<SteamWindow> waiting;
	private LinkedList<SteamWindow> queue;
	private final int cooldown = SteamWindow.initiationTime;
	private int cooldownTimer;
	
	public WindowQueue() {
		queue = new LinkedList<SteamWindow>();
		waiting = new ArrayList<SteamWindow>();
	}

	public void run() {
		if((queue.size() < 2) && !waiting.isEmpty()) {
			addToQueue();
		}
		
		if(!queue.isEmpty())
		for(SteamWindow w : queue) {
			if(w.isAlive())
				w.run();
			else
				queue.remove(w);
		}
	}
	
	public void add(SteamWindow e) {
		waiting.add(e);
	}
	
	private void addToQueue() {
		if(cooldownTimer == 0) {
			cooldownTimer = cooldown;
			queue.addFirst(waiting.get(0));
			waiting.remove(0);
			if(queue.size() > 1)
				queue.get(1).setQueuePos();
			/*if(queue.size() > 2)
				queue.get(2).setQueuePos();*/
		}else
			cooldownTimer--;
	}
	
	public int getQueueSize() { return waiting.size() + queue.size(); }
}
