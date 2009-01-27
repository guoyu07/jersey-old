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

package com.sun.jersey.impl.template;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.view.ImplicitProduces;
import com.sun.jersey.impl.AbstractResourceTester;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class ImplicitProducesTemplateProcessorTest extends AbstractResourceTester {
    
    public ImplicitProducesTemplateProcessorTest(String testName) {
        super(testName);
    }

    @Path("/")
    @ImplicitProduces("text/plain")
    public static class ImplicitWithGetTemplate {
        @GET
        @Produces("application/foo")
        public String toString() {
            return "ImplicitWithGetTemplate";
        }
    }

    public void testImplicitWithGetTemplate() throws IOException {
        ResourceConfig rc = new DefaultResourceConfig(ImplicitWithGetTemplate.class,
                TestTemplateProcessor.class);
        rc.getFeatures().put(ResourceConfig.FEATURE_IMPLICIT_VIEWABLES, true);
        initiateWebApplication(rc);
        WebResource r = resource("/");

        Properties p = new Properties();
        p.load(r.accept("text/plain", "application/foo").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithGetTemplate/index.testp", p.getProperty("path"));
        assertEquals("ImplicitWithGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("application/foo", "text/plain").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithGetTemplate/index.testp", p.getProperty("path"));
        assertEquals("ImplicitWithGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("text/plain;q=0.5", "application/foo").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithGetTemplate/index.testp", p.getProperty("path"));
        assertEquals("ImplicitWithGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("application/foo", "text/plain;q=0.5").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithGetTemplate/index.testp", p.getProperty("path"));
        assertEquals("ImplicitWithGetTemplate", p.getProperty("model"));

        assertEquals("ImplicitWithGetTemplate", r.accept("application/foo").get(String.class));
    }

    @Path("/")
    @ImplicitProduces("text/plain")
    public static class ImplicitWithSubResourceGetTemplate {
        @Path("sub")
        @GET
        @Produces("application/foo")
        public String toString() {
            return "ImplicitWithSubResourceGetTemplate";
        }
    }

    public void testImplicitWithSubResourceGetTemplate() throws IOException {
        ResourceConfig rc = new DefaultResourceConfig(ImplicitWithSubResourceGetTemplate.class,
                TestTemplateProcessor.class);
        rc.getFeatures().put(ResourceConfig.FEATURE_IMPLICIT_VIEWABLES, true);
        initiateWebApplication(rc);
        WebResource r = resource("/sub");

        Properties p = new Properties();
        p.load(r.accept("text/plain", "application/foo").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithSubResourceGetTemplate/sub.testp", p.getProperty("path"));
        assertEquals("ImplicitWithSubResourceGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("application/foo", "text/plain").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithSubResourceGetTemplate/sub.testp", p.getProperty("path"));
        assertEquals("ImplicitWithSubResourceGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("text/plain;q=0.5", "application/foo").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithSubResourceGetTemplate/sub.testp", p.getProperty("path"));
        assertEquals("ImplicitWithSubResourceGetTemplate", p.getProperty("model"));

        p = new Properties();
        p.load(r.accept("application/foo", "text/plain;q=0.5").get(InputStream.class));
        assertEquals("/com/sun/jersey/impl/template/ImplicitProducesTemplateProcessorTest/ImplicitWithSubResourceGetTemplate/sub.testp", p.getProperty("path"));
        assertEquals("ImplicitWithSubResourceGetTemplate", p.getProperty("model"));

        assertEquals("ImplicitWithSubResourceGetTemplate", r.accept("application/foo").get(String.class));
    }

}