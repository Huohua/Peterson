package in.huohua.peterson.misc;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhangxinzheng on 9/26/14.
 */
final public class ABUtils {
    private String deviceId;
    private Map<String, Map<String, String>> abChoices;
    private String defaultChoice;
    private Context context;
    private boolean inited = false;

    private volatile static ABUtils singleton;
    private ABUtils (Context context) {
        this.context = context;
        abChoices = new HashMap<String, Map<String, String>>();
        deviceId = DeviceUtils.getDeviceId(context);
    }

    public void initData(String jsonConfig) {
        abChoices.clear();
        try {
            JSONObject object = new JSONObject(jsonConfig);
            for (Iterator<String> iter = object.keys(); iter.hasNext();) {
                String key1= iter.next();
                JSONObject subObject = (JSONObject) object.get(key1);
                HashMap<String, String>subHash = new HashMap<String, String>();
                for (Iterator<String> iter2 = subObject.keys(); iter2.hasNext();) {
                    String key2 = iter2.next();
                    subHash.put(key2, (String) subObject.get(key2));
                }
                abChoices.put(key1, subHash);
                inited = true;
            }

        } catch (JSONException e) {
            Log.e("zxz", "json parse fail");
        }
    }

    public String getABResult(String key) throws Exception{
        if (!inited) {
            throw new Exception("not inited");
        }
        Map<String, String> abs = abChoices.get(key);
        int flag = 0;
        if (deviceId.length() > 0) {
            flag = Integer.parseInt(deviceId.substring(deviceId.length()-1), 16);
        }
        if (abs != null && abs.size() > 0) {
            String [] absArray = new String[abs.size()];
            abs.keySet().toArray(absArray);
            for (String i : absArray) {
                if (abs.get(i).equals("default")) {
                    return i;
                }
            }
            String abKey = absArray[flag % abs.size()];
            return abKey;
        }
        return null;
    }

    public static ABUtils getSingleton(Context context) {
        if (singleton == null) {
            synchronized (ABUtils.class) {
                if (singleton == null) {
                    singleton = new ABUtils(context);
                }
            }
        }
        return singleton;
    }
}
