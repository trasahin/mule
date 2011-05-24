/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package net.webservicex;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.8
 * Tue May 11 18:53:01 ART 2010
 * Generated source version: 2.2.8
 * 
 */


@WebServiceClient(name = "StockQuote", 
                  wsdlLocation = "file:stockquote.wsdl",
                  targetNamespace = "http://www.webserviceX.NET/") 
public class StockQuote extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://www.webserviceX.NET/", "StockQuote");
    public final static QName StockQuoteHttpPost = new QName("http://www.webserviceX.NET/", "StockQuoteHttpPost");
    public final static QName StockQuoteSoap = new QName("http://www.webserviceX.NET/", "StockQuoteSoap");
    public final static QName StockQuoteSoap12 = new QName("http://www.webserviceX.NET/", "StockQuoteSoap12");
    public final static QName StockQuoteHttpGet = new QName("http://www.webserviceX.NET/", "StockQuoteHttpGet");
    static {
        URL url = null;
        try {
            url = new URL("file:stockquote.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from file:stockquote.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public StockQuote(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public StockQuote(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public StockQuote() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns StockQuoteHttpPost
     */
    @WebEndpoint(name = "StockQuoteHttpPost")
    public StockQuoteHttpPost getStockQuoteHttpPost() {
        return super.getPort(StockQuoteHttpPost, StockQuoteHttpPost.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StockQuoteHttpPost
     */
    @WebEndpoint(name = "StockQuoteHttpPost")
    public StockQuoteHttpPost getStockQuoteHttpPost(WebServiceFeature... features) {
        return super.getPort(StockQuoteHttpPost, StockQuoteHttpPost.class, features);
    }
    /**
     * 
     * @return
     *     returns StockQuoteSoap
     */
    @WebEndpoint(name = "StockQuoteSoap")
    public StockQuoteSoap getStockQuoteSoap() {
        return super.getPort(StockQuoteSoap, StockQuoteSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StockQuoteSoap
     */
    @WebEndpoint(name = "StockQuoteSoap")
    public StockQuoteSoap getStockQuoteSoap(WebServiceFeature... features) {
        return super.getPort(StockQuoteSoap, StockQuoteSoap.class, features);
    }
    /**
     * 
     * @return
     *     returns StockQuoteSoap
     */
    @WebEndpoint(name = "StockQuoteSoap12")
    public StockQuoteSoap getStockQuoteSoap12() {
        return super.getPort(StockQuoteSoap12, StockQuoteSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StockQuoteSoap
     */
    @WebEndpoint(name = "StockQuoteSoap12")
    public StockQuoteSoap getStockQuoteSoap12(WebServiceFeature... features) {
        return super.getPort(StockQuoteSoap12, StockQuoteSoap.class, features);
    }
    /**
     * 
     * @return
     *     returns StockQuoteHttpGet
     */
    @WebEndpoint(name = "StockQuoteHttpGet")
    public StockQuoteHttpGet getStockQuoteHttpGet() {
        return super.getPort(StockQuoteHttpGet, StockQuoteHttpGet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns StockQuoteHttpGet
     */
    @WebEndpoint(name = "StockQuoteHttpGet")
    public StockQuoteHttpGet getStockQuoteHttpGet(WebServiceFeature... features) {
        return super.getPort(StockQuoteHttpGet, StockQuoteHttpGet.class, features);
    }

}