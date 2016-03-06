/**
 * Author : Rishi Gupta
 * 
 * This file is part of 'serial communication manager' library.
 * Copyright (C) <2014-2016>  <Rishi Gupta>
 *
 * This 'serial communication manager' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The 'serial communication manager' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with 'serial communication manager'.  If not, see <http://www.gnu.org/licenses/>.
 */

package test12;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.ENDIAN;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.NUMOFBYTES;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;

public class Test12 {
	public static void main(String[] args) {
		try {
			SerialComManager scm = new SerialComManager();

			String PORT = null;
			String PORT1 = null;
			int osType = scm.getOSType();
			if(osType == SerialComManager.OS_LINUX) {
				PORT = "/dev/ttyUSB0";
				PORT1 = "/dev/ttyUSB1";
			}else if(osType == SerialComManager.OS_WINDOWS) {
				PORT = "COM51";
				PORT1 = "COM52";
			}else if(osType == SerialComManager.OS_MAC_OS_X) {
				PORT = "/dev/cu.usbserial-A70362A3";
				PORT1 = "/dev/cu.usbserial-A602RDCH";
			}else if(osType == SerialComManager.OS_SOLARIS) {
				PORT = null;
				PORT1 = null;
			}else{
			}

			// open and configure port that will listen data
			long handle = scm.openComPort(PORT, true, true, false);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);

			// open and configure port which will send data
			long handle1 = scm.openComPort(PORT1, true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);

			// 350 = 00000001 01011110, 2 bytes required, big endian format
			scm.writeSingleInt(handle, 350, 0, ENDIAN.E_BIG, NUMOFBYTES.NUM_2);
			Thread.sleep(100);
			byte[] arr = new byte[2];
			arr = scm.readBytes(handle1, 2);
			System.out.println("dataa: " + arr[0]); // prints 1  which is 00000001
			System.out.println("datab: " + arr[1]); // prints 94 which is 01011110

			// 350 = 00000001 01011110, 2 bytes required, little endian format
			scm.writeSingleInt(handle, 350, 0, ENDIAN.E_LITTLE, NUMOFBYTES.NUM_2);
			Thread.sleep(100);
			byte[] arr1 = new byte[2];
			arr1 = scm.readBytes(handle1, 2);
			System.out.println("dataa: " + arr1[0]); // prints 94 which is 01011110
			System.out.println("datab: " + arr1[1]); // prints 1  which is 00000001

			// 7050354 = 00000000 01101011 10010100 01110010, 4 bytes required, big endian format
			scm.writeSingleInt(handle, 7050354, 0, ENDIAN.E_BIG, NUMOFBYTES.NUM_4);
			Thread.sleep(100);
			byte[] arr2 = new byte[4];
			arr2 = scm.readBytes(handle1, 4);
			System.out.println("dataa: " + arr2[0]); // prints 0    which is 00000000
			System.out.println("datab: " + arr2[1]); // prints 107  which is 01101011
			System.out.println("datac: " + arr2[2]); // prints -108 which is 10010100
			System.out.println("datad: " + arr2[3]); // prints 114  which is 01110010

			// 7050354 = 00000000 01101011 10010100 01110010, 4 bytes required, little endian format
			scm.writeSingleInt(handle, 7050354, 0, ENDIAN.E_LITTLE, NUMOFBYTES.NUM_4);
			Thread.sleep(100);
			byte[] arr3 = new byte[4];
			arr3 = scm.readBytes(handle1, 4);
			System.out.println("dataa: " + arr3[0]); // prints 114  which is 01110010
			System.out.println("datab: " + arr3[1]); // prints -108 which is 10010100
			System.out.println("datac: " + arr3[2]); // prints 107  which is 01101011
			System.out.println("datad: " + arr3[3]); // prints 0    which is 00000000

			scm.closeComPort(handle);
			scm.closeComPort(handle1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
