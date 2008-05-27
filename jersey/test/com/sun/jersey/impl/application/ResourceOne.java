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

package com.sun.jersey.impl.application;

import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.core.HttpResponseContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

    
@Path("/resource1")
public class ResourceOne {
    
    @GET
    public void handleRequest(HttpRequestContext request, HttpResponseContext response) {
        if (!request.getHttpMethod().equals("GET"))
            throw new RuntimeException("Method didn't match");
        
        String s = request.getEntity(String.class);
        if (!s.equals("RESOURCE-ONE"))
            throw new RuntimeException("Content didn't match");
    }
}