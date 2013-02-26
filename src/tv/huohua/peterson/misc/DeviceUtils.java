package tv.huohua.peterson.misc;

import java.util.UUID;

import android.content.Context;
import android.telephony.TelephonyManager;

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
}
