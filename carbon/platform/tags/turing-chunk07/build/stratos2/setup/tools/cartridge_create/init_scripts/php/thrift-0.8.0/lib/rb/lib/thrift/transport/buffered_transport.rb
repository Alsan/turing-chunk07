# encoding: ascii-8bit
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

module Thrift
  class BufferedTransport < BaseTransport
    DEFAULT_BUFFER = 4096
    
    def initialize(transport)
      @transport = transport
      @wbuf = ''
      @rbuf = ''
      @index = 0
    end

    def open?
      return @transport.open?
    end

    def open
      @transport.open
    end

    def close
      flush
      @transport.close
    end

    def read(sz)
      @index += sz
      ret = @rbuf.slice(@index - sz, sz) || ''

      if ret.length == 0
        @rbuf = @transport.read([sz, DEFAULT_BUFFER].max)
        @index = sz
        ret = @rbuf.slice(0, sz) || ''
      end

      ret
    end

    def read_byte
      # If the read buffer is exhausted, try to read up to DEFAULT_BUFFER more bytes into it.
      if @index >= @rbuf.size
        @rbuf = @transport.read(DEFAULT_BUFFER)
        @index = 0
      end

      # The read buffer has some data now, read a single byte. Using get_string_byte() avoids
      # allocating a temp string of size 1 unnecessarily.
      @index += 1
      return ::Thrift::TransportUtils.get_string_byte(@rbuf, @index - 1)
    end

    def read_into_buffer(buffer, size)
      i = 0
      while i < size
        # If the read buffer is exhausted, try to read up to DEFAULT_BUFFER more bytes into it.
        if @index >= @rbuf.size
          @rbuf = @transport.read(DEFAULT_BUFFER)
          @index = 0
        end

        # The read buffer has some data now, so copy bytes over to the output buffer.
        byte = ::Thrift::TransportUtils.get_string_byte(@rbuf, @index)
        ::Thrift::TransportUtils.set_string_byte(buffer, i, byte)
        @index += 1
        i += 1
      end
      i
    end

    def write(buf)
      @wbuf << buf
    end

    def flush
      if @wbuf != ''
        @transport.write(@wbuf)
        @wbuf = ''
      end
      
      @transport.flush
    end
  end

  class BufferedTransportFactory < BaseTransportFactory
    def get_transport(transport)
      return BufferedTransport.new(transport)
    end
  end
end