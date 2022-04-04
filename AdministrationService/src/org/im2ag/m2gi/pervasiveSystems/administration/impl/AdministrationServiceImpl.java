package org.im2ag.m2gi.pervasiveSystems.administration.impl;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.im2ag.m2gi.pervasiveSystem.safetyService.configuration.SafetyAdministration;
import org.im2ag.m2gi.pervasiveSystems.administration.AdministrationServiceManager;
import org.im2ag.m2gi.pervasiveSystems.administration.security.AdministrationSecurityServiceManager;
import org.im2ag.m2gi.pervasiveSystems.communicationService.configuration.CommunicationFunctionalities;
import org.im2ag.m2gi.pervasiveSystems.doorWindowService.administration.DoorWindowAdministration;
import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityConfiguration;
import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityMode;

public class AdministrationServiceImpl implements AdministrationServiceManager, AdministrationSecurityServiceManager {

	/** Field for securityConfiguration dependency */
	private SecurityConfiguration securityConfiguration;
	/** Field for communicationHandler dependency */
	private CommunicationFunctionalities communicationHandler;
	/** Field for safetyAdministration dependency */
	private SafetyAdministration safetyAdministration;
	/** Field for doorWindowAdministration dependency */
	private DoorWindowAdministration doorWindowAdministration;

	@Override
	public SecurityMode getSecurityMode() {
		return this.securityConfiguration.getSecurityMode();
	}

	@Override
	public void setSecurityMode(SecurityMode mode) {
		this.securityConfiguration.setSecurityMode(mode);
		this.sendRequest("mode", mode.name());
	}

	@Override
	public void stopSafetySirens() {
		this.safetyAdministration.stopSafetySirens();
	}

	@Override
	public void stopSecuritySirens() {
		this.securityConfiguration.stopSecuritySirens();
	}

	@Override
	public void requestPolice() {
		this.communicationHandler.callPolice();
	}

	@Override
	public void requestFiremen() {
		this.communicationHandler.callFiremen();
	}

	@Override
	public void requestAmbulance() {
		this.communicationHandler.callAmbulance();
	}

	@Override
	public void informOfIntrusion() {
		System.out.println("An intrusion has been detected ! Informing the police ...");
		this.communicationHandler.callPolice();
		this.sendRequest("SecurityHasard", "True");
	}

	@Override
	public void informOfHealthHasard() {
		System.out.println("A health hasard has been detected ! Starting aeratio. Calling the firemen and an ambulance ...");
		this.doorWindowAdministration.startAeration();
		this.communicationHandler.callFiremen();
		this.communicationHandler.callAmbulance();
		this.sendRequest("HealthHasard", "True");
	}

	private void sendRequest(String name, String val) {
		try {
			Desktop.getDesktop().browse(URI.create("http://localhost:4200/projects?"+name+"="+val));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
