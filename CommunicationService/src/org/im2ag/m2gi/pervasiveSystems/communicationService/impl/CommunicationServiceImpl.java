package org.im2ag.m2gi.pervasiveSystems.communicationService.impl;

import org.im2ag.m2gi.pervasiveSystems.communicationService.configuration.CommunicationFunctionalities;

public class CommunicationServiceImpl implements CommunicationFunctionalities {

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Communication Service is starting...");
	}
	
	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Communication Service is stopping...");
	}
	
	@Override
	public void callPolice() {
		System.out.println("Calling police ...");
	}

	@Override
	public void callFiremen() {
		System.out.println("Calling firemen ...");
	}

	@Override
	public void callAmbulance() {
		System.out.println("Calling ambulance ...");
	}
}
