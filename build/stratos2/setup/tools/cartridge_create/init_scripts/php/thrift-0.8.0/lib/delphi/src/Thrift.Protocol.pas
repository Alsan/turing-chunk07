(*
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
 *)

{$SCOPEDENUMS ON}

unit Thrift.Protocol;

interface

uses
  Classes,
  SysUtils,
  Contnrs,
  Thrift.Stream,
  Thrift.Collections,
  Thrift.Transport;

type

  TType = (
    Stop = 0,
    Void = 1,
    Bool_ = 2,
    Byte_ = 3,
    Double_ = 4,
    I16 = 6,
    I32 = 8,
    I64 = 10,
    String_ = 11,
    Struct = 12,
    Map = 13,
    Set_ = 14,
    List = 15
  );

  TMessageType = (
    Call = 1,
    Reply = 2,
    Exception = 3,
    Oneway = 4
  );

  IProtocol = interface;
  IStruct = interface;

  IProtocolFactory = interface
    ['{7CD64A10-4E9F-4E99-93BF-708A31F4A67B}']
    function GetProtocol( trans: ITransport): IProtocol;
  end;

  TThriftStringBuilder = class( TStringBuilder)
  public
    function Append(const Value: TBytes): TStringBuilder; overload;
    function Append(const Value: IThriftContainer): TStringBuilder; overload;
  end;

  TProtocolException = class( Exception )
  public
    const
      UNKNOWN : Integer = 0;
      INVALID_DATA : Integer = 1;
      NEGATIVE_SIZE : Integer = 2;
      SIZE_LIMIT : Integer = 3;
      BAD_VERSION : Integer = 4;
      NOT_IMPLEMENTED : Integer = 5;
  protected
    FType : Integer;
  public
    constructor Create; overload;
    constructor Create( type_: Integer ); overload;
    constructor Create( type_: Integer; const msg: string); overload;
  end;

  IMap = interface
    ['{30531D97-7E06-4233-B800-C3F53CCD23E7}']
    function GetKeyType: TType;
    procedure SetKeyType( Value: TType);
    function GetValueType: TType;
    procedure SetValueType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
    property KeyType: TType read GetKeyType write SetKeyType;
    property ValueType: TType read GetValueType write SetValueType;
    property Count: Integer read GetCount write SetCount;
  end;

  TMapImpl = class( TInterfacedObject, IMap)
  private
    FValueType: TType;
    FKeyType: TType;
    FCount: Integer;
  protected
    function GetKeyType: TType;
    procedure SetKeyType( Value: TType);
    function GetValueType: TType;
    procedure SetValueType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
  public
    constructor Create( AValueType: TType; AKeyType: TType; ACount: Integer); overload;
    constructor Create; overload;
  end;

  IList = interface
    ['{6763E1EA-A934-4472-904F-0083980B9B87}']
    function GetElementType: TType;
    procedure SetElementType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
    property ElementType: TType read GetElementType write SetElementType;
    property Count: Integer read GetCount write SetCount;
  end;

  TListImpl = class( TInterfacedObject, IList)
  private
    FElementType: TType;
    FCount : Integer;
  protected
    function GetElementType: TType;
    procedure SetElementType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
  public
    constructor Create( AElementType: TType; ACount: Integer); overload;
    constructor Create; overload;
  end;

  ISet = interface
    ['{A8671700-7514-4C1E-8A05-62786872005F}']
    function GetElementType: TType;
    procedure SetElementType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
    property ElementType: TType read GetElementType write SetElementType;
    property Count: Integer read GetCount write SetCount;
  end;

  TSetImpl = class( TInterfacedObject, ISet)
  private
    FCount: Integer;
    FElementType: TType;
  protected
    function GetElementType: TType;
    procedure SetElementType( Value: TType);
    function GetCount: Integer;
    procedure SetCount( Value: Integer);
  public
    constructor Create( AElementType: TType; ACount: Integer); overload;
    constructor Create; overload;
  end;

  IMessage = interface
    ['{9E368B4A-B1FA-43E7-8CF5-56C66D256CA7}']
    function GetName: string;
    procedure SetName( const Value: string);
    function GetType: TMessageType;
    procedure SetType( Value: TMessageType);
    function GetSeqID: Integer;
    procedure SetSeqID( Value: Integer);
    property Name: string read GetName write SetName;
    property Type_: TMessageType read GetType write SetType;
    property SeqID: Integer read GetSeqID write SetSeqID;
  end;

  TMessageImpl = class( TInterfacedObject, IMessage )
  private
    FName: string;
    FMessageType: TMessageType;
    FSeqID: Integer;
  protected
    function GetName: string;
    procedure SetName( const Value: string);
    function GetType: TMessageType;
    procedure SetType( Value: TMessageType);
    function GetSeqID: Integer;
    procedure SetSeqID( Value: Integer);
  public
    property Name: string read FName write FName;
    property Type_: TMessageType read FMessageType write FMessageType;
    property SeqID: Integer read FSeqID write FSeqID;
    constructor Create( AName: string; AMessageType: TMessageType; ASeqID: Integer); overload;
    constructor Create; overload;
  end;

  IField = interface
    ['{F0D43BE5-7883-442E-83FF-0580CC632B72}']
    function GetName: string;
    procedure SetName( const Value: string);
    function GetType: TType;
    procedure SetType( Value: TType);
    function GetId: SmallInt;
    procedure SetId( Value: SmallInt);
    property Name: string read GetName write SetName;
    property Type_: TType read GetType write SetType;
    property Id: SmallInt read GetId write SetId;
  end;

  TFieldImpl = class( TInterfacedObject, IField)
  private
    FName : string;
    FType : TType;
    FId   : SmallInt;
  protected
    function GetName: string;
    procedure SetName( const Value: string);
    function GetType: TType;
    procedure SetType( Value: TType);
    function GetId: SmallInt;
    procedure SetId( Value: SmallInt);
  public
    constructor Create( const AName: string; const AType: TType; AId: SmallInt); overload;
    constructor Create; overload;
  end;

  TProtocolUtil = class
  public
    class procedure Skip( prot: IProtocol; type_: TType);
  end;

  IProtocol = interface
    ['{FD95C151-1527-4C96-8134-B902BFC4B4FC}']
    function GetTransport: ITransport;
    procedure WriteMessageBegin( message: IMessage);
    procedure WriteMessageEnd;
    procedure WriteStructBegin(struc: IStruct);
    procedure WriteStructEnd;
    procedure WriteFieldBegin(field: IField);
    procedure WriteFieldEnd;
    procedure WriteFieldStop;
    procedure WriteMapBegin(map: IMap);
    procedure WriteMapEnd;
    procedure WriteListBegin( list: IList);
    procedure WriteListEnd();
    procedure WriteSetBegin( set_: ISet );
    procedure WriteSetEnd();
    procedure WriteBool( b: Boolean);
    procedure WriteByte( b: ShortInt);
    procedure WriteI16( i16: SmallInt);
    procedure WriteI32( i32: Integer);
    procedure WriteI64( i64: Int64);
    procedure WriteDouble( d: Double);
    procedure WriteString( const s: string );
    procedure WriteAnsiString( const s: AnsiString);
    procedure WriteBinary( const b: TBytes);

    function ReadMessageBegin: IMessage;
    procedure ReadMessageEnd();
    function ReadStructBegin: IStruct;
    procedure ReadStructEnd;
    function ReadFieldBegin: IField;
    procedure ReadFieldEnd();
    function ReadMapBegin: IMap;
    procedure ReadMapEnd();
    function ReadListBegin: IList;
    procedure ReadListEnd();
    function ReadSetBegin: ISet;
    procedure ReadSetEnd();
    function ReadBool: Boolean;
    function ReadByte: ShortInt;
    function ReadI16: SmallInt;
    function ReadI32: Integer;
    function ReadI64: Int64;
    function ReadDouble:Double;
    function ReadBinary: TBytes;
    function ReadString: string;
    function ReadAnsiString: AnsiString;
    property Transport: ITransport read GetTransport;
  end;

  TProtocolImpl = class abstract( TInterfacedObject, IProtocol)
  protected
    FTrans : ITransport;
    function GetTransport: ITransport;
  public
    procedure WriteMessageBegin( message: IMessage); virtual; abstract;
    procedure WriteMessageEnd; virtual; abstract;
    procedure WriteStructBegin(struc: IStruct); virtual; abstract;
    procedure WriteStructEnd; virtual; abstract;
    procedure WriteFieldBegin(field: IField); virtual; abstract;
    procedure WriteFieldEnd; virtual; abstract;
    procedure WriteFieldStop; virtual; abstract;
    procedure WriteMapBegin(map: IMap); virtual; abstract;
    procedure WriteMapEnd; virtual; abstract;
    procedure WriteListBegin( list: IList); virtual; abstract;
    procedure WriteListEnd(); virtual; abstract;
    procedure WriteSetBegin( set_: ISet ); virtual; abstract;
    procedure WriteSetEnd(); virtual; abstract;
    procedure WriteBool( b: Boolean); virtual; abstract;
    procedure WriteByte( b: ShortInt); virtual; abstract;
    procedure WriteI16( i16: SmallInt); virtual; abstract;
    procedure WriteI32( i32: Integer); virtual; abstract;
    procedure WriteI64( i64: Int64); virtual; abstract;
    procedure WriteDouble( d: Double); virtual; abstract;
    procedure WriteString( const s: string ); virtual;
    procedure WriteAnsiString( const s: AnsiString); virtual;
    procedure WriteBinary( const b: TBytes); virtual; abstract;

    function ReadMessageBegin: IMessage; virtual; abstract;
    procedure ReadMessageEnd(); virtual; abstract;
    function ReadStructBegin: IStruct; virtual; abstract;
    procedure ReadStructEnd; virtual; abstract;
    function ReadFieldBegin: IField; virtual; abstract;
    procedure ReadFieldEnd(); virtual; abstract;
    function ReadMapBegin: IMap; virtual; abstract;
    procedure ReadMapEnd(); virtual; abstract;
    function ReadListBegin: IList; virtual; abstract;
    procedure ReadListEnd(); virtual; abstract;
    function ReadSetBegin: ISet; virtual; abstract;
    procedure ReadSetEnd(); virtual; abstract;
    function ReadBool: Boolean; virtual; abstract;
    function ReadByte: ShortInt; virtual; abstract;
    function ReadI16: SmallInt; virtual; abstract;
    function ReadI32: Integer; virtual; abstract;
    function ReadI64: Int64; virtual; abstract;
    function ReadDouble:Double; virtual; abstract;
    function ReadBinary: TBytes; virtual; abstract;
    function ReadString: string; virtual;
    function ReadAnsiString: AnsiString; virtual;

    property Transport: ITransport read GetTransport;

    constructor Create( trans: ITransport );
  end;

  IBase = interface
    ['{08D9BAA8-5EAA-410F-B50B-AC2E6E5E4155}']
    function ToString: string;
    procedure Read( iprot: IProtocol);
    procedure Write( iprot: IProtocol);
  end;

  IStruct = interface
    ['{5DCE39AA-C916-4BC7-A79B-96A0C36B2220}']
    procedure SetName(const Value: string);
    function GetName: string;
    property Name: string read GetName write SetName;
  end;

  TStructImpl = class( TInterfacedObject, IStruct )
  private
    FName: string;
  protected
    function GetName: string;
    procedure SetName(const Value: string);
  public
    constructor Create( const AName: string);
  end;

  TBinaryProtocolImpl = class( TProtocolImpl )
  protected
    const
      VERSION_MASK : Cardinal = $ffff0000;
      VERSION_1 : Cardinal = $80010000;
  protected
    FStrictRead : Boolean;
    FStrictWrite : Boolean;
    FReadLength : Integer;
    FCheckReadLength : Boolean;

  private
    function ReadAll( var buf: TBytes; off: Integer; len: Integer ): Integer;
    function ReadStringBody( size: Integer): string;
    procedure CheckReadLength( len: Integer );
  public

    type
      TFactory = class( TInterfacedObject, IProtocolFactory)
      protected
        FStrictRead : Boolean;
        FStrictWrite : Boolean;
      public
        function GetProtocol(trans: ITransport): IProtocol;
        constructor Create( AStrictRead, AStrictWrite: Boolean ); overload;
        constructor Create; overload;
      end;

    constructor Create( trans: ITransport); overload;
    constructor Create( trans: ITransport; strictRead: Boolean; strictWrite: Boolean); overload;

    procedure WriteMessageBegin( message: IMessage); override;
    procedure WriteMessageEnd; override;
    procedure WriteStructBegin(struc: IStruct); override;
    procedure WriteStructEnd; override;
    procedure WriteFieldBegin(field: IField); override;
    procedure WriteFieldEnd; override;
    procedure WriteFieldStop; override;
    procedure WriteMapBegin(map: IMap); override;
    procedure WriteMapEnd; override;
    procedure WriteListBegin( list: IList); override;
    procedure WriteListEnd(); override;
    procedure WriteSetBegin( set_: ISet ); override;
    procedure WriteSetEnd(); override;
    procedure WriteBool( b: Boolean); override;
    procedure WriteByte( b: ShortInt); override;
    procedure WriteI16( i16: SmallInt); override;
    procedure WriteI32( i32: Integer); override;
    procedure WriteI64( i64: Int64); override;
    procedure WriteDouble( d: Double); override;
    procedure WriteBinary( const b: TBytes); override;

    function ReadMessageBegin: IMessage; override;
    procedure ReadMessageEnd(); override;
    function ReadStructBegin: IStruct; override;
    procedure ReadStructEnd; override;
    function ReadFieldBegin: IField; override;
    procedure ReadFieldEnd(); override;
    function ReadMapBegin: IMap; override;
    procedure ReadMapEnd(); override;
    function ReadListBegin: IList; override;
    procedure ReadListEnd(); override;
    function ReadSetBegin: ISet; override;
    procedure ReadSetEnd(); override;
    function ReadBool: Boolean; override;
    function ReadByte: ShortInt; override;
    function ReadI16: SmallInt; override;
    function ReadI32: Integer; override;
    function ReadI64: Int64; override;
    function ReadDouble:Double; override;
    function ReadBinary: TBytes; override;

    procedure SetReadLength( readLength: Integer );
  end;

implementation

function ConvertInt64ToDouble( n: Int64): Double;
begin
  ASSERT( SizeOf(n) = SizeOf(Result));
  System.Move( n, Result, SizeOf(Result));
end;

function ConvertDoubleToInt64( d: Double): Int64;
begin
  ASSERT( SizeOf(d) = SizeOf(Result));
  System.Move( d, Result, SizeOf(Result));
end;

{ TFieldImpl }

constructor TFieldImpl.Create(const AName: string; const AType: TType;
  AId: SmallInt);
begin
  FName := AName;
  FType := AType;
  FId := AId;
end;

constructor TFieldImpl.Create;
begin
  FName := '';
  FType := Low(TType);
  FId   := 0;
end;

function TFieldImpl.GetId: SmallInt;
begin
  Result := FId;
end;

function TFieldImpl.GetName: string;
begin
  Result := FName;
end;

function TFieldImpl.GetType: TType;
begin
  Result := FType;
end;

procedure TFieldImpl.SetId(Value: SmallInt);
begin
  FId := Value;
end;

procedure TFieldImpl.SetName(const Value: string);
begin
  FName := Value;
end;

procedure TFieldImpl.SetType(Value: TType);
begin
  FType := Value;
end;

{ TProtocolImpl }

constructor TProtocolImpl.Create(trans: ITransport);
begin
  inherited Create;
  FTrans := trans;
end;

function TProtocolImpl.GetTransport: ITransport;
begin
  Result := FTrans;
end;

function TProtocolImpl.ReadAnsiString: AnsiString;
var
  b : TBytes;
  len : Integer;
begin
  Result := '';
  b := ReadBinary;
  len := Length( b );
  if len > 0 then
  begin
    SetLength( Result, len);
    System.Move( b[0], Pointer(Result)^, len );
  end;
end;

function TProtocolImpl.ReadString: string;
begin
  Result := TEncoding.UTF8.GetString( ReadBinary );
end;

procedure TProtocolImpl.WriteAnsiString(const s: AnsiString);
var
  b : TBytes;
  len : Integer;
begin
  len := Length(s);
  SetLength( b, len);
  if len > 0 then
  begin
    System.Move( Pointer(s)^, b[0], len );
  end;
  WriteBinary( b );
end;

procedure TProtocolImpl.WriteString(const s: string);
var
  b : TBytes;
begin
  b := TEncoding.UTF8.GetBytes(s);
  WriteBinary( b );
end;

{ TProtocolUtil }

class procedure TProtocolUtil.Skip( prot: IProtocol; type_: TType);
begin

end;

{ TStructImpl }

constructor TStructImpl.Create(const AName: string);
begin
  inherited Create;
  FName := AName;
end;

function TStructImpl.GetName: string;
begin
  Result := FName;
end;

procedure TStructImpl.SetName(const Value: string);
begin
  FName := Value;
end;

{ TMapImpl }

constructor TMapImpl.Create(AValueType, AKeyType: TType; ACount: Integer);
begin
  inherited Create;
  FValueType := AValueType;
  FKeyType := AKeyType;
  FCount := ACount;
end;

constructor TMapImpl.Create;
begin

end;

function TMapImpl.GetCount: Integer;
begin
  Result := FCount;
end;

function TMapImpl.GetKeyType: TType;
begin
  Result := FKeyType;
end;

function TMapImpl.GetValueType: TType;
begin
  Result := FValueType;
end;

procedure TMapImpl.SetCount(Value: Integer);
begin
  FCount := Value;
end;

procedure TMapImpl.SetKeyType(Value: TType);
begin
  FKeyType := Value;
end;

procedure TMapImpl.SetValueType(Value: TType);
begin
  FValueType := Value;
end;

{ IMessage }

constructor TMessageImpl.Create(AName: string; AMessageType: TMessageType;
  ASeqID: Integer);
begin
  inherited Create;
  FName := AName;
  FMessageType := AMessageType;
  FSeqID := ASeqID;
end;

constructor TMessageImpl.Create;
begin
  inherited;
end;

function TMessageImpl.GetName: string;
begin
  Result := FName;
end;

function TMessageImpl.GetSeqID: Integer;
begin
  Result := FSeqID;
end;

function TMessageImpl.GetType: TMessageType;
begin
  Result := FMessageType;
end;

procedure TMessageImpl.SetName(const Value: string);
begin
  FName := Value;
end;

procedure TMessageImpl.SetSeqID(Value: Integer);
begin
  FSeqID := Value;
end;

procedure TMessageImpl.SetType(Value: TMessageType);
begin
  FMessageType := Value;
end;

{ ISet }

constructor TSetImpl.Create( AElementType: TType; ACount: Integer);
begin
  inherited Create;
  FCount := ACount;
  FElementType := AElementType;
end;

constructor TSetImpl.Create;
begin

end;

function TSetImpl.GetCount: Integer;
begin
  Result := FCount;
end;

function TSetImpl.GetElementType: TType;
begin
  Result := FElementType;
end;

procedure TSetImpl.SetCount(Value: Integer);
begin
  FCount := Value;
end;

procedure TSetImpl.SetElementType(Value: TType);
begin
  FElementType := Value;
end;

{ IList }

constructor TListImpl.Create( AElementType: TType; ACount: Integer);
begin
  inherited Create;
  FCount := ACount;
  FElementType := AElementType;
end;

constructor TListImpl.Create;
begin

end;

function TListImpl.GetCount: Integer;
begin
  Result := FCount;
end;

function TListImpl.GetElementType: TType;
begin
  Result := FElementType;
end;

procedure TListImpl.SetCount(Value: Integer);
begin
  FCount := Value;
end;

procedure TListImpl.SetElementType(Value: TType);
begin
  FElementType := Value;
end;

{ TBinaryProtocolImpl }

constructor TBinaryProtocolImpl.Create( trans: ITransport);
begin
  Create( trans, False, True);
end;

procedure TBinaryProtocolImpl.CheckReadLength(len: Integer);
begin
  if FCheckReadLength then
  begin
    Dec( FReadLength, len);
    if FReadLength < 0 then
    begin
      raise Exception.Create( 'Message length exceeded: ' + IntToStr( len ) );
    end;
  end;
end;

constructor TBinaryProtocolImpl.Create(trans: ITransport; strictRead,
  strictWrite: Boolean);
begin
  inherited Create( trans );
  FStrictRead := strictRead;
  FStrictWrite := strictWrite;
end;

function TBinaryProtocolImpl.ReadAll( var buf: TBytes; off,
  len: Integer): Integer;
begin
  CheckReadLength( len );
  Result := FTrans.ReadAll( buf, off, len );
end;

function TBinaryProtocolImpl.ReadBinary: TBytes;
var
  size : Integer;
  buf : TBytes;
begin
  size := ReadI32;
  CheckReadLength( size );
  SetLength( buf, size );
  FTrans.ReadAll( buf, 0, size);
  Result := buf;
end;

function TBinaryProtocolImpl.ReadBool: Boolean;
begin
  Result := ReadByte = 1;
end;

function TBinaryProtocolImpl.ReadByte: ShortInt;
var
  bin : TBytes;
begin
  SetLength( bin, 1);
  ReadAll( bin, 0, 1 );
  Result := ShortInt( bin[0]);
end;

function TBinaryProtocolImpl.ReadDouble: Double;
begin
  Result := ConvertInt64ToDouble( ReadI64 )
end;

function TBinaryProtocolImpl.ReadFieldBegin: IField;
var
  field : IField;
begin
  field := TFieldImpl.Create;
  field.Type_ := TType( ReadByte);
  if ( field.Type_ <> TType.Stop ) then
  begin
    field.Id := ReadI16;
  end;
  Result := field;
end;

procedure TBinaryProtocolImpl.ReadFieldEnd;
begin

end;

function TBinaryProtocolImpl.ReadI16: SmallInt;
var
  i16in : TBytes;
begin
  SetLength( i16in, 2 );
  ReadAll( i16in, 0, 2);
  Result := SmallInt(((i16in[0] and $FF) shl 8) or (i16in[1] and $FF));
end;

function TBinaryProtocolImpl.ReadI32: Integer;
var
  i32in : TBytes;
begin
  SetLength( i32in, 4 );
  ReadAll( i32in, 0, 4);

  Result := Integer(
    ((i32in[0] and $FF) shl 24) or
    ((i32in[1] and $FF) shl 16) or
    ((i32in[2] and $FF) shl 8) or
     (i32in[3] and $FF));

end;

function TBinaryProtocolImpl.ReadI64: Int64;
var
  i64in : TBytes;
begin
  SetLength( i64in, 8);
  ReadAll( i64in, 0, 8);
  Result :=
    (Int64( i64in[0] and $FF) shl 56) or
    (Int64( i64in[1] and $FF) shl 48) or
    (Int64( i64in[2] and $FF) shl 40) or
    (Int64( i64in[3] and $FF) shl 32) or
    (Int64( i64in[4] and $FF) shl 24) or
    (Int64( i64in[5] and $FF) shl 16) or
    (Int64( i64in[6] and $FF) shl 8) or
    (Int64( i64in[7] and $FF));
end;

function TBinaryProtocolImpl.ReadListBegin: IList;
var
  list : IList;
begin
  list := TListImpl.Create;
  list.ElementType := TType( ReadByte );
  list.Count := ReadI32;
  Result := list;
end;

procedure TBinaryProtocolImpl.ReadListEnd;
begin

end;

function TBinaryProtocolImpl.ReadMapBegin: IMap;
var
  map : IMap;
begin
  map := TMapImpl.Create;
  map.KeyType := TType( ReadByte );
  map.ValueType := TType( ReadByte );
  map.Count := ReadI32;
  Result := map;
end;

procedure TBinaryProtocolImpl.ReadMapEnd;
begin

end;

function TBinaryProtocolImpl.ReadMessageBegin: IMessage;
var
  size : Integer;
  version : Integer;
  message : IMessage;
begin
  message := TMessageImpl.Create;
  size := ReadI32;
  if (size < 0) then
  begin
    version := size and Integer( VERSION_MASK);
    if ( version <> Integer( VERSION_1)) then
    begin
      raise TProtocolException.Create(TProtocolException.BAD_VERSION, 'Bad version in ReadMessageBegin: ' + IntToStr(version) );
    end;
    message.Type_ := TMessageType( size and $000000ff);
    message.Name := ReadString;
    message.SeqID := ReadI32;
  end else
  begin
    if FStrictRead then
    begin
      raise TProtocolException.Create( TProtocolException.BAD_VERSION, 'Missing version in readMessageBegin, old client?' );
    end;
    message.Name := ReadStringBody( size );
    message.Type_ := TMessageType( ReadByte );
    message.SeqID := ReadI32;
  end;
  Result := message;
end;

procedure TBinaryProtocolImpl.ReadMessageEnd;
begin
  inherited;

end;

function TBinaryProtocolImpl.ReadSetBegin: ISet;
var
  set_ : ISet;
begin
  set_ := TSetImpl.Create;
  set_.ElementType := TType( ReadByte );
  set_.Count := ReadI32;
  Result := set_;
end;

procedure TBinaryProtocolImpl.ReadSetEnd;
begin

end;

function TBinaryProtocolImpl.ReadStringBody( size: Integer): string;
var
  buf : TBytes;
begin
  CheckReadLength( size );
  SetLength( buf, size );
  FTrans.ReadAll( buf, 0, size );
  Result := TEncoding.UTF8.GetString( buf);
end;

function TBinaryProtocolImpl.ReadStructBegin: IStruct;
begin
  Result := TStructImpl.Create('');
end;

procedure TBinaryProtocolImpl.ReadStructEnd;
begin
  inherited;

end;

procedure TBinaryProtocolImpl.SetReadLength(readLength: Integer);
begin
  FReadLength := readLength;
  FCheckReadLength := True;
end;

procedure TBinaryProtocolImpl.WriteBinary( const b: TBytes);
begin
  WriteI32( Length(b));
  FTrans.Write(b, 0, Length( b));
end;

procedure TBinaryProtocolImpl.WriteBool(b: Boolean);
begin
  if b then
  begin
    WriteByte( 1 );
  end else
  begin
    WriteByte( 0 );
  end;
end;

procedure TBinaryProtocolImpl.WriteByte(b: ShortInt);
var
  a : TBytes;
begin
  SetLength( a, 1);
  a[0] := Byte( b );
  FTrans.Write( a, 0, 1 );
end;

procedure TBinaryProtocolImpl.WriteDouble(d: Double);
begin
  WriteI64(ConvertDoubleToInt64(d));
end;

procedure TBinaryProtocolImpl.WriteFieldBegin(field: IField);
begin
  WriteByte(ShortInt(field.Type_));
  WriteI16(field.ID);
end;

procedure TBinaryProtocolImpl.WriteFieldEnd;
begin

end;

procedure TBinaryProtocolImpl.WriteFieldStop;
begin
  WriteByte(ShortInt(TType.Stop));
end;

procedure TBinaryProtocolImpl.WriteI16(i16: SmallInt);
var
  i16out : TBytes;
begin
  SetLength( i16out, 2);
  i16out[0] := Byte($FF and (i16 shr 8));
  i16out[1] := Byte($FF and i16);
  FTrans.Write( i16out );
end;

procedure TBinaryProtocolImpl.WriteI32(i32: Integer);
var
  i32out : TBytes;
begin
  SetLength( i32out, 4);
  i32out[0] := Byte($FF and (i32 shr 24));
  i32out[1] := Byte($FF and (i32 shr 16));
  i32out[2] := Byte($FF and (i32 shr 8));
  i32out[3] := Byte($FF and i32);
  FTrans.Write( i32out, 0, 4);
end;

procedure TBinaryProtocolImpl.WriteI64(i64: Int64);
var
  i64out : TBytes;
begin
  SetLength( i64out, 8);
  i64out[0] := Byte($FF and (i64 shr 56));
  i64out[1] := Byte($FF and (i64 shr 48));
  i64out[2] := Byte($FF and (i64 shr 40));
  i64out[3] := Byte($FF and (i64 shr 32));
  i64out[4] := Byte($FF and (i64 shr 24));
  i64out[5] := Byte($FF and (i64 shr 16));
  i64out[6] := Byte($FF and (i64 shr 8));
  i64out[7] := Byte($FF and i64);
  FTrans.Write( i64out, 0, 8);
end;

procedure TBinaryProtocolImpl.WriteListBegin(list: IList);
begin
  WriteByte(ShortInt(list.ElementType));
  WriteI32(list.Count);
end;

procedure TBinaryProtocolImpl.WriteListEnd;
begin

end;

procedure TBinaryProtocolImpl.WriteMapBegin(map: IMap);
begin
  WriteByte(ShortInt(map.KeyType));
  WriteByte(ShortInt(map.ValueType));
  WriteI32(map.Count);
end;

procedure TBinaryProtocolImpl.WriteMapEnd;
begin

end;

procedure TBinaryProtocolImpl.WriteMessageBegin( message: IMessage);
var
  version : Cardinal;
begin
  if FStrictWrite then
  begin
    version := VERSION_1 or Cardinal( message.Type_);
    WriteI32( Integer( version) );
    WriteString( message.Name);
  	WriteI32(message.SeqID);
  end else
  begin
    WriteString(message.Name);
    WriteByte(ShortInt(message.Type_));
    WriteI32(message.SeqID);
  end;
end;

procedure TBinaryProtocolImpl.WriteMessageEnd;
begin

end;

procedure TBinaryProtocolImpl.WriteSetBegin(set_: ISet);
begin
  WriteByte(ShortInt(set_.ElementType));
  WriteI32(set_.Count);
end;

procedure TBinaryProtocolImpl.WriteSetEnd;
begin

end;

procedure TBinaryProtocolImpl.WriteStructBegin(struc: IStruct);
begin

end;

procedure TBinaryProtocolImpl.WriteStructEnd;
begin

end;

{ TProtocolException }

constructor TProtocolException.Create;
begin
  inherited Create('');
  FType := UNKNOWN;
end;

constructor TProtocolException.Create(type_: Integer);
begin
  inherited Create('');
  FType := type_;
end;

constructor TProtocolException.Create(type_: Integer; const msg: string);
begin
  inherited Create( msg );
  FType := type_;
end;

{ TThriftStringBuilder }

function TThriftStringBuilder.Append(const Value: TBytes): TStringBuilder;
begin
  Result := Append( string( RawByteString(Value)) );
end;

function TThriftStringBuilder.Append(
  const Value: IThriftContainer): TStringBuilder;
begin
  Result := Append( Value.ToString );
end;

{ TBinaryProtocolImpl.TFactory }

constructor TBinaryProtocolImpl.TFactory.Create(AStrictRead, AStrictWrite: Boolean);
begin
  FStrictRead := AStrictRead;
  FStrictWrite := AStrictWrite;
end;

constructor TBinaryProtocolImpl.TFactory.Create;
begin
  Create( False, True )
end;

function TBinaryProtocolImpl.TFactory.GetProtocol(trans: ITransport): IProtocol;
begin
  Result := TBinaryProtocolImpl.Create( trans );
end;

end.

