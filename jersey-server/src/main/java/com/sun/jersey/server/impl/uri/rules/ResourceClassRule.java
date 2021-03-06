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

package com.sun.jersey.server.impl.uri.rules;

import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.core.reflection.ReflectionHelper;
import com.sun.jersey.spi.uri.rules.UriRule;
import com.sun.jersey.spi.uri.rules.UriRuleContext;
import com.sun.jersey.server.probes.UriRuleProbeProvider;
import java.util.Iterator;

/**
 * The rule for accepting a resource class.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public final class ResourceClassRule extends BaseRule {

    private final Class resourceClass;
    
    public ResourceClassRule(UriTemplate template, Class resourceClass) {
        super(template);
        this.resourceClass = resourceClass;
    }
    
    public boolean accept(CharSequence path, Object resource, UriRuleContext context) {
        // Set the template values
        pushMatch(context);

        // Get the resource instance from the resource class
        resource = context.getResource(resourceClass);
        context.pushResource(resource);

        if (context.isTracingEnabled()) {
            context.trace(String.format("accept resource: \"%s\" -> @Path(\"%s\") %s",
                    context.getUriInfo().getMatchedURIs().get(0),
                    getTemplate().getTemplate(),
                    ReflectionHelper.objectToString(resource)));
        }

        UriRuleProbeProvider.ruleAccept(ResourceClassRule.class.getSimpleName(), path,
                resource);

        // Match sub-rules on the resource class
        final Iterator<UriRule> matches = context.getRules(resourceClass).
                match(path, context);
        while (matches.hasNext())
            if (matches.next().accept(path, resource, context))
                return true;

        return false;
    }
}
