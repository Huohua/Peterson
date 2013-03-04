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

import android.content.Context;

abstract public class AbsApi<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract ApiCallResponse<T> call(Context context);

    public ApiCallResponse<T> getEmptyApiCallResponse() {
        return new ApiCallResponse<T>(this);
    }
}
