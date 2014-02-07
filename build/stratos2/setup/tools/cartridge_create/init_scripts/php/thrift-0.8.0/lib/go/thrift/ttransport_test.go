/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package thrift_test

import (
  . "thrift"
  "testing"
  "net"
  "os"
  "strconv"
)

const TRANSPORT_BINARY_DATA_SIZE = 4096

var (
  transport_bdata []byte // test data for writing; same as data
)

func init() {
  transport_bdata = make([]byte, TRANSPORT_BINARY_DATA_SIZE)
  for i := 0; i < TRANSPORT_BINARY_DATA_SIZE; i++ {
    transport_bdata[i] = byte((i + 'a') % 255)
  }
}

func TransportTest(t *testing.T, writeTrans TTransport, readTrans TTransport) {
  buf := make([]byte, TRANSPORT_BINARY_DATA_SIZE)
  if !writeTrans.IsOpen() {
    err := writeTrans.Open()
    if err != nil {
      t.Fatalf("Transport %T cannot open write transport: %s", writeTrans, err)
    }
  }
  if !readTrans.IsOpen() {
    err := readTrans.Open()
    if err != nil {
      t.Fatalf("Transport %T cannot open read transport: %s", readTrans, err)
    }
  }
  _, err := writeTrans.Write(transport_bdata)
  if err != nil {
    t.Fatalf("Transport %T cannot write binary data of length %d: %s", writeTrans, len(transport_bdata), err)
  }
  err = writeTrans.Flush()
  if err != nil {
    t.Fatalf("Transport %T cannot flush write of binary data: %s", writeTrans, err)
  }
  n, err := readTrans.ReadAll(buf)
  if err != nil {
    t.Errorf("Transport %T cannot read binary data of length %d: %s", readTrans, TRANSPORT_BINARY_DATA_SIZE, err)
  }
  if n != TRANSPORT_BINARY_DATA_SIZE {
    t.Errorf("Transport %T read only %d instead of %d bytes of binary data", readTrans, n, TRANSPORT_BINARY_DATA_SIZE)
  }
  for k, v := range buf {
    if v != transport_bdata[k] {
      t.Fatalf("Transport %T read %d instead of %d for index %d of binary data 2", readTrans, v, transport_bdata[k], k)
    }
  }
  _, err = writeTrans.Write(transport_bdata)
  if err != nil {
    t.Fatalf("Transport %T cannot write binary data 2 of length %d: %s", writeTrans, len(transport_bdata), err)
  }
  err = writeTrans.Flush()
  if err != nil {
    t.Fatalf("Transport %T cannot flush write binary data 2: %s", writeTrans, err)
  }
  b := readTrans.Peek()
  if b != true {
    t.Errorf("Transport %T returned %s for Peek()", readTrans, b)
  }
  buf = make([]byte, TRANSPORT_BINARY_DATA_SIZE)
  read := 1
  for n = 0; n < TRANSPORT_BINARY_DATA_SIZE && read != 0; {
    read, err = readTrans.Read(buf[n:])
    if err != nil {
      t.Errorf("Transport %T cannot read binary data 2 of total length %d from offset %d: %s", readTrans, TRANSPORT_BINARY_DATA_SIZE, n, err)
    }
    n += read
  }
  if n != TRANSPORT_BINARY_DATA_SIZE {
    t.Errorf("Transport %T read only %d instead of %d bytes of binary data 2", readTrans, n, TRANSPORT_BINARY_DATA_SIZE)
  }
  for k, v := range buf {
    if v != transport_bdata[k] {
      t.Fatalf("Transport %T read %d instead of %d for index %d of binary data 2", readTrans, v, transport_bdata[k], k)
    }
  }
}

func CloseTransports(t *testing.T, readTrans TTransport, writeTrans TTransport) {
  err := readTrans.Close()
  if err != nil {
    t.Errorf("Transport %T cannot close read transport: %s", readTrans, err)
  }
  if writeTrans != readTrans {
    err = writeTrans.Close()
    if err != nil {
      t.Errorf("Transport %T cannot close write transport: %s", writeTrans, err)
    }
  }
}

func FindAvailableTCPServerPort(startPort int) (net.Addr, os.Error) {
  for i := startPort; i < 65535; i++ {
    s := "127.0.0.1:" + strconv.Itoa(i)
    l, err := net.Listen("tcp", s)
    if err == nil {
      l.Close()
      return net.ResolveTCPAddr("tcp", s)
    }
  }
  return nil, NewTTransportException(UNKNOWN_TRANSPORT_EXCEPTION, "Could not find available server port")
}
