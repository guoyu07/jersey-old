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

package com.sun.jersey.impl.resource;

import com.sun.jersey.impl.AbstractResourceTester;
import com.sun.jersey.api.client.ClientResponse;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class PutTest extends AbstractResourceTester {
    
    public PutTest(String testName) {
        super(testName);
    }

    @Path("/PutNoInputNoReturnResource")
    static public class PutNoInputNoReturnResource { 
        @PUT
        public void doPut() {
        }
    }
    
    @Path("/PutNoReturnResource")
    static public class PutNoReturnResource { 
        @PUT
        public void doPut(String in) {
            assertEquals("PutNoReturnResource", in);
        }
    }
    
    @Path("/PutNoInputResource")
    static public class PutNoInputResource { 
        @PUT
        public String doPut() {
            return "PutNoInputResource";
        }
    }
    
    @Path("/PutResource")
    static public class PutResource { 
        @PUT
        public String doPut(String in) {
            assertEquals("PutResource", in);
            return "PutResource";
        }
    }
    
    public void testPut() {
        initiateWebApplication(PutNoInputNoReturnResource.class, 
                PutNoReturnResource.class,
                PutNoInputResource.class,
                PutResource.class);

        ClientResponse response = resource("/PutNoInputNoReturnResource").
                put(ClientResponse.class);
        assertEquals(204, response.getStatus());
        
        response = resource("/PutNoReturnResource").
                put(ClientResponse.class, "PutNoReturnResource");
        assertEquals(204, response.getStatus());
        
        String s = resource("/PutNoInputResource").put(String.class);
        assertEquals(s, "PutNoInputResource");
        
        s = resource("/PutResource").put(String.class, "PutResource");
        assertEquals(s, "PutResource");
    }
}