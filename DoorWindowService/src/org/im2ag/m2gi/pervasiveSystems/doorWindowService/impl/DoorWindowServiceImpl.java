package org.im2ag.m2gi.pervasiveSystems.doorWindowService.impl;

import org.im2ag.m2gi.pervasiveSystems.doorWindowService.administration.DoorWindowAdministration;

public class DoorWindowServiceImpl implements DoorWindowAdministration {

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Stopping DoorWindowService ...");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Starting DoorWindowService ...");
	}

	@Override
	public void openDoor() {
		System.out.println("Opening the door ...");
	}

	@Override
	public void startAeration() {
		System.out.println("Starting aeration ...");
	}

	@Override
	public void closeDoor() {
		System.out.println("Closing the door ...");
	}

	@Override
	public void stopAeration() {
		System.out.println("Stopping aeration ...");
	}

}
