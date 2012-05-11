/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.debug.internal;

import java.util.*;

public abstract class DebugValue {

    public static final Comparator<DebugValue> ORDER_BY_NAME = new Comparator<DebugValue>() {
        @Override
        public int compare(DebugValue o1, DebugValue o2) {
            // this keeps the "Runs" metric at the top of the list
            if (o1.getName().equals("Runs")) {
                return o2.getName().equals("Runs") ? 0 : -1;
            }
            if (o2.getName().equals("Runs")) {
                return o1.getName().equals("Runs") ? 0 : 1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    };

    private String name;
    private int index;

    protected DebugValue(String name) {
        this.name = name;
        this.index = -1;
    }

    protected long getCurrentValue() {
        ensureInitialized();
        return DebugScope.getInstance().getCurrentValue(index);
    }

    protected void setCurrentValue(long l) {
        ensureInitialized();
        DebugScope.getInstance().setCurrentValue(index, l);
    }

    private void ensureInitialized() {
        if (index == -1) {
            index = KeyRegistry.register(name, this);
        }
    }

    protected void addToCurrentValue(long timeSpan) {
        setCurrentValue(getCurrentValue() + timeSpan);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public abstract String toString(long value);
}
