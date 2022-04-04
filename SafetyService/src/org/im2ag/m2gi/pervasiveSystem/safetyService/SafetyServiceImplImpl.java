package org.im2ag.m2gi.pervasiveSystem.safetyService;

import java.util.Map;

import org.im2ag.m2gi.pervasiveSystem.safetyService.configuration.SafetyAdministration;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.gasSensor.CarbonDioxydeSensor;
import fr.liglab.adele.icasa.device.gasSensor.CarbonMonoxydeSensor;
import fr.liglab.adele.icasa.device.security.Siren;
import org.im2ag.m2gi.pervasiveSystems.administration.security.AdministrationSecurityServiceManager;

public class SafetyServiceImplImpl implements DeviceListener, SafetyAdministration {

	/** Field for carbonMonoxydeSensors dependency */
	private CarbonMonoxydeSensor[] carbonMonoxydeSensors;

	/** Field for carbonDioxydeSensors dependency */
	private CarbonDioxydeSensor[] carbonDioxydeSensors;

	/** Field for sirens dependency */
	private Siren[] sirens;

	/** Field for safetyAdministrator dependency */
	private AdministrationSecurityServiceManager safetyAdministrator;

	/** Bind Method for sirens dependency */
	public void bindSiren(Siren siren, Map properties) {
		siren.addListener(this);
	}

	/** Unbind Method for sirens dependency */
	public void unbindSiren(Siren siren, Map properties) {
		siren.removeListener(this);
	}

	/** Bind Method for carbonDioxydeSensors dependency */
	public void bindCarbonDioxydeSensor(CarbonDioxydeSensor carbonDioxydeSensor, Map properties) {
		carbonDioxydeSensor.addListener(this);
	}

	/** Unbind Method for carbonDioxydeSensors dependency */
	public void unbindCarbonDioxydeSensor(CarbonDioxydeSensor carbonDioxydeSensor, Map properties) {
		carbonDioxydeSensor.removeListener(this);
	}

	/** Bind Method for carbonMonoxydeSensors dependency */
	public void bindCarbonMonoxydeSensor(CarbonMonoxydeSensor carbonMonoxydeSensor, Map properties) {
		carbonMonoxydeSensor.addListener(this);
	}

	/** Unbind Method for carbonMonoxydeSensors dependency */
	public void unbindCarbonMonoxydeSensor(CarbonMonoxydeSensor carbonMonoxydeSensor, Map properties) {
		carbonMonoxydeSensor.removeListener(this);
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		if (device instanceof CarbonMonoxydeSensor) {
			if (propertyName.equals(CarbonMonoxydeSensor.CARBON_MONOXYDE_SENSOR_CURRENT_CONCENTRATION)) {
				if ((double) newValue > 55000) {
					System.out.println("Potential CO poisoning detected !");
					startSafetySirens();
					safetyAdministrator.informOfHealthHasard();
				}
			}
		} else if (device instanceof CarbonDioxydeSensor) {
			if (propertyName.equals(CarbonDioxydeSensor.CARBON_DIOXYDE_SENSOR_CURRENT_CONCENTRATION)) {
				if ((double) newValue > 1500000) {
					System.out.println("Potential CO2 poisoning detected !");
					startSafetySirens();
					safetyAdministrator.informOfHealthHasard();
				}
			}
		}
	}

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Safety Service is stopping ...");
		for (CarbonMonoxydeSensor carbonMonoxydeSensor : carbonMonoxydeSensors) {
			carbonMonoxydeSensor.removeListener(this);
		}
		for (CarbonDioxydeSensor carbonDioxydeSensor : carbonDioxydeSensors) {
			carbonDioxydeSensor.removeListener(this);
		}
		stopSafetySirens();
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Safety Service is starting ...");
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

	private void startSafetySirens() {
		for (Siren siren : sirens) {
			siren.turnOn();
		}
	}

	@Override
	public void stopSafetySirens() {
		for (Siren siren : sirens) {
			siren.turnOff();
		}
	}
}
