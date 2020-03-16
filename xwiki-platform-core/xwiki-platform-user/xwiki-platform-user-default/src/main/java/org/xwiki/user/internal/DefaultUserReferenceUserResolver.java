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
package org.xwiki.user.internal;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.configuration.ConfigurationSource;
import org.xwiki.user.CurrentUserReference;
import org.xwiki.user.GuestUserReference;
import org.xwiki.user.SuperAdminUserReference;
import org.xwiki.user.User;
import org.xwiki.user.UserReference;
import org.xwiki.user.UserReferenceUserResolverType;
import org.xwiki.user.UserResolver;

/**
 * Document-based implementation of {@link UserResolver} which proxies to the specific UserResolver implementation
 * based on the type of the passed {@link UserReference}.
 *
 * @version $Id$
 * @since 12.2RC1
 */
@Component
@Singleton
public class DefaultUserReferenceUserResolver implements UserResolver<UserReference>
{
    @Inject
    @Named("context")
    private ComponentManager componentManager;

    @Inject
    @Named("superadminuser")
    private ConfigurationSource superAdminConfigurationSource;

    @Inject
    @Named("guestuser")
    private ConfigurationSource guestConfigurationSource;

    @Override
    public User resolve(UserReference userReference, Object... parameters)
    {
        User user;

        // Handle special cases
        UserReference normalizedUserReference = userReference;
        if (normalizedUserReference == null) {
            normalizedUserReference = CurrentUserReference.INSTANCE;
        }
        if (SuperAdminUserReference.INSTANCE == normalizedUserReference) {
            user = new SuperAdminUser(this.superAdminConfigurationSource);
        } else if (GuestUserReference.INSTANCE == normalizedUserReference) {
            user = new GuestUser(this.guestConfigurationSource);
        } else {
            user = resolveUserResolver(normalizedUserReference).resolve(normalizedUserReference, parameters);
        }
        return user;
    }

    private UserResolver<UserReference> resolveUserResolver(UserReference userReference)
    {
        try {
            return this.componentManager.getInstance(UserReferenceUserResolverType.INSTANCE,
                userReference.getClass().getName());
        } catch (ComponentLookupException e) {
            // If there's no resolver for the passed UserReference type, then the XWiki instance cannot work and thus
            // we need to fail hard and fast. Hence the runtime exception.
            throw new RuntimeException(String.format(
                "Failed to find user resolver for role [%s] and hint [%s]", UserReferenceUserResolverType.INSTANCE,
                userReference.getClass().getName()), e);
        }
    }
}