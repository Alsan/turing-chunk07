#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

if [ -z "$QPID_HOME" ]; then
    export QPID_HOME=$(dirname $(dirname $(dirname $(readlink -f $0))))
    export PATH=${PATH}:${QPID_HOME}/bin
fi

# Parse arguements taking all - prefixed args as JAVA_OPTS
for arg in "$@"; do
    if [[ $arg == -java:* ]]; then
        JAVA_OPTS="${JAVA_OPTS}-`echo $arg|cut -d ':' -f 2`  "
    else
        ARGS="${ARGS}$arg "
    fi
done

# Set classpath to include Qpid jar with all required jars in manifest
QPID_LIBS=$QPID_HOME/lib/qpid-all.jar

# Set other variables used by the qpid-run script before calling
export JAVA=java        JAVA_VM=-server        JAVA_MEM=-Xmx1024m        QPID_CLASSPATH=$QPID_LIBS

. qpid-run -Xms256m -Dlog4j.configuration=file://${QPID_HOME}/etc/perftests.log4j -Dbadger.level=warn -Damqj.test.logging.level=info -Damqj.logging.level=warn org.apache.qpid.junit.extensions.TKTestRunner -n PQBT-TX-Qpid-01   -s[1000]   -c[1]                   -o $QPID_WORK/results -t testAsyncPingOk org.wso2.andes.client.ping.PingAsyncTestPerf   persistent=true  pubsub=false uniqueDests=true  numConsumers=0   transacted=true  consTransacted=true  consAckMode=0   commitBatchSize=1   batchSize=1000   messageSize=256   destinationCount=1  rate=0  maxPending=0  ${ARGS}
