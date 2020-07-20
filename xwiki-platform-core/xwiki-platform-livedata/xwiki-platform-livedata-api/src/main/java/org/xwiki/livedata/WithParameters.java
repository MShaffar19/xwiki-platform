/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.livedata;

import java.util.Map;

import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.stability.Unstable;

/**
 * Interface used by components that have parameters. Should be used only for components that have
 * {@link ComponentInstantiationStrategy#PER_LOOKUP}.
 * 
 * @version $Id$
 * @since 12.6RC1
 */
@Unstable
public interface WithParameters
{
    /**
     * @return the parameters of this component
     */
    Map<String, Object> getParameters();
}