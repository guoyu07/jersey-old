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

package com.sun.ws.rest.spi.resource;

/**
 * A provider that manages the creation of resource class instances. A provider
 * instance is specific to a particular class of resource.
 */
public interface ResourceProvider {

    /**
     * Specifies the class of the resource that the provider
     * instance will manage access to.
     *
     * @param resourceClass the class of the resource
     */
    void init(Class<?> resourceClass);
    
    /**
     * Called to obtain an instance of a resource class.
     * 
     * @param context a context for resource providers, used to perform
     * resource injection on new instances and obtain values for constructor
     * parameters
     * @return an initialized instance of the supplied class
     */
    Object getInstance(ResourceProviderContext context);
}
