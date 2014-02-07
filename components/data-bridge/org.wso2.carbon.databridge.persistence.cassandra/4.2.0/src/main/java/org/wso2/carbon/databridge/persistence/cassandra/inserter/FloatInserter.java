package org.wso2.carbon.databridge.persistence.cassandra.inserter;

import me.prettyprint.cassandra.serializers.FloatSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class FloatInserter implements TypeInserter {

    private final static StringSerializer stringSerializer = StringSerializer.get();
    private final static FloatSerializer floatSerializer = FloatSerializer.get();

    @Override
    public Mutator addDataToBatchInsertion(Object data, String streamColumnFamily,
                                           String columnName, String rowKey, Mutator<String> mutator) {
        Float floatVal = ((data) instanceof Double) ? ((Double) data).floatValue() : (Float) data;
        if (floatVal != null) {
            mutator.addInsertion(rowKey, streamColumnFamily,
                    HFactory.createColumn(columnName, floatVal, stringSerializer, floatSerializer));
        }
        return mutator;
    }
}
