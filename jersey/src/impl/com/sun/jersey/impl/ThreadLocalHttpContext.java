/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved. 
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License. 
 * 
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package com.sun.jersey.impl;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;
import com.sun.jersey.api.uri.ExtendedUriInfo;
import java.util.Map;

/**
 * Implementation of {@link HttpContext} using {@link ThreadLocal}
 * to store {@link HttpRequestContext} and {@link HttpResponseContext} instances
 * associated with threads.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public final class ThreadLocalHttpContext implements HttpContext {
    private ThreadLocal<HttpContext> context = new ThreadLocal<HttpContext>();

    /**
     * Set the {@link HttpRequestContext} and {@link HttpResponseContext} instances
     * for the current thread.
     */
    public void set(HttpContext context) {
        this.context.set(context);
    }

    public ExtendedUriInfo getUriInfo() {
        return context.get().getUriInfo();
    }
    
    public HttpRequestContext getRequest() {
        return context.get().getRequest();
    }

    public HttpResponseContext getResponse() {
        return context.get().getResponse();
    }
    
    public Map<String, Object> getProperties() {
        return context.get().getProperties();
    }
}