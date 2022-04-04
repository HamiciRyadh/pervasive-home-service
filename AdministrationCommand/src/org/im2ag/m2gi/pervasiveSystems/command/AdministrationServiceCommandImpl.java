package org.im2ag.m2gi.pervasiveSystems.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.im2ag.m2gi.pervasiveSystems.administration.AdministrationServiceManager;
import org.im2ag.m2gi.pervasiveSystems.administration.security.AdministrationSecurityServiceManager;
import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityMode;

import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
//Create an instance of the component
@Instantiate(name = "pervasive.systems.administration.service.command")
//Use the handler command and declare the command as a command provider. The
//namespace is used to prevent name collision.
@CommandProvider(namespace = "admincommand")
public class AdministrationServiceCommandImpl {

	/** Field for followMeAdministration dependency */
	@Requires
	private AdministrationServiceManager administrationServiceManager;
	
	@Requires
	private AdministrationSecurityServiceManager administrationSecurityServiceManager;
	
    // Each command should start with a @Command annotation
    @Command
    public void setSecurityMode(String mode) {
        // The targeted goal
    	SecurityMode targetMode = SecurityMode.valueOf(mode.toUpperCase());
        
        //call the administration service to configure it :
    	administrationServiceManager.setSecurityMode(targetMode);
    }
    
    @Command
    public void getSecurityMode(){
        System.out.println("Current security mode: " + administrationServiceManager.getSecurityMode().name());
    }
 
    @Command
    public void stopSafetySirens(){
        System.out.println("Stopping the safety sirens ...");
        administrationServiceManager.stopSafetySirens();
    }
    
    @Command
    public void stopSecuritySirens(){
        System.out.println("Stopping the security sirens ...");
        administrationServiceManager.stopSecuritySirens();
    }
    
    @Command
    public void simulateIntrusion() {
    	System.out.println("Simulating an intruder ...");
    	administrationSecurityServiceManager.informOfIntrusion();
    }
    
    @Command
    public void simulateHealthHasard() {
    	System.out.println("Simulating a health hasard ...");
    	administrationSecurityServiceManager.informOfHealthHasard();
    }
}
