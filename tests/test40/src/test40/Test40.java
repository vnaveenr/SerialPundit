/**
 * Author : Rishi Gupta
 * 
 * This file is part of 'serial communication manager' library.
 *
 * The 'serial communication manager' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The 'serial communication manager' is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with serial communication manager. If not, see <http://www.gnu.org/licenses/>.
 */
 
package test40;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;

// find port name from given handle
public class Test40 {
	public static void main(String[] args) {

				SerialComManager scm = new SerialComManager();
				
				String PORT = null;
				String PORT1 = null;
				int osType = SerialComManager.getOSType();
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
				
				try {
					long handle = scm.openComPort(PORT, true, true, true);
					scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
					scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
					
					long handle1 = scm.openComPort(PORT1, true, true, true);
					scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
					scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
					
					String str = scm.getPortName(handle);
					if(str == null) {
						System.out.println("unable to find port name");
					}else {
						System.out.println("port name : " + str);
					}
					
					String str1 = scm.getPortName(handle1);
					if(str1 == null) {
						System.out.println("unable to find port name");
					}else {
						System.out.println("port name : " + str1);
					}
					
					scm.closeComPort(handle);
					scm.closeComPort(handle1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}