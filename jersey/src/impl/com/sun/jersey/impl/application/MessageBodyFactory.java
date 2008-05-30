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

package com.sun.jersey.impl.application;

import com.sun.jersey.impl.model.MediaTypeHelper;
import com.sun.jersey.impl.util.KeyComparator;
import com.sun.jersey.impl.util.KeyComparatorHashMap;
import com.sun.jersey.spi.container.MessageBodyContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.ConsumeMime;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWorkers;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public final class MessageBodyFactory implements MessageBodyContext, MessageBodyWorkers {
    private static final Logger LOGGER = Logger.getLogger(MessageBodyFactory.class.getName());
    
    private static final KeyComparator<MediaType> MEDIA_TYPE_COMPARATOR = 
            new KeyComparator<MediaType>() {
        public boolean equals(MediaType x, MediaType y) {
            return (x.getType().equalsIgnoreCase(y.getType())
                    && x.getSubtype().equalsIgnoreCase(y.getSubtype()));
        }

        public int hash(MediaType k) {
            return k.hashCode();
        }

        public int compare(MediaType o1, MediaType o2) {
            throw new UnsupportedOperationException("Not supported yet.");
        }        
    };
    
    private final ComponentProviderCache componentProviderCache;
    
    private Map<MediaType, List<MessageBodyReader>> readerProviders;
    
    private Map<MediaType, List<MessageBodyWriter>> writerProviders;
    
    public MessageBodyFactory(ComponentProviderCache componentProviderCache) {
        this.componentProviderCache = componentProviderCache;
    }
     
    public void init() {
        this.readerProviders = getProviderMap(MessageBodyReader.class, ConsumeMime.class);    
        this.writerProviders = getProviderMap(MessageBodyWriter.class, ProduceMime.class);        
    }
    
    private <T> Map<MediaType, List<T>> getProviderMap(
            Class<T> serviceClass,
            Class<?> annotationClass) {
        Map<MediaType, List<T>> s = new KeyComparatorHashMap<MediaType, List<T>>(
                MEDIA_TYPE_COMPARATOR);
        
        for (T provider : componentProviderCache.getProvidersAndServices(serviceClass)) {
            String values[] = getAnnotationValues(provider.getClass(), annotationClass);
            if (values==null)
                getClassCapability(s, provider, MediaTypeHelper.GENERAL_MEDIA_TYPE);
            else
                for (String type: values)
                    getClassCapability(s, provider, MediaType.valueOf(type));            
            
        }   
        
        return s;        
    }

    private <T> void getClassCapability(Map<MediaType, List<T>> capabilities, 
            T provider, MediaType mediaType) {
        if (!capabilities.containsKey(mediaType))
            capabilities.put(mediaType, new ArrayList<T>());
        
        List<T> providers = capabilities.get(mediaType);
        providers.add(provider);
    }
    
    private String[] getAnnotationValues(Class<?> clazz, Class<?> annotationClass) {
        String values[] = null;
        if (annotationClass.equals(ConsumeMime.class)) {
            ConsumeMime consumes = clazz.getAnnotation(ConsumeMime.class);
            if (consumes != null)
                values = consumes.value();
        } else if (annotationClass.equals(ProduceMime.class)) {
            ProduceMime produces = clazz.getAnnotation(ProduceMime.class);
            if (produces != null)
                values = produces.value();
        }
        return values;
    }
    
    // MessageBodyContext
    
    @SuppressWarnings("unchecked")
    public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> c, Type t, 
            Annotation[] as, 
            MediaType mediaType) {
        
        List<MediaType> searchTypes = createSearchList(mediaType);
        for (MediaType mt: searchTypes) {
            List<MessageBodyReader> readers = readerProviders.get(mt);
            if (readers==null)
                continue;
            for (MessageBodyReader p: readers) {
                if (p.isReadable(c, t, as))
                    return p;
            }
        }
        LOGGER.severe("A message body reader for Java type, " + c + 
                ", and MIME media type, " + mediaType + ", was not found");    
        return null;        
    }
    
    @SuppressWarnings("unchecked")
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> c, Type t,
            Annotation[] as,
            MediaType mediaType) {
        List<MediaType> searchTypes = createSearchList(mediaType);
        for (MediaType mt: searchTypes) {
            List<MessageBodyWriter> writers = writerProviders.get(mt);
            if (writers==null)
                continue;
            for (MessageBodyWriter p: writers) {
                if (p.isWriteable(c, t, as))
                    return p;
            }
        }
        
        LOGGER.severe("A message body writer for Java type, " + c + 
                ", and MIME media type, " + mediaType + ", was not found");
        return null;        
    }
    
    private List<MediaType> createSearchList(MediaType mediaType) {
        if (mediaType==null)
            return Arrays.asList(MediaTypeHelper.GENERAL_MEDIA_TYPE);
        else
            return Arrays.asList(mediaType, 
                    new MediaType(mediaType.getType(), MediaType.MEDIA_TYPE_WILDCARD), 
                    MediaTypeHelper.GENERAL_MEDIA_TYPE);
    }

    // MessageBodyWorkers
    
    public <T> List<MessageBodyReader<T>> getMessageBodyReaders(
            MediaType mediaType, Class<T> type, Type genericType, Annotation annotations[]) {
        return Collections.singletonList(getMessageBodyReader(type, genericType, 
                annotations, mediaType));
    }

    public <T> List<MessageBodyWriter<T>> getMessageBodyWriters(
            MediaType mediaType, Class<T> type, Type genericType, Annotation annotations[]) {
        return Collections.singletonList(getMessageBodyWriter(type, genericType,
                annotations, mediaType));
    }
}