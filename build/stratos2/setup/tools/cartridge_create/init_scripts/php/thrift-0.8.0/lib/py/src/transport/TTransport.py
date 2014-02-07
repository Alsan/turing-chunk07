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

from cStringIO import StringIO
from struct import pack,unpack
from thrift.Thrift import TException

class TTransportException(TException):

  """Custom Transport Exception class"""

  UNKNOWN = 0
  NOT_OPEN = 1
  ALREADY_OPEN = 2
  TIMED_OUT = 3
  END_OF_FILE = 4

  def __init__(self, type=UNKNOWN, message=None):
    TException.__init__(self, message)
    self.type = type

class TTransportBase:

  """Base class for Thrift transport layer."""

  def isOpen(self):
    pass

  def open(self):
    pass

  def close(self):
    pass

  def read(self, sz):
    pass

  def readAll(self, sz):
    buff = ''
    have = 0
    while (have < sz):
      chunk = self.read(sz-have)
      have += len(chunk)
      buff += chunk

      if len(chunk) == 0:
        raise EOFError()

    return buff

  def write(self, buf):
    pass

  def flush(self):
    pass

# This class should be thought of as an interface.
class CReadableTransport:
  """base class for transports that are readable from C"""

  # TODO(dreiss): Think about changing this interface to allow us to use
  #               a (Python, not c) StringIO instead, because it allows
  #               you to write after reading.

  # NOTE: This is a classic class, so properties will NOT work
  #       correctly for setting.
  @property
  def cstringio_buf(self):
    """A cStringIO buffer that contains the current chunk we are reading."""
    pass

  def cstringio_refill(self, partialread, reqlen):
    """Refills cstringio_buf.

    Returns the currently used buffer (which can but need not be the same as
    the old cstringio_buf). partialread is what the C code has read from the
    buffer, and should be inserted into the buffer before any more reads.  The
    return value must be a new, not borrowed reference.  Something along the
    lines of self._buf should be fine.

    If reqlen bytes can't be read, throw EOFError.
    """
    pass

class TServerTransportBase:

  """Base class for Thrift server transports."""

  def listen(self):
    pass

  def accept(self):
    pass

  def close(self):
    pass

class TTransportFactoryBase:

  """Base class for a Transport Factory"""

  def getTransport(self, trans):
    return trans

class TBufferedTransportFactory:

  """Factory transport that builds buffered transports"""

  def getTransport(self, trans):
    buffered = TBufferedTransport(trans)
    return buffered


class TBufferedTransport(TTransportBase,CReadableTransport):

  """Class that wraps another transport and buffers its I/O.

  The implementation uses a (configurable) fixed-size read buffer
  but buffers all writes until a flush is performed.
  """

  DEFAULT_BUFFER = 4096

  def __init__(self, trans, rbuf_size = DEFAULT_BUFFER):
    self.__trans = trans
    self.__wbuf = StringIO()
    self.__rbuf = StringIO("")
    self.__rbuf_size = rbuf_size

  def isOpen(self):
    return self.__trans.isOpen()

  def open(self):
    return self.__trans.open()

  def close(self):
    return self.__trans.close()

  def read(self, sz):
    ret = self.__rbuf.read(sz)
    if len(ret) != 0:
      return ret

    self.__rbuf = StringIO(self.__trans.read(max(sz, self.__rbuf_size)))
    return self.__rbuf.read(sz)

  def write(self, buf):
    self.__wbuf.write(buf)

  def flush(self):
    out = self.__wbuf.getvalue()
    # reset wbuf before write/flush to preserve state on underlying failure
    self.__wbuf = StringIO()
    self.__trans.write(out)
    self.__trans.flush()

  # Implement the CReadableTransport interface.
  @property
  def cstringio_buf(self):
    return self.__rbuf

  def cstringio_refill(self, partialread, reqlen):
    retstring = partialread
    if reqlen < self.__rbuf_size:
      # try to make a read of as much as we can.
      retstring += self.__trans.read(self.__rbuf_size)

    # but make sure we do read reqlen bytes.
    if len(retstring) < reqlen:
      retstring += self.__trans.readAll(reqlen - len(retstring))

    self.__rbuf = StringIO(retstring)
    return self.__rbuf

class TMemoryBuffer(TTransportBase, CReadableTransport):
  """Wraps a cStringIO object as a TTransport.

  NOTE: Unlike the C++ version of this class, you cannot write to it
        then immediately read from it.  If you want to read from a
        TMemoryBuffer, you must either pass a string to the constructor.
  TODO(dreiss): Make this work like the C++ version.
  """

  def __init__(self, value=None):
    """value -- a value to read from for stringio

    If value is set, this will be a transport for reading,
    otherwise, it is for writing"""
    if value is not None:
      self._buffer = StringIO(value)
    else:
      self._buffer = StringIO()

  def isOpen(self):
    return not self._buffer.closed

  def open(self):
    pass

  def close(self):
    self._buffer.close()

  def read(self, sz):
    return self._buffer.read(sz)

  def write(self, buf):
    self._buffer.write(buf)

  def flush(self):
    pass

  def getvalue(self):
    return self._buffer.getvalue()

  # Implement the CReadableTransport interface.
  @property
  def cstringio_buf(self):
    return self._buffer

  def cstringio_refill(self, partialread, reqlen):
    # only one shot at reading...
    raise EOFError()

class TFramedTransportFactory:

  """Factory transport that builds framed transports"""

  def getTransport(self, trans):
    framed = TFramedTransport(trans)
    return framed


class TFramedTransport(TTransportBase, CReadableTransport):

  """Class that wraps another transport and frames its I/O when writing."""

  def __init__(self, trans,):
    self.__trans = trans
    self.__rbuf = StringIO()
    self.__wbuf = StringIO()

  def isOpen(self):
    return self.__trans.isOpen()

  def open(self):
    return self.__trans.open()

  def close(self):
    return self.__trans.close()

  def read(self, sz):
    ret = self.__rbuf.read(sz)
    if len(ret) != 0:
      return ret

    self.readFrame()
    return self.__rbuf.read(sz)

  def readFrame(self):
    buff = self.__trans.readAll(4)
    sz, = unpack('!i', buff)
    self.__rbuf = StringIO(self.__trans.readAll(sz))

  def write(self, buf):
    self.__wbuf.write(buf)

  def flush(self):
    wout = self.__wbuf.getvalue()
    wsz = len(wout)
    # reset wbuf before write/flush to preserve state on underlying failure
    self.__wbuf = StringIO()
    # N.B.: Doing this string concatenation is WAY cheaper than making
    # two separate calls to the underlying socket object. Socket writes in
    # Python turn out to be REALLY expensive, but it seems to do a pretty
    # good job of managing string buffer operations without excessive copies
    buf = pack("!i", wsz) + wout
    self.__trans.write(buf)
    self.__trans.flush()

  # Implement the CReadableTransport interface.
  @property
  def cstringio_buf(self):
    return self.__rbuf

  def cstringio_refill(self, prefix, reqlen):
    # self.__rbuf will already be empty here because fastbinary doesn't
    # ask for a refill until the previous buffer is empty.  Therefore,
    # we can start reading new frames immediately.
    while len(prefix) < reqlen:
      self.readFrame()
      prefix += self.__rbuf.getvalue()
    self.__rbuf = StringIO(prefix)
    return self.__rbuf


class TFileObjectTransport(TTransportBase):
  """Wraps a file-like object to make it work as a Thrift transport."""

  def __init__(self, fileobj):
    self.fileobj = fileobj

  def isOpen(self):
    return True

  def close(self):
    self.fileobj.close()

  def read(self, sz):
    return self.fileobj.read(sz)

  def write(self, buf):
    self.fileobj.write(buf)

  def flush(self):
    self.fileobj.flush()
