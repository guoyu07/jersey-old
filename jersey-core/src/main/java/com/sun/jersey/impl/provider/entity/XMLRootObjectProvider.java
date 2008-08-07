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

package com.sun.jersey.impl.provider.entity;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.impl.ImplMessages;
import com.sun.jersey.impl.util.ThrowHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class XMLRootObjectProvider extends AbstractJAXBProvider<Object> {
    
    XMLRootObjectProvider(Providers ps) {
        super(ps);
    }
    
    XMLRootObjectProvider(Providers ps, MediaType mt) {
        super(ps, mt);
    }
    
    @Override
    protected JAXBContext getStoredJAXBContext(Class type) throws JAXBException {
        return null;
    }
    
    @Produces("application/xml")
    @Consumes("application/xml")
    public static final class App extends XMLRootObjectProvider {
        public App(@Context Providers ps) { super(ps , MediaType.APPLICATION_XML_TYPE); }
    }
    
    @Produces("text/xml")
    @Consumes("text/xml")
    public static final class Text extends XMLRootObjectProvider {
        public Text(@Context Providers ps) { super(ps , MediaType.TEXT_XML_TYPE); }
    }
    
    @Produces("*/*")
    @Consumes("*/*")
    public static final class General extends XMLRootObjectProvider {
        public General(@Context Providers ps) { super(ps); }
    }
    
    public boolean isReadable(Class<?> type, Type genericType, Annotation annotations[]) {
        try {
            return Object.class == type && getUnmarshaller(type) != null;
        } catch (JAXBException cause) {
            throw ThrowHelper.withInitCause(cause,
                    new ContainerException(ImplMessages.ERROR_MARSHALLING_JAXB(type))
                    );
        }
    }
    
    public Object readFrom(
            Class<Object> type, 
            Type genericType, 
            Annotation annotations[],
            MediaType mediaType, 
            MultivaluedMap<String, String> httpHeaders, 
            InputStream entityStream) throws IOException {        
        try {
            return getUnmarshaller(type, mediaType).unmarshal(entityStream);
        } catch (JAXBException cause) {
            throw ThrowHelper.withInitCause(cause,
                    new IOException(ImplMessages.ERROR_MARSHALLING_JAXB(type))
                    );
        }
    }

    public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2) {
        return false;
    }

    public void writeTo(Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, 
            MediaType arg4, MultivaluedMap<String, Object> arg5, OutputStream arg6) 
            throws IOException, WebApplicationException {
        throw new IllegalArgumentException();
    }
}