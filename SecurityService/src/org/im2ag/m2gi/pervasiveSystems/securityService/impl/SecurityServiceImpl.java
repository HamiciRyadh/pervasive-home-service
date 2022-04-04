package org.im2ag.m2gi.pervasiveSystems.securityService.impl;

import java.util.Map;

import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityConfiguration;
import org.im2ag.m2gi.pervasiveSystems.securityService.configuration.SecurityMode;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.doorWindow.DoorWindowSensor;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;
import fr.liglab.adele.icasa.device.security.Siren;
import org.im2ag.m2gi.pervasiveSystems.administration.security.AdministrationSecurityServiceManager;
import fr.liglab.adele.icasa.device.sound.Speaker;

public class SecurityServiceImpl implements DeviceListener, SecurityConfiguration {

	/** Field for sirens dependency */
	private Siren[] sirens;
	/** Field for presenceSensors dependency */
	private PresenceSensor[] presenceSensors;
	/** Field for doorWindowSensors dependency */
	private DoorWindowSensor[] doorWindowSensors;

	private SecurityMode mode = SecurityMode.ENABLED;

	/** Field for securityAdministration dependency */
	private AdministrationSecurityServiceManager securityAdministration;
	/** Field for speakers dependency */
	private Speaker[] speakers;

	/** Bind Method for presenceSensors dependency */
	public void bindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("Binding presence sensor N� " + presenceSensor.getSerialNumber());
		presenceSensor.addListener(this);
	}

	/** Unbind Method for presenceSensors dependency */
	public void unbindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("Unbinding presence sensor N� " + presenceSensor.getSerialNumber());
		presenceSensor.removeListener(this);
	}

	/** Bind Method for doorWindowSensors dependency */
	public void bindDoorWindowSensor(DoorWindowSensor doorWindowSensor, Map properties) {
		System.out.println("Binding door window sensor N� " + doorWindowSensor.getSerialNumber());
		doorWindowSensor.addListener(this);
	}

	/** Unbind Method for doorWindowSensors dependency */
	public void unbindDoorWindowSensor(DoorWindowSensor doorWindowSensor, Map properties) {
		System.out.println("Unbinding door window sensor N� " + doorWindowSensor.getSerialNumber());
		doorWindowSensor.removeListener(this);
	}

	/** Bind Method for sirens dependency */
	public void bindSiren(Siren siren, Map properties) {
		System.out.println("Binding siren N� " + siren.getSerialNumber());
		siren.addListener(this);
	}

	/** Unbind Method for sirens dependency */
	public void unbindSiren(Siren siren, Map properties) {
		System.out.println("Unbinding siren N� " + siren.getSerialNumber());
		siren.removeListener(this);
	}

	/** Bind Method for speakers dependency */
	public void bindSpeaker(Speaker speaker, Map properties) {
		speaker.addListener(this);
	}

	/** Unbind Method for speakers dependency */
	public void unbindSpeaker(Speaker speaker, Map properties) {
		speaker.removeListener(this);
	}
	
	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Security Service is stopping...");
		for (PresenceSensor ps : presenceSensors)
			ps.removeListener(this);
		for (DoorWindowSensor dws : doorWindowSensors)
			dws.removeListener(this);
		for (Siren s : sirens)
			s.removeListener(this);
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Security Service is starting...");
	}

	/* DeviceListener implementation */

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		System.out.println("Property changed: " + propertyName + ", old value: " + oldValue + ", new value: " + newValue);

		switch (mode) {
		case DISABLED: {
			handleDisabled();
			break;
		}
		case ENABLED: {
			handleEnabled(device, propertyName, oldValue, newValue);
			break;
		}
		case NIGHT: {
			handleNight(device, propertyName, oldValue, newValue);
			break;
		}
		case VACATION: {
			// In vacation mode the system acts as in enabled mode with the addition of a presence simulation
			// generated by a PresenceSimulatorService if available.
			handleEnabled(device, propertyName, oldValue, newValue);
			break;
		}
		}
	}

	private void handleDisabled() {
		// If it is disabled, make sure that all of the sirens are turned off then leave.
		stopSecuritySirens();
	}

	private void handleEnabled(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		// If it is enabled, use the presence sensors and door window sensors to detect presence and start the siren.
		if (device instanceof PresenceSensor) {
			final PresenceSensor changingSensor = (PresenceSensor) device;
			if (propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {
				// Detected a presence, start the alarm
				if (changingSensor.getSensedPresence()) {
					startSecuritySirens();
					this.securityAdministration.requestPolice();
				}
			}
		} else if (device instanceof DoorWindowSensor) {
			final DoorWindowSensor changingSensor = (DoorWindowSensor) device;
			if (propertyName.equals(DoorWindowSensor.DOOR_WINDOW_SENSOR_OPENING_DETECTCION)) {
				// Detected a presence, start the alarm
				if (changingSensor.isOpened()) {
					startSecuritySirens();
					this.securityAdministration.informOfIntrusion();
				}
			}
		}
	}

	private void handleNight(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		// During the night use only the door window sensors to let the inhabitants move around the house.
		if (device instanceof DoorWindowSensor) {
			final DoorWindowSensor changingSensor = (DoorWindowSensor) device;
			if (propertyName.equals(DoorWindowSensor.DOOR_WINDOW_SENSOR_OPENING_DETECTCION)) {
				// Detected a presence, start the alarm
				if (changingSensor.isOpened()) {
					startSecuritySirens();
					this.securityAdministration.informOfIntrusion();
				}
			}
		}
	}

	private void startSecuritySirens() {
		int nbSirenOn = 0;
		for (Siren siren : sirens) {
			if (!siren.getFault().equals(siren.FAULT_YES)) {
				siren.turnOn();
				nbSirenOn++;
			}
		}
		System.out.println("Number of sirens on: " + nbSirenOn);
		if (nbSirenOn == 0) {
			System.out.println("No working siren detected, using speakers instead.");
			for (Speaker speaker: speakers) {
				if (!speaker.getFault().equals(speaker.FAULT_YES)) {
					speaker.setVolume(100);
				}
			}
		}
	}
	
	@Override
	public void stopSecuritySirens() {
		for (Siren siren : sirens) {
			siren.turnOff();
		}
		for (Speaker speaker : speakers) {
			speaker.setVolume(0);
		}
	}

	@Override
	public void deviceAdded(GenericDevice arg0) {
	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
	}

	@Override
	public SecurityMode getSecurityMode() {
		return mode;
	}

	@Override
	public void setSecurityMode(SecurityMode mode) {
		if (mode != null)
			this.mode = mode;
	}
}