/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.jexl2;

import org.apache.commons.jexl2.parser.ASTJexlScript;

/**
 * Instances of ExpressionImpl are created by the {@link JexlEngine},
 * and this is the default implementation of the {@link Expression} and
 * {@link Script} interface.
 * @since 1.0
 */
public class ExpressionImpl implements Expression, Script {
    /** The engine for this expression. */
    protected final JexlEngine jexl;
    /**
     * Original expression stripped from leading & trailing spaces.
     */
    protected final String expression;
    /**
     * The resulting AST we can interpret.
     */
    protected final ASTJexlScript script;


    /**
     * Do not let this be generally instantiated with a 'new'.
     *
     * @param engine the interpreter to evaluate the expression
     * @param expr the expression.
     * @param ref the parsed expression.
     */
    protected ExpressionImpl(JexlEngine engine, String expr, ASTJexlScript ref) {
        jexl = engine;
        expression = expr;
        script = ref;
    }

    /**
     * {@inheritDoc}
     */
    public Object evaluate(JexlContext context) {
        if (script.jjtGetNumChildren() < 1) {
            return null;
        }
        Interpreter interpreter = jexl.createInterpreter(context);
        interpreter.setArguments(script.getRegisters(), script.createArguments((Object[]) null));
        return interpreter.interpret(script.jjtGetChild(0));
    }

    /**
     * {@inheritDoc}
     */
    public String dump() {
        Debugger debug = new Debugger();
        boolean d = debug.debug(script);
        return debug.data() + (d? " /*" + debug.start() + ":" + debug.end() + "*/" : "/*?:?*/ ");
    }

    /**
     * {@inheritDoc}
     */
    public String getExpression() {
        return expression;
    }
    
    /**
     * Gets this script parameters.
     * @return the parameters or null
     */
    public String[] getParameters() {
        return script.getParameters();
    }
       
    /**
     * Gets this script local variables.
     * @return the local variables or null
     */
    public String[] getLocalVariables() {
        return script.getLocalVariables();
    }

    /**
     * Provide a string representation of the expression.
     *
     * @return the expression or blank if it's null.
     */
    @Override
    public String toString() {
        String expr = getExpression();
        return expr == null ? "" : expr;
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return toString();
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(JexlContext context) {
        Interpreter interpreter = jexl.createInterpreter(context);
        interpreter.setArguments(script.getRegisters(), script.createArguments((Object[]) null));
        return interpreter.interpret(script);
    }
    
    /**
     * {@inheritDoc}
     */
    public Object execute(JexlContext context, Object...args) {
        Interpreter interpreter = jexl.createInterpreter(context);
        interpreter.setArguments(script.getRegisters(), script.createArguments(args));
        return interpreter.interpret(script);
    }

}