/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.sun.jersey.server.impl.inject;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.inject.Injectable;
import java.util.ArrayList;
import java.util.List;

/**
 * An injectable that obtains an injectable value given the {@link HttpContext}.
 * 
 * @param <T> the type of the injectable value.
 * @author Paul.Sandoz@Sun.Com
 */
public abstract class AbstractHttpContextInjectable<T> implements Injectable<T> {

    /**
     * This implementation throws an {@link IllegalStateException}.
     * 
     */
    public T getValue() {
        throw new IllegalStateException();
    }

    /**
     * Get the injectable value given the {@link HttpContext}.
     *
     * @param c the http context,
     * @return the value.
     * 
     */
    public abstract T getValue(HttpContext c);

    /**
     * Transform a list of {@link Injectable} into a list of this class.
     *
     * @param l the list of injectable.
     * @return the list of this class.
     */
    public static List<AbstractHttpContextInjectable> transform(List<Injectable> l) {
        List<AbstractHttpContextInjectable> al = new ArrayList<AbstractHttpContextInjectable>(l.size());

        for (Injectable i : l) {
            al.add(transform(i));
        }

        return al;
    }

    /**
     * Transform a {@link Injectable} into an instance of this class.
     * <p>
     * @param i the injectable.
     * @return an instance of this class.
     */
    public static AbstractHttpContextInjectable transform(final Injectable i) {
        if (i == null) {
            return null;
        } else if (i instanceof AbstractHttpContextInjectable) {
            return (AbstractHttpContextInjectable)i;
        } else {
            return new AbstractHttpContextInjectable() {
                @Override
                public Object getValue(HttpContext c) {
                    return i.getValue();
                }
            };
        }
    }
}