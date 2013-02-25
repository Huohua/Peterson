package tv.huohua.peterson.misc;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

final public class ApplicationUtils {
    public static Boolean isApplicationInstalled(final Context context, final String className) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(className);
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return (list.size() > 0);
    }

    private ApplicationUtils() {
    }
}
