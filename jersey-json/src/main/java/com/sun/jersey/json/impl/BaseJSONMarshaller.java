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
package com.sun.jersey.json.impl;

import com.sun.jersey.api.json.JSONConfigurated;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONMarshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author Jakub.Podlesak@Sun.COM
 */
public class BaseJSONMarshaller implements JSONMarshaller, JSONConfigurated {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    
    protected final Marshaller jaxbMarshaller;
    
    protected final JSONConfiguration jsonConfig;

    public BaseJSONMarshaller(JAXBContext jaxbContext, JSONConfiguration jsonConfig) throws JAXBException {
        this(jaxbContext.createMarshaller(), jsonConfig);
    }

    public BaseJSONMarshaller(Marshaller jaxbMarshaller, JSONConfiguration jsonConfig) {
        this.jsonConfig = jsonConfig;
        this.jaxbMarshaller = jaxbMarshaller;
    }

    // JSONConfigurated

    public JSONConfiguration getJSONConfiguration() {
        return jsonConfig;
    }

    // JSONMarshaller

    public void marshallToJSON(Object o, OutputStream outputStream) throws JAXBException {
        marshallToJSON(o, new OutputStreamWriter(outputStream, UTF8));
    }

    public void marshallToJSON(Object o, Writer writer) throws JAXBException {
        try {
            jaxbMarshaller.marshal(o, Stax2JsonFactory.createWriter(writer, jsonConfig));
        } catch (IOException ex) {
            Logger.getLogger(BaseJSONMarshaller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
