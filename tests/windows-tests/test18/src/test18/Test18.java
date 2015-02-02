package test18;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.ISerialComEventListener;
import com.embeddedunveiled.serial.SerialComLineEvent;

class EventListener implements ISerialComEventListener{
	@Override
	public void onNewSerialEvent(SerialComLineEvent lineEvent) {
		System.out.println("eventCTS : " + lineEvent.getCTS());
		System.out.println("eventDSR : " + lineEvent.getDSR());
	}
}

// Custom baud rate setting and register/unregister listener many times
public class Test18 {
	public static void main(String[] args) {
		
		long handle = 0;
		SerialComManager scm = new SerialComManager();
		
		// instantiate class which is will implement ISerialComEventListener interface
		EventListener eventListener = new EventListener();
		
		try {
			handle = scm.openComPort("COM51", true, true, true);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.BCUSTOM, 512000);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			System.out.println("" + scm.registerLineEventListener(handle, eventListener));
			
			long handle1 = scm.openComPort("COM52", true, true, true);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.BCUSTOM, 512000);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			Thread.sleep(1000);
			scm.setDTR(handle1, true);
			Thread.sleep(1000);
			scm.setRTS(handle1, true);
			Thread.sleep(1000);
			scm.setDTR(handle1, false);
			Thread.sleep(1000);
			
			// unregister data listener
			System.out.println("" + scm.unregisterLineEventListener(eventListener));
			//Thread.sleep(1000);
			
			System.out.println("" + scm.registerLineEventListener(handle, eventListener));
			Thread.sleep(1000);
			scm.setDTR(handle1, true);
			Thread.sleep(1000);
			scm.setRTS(handle1, true);
			Thread.sleep(1000);
			
			System.out.println("" + scm.unregisterLineEventListener(eventListener));
			Thread.sleep(1000);
			
			// close the port releasing handle
			scm.closeComPort(handle);
			scm.closeComPort(handle1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
