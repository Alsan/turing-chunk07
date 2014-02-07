/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ode.bpel.compiler.api;

import org.apache.ode.bpel.o.OVarType;
import org.apache.ode.bpel.compiler.bom.Process;
import org.apache.ode.bpel.compiler.bom.Expression;

public interface ExpressionValidator {
    /**
     * Notifies process compilation began
     *
     * @param compilerContext
     */
    void bpelImportsLoaded(Process source, CompilerContext compilerContext) throws CompilationException;

    /**
     * Notifies process compilation completed.
     * Mainly for cleaning up resources.
     *
     * @param compilerContext
     */
    void bpelCompilationCompleted(Process source) throws CompilationException;

    /**
     * Validate given expression.
     * @param source It's a Query or Expression
     * @param requestedResultType It's OVarType or underlying Expression Validator's type.
     *     It may be null if there are no constrains for result type
     * @return Evaluated expression's type
     */
    Object validate(Expression source, OVarType rootNodeType, Object requestedResultType) throws CompilationException;

}
