/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.xml.functional;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

import org.w3c.dom.Node;

public class W3CDocumentXPathNodeEvaluatorTestCase extends FunctionalTestCase
{
    private static final String XML_INPUT =
        "<root>" +
        "  <table>" +
        "    <name>African Coffee Table</name>" +
        "    <width>80</width>" +
        "    <length>120</length>" +
        "  </table>" +
        "</root>";

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/xml/w3c-dom-xpath-node-config.xml";
    }

    public void testW3CDocument() throws Exception
    {
        MuleClient client = new MuleClient();

        MuleMessage message = new DefaultMuleMessage(XML_INPUT);
        MuleMessage response = client.send("vm://test", message);
        assertNotNull(response);
        assertNotNull(response.getPayload());
        assertTrue(response.getPayload() instanceof Node);
    }
}