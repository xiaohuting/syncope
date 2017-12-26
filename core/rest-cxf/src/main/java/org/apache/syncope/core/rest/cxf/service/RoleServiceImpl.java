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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import org.apache.syncope.common.lib.to.RoleTO;
import org.apache.syncope.common.rest.api.RESTHeaders;
import org.apache.syncope.common.rest.api.service.RoleService;
import org.apache.syncope.core.logic.RoleLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends AbstractServiceImpl implements RoleService {

    @Autowired
    private RoleLogic logic;

    @Override
    public List<RoleTO> list() {
        return logic.list();
    }

    @Override
    public RoleTO read(final String key) {
        return logic.read(key);
    }

    @Override
    public Response create(final RoleTO roleTO) {
        RoleTO created = logic.create(roleTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getKey()).build();
        return Response.created(location).
                header(RESTHeaders.RESOURCE_KEY, created.getKey()).
                build();
    }

    @Override
    public Response update(final RoleTO roleTO) {
        logic.update(roleTO);
        return Response.noContent().build();
    }

    @Override
    public Response delete(final String key) {
        logic.delete(key);
        return Response.noContent().build();
    }

    @Override
    public Response getConsoleLayoutInfo(final String key) {
        String template = logic.getConsoleLayoutInfo(key);
        StreamingOutput sout = (os) -> os.write(template.getBytes());

        return Response.ok(sout).
                type(MediaType.APPLICATION_JSON_TYPE).
                build();
    }

    @Override
    public Response setConsoleLayoutInfo(final String key, final InputStream consoleLayoutIn) {
        try {
            logic.setConsoleLayoutInfo(key, IOUtils.toString(consoleLayoutIn, StandardCharsets.UTF_8.name()));
            return Response.noContent().build();
        } catch (final IOException e) {
            LOG.error("While setting console layout info for role {}", key, e);
            throw new InternalServerErrorException("Could not read entity", e);
        }
    }

    @Override
    public Response removeConsoleLayoutInfo(final String key) {
        logic.setConsoleLayoutInfo(key, null);
        return Response.noContent().build();
    }

}
