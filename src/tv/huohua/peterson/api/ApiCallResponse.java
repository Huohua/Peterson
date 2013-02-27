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

package tv.huohua.peterson.api;

public class ApiCallResponse {
    private long accessTime;
    final private IHHApi api;
    private Object data;
    private boolean isSucceeded;

    public ApiCallResponse(final IHHApi api) {
        this.api = api;
        this.isSucceeded = true;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public IHHApi getApi() {
        return api;
    }

    public Object getData() {
        return data;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setAccessTime(final long accessTime) {
        this.accessTime = accessTime;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public void setSucceeded(final boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }
}
