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

import tv.huohua.peterson.social.WeiboAccessTokenKeeper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

public class WeiboAuthorizer {
    class AuthDialogListener implements WeiboAuthListener {
        @Override
        public void onCancel() {
            handler.sendMessage(handler.obtainMessage(MSG_AUTH_CANCELED, WeiboAuthorizer.this));
        }

        @Override
        public void onComplete(final Bundle values) {
            final String token = values.getString("access_token");
            final String expiresIn = values.getString("expires_in");
            final Oauth2AccessToken accessToken = new Oauth2AccessToken(token, expiresIn);
            if (accessToken.isSessionValid()) {
                WeiboAccessTokenKeeper.keepAccessToken(activity, accessToken);
            }
            Log.i(TAG, "Auth finished.");
            handler.sendMessage(handler.obtainMessage(MSG_AUTH_FINISHED, WeiboAuthorizer.this));
        }

        @Override
        public void onError(final WeiboDialogError error) {
            Log.e(TAG, "Auth error : " + error.getMessage());
            handler.sendMessage(handler.obtainMessage(MSG_AUTH_ERROR, WeiboAuthorizer.this));
        }

        @Override
        public void onWeiboException(final WeiboException e) {
            Log.e(TAG, "Auth exception : " + e.getMessage());
            handler.sendMessage(handler.obtainMessage(MSG_AUTH_ERROR, WeiboAuthorizer.this));
        }
    }

    public interface WeiboAuthorizationListener {
        void onAuthorizationCanceled(WeiboAuthorizer authorizer);

        void onAuthorizationError(WeiboAuthorizer authorizer);

        void onAuthorizationSucceeded(WeiboAuthorizer authorizer);
    }

    static private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message message) {
            if (message.obj != null && message.obj instanceof WeiboAuthorizer) {
                final WeiboAuthorizer authorizer = (WeiboAuthorizer) message.obj;
                switch (message.what) {
                case MSG_AUTH_CANCELED:
                    if (authorizer.onWeiboAuthorizedListener != null) {
                        authorizer.onWeiboAuthorizedListener.onAuthorizationCanceled(authorizer);
                    }
                    break;
                case MSG_AUTH_ERROR:
                    if (authorizer.onWeiboAuthorizedListener != null) {
                        authorizer.onWeiboAuthorizedListener.onAuthorizationError(authorizer);
                    }
                    break;
                case MSG_AUTH_FINISHED:
                    if (authorizer.onWeiboAuthorizedListener != null) {
                        authorizer.onWeiboAuthorizedListener.onAuthorizationSucceeded(authorizer);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    };

    static private final int MSG_AUTH_CANCELED = 0;
    static private final int MSG_AUTH_ERROR = 1;
    static private final int MSG_AUTH_FINISHED = 2;
    public static final String TAG = WeiboAuthorizer.class.getName();

    public static Oauth2AccessToken getAccessToken(final Context context) {
        return WeiboAccessTokenKeeper.readAccessToken(context);
    }

    public static boolean isAuthed(final Activity activity) {
        final Oauth2AccessToken accessToken = WeiboAccessTokenKeeper.readAccessToken(activity);
        return (accessToken != null && accessToken.isSessionValid());
    }

    public static void unauth(final Context context) {
        WeiboAccessTokenKeeper.clear(context);
    }

    private final Activity activity;
    private WeiboAuthorizationListener onWeiboAuthorizedListener;
    private SsoHandler ssoHandler;
    private final Weibo weibo;

    public WeiboAuthorizer(final Activity activity, final String consumerKey, final String redirectUrl) {
        this.activity = activity;
        this.weibo = Weibo.getInstance(consumerKey, redirectUrl);
    }

    public Oauth2AccessToken getAccessToken() {
        return getAccessToken(activity);
    }

    public Activity getActivity() {
        return activity;
    }

    public WeiboAuthorizationListener getOnWeiboAuthorizedListener() {
        return onWeiboAuthorizedListener;
    }

    public Weibo getWeibo() {
        return weibo;
    }

    public boolean isAuthed() {
        return isAuthed(activity);
    }

    public void setOnWeiboAuthorizedListener(final WeiboAuthorizationListener onWeiboAuthorizedListener) {
        this.onWeiboAuthorizedListener = onWeiboAuthorizedListener;
    }

    public SsoHandler startAuth() {
        try {
            Class.forName("com.weibo.sdk.android.sso.SsoHandler");
            ssoHandler = new SsoHandler(activity, weibo);
            ssoHandler.authorize(new AuthDialogListener());
            return ssoHandler;
        } catch (final ClassNotFoundException exception) {
            Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found");
        }
        weibo.authorize(activity, new AuthDialogListener());
        return null;
    }

    public void unauth() {
        unauth(activity);
    }
}
