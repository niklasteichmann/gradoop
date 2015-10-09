/*
 * This file is part of Gradoop.
 *
 * Gradoop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gradoop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gradoop.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gradoop.model.impl.operators.unary;

import org.gradoop.model.FlinkTestBase;
import org.gradoop.model.impl.LogicalGraph;
import org.gradoop.model.impl.functions.UnaryFunction;
import org.gradoop.model.impl.pojo.DefaultEdgeData;
import org.gradoop.model.impl.pojo.DefaultGraphData;
import org.gradoop.model.impl.pojo.DefaultVertexData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class LogicalGraphAggregateTest extends FlinkTestBase {

  public LogicalGraphAggregateTest(TestExecutionMode mode) {
    super(mode);
  }

  @Test
  public void aggregateEdgeCountTest() throws Exception {

    LogicalGraph<DefaultVertexData, DefaultEdgeData, DefaultGraphData>
      forumGraph = getGraphStore().getGraph(3L);
    final String aggPropertyKey = "eCount";

    UnaryFunction<LogicalGraph<DefaultVertexData, DefaultEdgeData,
      DefaultGraphData>, Long>
      aggregateFunc =
      new UnaryFunction<LogicalGraph<DefaultVertexData, DefaultEdgeData,
        DefaultGraphData>, Long>() {
        @Override
        public Long execute(
          LogicalGraph<DefaultVertexData, DefaultEdgeData, DefaultGraphData>
            entity) throws
          Exception {
          return entity.getEdges().count();
        }
      };

    LogicalGraph<DefaultVertexData, DefaultEdgeData, DefaultGraphData>
      newGraph = forumGraph.aggregate(aggPropertyKey, aggregateFunc);

    assertNotNull("graph was null", newGraph);
    assertEquals("wrong property count", 1, newGraph.getPropertyCount());
    assertEquals("wrong property value", 4L,
      newGraph.getProperty(aggPropertyKey));

    // original graph needs to be unchanged
    assertEquals("wrong property count", 0L, forumGraph.getPropertyCount());
  }
}