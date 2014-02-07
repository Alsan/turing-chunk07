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
package org.apache.cassandra.dht;

import java.util.Collections;
import java.util.List;

import org.apache.cassandra.db.RowPosition;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.Pair;

/**
 * AbstractBounds containing only its left endpoint: [left, right).  Used by CQL key >= X AND key < Y range scans.
 */
public class IncludingExcludingBounds<T extends RingPosition> extends AbstractBounds<T>
{
    public IncludingExcludingBounds(T left, T right)
    {
        this(left, right, StorageService.getPartitioner());
    }

    IncludingExcludingBounds(T left, T right, IPartitioner partitioner)
    {
        super(left, right, partitioner);
        // unlike a Range, an IncludingExcludingBounds may not wrap, nor have
        // right == left unless the right is the min token
        assert left.compareTo(right) < 0 || right.isMinimum(partitioner) : "[" + left + "," + right + ")";
    }

    public boolean contains(T position)
    {
        return (Range.contains(left, right, position) || left.equals(position)) && !right.equals(position);
    }

    public Pair<AbstractBounds<T>, AbstractBounds<T>> split(T position)
    {
        assert contains(position);
        AbstractBounds<T> lb = new Bounds<T>(left, position, partitioner);
        AbstractBounds<T> rb = new ExcludingBounds<T>(position, right, partitioner);
        return new Pair<AbstractBounds<T>, AbstractBounds<T>>(lb, rb);
    }

    public List<? extends AbstractBounds<T>> unwrap()
    {
        // IncludingExcludingBounds objects never wrap
        return Collections.<AbstractBounds<T>>singletonList(this);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof IncludingExcludingBounds))
            return false;
        IncludingExcludingBounds<T> rhs = (IncludingExcludingBounds<T>)o;
        return left.equals(rhs.left) && right.equals(rhs.right);
    }

    @Override
    public String toString()
    {
        return "(" + left + "," + right + ")";
    }

    /**
     * Compute a bounds of keys corresponding to a given bounds of token.
     */
    private static IncludingExcludingBounds<RowPosition> makeRowBounds(Token left, Token right, IPartitioner partitioner)
    {
        return new IncludingExcludingBounds<RowPosition>(left.maxKeyBound(partitioner), right.minKeyBound(partitioner), partitioner);
    }

    public AbstractBounds<RowPosition> toRowBounds()
    {
        return (left instanceof Token) ? makeRowBounds((Token)left, (Token)right, partitioner) : (IncludingExcludingBounds<RowPosition>)this;
    }

    public AbstractBounds<Token> toTokenBounds()
    {
        return (left instanceof RowPosition) ? new IncludingExcludingBounds<Token>(((RowPosition)left).getToken(), ((RowPosition)right).getToken(), partitioner) : (IncludingExcludingBounds<Token>)this;
    }
}
