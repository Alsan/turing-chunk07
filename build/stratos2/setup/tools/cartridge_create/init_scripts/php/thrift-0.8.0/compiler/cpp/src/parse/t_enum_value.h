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

#ifndef T_ENUM_VALUE_H
#define T_ENUM_VALUE_H

#include <string>
#include "t_doc.h"

/**
 * A constant. These are used inside of enum definitions. Constants are just
 * symbol identifiers that may or may not have an explicit value associated
 * with them.
 *
 */
class t_enum_value : public t_doc {
 public:
  t_enum_value(std::string name) :
    name_(name),
    has_value_(false),
    value_(0) {}

  t_enum_value(std::string name, int value) :
    name_(name),
    has_value_(true),
    value_(value) {}

  ~t_enum_value() {}

  const std::string& get_name() {
    return name_;
  }

  bool has_value() {
    return has_value_;
  }

  int get_value() {
    return value_;
  }

  void set_value(int val) {
    has_value_ = true;
    value_ = val;
  }

 private:
  std::string name_;
  bool has_value_;
  int value_;
};

#endif
