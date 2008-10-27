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

package com.sun.jersey.impl.container.servlet;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.core.spi.component.ioc.IoCInstantiatedComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCProxiedComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProvider;
import com.sun.jersey.server.impl.inject.ServerInjectableProviderContext;
import com.sun.jersey.server.spi.component.ResourceComponentConstructor;
import com.sun.jersey.server.spi.component.ResourceComponentInjector;
import com.sun.jersey.server.spi.component.ResourceComponentProvider;
import com.sun.jersey.server.spi.component.ResourceComponentProviderFactory;
import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

/**
 * A provider that maintains a per session resource class instance
 */
public final class PerSessionFactory implements ResourceComponentProviderFactory {

    private final ServerInjectableProviderContext sipc;

    private final HttpServletRequest hsr;

    private final HttpContext threadLocalHc;

    public PerSessionFactory(
            @Context ServerInjectableProviderContext sipc,
            @Context HttpServletRequest hsr,
            @Context HttpContext threadLocalHc) {
        this.hsr = hsr;
        this.sipc = sipc;
        this.threadLocalHc = threadLocalHc;
    }

    public ResourceComponentProvider getComponentProvider(Class c) {
        return new PerSesson();
    }

    public ResourceComponentProvider getComponentProvider(IoCComponentProvider icp, Class c) {
        if (icp instanceof IoCInstantiatedComponentProvider) {
            return new PerSessonInstantiated((IoCInstantiatedComponentProvider)icp);
        } else if (icp instanceof IoCProxiedComponentProvider) {
            return new PerSessonProxied((IoCProxiedComponentProvider)icp);
        }
        throw new IllegalStateException();
    }

    private class PerSesson implements ResourceComponentProvider {
        private Class c;

        private ResourceComponentConstructor rcc;

        private ResourceComponentInjector rci;

        public void init(AbstractResource abstractResource) {
            this.c = abstractResource.getResourceClass();

            this.rcc = new ResourceComponentConstructor(
                    sipc,
                    ComponentScope.Undefined,
                    abstractResource);
            this.rci = new ResourceComponentInjector(
                    sipc,
                    ComponentScope.Undefined,
                    abstractResource);
        }

        public Object getInstance() {
            return getInstance(threadLocalHc);
        }

        public Object getInstance(HttpContext hc) {
            Object o = hsr.getSession().getAttribute(c.getName());
            if (o != null) return o;

            try {
                o = rcc.getInstance(hc);
                rci.inject(hc, o);

                hsr.getSession().setAttribute(c.getName(), o);

                return o;
            } catch (InstantiationException ex) {
                throw new ContainerException("Unable to create resource", ex);
            } catch (IllegalAccessException ex) {
                throw new ContainerException("Unable to create resource", ex);
            } catch (InvocationTargetException ex) {
                // Propagate the target exception so it may be mapped to a response
                throw new MappableContainerException(ex.getTargetException());
            } catch (WebApplicationException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                throw new ContainerException("Unable to create resource", ex);
            }
        }
    }

    private class PerSessonInstantiated implements ResourceComponentProvider {
        private final IoCInstantiatedComponentProvider iicp;

        private Class c;

        private ResourceComponentConstructor rcc;

        private ResourceComponentInjector rci;

        PerSessonInstantiated(IoCInstantiatedComponentProvider iicp) {
            this.iicp = iicp;
        }

        public void init(AbstractResource abstractResource) {
            this.c = abstractResource.getResourceClass();

            this.rci = new ResourceComponentInjector(
                    sipc,
                    ComponentScope.Undefined,
                    abstractResource);
        }

        public Object getInstance() {
            return getInstance(threadLocalHc);
        }

        public Object getInstance(HttpContext hc) {
            Object o = hsr.getSession().getAttribute(c.getName());
            if (o != null) return o;

            o = iicp.getInstance();
            rci.inject(hc, iicp.getInjectableInstance(o));

            hsr.getSession().setAttribute(c.getName(), o);

            return o;
        }
    }

    private class PerSessonProxied implements ResourceComponentProvider {
        private final IoCProxiedComponentProvider ipcp;

        private Class c;

        private ResourceComponentConstructor rcc;

        private ResourceComponentInjector rci;

        PerSessonProxied(IoCProxiedComponentProvider ipcp) {
            this.ipcp = ipcp;
        }

        public void init(AbstractResource abstractResource) {
            this.c = abstractResource.getResourceClass();

            this.rcc = new ResourceComponentConstructor(
                    sipc,
                    ComponentScope.Undefined,
                    abstractResource);
            this.rci = new ResourceComponentInjector(
                    sipc,
                    ComponentScope.Undefined,
                    abstractResource);
        }

        public Object getInstance() {
            return getInstance(threadLocalHc);
        }

        public Object getInstance(HttpContext hc) {
            Object o = hsr.getSession().getAttribute(c.getName());
            if (o != null) return o;

            try {
                o = rcc.getInstance(hc);
                rci.inject(hc, o);
                Object po = ipcp.proxy(o);

                hsr.getSession().setAttribute(c.getName(), po);

                return po;
            } catch (InstantiationException ex) {
                throw new ContainerException("Unable to create resource", ex);
            } catch (IllegalAccessException ex) {
                throw new ContainerException("Unable to create resource", ex);
            } catch (InvocationTargetException ex) {
                // Propagate the target exception so it may be mapped to a response
                throw new MappableContainerException(ex.getTargetException());
            } catch (WebApplicationException ex) {
                throw ex;
            } catch (RuntimeException ex) {
                throw new ContainerException("Unable to create resource", ex);
            }
        }
    }
}