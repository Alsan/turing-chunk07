/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ql.optimizer.pcr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.hadoop.hive.ql.exec.FunctionRegistry;
import org.apache.hadoop.hive.ql.lib.DefaultGraphWalker;
import org.apache.hadoop.hive.ql.lib.DefaultRuleDispatcher;
import org.apache.hadoop.hive.ql.lib.Dispatcher;
import org.apache.hadoop.hive.ql.lib.GraphWalker;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.lib.NodeProcessor;
import org.apache.hadoop.hive.ql.lib.NodeProcessorCtx;
import org.apache.hadoop.hive.ql.lib.Rule;
import org.apache.hadoop.hive.ql.lib.RuleRegExp;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.metadata.Partition;
import org.apache.hadoop.hive.ql.metadata.Table;
import org.apache.hadoop.hive.ql.optimizer.ppr.PartExprEvalUtils;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.plan.ExprNodeColumnDesc;
import org.apache.hadoop.hive.ql.plan.ExprNodeConstantDesc;
import org.apache.hadoop.hive.ql.plan.ExprNodeDesc;
import org.apache.hadoop.hive.ql.plan.ExprNodeFieldDesc;
import org.apache.hadoop.hive.ql.plan.ExprNodeGenericFuncDesc;
import org.apache.hadoop.hive.ql.plan.ExprNodeNullDesc;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;

/**
 * Expression processor factory for partition condition removing. Each processor tries to
 * calculate a result vector from its children's result vectors.
 * Each element is the result for one of the pruned partitions.
 * It also generates node by Modifying expr trees with partition conditions removed
 */
public final class PcrExprProcFactory {
  static Object evalExprWithPart(ExprNodeDesc expr, Partition p) throws SemanticException {
    StructObjectInspector rowObjectInspector;
    Table tbl = p.getTable();
    LinkedHashMap<String, String> partSpec = p.getSpec();

    try {
      rowObjectInspector = (StructObjectInspector) tbl
          .getDeserializer().getObjectInspector();
    } catch (SerDeException e) {
      throw new SemanticException(e);
    }

    try {
      return PartExprEvalUtils.evalExprWithPart(expr, partSpec, rowObjectInspector);
    } catch (HiveException e) {
      throw new SemanticException(e);
    }
  }

  static Boolean ifResultsAgree(Boolean[] resultVector) {
    Boolean result = null;
    for (Boolean b : resultVector) {
      if (b == null) {
        return null;
      } else if (result == null) {
        result = b;
      } else if (!result.equals(b)) {
        return null;
      }
    }
    return result;
  }

  static Object ifResultsAgree(Object[] resultVector) {
    Object result = null;
    for (Object b : resultVector) {
      if (b == null) {
        return null;
      } else if (result == null) {
        result = b;
      } else if (!result.equals(b)) {
        return null;
      }
    }
    return result;
  }

  static NodeInfoWrapper getResultWrapFromResults(Boolean[] results,
      ExprNodeGenericFuncDesc fd, Object[] nodeOutputs) {
    Boolean ifAgree = ifResultsAgree(results);
    if (ifAgree == null) {
      return new NodeInfoWrapper(WalkState.DIVIDED, results,
          getOutExpr(fd, nodeOutputs));
    } else if (ifAgree.booleanValue() == true) {
      return new NodeInfoWrapper(WalkState.TRUE, null,
          new ExprNodeConstantDesc(fd.getTypeInfo(), Boolean.TRUE));
    } else {
      return new NodeInfoWrapper(WalkState.FALSE, null,
          new ExprNodeConstantDesc(fd.getTypeInfo(), Boolean.FALSE));

    }
  }

  private PcrExprProcFactory() {
    // prevent instantiation
  }

  static Boolean opAnd(Boolean op1, Boolean op2) {
    if (op1.equals(Boolean.FALSE) || op2.equals(Boolean.FALSE)) {
      return Boolean.FALSE;
    }
    if (op1 == null || op2 == null) {
      return null;
    }
    return Boolean.TRUE;
  }

  static Boolean opOr(Boolean op1, Boolean op2) {
    if (op1.equals(Boolean.TRUE) || op2.equals(Boolean.TRUE)) {
      return Boolean.TRUE;
    }
    if (op1 == null || op2 == null) {
      return null;
    }
    return Boolean.FALSE;
  }

  static Boolean opNot(Boolean op) {
    if (op.equals(Boolean.TRUE)) {
      return Boolean.FALSE;
    }
    if (op.equals(Boolean.FALSE)) {
      return Boolean.TRUE;
    }
    return null;
  }

  public enum WalkState {
    PART_COL, TRUE, FALSE, CONSTANT, UNKNOWN, DIVIDED
  }

  public static class NodeInfoWrapper {
    public NodeInfoWrapper(WalkState state, Boolean[] resultVector, ExprNodeDesc outExpr) {
      super();
      this.state = state;
      ResultVector = resultVector;
      this.outExpr = outExpr;
    }

    WalkState state;
    public Boolean[] ResultVector;
    public ExprNodeDesc outExpr;
  }

  /**
   * Processor for column expressions.
   */
  public static class ColumnExprProcessor implements NodeProcessor {

    @Override
    public Object process(Node nd, Stack<Node> stack, NodeProcessorCtx procCtx,
        Object... nodeOutputs) throws SemanticException {
      ExprNodeColumnDesc cd = (ExprNodeColumnDesc) nd;
      PcrExprProcCtx epc = (PcrExprProcCtx) procCtx;
      if (cd.getTabAlias().equalsIgnoreCase(epc.getTabAlias())
          && cd.getIsPartitionColOrVirtualCol()) {
        return new NodeInfoWrapper(WalkState.PART_COL, null, cd);
      } else {
        return new NodeInfoWrapper(WalkState.UNKNOWN, null, cd);
      }
    }
  }

  public static ExprNodeGenericFuncDesc getOutExpr(
      ExprNodeGenericFuncDesc funcExpr, Object[] nodeOutputs) {
    ArrayList<ExprNodeDesc> children = new ArrayList<ExprNodeDesc>();
    if (nodeOutputs != null) {
      for (Object child : nodeOutputs) {
        NodeInfoWrapper wrapper = (NodeInfoWrapper) child;
        children.add(wrapper.outExpr);
      }
    }
    funcExpr.setChildExprs(children);

    return funcExpr;
  }

  /**
   * Processor for Generic functions
   *
   * If it is AND, OR or NOT, we replace the node to be the constant true or
   * false if we are sure the result from children, or cut one of the child
   * if we know partial results. In case of both child has a result vector,
   * we calculate the result vector for the node. If all partitions agree on
   * a result, we replace the node with constant true or false. Otherwise, we
   * pass the vector result. For other Generic functions, if it is non-deterministic
   * we simply pass it (with children adjusted based on results from children).
   * If it is deterministic, we evaluate result vector if any of the children
   * is partition column. Otherwise, we pass it as it is.
   */
  public static class GenericFuncExprProcessor implements NodeProcessor {
    @Override
    public Object process(Node nd, Stack<Node> stack, NodeProcessorCtx procCtx,
        Object... nodeOutputs) throws SemanticException {
      PcrExprProcCtx ctx = (PcrExprProcCtx) procCtx;
      ExprNodeGenericFuncDesc fd = (ExprNodeGenericFuncDesc) nd;

      if (FunctionRegistry.isOpNot(fd)) {
        assert (nodeOutputs.length == 1);
        NodeInfoWrapper wrapper = (NodeInfoWrapper) nodeOutputs[0];
        if (wrapper.state == WalkState.TRUE) {
          ExprNodeConstantDesc falseDesc = new ExprNodeConstantDesc(
              wrapper.outExpr.getTypeInfo(), Boolean.FALSE);
          return new NodeInfoWrapper(WalkState.FALSE, null, falseDesc);
        } else if (wrapper.state == WalkState.FALSE) {
          ExprNodeConstantDesc trueDesc = new ExprNodeConstantDesc(
              wrapper.outExpr.getTypeInfo(), Boolean.TRUE);
          return new NodeInfoWrapper(WalkState.TRUE, null, trueDesc);
        } else if (wrapper.state == WalkState.DIVIDED) {
          Boolean[] results = new Boolean[ctx.getPartList().size()];
          for (int i = 0; i < ctx.getPartList().size(); i++) {
            results[i] = opNot(wrapper.ResultVector[i]);
          }
          return new NodeInfoWrapper(WalkState.DIVIDED, results,
              getOutExpr(fd, nodeOutputs));
        } else {
          return new NodeInfoWrapper(wrapper.state, null,
              getOutExpr(fd, nodeOutputs));
        }
      } else if (FunctionRegistry.isOpAnd(fd)) {
        assert (nodeOutputs.length == 2);
        NodeInfoWrapper c1 = (NodeInfoWrapper)nodeOutputs[0];
        NodeInfoWrapper c2 = (NodeInfoWrapper)nodeOutputs[1];

        if (c1.state == WalkState.FALSE) {
          return c1;
        } else if (c2.state == WalkState.FALSE) {
          return c2;
        } else if (c1.state == WalkState.TRUE) {
          return c2;
        } else if (c2.state == WalkState.TRUE) {
          return c1;
        } else if (c1.state == WalkState.UNKNOWN || c2.state == WalkState.UNKNOWN) {
          return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
        } else if (c1.state == WalkState.DIVIDED && c2.state == WalkState.DIVIDED) {
          Boolean[] results = new Boolean[ctx.getPartList().size()];
          for (int i = 0; i < ctx.getPartList().size(); i++) {
            results[i] = opAnd(c1.ResultVector[i], c2.ResultVector[i]);
          }
          return getResultWrapFromResults(results, fd, nodeOutputs);
        }
        return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
      } else if (FunctionRegistry.isOpOr(fd)) {
        assert (nodeOutputs.length == 2);
        NodeInfoWrapper c1 = (NodeInfoWrapper)nodeOutputs[0];
        NodeInfoWrapper c2 = (NodeInfoWrapper)nodeOutputs[1];

        if (c1.state == WalkState.TRUE) {
          return c1;
        } else if (c2.state == WalkState.TRUE) {
          return c2;
        } else if (c1.state == WalkState.FALSE) {
          return c2;
        } else if (c2.state == WalkState.FALSE) {
          return c1;
        } else if (c1.state == WalkState.UNKNOWN || c2.state == WalkState.UNKNOWN) {
          return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
        } else if (c1.state == WalkState.DIVIDED && c2.state == WalkState.DIVIDED) {
          Boolean[] results = new Boolean[ctx.getPartList().size()];
          for (int i = 0; i < ctx.getPartList().size(); i++) {
            results[i] = opOr(c1.ResultVector[i], c2.ResultVector[i]);
          }
          return getResultWrapFromResults(results, fd, nodeOutputs);
        }
        return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
      } else if (!FunctionRegistry.isDeterministic(fd.getGenericUDF())) {
        // If it's a non-deterministic UDF, set unknown to true
        return new NodeInfoWrapper(WalkState.UNKNOWN, null,
            getOutExpr(fd, nodeOutputs));
      } else {
        // If any child is unknown, set unknown to true
        boolean has_part_col = false;
        for (Object child : nodeOutputs) {
          NodeInfoWrapper wrapper = (NodeInfoWrapper) child;
          if (wrapper.state == WalkState.UNKNOWN) {
            return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
          } else if (wrapper.state == WalkState.PART_COL) {
            has_part_col = true;
          }
        }

        if (has_part_col) {
          //  we need to evaluate result for every pruned partition
          if (fd.getTypeInfo().equals(TypeInfoFactory.booleanTypeInfo)) {
            // if the return type of the GenericUDF is boolean and all partitions agree on
            // a result, we update the state of the node to be TRUE of FALSE
            Boolean[] results = new Boolean[ctx.getPartList().size()];
            for (int i = 0; i < ctx.getPartList().size(); i++) {
              results[i] = (Boolean) evalExprWithPart(fd, ctx.getPartList().get(i));
            }
            return getResultWrapFromResults(results, fd, nodeOutputs);
          }

          // the case that return type of the GenericUDF is not boolean, and if not all partition
          // agree on result, we make the node UNKNOWN. If they all agree, we replace the node
          // to be a CONSTANT node with value to be the agreed result.
          Object[] results = new Object[ctx.getPartList().size()];
          for (int i = 0; i < ctx.getPartList().size(); i++) {
            results[i] = evalExprWithPart(fd, ctx.getPartList().get(i));
          }
          Object result = ifResultsAgree(results);
          if (result == null) {
            // if the result is not boolean and not all partition agree on the
            // result, we don't remove the condition. Potentially, it can miss
            // the case like "where ds % 3 == 1 or ds % 3 == 2"
            // TODO: handle this case by making result vector to handle all
            // constant values.
            return new NodeInfoWrapper(WalkState.UNKNOWN, null, getOutExpr(fd, nodeOutputs));
          }
          return new NodeInfoWrapper(WalkState.CONSTANT, null,
              new ExprNodeConstantDesc(fd.getTypeInfo(), result));
        }

        return new NodeInfoWrapper(WalkState.CONSTANT, null, getOutExpr(fd, nodeOutputs));
      }
    }
  };

  /**
   * FieldExprProcessor.
   *
   */
  public static class FieldExprProcessor implements NodeProcessor {

    @Override
    public Object process(Node nd, Stack<Node> stack, NodeProcessorCtx procCtx,
        Object... nodeOutputs) throws SemanticException {
      ExprNodeFieldDesc fnd = (ExprNodeFieldDesc) nd;
      boolean unknown = false;
      int idx = 0;
      for (Object child : nodeOutputs) {
        NodeInfoWrapper wrapper = (NodeInfoWrapper) child;
        if (wrapper.state == WalkState.UNKNOWN) {
          unknown = true;
        }
      }

      assert (idx == 0);

      if (unknown) {
        return new NodeInfoWrapper(WalkState.UNKNOWN, null, fnd);
      } else {
        return new NodeInfoWrapper(WalkState.CONSTANT, null, fnd);
      }
    }

  }

  /**
   * Processor for constants and null expressions. For such expressions the
   * processor simply returns.
   */
  public static class DefaultExprProcessor implements NodeProcessor {

    @Override
    public Object process(Node nd, Stack<Node> stack, NodeProcessorCtx procCtx,
        Object... nodeOutputs) throws SemanticException {
      if (nd instanceof ExprNodeConstantDesc || nd instanceof ExprNodeNullDesc) {
        return new NodeInfoWrapper(WalkState.CONSTANT, null,
            (ExprNodeDesc) nd);
      }
      assert (false);
      return null;
    }
  }

  public static NodeProcessor getDefaultExprProcessor() {
    return new DefaultExprProcessor();
  }

  public static NodeProcessor getGenericFuncProcessor() {
    return new GenericFuncExprProcessor();
  }

  public static NodeProcessor getFieldProcessor() {
    return new FieldExprProcessor();
  }

  public static NodeProcessor getColumnProcessor() {
    return new ColumnExprProcessor();
  }

  /**
   * Remove partition conditions when necessary from the the expression tree.
   *
   * @param tabAlias
   *          the table alias
   * @param parts
   *          the list of all pruned partitions for the
   * @param pred
   *          expression tree of the target filter operator
   * @return the node information of the root expression
   * @throws SemanticException
   */
  public static NodeInfoWrapper walkExprTree(
      String tabAlias, ArrayList<Partition> parts, ExprNodeDesc pred)
      throws SemanticException {
    // Create the walker, the rules dispatcher and the context.
    PcrExprProcCtx pprCtx = new PcrExprProcCtx(tabAlias, parts);

    Map<Rule, NodeProcessor> exprRules = new LinkedHashMap<Rule, NodeProcessor>();
    exprRules.put(
        new RuleRegExp("R1", ExprNodeColumnDesc.class.getName() + "%"),
        getColumnProcessor());
    exprRules.put(
        new RuleRegExp("R2", ExprNodeFieldDesc.class.getName() + "%"),
        getFieldProcessor());
    exprRules.put(new RuleRegExp("R5", ExprNodeGenericFuncDesc.class.getName()
        + "%"), getGenericFuncProcessor());

    // The dispatcher fires the processor corresponding to the closest matching
    // rule and passes the context along
    Dispatcher disp = new DefaultRuleDispatcher(getDefaultExprProcessor(),
        exprRules, pprCtx);
    GraphWalker egw = new DefaultGraphWalker(disp);

    List<Node> startNodes = new ArrayList<Node>();
    startNodes.add(pred);

    HashMap<Node, Object> outputMap = new HashMap<Node, Object>();
    egw.startWalking(startNodes, outputMap);

    // Return the wrapper of the root node
    return (NodeInfoWrapper) outputMap.get(pred);
  }

}
