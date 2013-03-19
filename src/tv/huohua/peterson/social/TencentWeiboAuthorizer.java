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

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class TencentWeiboAuthorizer implements ISocialAuthorizer {
    public static interface AuthorizationListener {
        void onAuthorizationFinished(final TencentWeiboAuthorizer authorizer, final OAuthV2 oAuthV2);
    }

    public static class TencentWeiboAuthHandler {
        static final public int REQUEST_CODE_AUTHORIZATION = 213274384;

        private TencentWeiboAuthorizer authorizer;

        public TencentWeiboAuthHandler(final TencentWeiboAuthorizer authorizer) {
            this.authorizer = authorizer;
        }

        public void authorizeCallBack(final int requestCode, final int resultCode, final Intent data) {
            if (requestCode == REQUEST_CODE_AUTHORIZATION) {
                if (resultCode == OAuthV2AuthorizeWebView.RESULT_CODE) {
                    final OAuthV2 oAuthV2 = (OAuthV2) data.getExtras().getSerializable("oauth");
                    if (oAuthV2.getStatus() == 0) {
                        TencentWeiboAccessTokenKeeper.keepAccessToken(authorizer.activity, oAuthV2);
                    }
                    if (authorizer.authorizationListener != null) {
                        authorizer.authorizationListener.onAuthorizationFinished(authorizer, oAuthV2);
                    }
                }
            }
        }
    }

    public static OAuthV2 getOAuthV2Token(final Context context) {
        return TencentWeiboAccessTokenKeeper.readAccessToken(context);
    }

    public static boolean isAuthed(final Context context) {
        final OAuthV2 oAuthV2 = TencentWeiboAccessTokenKeeper.readAccessToken(context);
        return (oAuthV2 != null);
    }

    public static void unauth(final Context context) {
        TencentWeiboAccessTokenKeeper.clear(context);
    }

    private final Activity activity;
    private AuthorizationListener authorizationListener;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public TencentWeiboAuthorizer(final Activity activity, final String clientId, final String clientSecret,
            final String redirectUri) {
        this.activity = activity;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public AuthorizationListener getAuthorizationListener() {
        return authorizationListener;
    }

    @Override
    public boolean isAuthed() {
        return isAuthed(activity);
    }

    public void setAuthorizationListener(final AuthorizationListener authorizationListener) {
        this.authorizationListener = authorizationListener;
    }

    public TencentWeiboAuthHandler startAuth() {
        final OAuthV2 oAuth = new OAuthV2(redirectUri);
        oAuth.setClientId(clientId);
        oAuth.setClientSecret(clientSecret);

        final Intent intent = new Intent(activity, OAuthV2AuthorizeWebView.class);
        final TencentWeiboAuthHandler handler = new TencentWeiboAuthHandler(this);
        intent.putExtra("oauth", oAuth);
        activity.startActivityForResult(intent, TencentWeiboAuthHandler.REQUEST_CODE_AUTHORIZATION);
        return handler;
    }

    @Override
    public void unauth() {
        unauth(activity);
    }
}
