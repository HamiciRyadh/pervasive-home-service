package org.im2ag.m2gi.pervasiveSystems.securityService.configuration;

public interface SecurityConfiguration {
	
	public SecurityMode getSecurityMode();
	
	public void setSecurityMode(SecurityMode mode);
	
	public void stopSecuritySirens();
}
