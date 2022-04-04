package org.im2ag.m2gi.pervasiveSystems.administration;

import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityMode;

public interface AdministrationServiceManager {
	
	public SecurityMode getSecurityMode();
	
	public void setSecurityMode(SecurityMode mode);
	
	public void stopSafetySirens();
	
	public void stopSecuritySirens();

}
