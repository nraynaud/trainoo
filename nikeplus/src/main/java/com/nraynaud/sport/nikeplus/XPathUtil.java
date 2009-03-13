package com.nraynaud.sport.nikeplus;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class XPathUtil {
    static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private XPathUtil() {
    }

    static XPathExpression compile(final String expression) {
        try {
            return XPATH.compile(expression);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
