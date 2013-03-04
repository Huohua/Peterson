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

abstract public class AbsListApi<T> extends AbsApi<T[]> {
    private static final long serialVersionUID = 1L;

    protected int offset;
    protected int limit;

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }
}
