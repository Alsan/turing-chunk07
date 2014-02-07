/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
#include "queryplan_types.h"

namespace Apache { namespace Hadoop { namespace Hive {

int _kAdjacencyTypeValues[] = {
  AdjacencyType::CONJUNCTIVE,
  AdjacencyType::DISJUNCTIVE
};
const char* _kAdjacencyTypeNames[] = {
  "CONJUNCTIVE",
  "DISJUNCTIVE"
};
const std::map<int, const char*> _AdjacencyType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(2, _kAdjacencyTypeValues, _kAdjacencyTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

int _kNodeTypeValues[] = {
  NodeType::OPERATOR,
  NodeType::STAGE
};
const char* _kNodeTypeNames[] = {
  "OPERATOR",
  "STAGE"
};
const std::map<int, const char*> _NodeType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(2, _kNodeTypeValues, _kNodeTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

int _kOperatorTypeValues[] = {
  OperatorType::JOIN,
  OperatorType::MAPJOIN,
  OperatorType::EXTRACT,
  OperatorType::FILTER,
  OperatorType::FORWARD,
  OperatorType::GROUPBY,
  OperatorType::LIMIT,
  OperatorType::SCRIPT,
  OperatorType::SELECT,
  OperatorType::TABLESCAN,
  OperatorType::FILESINK,
  OperatorType::REDUCESINK,
  OperatorType::UNION,
  OperatorType::UDTF,
  OperatorType::LATERALVIEWJOIN,
  OperatorType::LATERALVIEWFORWARD,
  OperatorType::HASHTABLESINK,
  OperatorType::HASHTABLEDUMMY
};
const char* _kOperatorTypeNames[] = {
  "JOIN",
  "MAPJOIN",
  "EXTRACT",
  "FILTER",
  "FORWARD",
  "GROUPBY",
  "LIMIT",
  "SCRIPT",
  "SELECT",
  "TABLESCAN",
  "FILESINK",
  "REDUCESINK",
  "UNION",
  "UDTF",
  "LATERALVIEWJOIN",
  "LATERALVIEWFORWARD",
  "HASHTABLESINK",
  "HASHTABLEDUMMY"
};
const std::map<int, const char*> _OperatorType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(18, _kOperatorTypeValues, _kOperatorTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

int _kTaskTypeValues[] = {
  TaskType::MAP,
  TaskType::REDUCE,
  TaskType::OTHER
};
const char* _kTaskTypeNames[] = {
  "MAP",
  "REDUCE",
  "OTHER"
};
const std::map<int, const char*> _TaskType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(3, _kTaskTypeValues, _kTaskTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

int _kStageTypeValues[] = {
  StageType::CONDITIONAL,
  StageType::COPY,
  StageType::DDL,
  StageType::MAPRED,
  StageType::EXPLAIN,
  StageType::FETCH,
  StageType::FUNC,
  StageType::MAPREDLOCAL,
  StageType::MOVE,
  StageType::STATS
};
const char* _kStageTypeNames[] = {
  "CONDITIONAL",
  "COPY",
  "DDL",
  "MAPRED",
  "EXPLAIN",
  "FETCH",
  "FUNC",
  "MAPREDLOCAL",
  "MOVE",
  "STATS"
};
const std::map<int, const char*> _StageType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(10, _kStageTypeValues, _kStageTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

const char* Adjacency::ascii_fingerprint = "BC4F8C394677A1003AA9F56ED26D8204";
const uint8_t Adjacency::binary_fingerprint[16] = {0xBC,0x4F,0x8C,0x39,0x46,0x77,0xA1,0x00,0x3A,0xA9,0xF5,0x6E,0xD2,0x6D,0x82,0x04};

uint32_t Adjacency::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->node);
          this->__isset.node = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->children.clear();
            uint32_t _size0;
            ::apache::thrift::protocol::TType _etype3;
            iprot->readListBegin(_etype3, _size0);
            this->children.resize(_size0);
            uint32_t _i4;
            for (_i4 = 0; _i4 < _size0; ++_i4)
            {
              xfer += iprot->readString(this->children[_i4]);
            }
            iprot->readListEnd();
          }
          this->__isset.children = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast5;
          xfer += iprot->readI32(ecast5);
          this->adjacencyType = (AdjacencyType::type)ecast5;
          this->__isset.adjacencyType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Adjacency::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Adjacency");
  xfer += oprot->writeFieldBegin("node", ::apache::thrift::protocol::T_STRING, 1);
  xfer += oprot->writeString(this->node);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("children", ::apache::thrift::protocol::T_LIST, 2);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->children.size()));
    std::vector<std::string> ::const_iterator _iter6;
    for (_iter6 = this->children.begin(); _iter6 != this->children.end(); ++_iter6)
    {
      xfer += oprot->writeString((*_iter6));
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("adjacencyType", ::apache::thrift::protocol::T_I32, 3);
  xfer += oprot->writeI32((int32_t)this->adjacencyType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* Graph::ascii_fingerprint = "1F7FB604B3EF8F7AFB5DEAD15F2FC0B5";
const uint8_t Graph::binary_fingerprint[16] = {0x1F,0x7F,0xB6,0x04,0xB3,0xEF,0x8F,0x7A,0xFB,0x5D,0xEA,0xD1,0x5F,0x2F,0xC0,0xB5};

uint32_t Graph::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast7;
          xfer += iprot->readI32(ecast7);
          this->nodeType = (NodeType::type)ecast7;
          this->__isset.nodeType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->roots.clear();
            uint32_t _size8;
            ::apache::thrift::protocol::TType _etype11;
            iprot->readListBegin(_etype11, _size8);
            this->roots.resize(_size8);
            uint32_t _i12;
            for (_i12 = 0; _i12 < _size8; ++_i12)
            {
              xfer += iprot->readString(this->roots[_i12]);
            }
            iprot->readListEnd();
          }
          this->__isset.roots = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->adjacencyList.clear();
            uint32_t _size13;
            ::apache::thrift::protocol::TType _etype16;
            iprot->readListBegin(_etype16, _size13);
            this->adjacencyList.resize(_size13);
            uint32_t _i17;
            for (_i17 = 0; _i17 < _size13; ++_i17)
            {
              xfer += this->adjacencyList[_i17].read(iprot);
            }
            iprot->readListEnd();
          }
          this->__isset.adjacencyList = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Graph::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Graph");
  xfer += oprot->writeFieldBegin("nodeType", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32((int32_t)this->nodeType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("roots", ::apache::thrift::protocol::T_LIST, 2);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->roots.size()));
    std::vector<std::string> ::const_iterator _iter18;
    for (_iter18 = this->roots.begin(); _iter18 != this->roots.end(); ++_iter18)
    {
      xfer += oprot->writeString((*_iter18));
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("adjacencyList", ::apache::thrift::protocol::T_LIST, 3);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->adjacencyList.size()));
    std::vector<Adjacency> ::const_iterator _iter19;
    for (_iter19 = this->adjacencyList.begin(); _iter19 != this->adjacencyList.end(); ++_iter19)
    {
      xfer += (*_iter19).write(oprot);
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* Operator::ascii_fingerprint = "30917C758A752485AF223B697479DE6C";
const uint8_t Operator::binary_fingerprint[16] = {0x30,0x91,0x7C,0x75,0x8A,0x75,0x24,0x85,0xAF,0x22,0x3B,0x69,0x74,0x79,0xDE,0x6C};

uint32_t Operator::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->operatorId);
          this->__isset.operatorId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast20;
          xfer += iprot->readI32(ecast20);
          this->operatorType = (OperatorType::type)ecast20;
          this->__isset.operatorType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->operatorAttributes.clear();
            uint32_t _size21;
            ::apache::thrift::protocol::TType _ktype22;
            ::apache::thrift::protocol::TType _vtype23;
            iprot->readMapBegin(_ktype22, _vtype23, _size21);
            uint32_t _i25;
            for (_i25 = 0; _i25 < _size21; ++_i25)
            {
              std::string _key26;
              xfer += iprot->readString(_key26);
              std::string& _val27 = this->operatorAttributes[_key26];
              xfer += iprot->readString(_val27);
            }
            iprot->readMapEnd();
          }
          this->__isset.operatorAttributes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->operatorCounters.clear();
            uint32_t _size28;
            ::apache::thrift::protocol::TType _ktype29;
            ::apache::thrift::protocol::TType _vtype30;
            iprot->readMapBegin(_ktype29, _vtype30, _size28);
            uint32_t _i32;
            for (_i32 = 0; _i32 < _size28; ++_i32)
            {
              std::string _key33;
              xfer += iprot->readString(_key33);
              int64_t& _val34 = this->operatorCounters[_key33];
              xfer += iprot->readI64(_val34);
            }
            iprot->readMapEnd();
          }
          this->__isset.operatorCounters = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->done);
          this->__isset.done = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->started);
          this->__isset.started = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Operator::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Operator");
  xfer += oprot->writeFieldBegin("operatorId", ::apache::thrift::protocol::T_STRING, 1);
  xfer += oprot->writeString(this->operatorId);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("operatorType", ::apache::thrift::protocol::T_I32, 2);
  xfer += oprot->writeI32((int32_t)this->operatorType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("operatorAttributes", ::apache::thrift::protocol::T_MAP, 3);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->operatorAttributes.size()));
    std::map<std::string, std::string> ::const_iterator _iter35;
    for (_iter35 = this->operatorAttributes.begin(); _iter35 != this->operatorAttributes.end(); ++_iter35)
    {
      xfer += oprot->writeString(_iter35->first);
      xfer += oprot->writeString(_iter35->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("operatorCounters", ::apache::thrift::protocol::T_MAP, 4);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_I64, static_cast<uint32_t>(this->operatorCounters.size()));
    std::map<std::string, int64_t> ::const_iterator _iter36;
    for (_iter36 = this->operatorCounters.begin(); _iter36 != this->operatorCounters.end(); ++_iter36)
    {
      xfer += oprot->writeString(_iter36->first);
      xfer += oprot->writeI64(_iter36->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("done", ::apache::thrift::protocol::T_BOOL, 5);
  xfer += oprot->writeBool(this->done);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("started", ::apache::thrift::protocol::T_BOOL, 6);
  xfer += oprot->writeBool(this->started);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* Task::ascii_fingerprint = "AC741A136EFA51843AFC3A12F6A793D1";
const uint8_t Task::binary_fingerprint[16] = {0xAC,0x74,0x1A,0x13,0x6E,0xFA,0x51,0x84,0x3A,0xFC,0x3A,0x12,0xF6,0xA7,0x93,0xD1};

uint32_t Task::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->taskId);
          this->__isset.taskId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast37;
          xfer += iprot->readI32(ecast37);
          this->taskType = (TaskType::type)ecast37;
          this->__isset.taskType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->taskAttributes.clear();
            uint32_t _size38;
            ::apache::thrift::protocol::TType _ktype39;
            ::apache::thrift::protocol::TType _vtype40;
            iprot->readMapBegin(_ktype39, _vtype40, _size38);
            uint32_t _i42;
            for (_i42 = 0; _i42 < _size38; ++_i42)
            {
              std::string _key43;
              xfer += iprot->readString(_key43);
              std::string& _val44 = this->taskAttributes[_key43];
              xfer += iprot->readString(_val44);
            }
            iprot->readMapEnd();
          }
          this->__isset.taskAttributes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->taskCounters.clear();
            uint32_t _size45;
            ::apache::thrift::protocol::TType _ktype46;
            ::apache::thrift::protocol::TType _vtype47;
            iprot->readMapBegin(_ktype46, _vtype47, _size45);
            uint32_t _i49;
            for (_i49 = 0; _i49 < _size45; ++_i49)
            {
              std::string _key50;
              xfer += iprot->readString(_key50);
              int64_t& _val51 = this->taskCounters[_key50];
              xfer += iprot->readI64(_val51);
            }
            iprot->readMapEnd();
          }
          this->__isset.taskCounters = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->operatorGraph.read(iprot);
          this->__isset.operatorGraph = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->operatorList.clear();
            uint32_t _size52;
            ::apache::thrift::protocol::TType _etype55;
            iprot->readListBegin(_etype55, _size52);
            this->operatorList.resize(_size52);
            uint32_t _i56;
            for (_i56 = 0; _i56 < _size52; ++_i56)
            {
              xfer += this->operatorList[_i56].read(iprot);
            }
            iprot->readListEnd();
          }
          this->__isset.operatorList = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->done);
          this->__isset.done = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 8:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->started);
          this->__isset.started = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Task::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Task");
  xfer += oprot->writeFieldBegin("taskId", ::apache::thrift::protocol::T_STRING, 1);
  xfer += oprot->writeString(this->taskId);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("taskType", ::apache::thrift::protocol::T_I32, 2);
  xfer += oprot->writeI32((int32_t)this->taskType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("taskAttributes", ::apache::thrift::protocol::T_MAP, 3);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->taskAttributes.size()));
    std::map<std::string, std::string> ::const_iterator _iter57;
    for (_iter57 = this->taskAttributes.begin(); _iter57 != this->taskAttributes.end(); ++_iter57)
    {
      xfer += oprot->writeString(_iter57->first);
      xfer += oprot->writeString(_iter57->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("taskCounters", ::apache::thrift::protocol::T_MAP, 4);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_I64, static_cast<uint32_t>(this->taskCounters.size()));
    std::map<std::string, int64_t> ::const_iterator _iter58;
    for (_iter58 = this->taskCounters.begin(); _iter58 != this->taskCounters.end(); ++_iter58)
    {
      xfer += oprot->writeString(_iter58->first);
      xfer += oprot->writeI64(_iter58->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  if (this->__isset.operatorGraph) {
    xfer += oprot->writeFieldBegin("operatorGraph", ::apache::thrift::protocol::T_STRUCT, 5);
    xfer += this->operatorGraph.write(oprot);
    xfer += oprot->writeFieldEnd();
  }
  if (this->__isset.operatorList) {
    xfer += oprot->writeFieldBegin("operatorList", ::apache::thrift::protocol::T_LIST, 6);
    {
      xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->operatorList.size()));
      std::vector<Operator> ::const_iterator _iter59;
      for (_iter59 = this->operatorList.begin(); _iter59 != this->operatorList.end(); ++_iter59)
      {
        xfer += (*_iter59).write(oprot);
      }
      xfer += oprot->writeListEnd();
    }
    xfer += oprot->writeFieldEnd();
  }
  xfer += oprot->writeFieldBegin("done", ::apache::thrift::protocol::T_BOOL, 7);
  xfer += oprot->writeBool(this->done);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("started", ::apache::thrift::protocol::T_BOOL, 8);
  xfer += oprot->writeBool(this->started);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* Stage::ascii_fingerprint = "86EA3C7B0690AFED21A3D479E2B32378";
const uint8_t Stage::binary_fingerprint[16] = {0x86,0xEA,0x3C,0x7B,0x06,0x90,0xAF,0xED,0x21,0xA3,0xD4,0x79,0xE2,0xB3,0x23,0x78};

uint32_t Stage::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->stageId);
          this->__isset.stageId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          int32_t ecast60;
          xfer += iprot->readI32(ecast60);
          this->stageType = (StageType::type)ecast60;
          this->__isset.stageType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->stageAttributes.clear();
            uint32_t _size61;
            ::apache::thrift::protocol::TType _ktype62;
            ::apache::thrift::protocol::TType _vtype63;
            iprot->readMapBegin(_ktype62, _vtype63, _size61);
            uint32_t _i65;
            for (_i65 = 0; _i65 < _size61; ++_i65)
            {
              std::string _key66;
              xfer += iprot->readString(_key66);
              std::string& _val67 = this->stageAttributes[_key66];
              xfer += iprot->readString(_val67);
            }
            iprot->readMapEnd();
          }
          this->__isset.stageAttributes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->stageCounters.clear();
            uint32_t _size68;
            ::apache::thrift::protocol::TType _ktype69;
            ::apache::thrift::protocol::TType _vtype70;
            iprot->readMapBegin(_ktype69, _vtype70, _size68);
            uint32_t _i72;
            for (_i72 = 0; _i72 < _size68; ++_i72)
            {
              std::string _key73;
              xfer += iprot->readString(_key73);
              int64_t& _val74 = this->stageCounters[_key73];
              xfer += iprot->readI64(_val74);
            }
            iprot->readMapEnd();
          }
          this->__isset.stageCounters = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->taskList.clear();
            uint32_t _size75;
            ::apache::thrift::protocol::TType _etype78;
            iprot->readListBegin(_etype78, _size75);
            this->taskList.resize(_size75);
            uint32_t _i79;
            for (_i79 = 0; _i79 < _size75; ++_i79)
            {
              xfer += this->taskList[_i79].read(iprot);
            }
            iprot->readListEnd();
          }
          this->__isset.taskList = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->done);
          this->__isset.done = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->started);
          this->__isset.started = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Stage::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Stage");
  xfer += oprot->writeFieldBegin("stageId", ::apache::thrift::protocol::T_STRING, 1);
  xfer += oprot->writeString(this->stageId);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("stageType", ::apache::thrift::protocol::T_I32, 2);
  xfer += oprot->writeI32((int32_t)this->stageType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("stageAttributes", ::apache::thrift::protocol::T_MAP, 3);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->stageAttributes.size()));
    std::map<std::string, std::string> ::const_iterator _iter80;
    for (_iter80 = this->stageAttributes.begin(); _iter80 != this->stageAttributes.end(); ++_iter80)
    {
      xfer += oprot->writeString(_iter80->first);
      xfer += oprot->writeString(_iter80->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("stageCounters", ::apache::thrift::protocol::T_MAP, 4);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_I64, static_cast<uint32_t>(this->stageCounters.size()));
    std::map<std::string, int64_t> ::const_iterator _iter81;
    for (_iter81 = this->stageCounters.begin(); _iter81 != this->stageCounters.end(); ++_iter81)
    {
      xfer += oprot->writeString(_iter81->first);
      xfer += oprot->writeI64(_iter81->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("taskList", ::apache::thrift::protocol::T_LIST, 5);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->taskList.size()));
    std::vector<Task> ::const_iterator _iter82;
    for (_iter82 = this->taskList.begin(); _iter82 != this->taskList.end(); ++_iter82)
    {
      xfer += (*_iter82).write(oprot);
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("done", ::apache::thrift::protocol::T_BOOL, 6);
  xfer += oprot->writeBool(this->done);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("started", ::apache::thrift::protocol::T_BOOL, 7);
  xfer += oprot->writeBool(this->started);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* Query::ascii_fingerprint = "68300D63A5D40F2D17B9A9440FF626C1";
const uint8_t Query::binary_fingerprint[16] = {0x68,0x30,0x0D,0x63,0xA5,0xD4,0x0F,0x2D,0x17,0xB9,0xA9,0x44,0x0F,0xF6,0x26,0xC1};

uint32_t Query::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->queryId);
          this->__isset.queryId = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_STRING) {
          xfer += iprot->readString(this->queryType);
          this->__isset.queryType = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->queryAttributes.clear();
            uint32_t _size83;
            ::apache::thrift::protocol::TType _ktype84;
            ::apache::thrift::protocol::TType _vtype85;
            iprot->readMapBegin(_ktype84, _vtype85, _size83);
            uint32_t _i87;
            for (_i87 = 0; _i87 < _size83; ++_i87)
            {
              std::string _key88;
              xfer += iprot->readString(_key88);
              std::string& _val89 = this->queryAttributes[_key88];
              xfer += iprot->readString(_val89);
            }
            iprot->readMapEnd();
          }
          this->__isset.queryAttributes = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 4:
        if (ftype == ::apache::thrift::protocol::T_MAP) {
          {
            this->queryCounters.clear();
            uint32_t _size90;
            ::apache::thrift::protocol::TType _ktype91;
            ::apache::thrift::protocol::TType _vtype92;
            iprot->readMapBegin(_ktype91, _vtype92, _size90);
            uint32_t _i94;
            for (_i94 = 0; _i94 < _size90; ++_i94)
            {
              std::string _key95;
              xfer += iprot->readString(_key95);
              int64_t& _val96 = this->queryCounters[_key95];
              xfer += iprot->readI64(_val96);
            }
            iprot->readMapEnd();
          }
          this->__isset.queryCounters = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 5:
        if (ftype == ::apache::thrift::protocol::T_STRUCT) {
          xfer += this->stageGraph.read(iprot);
          this->__isset.stageGraph = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 6:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->stageList.clear();
            uint32_t _size97;
            ::apache::thrift::protocol::TType _etype100;
            iprot->readListBegin(_etype100, _size97);
            this->stageList.resize(_size97);
            uint32_t _i101;
            for (_i101 = 0; _i101 < _size97; ++_i101)
            {
              xfer += this->stageList[_i101].read(iprot);
            }
            iprot->readListEnd();
          }
          this->__isset.stageList = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 7:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->done);
          this->__isset.done = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 8:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->started);
          this->__isset.started = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Query::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Query");
  xfer += oprot->writeFieldBegin("queryId", ::apache::thrift::protocol::T_STRING, 1);
  xfer += oprot->writeString(this->queryId);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("queryType", ::apache::thrift::protocol::T_STRING, 2);
  xfer += oprot->writeString(this->queryType);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("queryAttributes", ::apache::thrift::protocol::T_MAP, 3);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_STRING, static_cast<uint32_t>(this->queryAttributes.size()));
    std::map<std::string, std::string> ::const_iterator _iter102;
    for (_iter102 = this->queryAttributes.begin(); _iter102 != this->queryAttributes.end(); ++_iter102)
    {
      xfer += oprot->writeString(_iter102->first);
      xfer += oprot->writeString(_iter102->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("queryCounters", ::apache::thrift::protocol::T_MAP, 4);
  {
    xfer += oprot->writeMapBegin(::apache::thrift::protocol::T_STRING, ::apache::thrift::protocol::T_I64, static_cast<uint32_t>(this->queryCounters.size()));
    std::map<std::string, int64_t> ::const_iterator _iter103;
    for (_iter103 = this->queryCounters.begin(); _iter103 != this->queryCounters.end(); ++_iter103)
    {
      xfer += oprot->writeString(_iter103->first);
      xfer += oprot->writeI64(_iter103->second);
    }
    xfer += oprot->writeMapEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("stageGraph", ::apache::thrift::protocol::T_STRUCT, 5);
  xfer += this->stageGraph.write(oprot);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("stageList", ::apache::thrift::protocol::T_LIST, 6);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->stageList.size()));
    std::vector<Stage> ::const_iterator _iter104;
    for (_iter104 = this->stageList.begin(); _iter104 != this->stageList.end(); ++_iter104)
    {
      xfer += (*_iter104).write(oprot);
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("done", ::apache::thrift::protocol::T_BOOL, 7);
  xfer += oprot->writeBool(this->done);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("started", ::apache::thrift::protocol::T_BOOL, 8);
  xfer += oprot->writeBool(this->started);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

const char* QueryPlan::ascii_fingerprint = "3418D1B0C20C288C8406186700B772E3";
const uint8_t QueryPlan::binary_fingerprint[16] = {0x34,0x18,0xD1,0xB0,0xC2,0x0C,0x28,0x8C,0x84,0x06,0x18,0x67,0x00,0xB7,0x72,0xE3};

uint32_t QueryPlan::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_LIST) {
          {
            this->queries.clear();
            uint32_t _size105;
            ::apache::thrift::protocol::TType _etype108;
            iprot->readListBegin(_etype108, _size105);
            this->queries.resize(_size105);
            uint32_t _i109;
            for (_i109 = 0; _i109 < _size105; ++_i109)
            {
              xfer += this->queries[_i109].read(iprot);
            }
            iprot->readListEnd();
          }
          this->__isset.queries = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->done);
          this->__isset.done = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_BOOL) {
          xfer += iprot->readBool(this->started);
          this->__isset.started = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t QueryPlan::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("QueryPlan");
  xfer += oprot->writeFieldBegin("queries", ::apache::thrift::protocol::T_LIST, 1);
  {
    xfer += oprot->writeListBegin(::apache::thrift::protocol::T_STRUCT, static_cast<uint32_t>(this->queries.size()));
    std::vector<Query> ::const_iterator _iter110;
    for (_iter110 = this->queries.begin(); _iter110 != this->queries.end(); ++_iter110)
    {
      xfer += (*_iter110).write(oprot);
    }
    xfer += oprot->writeListEnd();
  }
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("done", ::apache::thrift::protocol::T_BOOL, 2);
  xfer += oprot->writeBool(this->done);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldBegin("started", ::apache::thrift::protocol::T_BOOL, 3);
  xfer += oprot->writeBool(this->started);
  xfer += oprot->writeFieldEnd();
  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

}}} // namespace
