package test19;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.ISerialComDataListener;
import com.embeddedunveiled.serial.SerialComDataEvent;

class Data implements ISerialComDataListener{
	@Override
	public void onNewSerialDataAvailable(SerialComDataEvent data) {
		System.out.println("Read from serial port : " + new String(data.getDataBytes()) + "\n");
	}
}

public class Test19 {
	public static void main(String[] args) {
		
		long handle = 0;
		SerialComManager scm = new SerialComManager();
		
		// instantiate class which is will implement ISerialComDataListener interface
		Data dataListener = new Data();
		
		try {
			// open and configure port that will listen data
			handle = scm.openComPort("COM51", true, true, true);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			// open and configure port which will send data
			long handle1 = scm.openComPort("COM52", true, true, true);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			int x = 0;
			while(true) {
				System.out.println("1==" + x + scm.registerDataListener(handle, dataListener));
				Thread.sleep(100);
				scm.writeString(handle1, "T", 0);
				Thread.sleep(100);
				System.out.println("2" + scm.unregisterDataListener(dataListener));
				x++;
			}

			// close the port releasing handle
//			scm.closeComPort(handle);
//			scm.closeComPort(handle1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
