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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.UUID;

public class DeviceUtils {
    final private static String CACHE_KEY_DEVICE_ID = "DeviceUtils.DeviceId";
    final private static String CACHE_NAME = "DeviceUtils";
    private static String DEVICE_ID;

    public static String getDeviceId(final Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        if (DEVICE_ID == null) {
            DEVICE_ID = sharedPreferences.getString(CACHE_KEY_DEVICE_ID, null);
        }
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

            if (couldGetDeviceId(context) && !TextUtils.isEmpty(DEVICE_ID)) {
                Editor editor = sharedPreferences.edit();
                editor.putString(CACHE_KEY_DEVICE_ID, DEVICE_ID);
                editor.commit();
            }
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

    public static boolean couldGetDeviceId(final Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (TextUtils.isEmpty(tm.getDeviceId())) {
            return false;
        }
        if (TextUtils.isEmpty(tm.getSimSerialNumber())) {
            return false;
        }
        if (TextUtils.isEmpty(android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID))) {
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager){

        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
}
