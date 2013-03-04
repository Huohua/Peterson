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

import java.io.Serializable;

public class ApiCallResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long accessTime;
    final private AbsApi<T> api;
    private T data;
    private Object errorMessage;
    private boolean isSucceeded;

    public ApiCallResponse(final AbsApi<T> api) {
        this.api = api;
        this.isSucceeded = true;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public AbsApi<T> getApi() {
        return api;
    }

    public T getData() {
        return data;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setAccessTime(final long accessTime) {
        this.accessTime = accessTime;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setErrorMessage(final Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSucceeded(final boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }
}
