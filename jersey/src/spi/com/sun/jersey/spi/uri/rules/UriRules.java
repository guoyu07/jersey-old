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

package com.sun.jersey.spi.uri.rules;

import java.util.Iterator;
import java.util.List;

/**
 * A collection of URI rules that can be matched using associated patterns.
 * <p>
 * The precedence of the rules and the type of patterns is specified by an 
 * implementation of this interface.
 * 
 * @author Paul.Sandoz@Sun.Com
 */
public interface UriRules<R> {
    /**
     * Match a URI path to the collection of rules and iterate over
     * the matching rules.
     *
     * @param path the URI path to be matched
     * @param capturingGroupValues the list to store the values of a pattern's 
     *        capturing groups. This list will be cleared and modified each time 
     *        {@link Iterator#next} is called according to the pattern 
     *        associated with the returned rule. The values are stored in
     *        the same order as the pattern's capturing groups.
     * @return an iterator of matching rules
     */
    Iterator<R> match(CharSequence path, List<String> capturingGroupValues);
}