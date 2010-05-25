/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2010 Sun Microsystems, Inc. All rights reserved.
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
package com.sun.jersey.impl.inject;


import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Path;

import com.sun.jersey.impl.AbstractResourceTester;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class NonPublicNonStaticTest extends AbstractResourceTester {
    
    public NonPublicNonStaticTest(String testName) {
        super( testName );
    }

    public void testDummy() {
    }


    @Path("/")
    public class NonStaticResource {
    }

//    public void testNonStaticResource() {
//        initiateWebApplication(NonStaticResource.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }


    @Path("/")
    public static class NonStaticResourceSubResource {

      @Path("class")
      public Class<NonStaticResource> getClazz() {
          return NonStaticResource.class;
      }
    }

//    public void testNonStaticResourceSubResource() {
//        initiateWebApplication(NonStaticResourceSubResource.class);
//
//        assertEquals("class", resource("/class").get(String.class));
//    }


    @Path("/")
    static class NonPublicResource {
    }

//    public void testNonPublicResource() {
//        initiateWebApplication(NonPublicResource.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }


    @Path("/")
    public static class NonPublicResourceSubResource {

      @Path("class")
      public Class<NonPublicResource> getClazz() {
          return NonPublicResource.class;
      }
    }

//    public void testNonPublicSubResource() {
//        initiateWebApplication(NonPublicResourceSubResource.class);
//
//        assertEquals("foo", resource("/class").get(String.class));
//    }


    @Path("/")
    static class NonPublicResourceWithConstructor {
        public NonPublicResourceWithConstructor() {}
    }

//    public void testNonPublicResourceWithConstructor() {
//        initiateWebApplication(NonPublicResourceWithConstructor.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }


    @Path("/")
    public static class NonPublicResourceSubResourceWithConstructor {

      @Path("class")
      public Class<NonPublicResourceWithConstructor> getClazz() {
          return NonPublicResourceWithConstructor.class;
      }
    }

//    public void testNonPublicResourceSubResourceWithConstructor() {
//        initiateWebApplication(NonPublicResourceSubResourceWithConstructor.class);
//
//        assertEquals("foo", resource("/class").get(String.class));
//    }


    @Path("/")
    public static class ProviderResource {
    }

    @Provider
    public class NonStaticProvider implements MessageBodyWriter<String> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == String.class;
        }

        @Override
        public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
            entityStream.write(t.getBytes());
        }
    }

//    public void testNonStaticProvider() throws IOException {
//        initiateWebApplication(ProviderResource.class, NonStaticProvider.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }

    @Provider
    static class NonPublicProvider implements MessageBodyWriter<String> {

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == String.class;
        }

        @Override
        public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
            entityStream.write(t.getBytes());
        }
    }

//    public void testNonPublicProvider() {
//        initiateWebApplication(ProviderResource.class, NonPublicProvider.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }

    @Provider
    static class NonPublicProviderWithConstructor implements MessageBodyWriter<String> {

        public NonPublicProviderWithConstructor() {}
        
        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == String.class;
        }

        @Override
        public long getSize(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return -1;
        }

        @Override
        public void writeTo(String t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
            entityStream.write(t.getBytes());
        }
    }

//    public void testNonPublicProviderWithConstructor() {
//        initiateWebApplication(ProviderResource.class, NonPublicProviderWithConstructor.class);
//
//        assertEquals("foo", resource("/").get(String.class));
//    }
}