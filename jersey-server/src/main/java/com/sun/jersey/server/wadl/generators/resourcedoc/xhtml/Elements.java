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
package com.sun.jersey.server.wadl.generators.resourcedoc.xhtml;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 * This class provides a fluent interface to xhtml supporting jaxb bindings.<br>
 * Created on: Jun 17, 2008<br>
 * 
 * @author <a href="mailto:martin.grotzke@freiheit.com">Martin Grotzke</a>
 * @version $Id$
 */
public class Elements extends JAXBElement<XhtmlElementType> {

    public static Elements el( String elementName ) {
        return createElement( elementName );
    }

    public static Object val( String elementName, String value ) {
        return createElement( elementName, value );
    }
    
    private static final long serialVersionUID = 1L;
    
    public Elements(QName name, Class<XhtmlElementType> clazz,
            XhtmlElementType element) {
        super( name, clazz, element );
    }
    
    public Elements add( Object ... childNodes ) {
        if ( childNodes != null ) {
            for ( Object childNode : childNodes ) {
                getValue().getChildNodes().add( childNode );
            }
        }
        return this;
    }

    public Elements addChild( Object child ) {
        getValue().getChildNodes().add( child );
        return this;
    }

    private static Elements createElement( final String elementName ) {
        try {
            
            final XhtmlElementType element = new XhtmlElementType();
            
            final Elements jaxbElement = new Elements( 
                    new QName( "http://www.w3.org/1999/xhtml", elementName ),
                    XhtmlElementType.class,
                    element );
            
            return jaxbElement;
            
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    private static JAXBElement<XhtmlValueType> createElement( final String elementName, String value ) {
        try {
            
            final XhtmlValueType element = new XhtmlValueType();
            element.value = value;
            
            final JAXBElement<XhtmlValueType> jaxbElement = new JAXBElement<XhtmlValueType>( 
                    new QName( "http://www.w3.org/1999/xhtml", elementName ),
                    XhtmlValueType.class,
                    element );
            
            return jaxbElement;
            
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

}
