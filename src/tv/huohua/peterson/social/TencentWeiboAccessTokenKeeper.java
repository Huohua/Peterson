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

package tv.huohua.peterson.social;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.tencent.weibo.oauthv2.OAuthV2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class TencentWeiboAccessTokenKeeper {
    private static final String PREFERENCE_KEY = "token";
    private static final String PREFERENCES_NAME = "com_tencent_weibo_sdk_android";

    public static void clear(final Context context) {
        final SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        final Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static void keepAccessToken(final Context context, final OAuthV2 token) {
        final SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        final Editor editor = pref.edit();
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(token);
            oos.flush();
            String base64String = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            editor.putString(PREFERENCE_KEY, base64String);
            editor.commit();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public static OAuthV2 readAccessToken(final Context context) {
        final SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        final String base64String = pref.getString(PREFERENCE_KEY, "");
        final byte[] base64 = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64);
        try {
            final ObjectInputStream bis = new ObjectInputStream(byteArrayInputStream);
            try {
                final OAuthV2 token = (OAuthV2) bis.readObject();
                return token;
            } catch (final ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        } catch (final StreamCorruptedException exception) {
            exception.printStackTrace();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
