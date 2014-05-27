/*******************************************************************************
 * Copyright (c) 2013 Zheng Sun.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Zheng Sun - initial API and implementation
 ******************************************************************************/

package in.huohua.peterson.misc;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtils {
    private static String DEVICE_ID;

    public static String getDeviceId(final Context context) {
        if (DEVICE_ID == null) {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(context.getContentResolver(),
                            android.provider.Settings.Secure.ANDROID_ID);
            final UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32)
                    | tmSerial.hashCode());
            DEVICE_ID = deviceUuid.toString();
        }
        return DEVICE_ID;
    }

    private DeviceUtils() {
    }
    
    public static String getModel() {
        String model = android.os.Build.MODEL;
        return TextUtils.isEmpty(model) ? "N/A" : model;
    }
    
    public static String getBrand() {
        String brand = android.os.Build.BRAND;
        return TextUtils.isEmpty(brand) ? "N/A" : brand;
    }
    
    public static String getOSVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        return TextUtils.isEmpty(version) ? "N/A" : version;
    }
    
    public static String getCPUArch() {
        String cpuArch = System.getProperty("os.arch");
        return TextUtils.isEmpty(cpuArch) ? "N/A" : cpuArch;
    }
}
