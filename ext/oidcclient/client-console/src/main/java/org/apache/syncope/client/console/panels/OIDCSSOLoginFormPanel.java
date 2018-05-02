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
package org.apache.syncope.client.console.panels;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.syncope.client.console.SyncopeConsoleSession;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxDropDownChoicePanel;
import org.apache.syncope.common.lib.to.OIDCProviderTO;
import org.apache.syncope.common.rest.api.service.OIDCProviderService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.UrlUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OIDCSSOLoginFormPanel extends SSOLoginFormPanel {

    private static final long serialVersionUID = 1154933731474854671L;

    private static final Logger LOG = LoggerFactory.getLogger(OIDCSSOLoginFormPanel.class);

    public OIDCSSOLoginFormPanel(final String id) {
        super(id);

        List<OIDCProviderTO> available =
                SyncopeConsoleSession.get().getAnonymousClient().getService(OIDCProviderService.class).list();

        final Model<OIDCProviderTO> model = new Model<>();
        AjaxDropDownChoicePanel<OIDCProviderTO> ops =
                new AjaxDropDownChoicePanel<>("ops", "OpenID Connect", model, false);
        ops.setChoices(available);
        ops.setChoiceRenderer(new IChoiceRenderer<OIDCProviderTO>() {

            private static final long serialVersionUID = 1814750973898916102L;

            @Override
            public Object getDisplayValue(final OIDCProviderTO object) {
                return object.getName();
            }

            @Override
            public String getIdValue(final OIDCProviderTO object, final int index) {
                return object.getName();
            }

            @Override
            public OIDCProviderTO getObject(final String id,
                    final IModel<? extends List<? extends OIDCProviderTO>> choices) {
                return IterableUtils.find(choices.getObject(), new Predicate<OIDCProviderTO>() {

                    @Override
                    public boolean evaluate(final OIDCProviderTO object) {
                        return object.getName().equals(id);
                    }
                });
            }
        });

        ops.getField().add(new AjaxFormComponentUpdatingBehavior(Constants.ON_CHANGE) {

            private static final long serialVersionUID = -1107858522700306810L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                if (model.getObject() != null) {
                    try {
                        RequestCycle.get().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(
                                UrlUtils.rewriteToContextRelative("oidcclient/login?op="
                                        + URLEncoder.encode(
                                                model.getObject().getName(), StandardCharsets.UTF_8.name()),
                                        RequestCycle.get())));
                    } catch (Exception e) {
                        LOG.error("Could not redirect to the selected OP {}", model.getObject().getName(), e);
                    }
                }
            }
        });
        ops.setOutputMarkupPlaceholderTag(true);
        ops.setVisible(!available.isEmpty());
        add(ops);
    }

}
