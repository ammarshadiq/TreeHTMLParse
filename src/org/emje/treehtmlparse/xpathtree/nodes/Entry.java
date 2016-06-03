/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Ammar Shadiq
 *
 */

package org.emje.treehtmlparse.xpathtree.nodes;

public class Entry {
    public static final String XPATH_STRING_ENTRY = "xpath.string";
    public static final String XPATH_NORMALIZED_STRING_ENTRY = "xpath.string.normalized";
    public static final String XPATH_NODE_ENTRY = "xpath.node";
    public static final String XPATH_NODESET_ENTRY = "xpath.nodeset";
    public static final String XPATH_NUMBER_ENTRY = "xpath.number";
    public static final String XPATH_BOOLEAN_ENTRY = "xpath.boolean";
    public static final String STRING_ENTRY = "plain.string";
    public static final String XPATH_OUTLINK_NODESET_ENTRY = "xpath.outlink.nodeset";
    private boolean containsResult;
    private String xPathExpression;
    private String expressionType;
    private String resultString;
    private String[][] resultTable;
    private String[] resultList;

    public Entry() {
        this("", "xpath.string");
    }

    public Entry(String xPathExpression, String expressionType) {
        this.xPathExpression = xPathExpression;
        this.expressionType = expressionType;
    }

    public String getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

    public String getxPathExpression() {
        return xPathExpression;
    }

    public void setxPathExpression(String xPathExpression) {
        this.xPathExpression = xPathExpression;
    }

    /**
     * @return the resultString
     */
    public String getResultString() {
        return resultString;
    }

    /**
     * @param resultString the resultString to set
     */
    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

     /**
     * @return the containsResult
     */
    public boolean isContainsResult() {
        return containsResult;
    }

    /**
     * @param containsResult the containsResult to set
     */
    public void setContainsResult(boolean containsResult) {
        this.containsResult = containsResult;
    }

    public String[] getResultList() {
        return resultList;
    }

    public void setResultList(String[] resultList) {
        this.resultList = resultList;
    }

    public String[][] getResultTable() {
        return resultTable;
    }

    public void setResultTable(String[][] resultTable) {
        this.resultTable = resultTable;
    }

    @Override
    public String toString() {
        return this.getxPathExpression();
    }
}
