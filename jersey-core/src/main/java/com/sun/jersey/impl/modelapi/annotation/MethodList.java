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

package com.sun.jersey.impl.modelapi.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class MethodList implements Iterable<AnnotatedMethod> {

    private AnnotatedMethod[] methods;
    
    public MethodList(Class c) {
        this(c.getMethods());
    }
    
    public MethodList(List<Method> methods) {
        this.methods = new AnnotatedMethod[methods.size()];
        for (int i = 0; i < methods.size(); i++)
            this.methods[i] = new AnnotatedMethod(methods.get(i));        
    }
    
    public MethodList(Method... methods) {
        this.methods = new AnnotatedMethod[methods.length];
        for (int i = 0; i < methods.length; i++)
            this.methods[i] = new AnnotatedMethod(methods[i]);
    }
    
    public MethodList(AnnotatedMethod... methods) {        
        this.methods = methods;
    }

    public Iterator<AnnotatedMethod> iterator() {
      return Arrays.asList(methods).iterator();
    }
    
    public <T extends Annotation> MethodList isNotPublic() {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return !Modifier.isPublic(m.getMethod().getModifiers());
            }
        });
    }
    
    public <T extends Annotation> MethodList hasNumParams(final int i) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return m.getParameterTypes().length == i;
            }
        });
    }
    
    public <T extends Annotation> MethodList hasReturnType(final Class<?> r) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return m.getMethod().getReturnType() == r;
            }
        });
    }
    
    public <T extends Annotation> MethodList nameStartsWith(final String s) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return m.getMethod().getName().startsWith(s);
            }
        });
    }
    
    public <T extends Annotation> MethodList hasAnnotation(final Class<T> annotation) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return m.getAnnotation(annotation) != null;
            }
        });
    }
    
    public <T extends Annotation> MethodList hasMetaAnnotation(final Class<T> annotation) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                for (Annotation a : m.getAnnotations()) {
                    if (a.annotationType().getAnnotation(annotation) != null)
                        return true;
                }
                return false;
            }
        });
    }
    
    public <T extends Annotation> MethodList hasNotAnnotation(final Class<T> annotation) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                return m.getAnnotation(annotation) == null;
            }
        });
    }
    
    public <T extends Annotation> MethodList hasNotMetaAnnotation(final Class<T> annotation) {
        return filter(new Filter() {
            public boolean keep(AnnotatedMethod m) {
                for (Annotation a : m.getAnnotations()) {
                    if (a.annotationType().getAnnotation(annotation) != null)
                        return false;
                }                
                return true;
            }
        });
    }
    
    private interface Filter {
        boolean keep(AnnotatedMethod m);
    }
    
    private MethodList filter(Filter f) {
        List<AnnotatedMethod> r = new ArrayList<AnnotatedMethod>();
        for (AnnotatedMethod m : methods)
            if (f.keep(m))
                r.add(m);
        return new MethodList(r.toArray(new AnnotatedMethod[0]));
    }
}