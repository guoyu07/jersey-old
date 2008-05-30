/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://jersey.dev.java.net/CDDL+GPL.html
 * or jersey/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at jersey/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
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

package com.sun.jersey.api.core;

import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Encapsulates the response to a HTTP request.
 * <p>
 * The default state of a response is a HTTP response with a status code of 204 
 * (OK) with no HTTP response headers and no entity.
 */
public interface HttpResponseContext {
    
    /**
     * Set the response state from a Response instance. This replaces a 
     * pre-existing response state.
     * <p>
     * If an entity is set but there is no MIME media type declared for the 
     * Content-Type response header then the MIME media type will be set to 
     * "application/octet-stream".
     *
     * @param response the response.
     */
    void setResponse(Response response);
    
    /**
     * Set the response state from a Response instance. This replaces a 
     * pre-existing response state.
     *
     * @param response the response.
     * @param contentType the MIME media type to use fot the Content-Type response
     *        header if the header is not set by the response. If null then
     *        "application/octet-stream" will be used.
     */
    void setResponse(Response response, MediaType contentType);

    /**
     * Check if the response has been set using the setReponse methods.
     * 
     * @return true if the response has been set.
     */
    boolean isResponseSet();
    
    /**
     * Get the status of the response.
     */
    int getStatus();
    
    /**
     * Set the status of the response.
     */
    void setStatus(int status);
    
    /**
     * Get the entity of the response
     */
    Object getEntity();
    
    /**
     * Set the entity of the response
     */
    void setEntity(Object entity);
    
    /**
     * Get the HTTP response headers. The returned map is case-insensitive wrt
     * keys. Note that <code>setHttpResponse</code> can change the HTTP response
     * headers and may overwrite headers set previously.
     *
     * @return a mutable map of HTTP header names and values that will be
     * included in the response. Any headers explicitly set will override
     * automatically generated values.
     */
    MultivaluedMap<String, Object> getHttpHeaders();
    
    /**
     * Get an OutputStream to which an entity may be written.
     * <p>
     * The first byte written will cause the status code and headers 
     * (if any) to be committed to the underlying container.
     *
     * @return the output stream
     * @throws java.io.IOException if an IO error occurs
     */
    OutputStream getOutputStream() throws IOException;
    
    /**
     * Ascertain if a response has been committed to an underlying container.
     * <p>
     * A response is committed if the status code, headers (if any) have been
     * committed to the underlying container.
     *  
     * @return true if the response has been committed.
     */
    boolean isCommitted();
}
