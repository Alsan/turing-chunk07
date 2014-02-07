#
# Autogenerated by Thrift Compiler (0.7.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#

require 'fb303_types'
require 'hive_metastore_types'
require 'queryplan_types'


module JobTrackerState
  INITIALIZING = 1
  RUNNING = 2
  VALUE_MAP = {1 => "INITIALIZING", 2 => "RUNNING"}
  VALID_VALUES = Set.new([INITIALIZING, RUNNING]).freeze
end

class HiveClusterStatus
  include ::Thrift::Struct, ::Thrift::Struct_Union
  TASKTRACKERS = 1
  MAPTASKS = 2
  REDUCETASKS = 3
  MAXMAPTASKS = 4
  MAXREDUCETASKS = 5
  STATE = 6

  FIELDS = {
    TASKTRACKERS => {:type => ::Thrift::Types::I32, :name => 'taskTrackers'},
    MAPTASKS => {:type => ::Thrift::Types::I32, :name => 'mapTasks'},
    REDUCETASKS => {:type => ::Thrift::Types::I32, :name => 'reduceTasks'},
    MAXMAPTASKS => {:type => ::Thrift::Types::I32, :name => 'maxMapTasks'},
    MAXREDUCETASKS => {:type => ::Thrift::Types::I32, :name => 'maxReduceTasks'},
    STATE => {:type => ::Thrift::Types::I32, :name => 'state', :enum_class => JobTrackerState}
  }

  def struct_fields; FIELDS; end

  def validate
    unless @state.nil? || JobTrackerState::VALID_VALUES.include?(@state)
      raise ::Thrift::ProtocolException.new(::Thrift::ProtocolException::UNKNOWN, 'Invalid value of field state!')
    end
  end

  ::Thrift::Struct.generate_accessors self
end

class HiveServerException < ::Thrift::Exception
  include ::Thrift::Struct, ::Thrift::Struct_Union
  MESSAGE = 1
  ERRORCODE = 2
  SQLSTATE = 3

  FIELDS = {
    MESSAGE => {:type => ::Thrift::Types::STRING, :name => 'message'},
    ERRORCODE => {:type => ::Thrift::Types::I32, :name => 'errorCode'},
    SQLSTATE => {:type => ::Thrift::Types::STRING, :name => 'SQLState'}
  }

  def struct_fields; FIELDS; end

  def validate
  end

  ::Thrift::Struct.generate_accessors self
end

