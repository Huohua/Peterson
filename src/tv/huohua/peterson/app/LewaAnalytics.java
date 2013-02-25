package tv.huohua.peterson.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import android.content.Context;

public class LewaAnalytics {
    private static Method getOnEventMethod(final Object[] params) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("lewa.bi.BIAgent");
        final Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            if (method.getName().equals("onEvent")) {
                final Class<?> clazzes[] = method.getParameterTypes();
                if (clazzes.length != params.length) {
                    continue;
                }
                boolean match = true;
                for (int j = 0; j < clazzes.length; j++) {
                    if (!clazzes[j].isInstance(params[j])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return method;
                }
            }
        }
        return null;
    }

    public static void onEvent(final Context context, final String event_id) {
        try {
            final Method method = getOnEventMethod(new Object[] { context, event_id });
            if (method != null) {
                try {
                    method.invoke(null, new Object[] { context, event_id });
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (final ClassNotFoundException exception) {
        }
    }

    public static void onEvent(final Context context, final String event_id, final Map<String, String> params) {
        try {
            final Method method = getOnEventMethod(new Object[] { context, event_id, params });
            if (method != null) {
                try {
                    method.invoke(null, new Object[] { context, event_id, params });
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (final ClassNotFoundException exception) {
        }
    }

    public static void onEvent(final Context context, final String event_id, final String label) {
        try {
            final Method method = getOnEventMethod(new Object[] { context, event_id, label });
            if (method != null) {
                try {
                    method.invoke(null, new Object[] { context, event_id, label });
                } catch (IllegalArgumentException exception) {
                    exception.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                } catch (InvocationTargetException exception) {
                    exception.printStackTrace();
                }
            }
        } catch (final ClassNotFoundException exception) {
        }
    }
}
