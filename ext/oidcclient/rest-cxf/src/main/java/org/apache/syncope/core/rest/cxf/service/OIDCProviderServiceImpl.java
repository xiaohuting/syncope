/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.core.rest.cxf.service;

import java.net.URI;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.apache.syncope.common.lib.to.OIDCProviderTO;
import org.apache.syncope.common.rest.api.RESTHeaders;
import org.apache.syncope.common.rest.api.service.OIDCProviderService;
import org.apache.syncope.core.logic.OIDCProviderLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OIDCProviderServiceImpl extends AbstractServiceImpl implements OIDCProviderService {

    @Autowired
    private OIDCProviderLogic logic;

    @Override
    public Set<String> getActionsClasses() {
        return logic.getActionsClasses();
    }

    @Override
    public Response create(final OIDCProviderTO oidcProviderTO) {
        String created = logic.create(oidcProviderTO);

        URI location = uriInfo.getAbsolutePathBuilder().path(created).build();
        return Response.created(location).
                header(RESTHeaders.RESOURCE_KEY, created).
                build();
    }

    @Override
    public Response createFromDiscovery(final OIDCProviderTO oidcProviderTO) {
        String created = logic.createFromDiscovery(oidcProviderTO);

        URI location = uriInfo.getAbsolutePathBuilder().path(created).build();
        return Response.created(location).
                header(RESTHeaders.RESOURCE_KEY, created).
                build();
    }

    @Override
    public List<OIDCProviderTO> list() {
        return logic.list();

    }

    @Override
    public OIDCProviderTO read(final String key) {
        return logic.read(key);
    }

    @Override
    public void update(final OIDCProviderTO oidcProviderTO) {
        logic.update(oidcProviderTO);
    }

    @Override
    public void delete(final String key) {
        logic.delete(key);
    }
}
