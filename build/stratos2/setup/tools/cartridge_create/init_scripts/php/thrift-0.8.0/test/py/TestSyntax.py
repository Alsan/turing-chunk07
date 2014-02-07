#!/usr/bin/env python

#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#

import sys, glob
from optparse import OptionParser
parser = OptionParser()
parser.add_option('--genpydir', type='string', dest='genpydir', default='gen-py')
options, args = parser.parse_args()
del sys.argv[1:] # clean up hack so unittest doesn't complain
sys.path.insert(0, options.genpydir)
sys.path.insert(0, glob.glob('../../lib/py/build/lib.*')[0])

# Just import these generated files to make sure they are syntactically valid
from DebugProtoTest import EmptyService
from DebugProtoTest import Inherited
