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

unit Thrift.Transport;

interface

uses
  Classes,
  SysUtils,
  Sockets,
  Generics.Collections,
  Thrift.Collections,
  Thrift.Utils,
  Thrift.Stream,
  ActiveX,
  msxml;

type
  ITransport = interface
    ['{A4A9FC37-D620-44DC-AD21-662D16364CE4}']
    function GetIsOpen: Boolean;
    property IsOpen: Boolean read GetIsOpen;
    function Peek: Boolean;
    procedure Open;
    procedure Close;
    function Read(var buf: TBytes; off: Integer; len: Integer): Integer;
    function ReadAll(var buf: TBytes; off: Integer; len: Integer): Integer;
    procedure Write( const buf: TBytes); overload;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); overload;
    procedure Flush;
  end;

  TTransportImpl = class( TInterfacedObject, ITransport)
  protected
    function GetIsOpen: Boolean; virtual; abstract;
    property IsOpen: Boolean read GetIsOpen;
    function Peek: Boolean;
    procedure Open(); virtual; abstract;
    procedure Close(); virtual; abstract;
    function Read(var buf: TBytes; off: Integer; len: Integer): Integer; virtual; abstract;
    function ReadAll(var buf: TBytes; off: Integer; len: Integer): Integer; virtual;
    procedure Write( const buf: TBytes); overload; virtual;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); overload; virtual; abstract;
    procedure Flush; virtual;
  end;

  TTransportException = class( Exception )
  public
    type
      TExceptionType = (
        Unknown,
        NotOpen,
        AlreadyOpen,
        TimedOut,
        EndOfFile
      );
  private
    FType : TExceptionType;
  public
    constructor Create( AType: TExceptionType); overload;
    constructor Create( const msg: string); overload;
    constructor Create( AType: TExceptionType; const msg: string); overload;
    property Type_: TExceptionType read FType;
  end;

  IHTTPClient = interface( ITransport )
    ['{0F5DB8AB-710D-4338-AAC9-46B5734C5057}']
    procedure SetConnectionTimeout(const Value: Integer);
    function GetConnectionTimeout: Integer;
    procedure SetReadTimeout(const Value: Integer);
    function GetReadTimeout: Integer;
    function GetCustomHeaders: IThriftDictionary<string,string>;
    procedure SendRequest;
    property ConnectionTimeout: Integer read GetConnectionTimeout write SetConnectionTimeout;
    property ReadTimeout: Integer read GetReadTimeout write SetReadTimeout;
    property CustomHeaders: IThriftDictionary<string,string> read GetCustomHeaders;
  end;

  THTTPClientImpl = class( TTransportImpl, IHTTPClient)
  private
    FUri : string;
    FInputStream : IThriftStream;
    FOutputStream : IThriftStream;
    FConnectionTimeout : Integer;
    FReadTimeout : Integer;
    FCustomHeaders : IThriftDictionary<string,string>;

    function CreateRequest: IXMLHTTPRequest;
  protected
    function GetIsOpen: Boolean; override;
    procedure Open(); override;
    procedure Close(); override;
    function Read( var buf: TBytes; off: Integer; len: Integer): Integer; override;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); override;
    procedure Flush; override;

    procedure SetConnectionTimeout(const Value: Integer);
    function GetConnectionTimeout: Integer;
    procedure SetReadTimeout(const Value: Integer);
    function GetReadTimeout: Integer;
    function GetCustomHeaders: IThriftDictionary<string,string>;
    procedure SendRequest;
    property ConnectionTimeout: Integer read GetConnectionTimeout write SetConnectionTimeout;
    property ReadTimeout: Integer read GetReadTimeout write SetReadTimeout;
    property CustomHeaders: IThriftDictionary<string,string> read GetCustomHeaders;
  public
    constructor Create( const AUri: string);
    destructor Destroy; override;
  end;

  IServerTransport = interface
    ['{BF6B7043-DA22-47BF-8B11-2B88EC55FE12}']
    procedure Listen;
    procedure Close;
    function Accept: ITransport;
  end;

  TServerTransportImpl = class( TInterfacedObject, IServerTransport)
  protected
    function AcceptImpl: ITransport; virtual; abstract;
  public
    procedure Listen; virtual; abstract;
    procedure Close; virtual; abstract;
    function Accept: ITransport;
  end;

  ITransportFactory = interface
    ['{DD809446-000F-49E1-9BFF-E0D0DC76A9D7}']
    function GetTransport( ATrans: ITransport): ITransport;
  end;

  TTransportFactoryImpl = class( TInterfacedObject, ITransportFactory)
    function GetTransport( ATrans: ITransport): ITransport; virtual;
  end;

  TTcpSocketStreamImpl = class( TThriftStreamImpl )
  private
    FTcpClient : TCustomIpClient;
  protected
    procedure Write( const buffer: TBytes; offset: Integer; count: Integer); override;
    function Read( var buffer: TBytes; offset: Integer; count: Integer): Integer; override;
    procedure Open; override;
    procedure Close; override;
    procedure Flush; override;

    function IsOpen: Boolean; override;
    function ToArray: TBytes; override;
  public
    constructor Create( ATcpClient: TCustomIpClient);
  end;

  IStreamTransport = interface( ITransport )
    ['{A8479B47-2A3E-4421-A9A0-D5A9EDCC634A}']
    function GetInputStream: IThriftStream;
    function GetOutputStream: IThriftStream;
    property InputStream : IThriftStream read GetInputStream;
    property OutputStream : IThriftStream read GetOutputStream;
  end;

  TStreamTransportImpl = class( TTransportImpl, IStreamTransport)
  protected
    FInputStream : IThriftStream;
    FOutputStream : IThriftStream;
  protected
    function GetIsOpen: Boolean; override;

    function GetInputStream: IThriftStream;
    function GetOutputStream: IThriftStream;
  public
    property InputStream : IThriftStream read GetInputStream;
    property OutputStream : IThriftStream read GetOutputStream;

    procedure Open; override;
    procedure Close; override;
    procedure Flush; override;
    function Read(var buf: TBytes; off: Integer; len: Integer): Integer; override;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); override;
    constructor Create( AInputStream : IThriftStream; AOutputStream : IThriftStream);
    destructor Destroy; override;
  end;

  TBufferedStreamImpl = class( TThriftStreamImpl)
  private
    FStream : IThriftStream;
    FBufSize : Integer;
    FBuffer : TMemoryStream;
  protected
    procedure Write( const buffer: TBytes; offset: Integer; count: Integer); override;
    function Read( var buffer: TBytes; offset: Integer; count: Integer): Integer; override;
    procedure Open;  override;
    procedure Close; override;
    procedure Flush; override;
    function IsOpen: Boolean; override;
    function ToArray: TBytes; override;
  public
    constructor Create( AStream: IThriftStream; ABufSize: Integer);
    destructor Destroy; override;
  end;

  TServerSocketImpl = class( TServerTransportImpl)
  private
    FServer : TTcpServer;
    FPort : Integer;
    FClientTimeout : Integer;
    FUseBufferedSocket : Boolean;
    FOwnsServer : Boolean;
  protected
    function AcceptImpl: ITransport; override;
  public
    constructor Create( AServer: TTcpServer ); overload;
    constructor Create( AServer: TTcpServer; AClientTimeout: Integer); overload;
    constructor Create( APort: Integer); overload;
    constructor Create( APort: Integer; AClientTimeout: Integer); overload;
    constructor Create( APort: Integer; AClientTimeout: Integer;
      AUseBufferedSockets: Boolean); overload;
    destructor Destroy; override;
    procedure Listen; override;
    procedure Close; override;
  end;

  TBufferedTransportImpl = class( TTransportImpl )
  private
    FInputBuffer : IThriftStream;
    FOutputBuffer : IThriftStream;
    FTransport : IStreamTransport;
    FBufSize : Integer;

    procedure InitBuffers;
    function GetUnderlyingTransport: ITransport;
  protected
    function GetIsOpen: Boolean; override;
    procedure Flush; override;
  public
    procedure Open(); override;
    procedure Close(); override;
    function Read(var buf: TBytes; off: Integer; len: Integer): Integer; override;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); override;
    constructor Create( ATransport : IStreamTransport ); overload;
    constructor Create( ATransport : IStreamTransport; ABufSize: Integer); overload;
    property UnderlyingTransport: ITransport read GetUnderlyingTransport;
    property IsOpen: Boolean read GetIsOpen;
  end;

  TSocketImpl = class(TStreamTransportImpl)
  private
    FClient : TCustomIpClient;
    FOwnsClient : Boolean;
    FHost : string;
    FPort : Integer;
    FTimeout : Integer;

    procedure InitSocket;
  protected
    function GetIsOpen: Boolean; override;
  public
    procedure Open; override;
    constructor Create( AClient : TCustomIpClient); overload;
    constructor Create( const AHost: string; APort: Integer); overload;
    constructor Create( const AHost: string; APort: Integer; ATimeout: Integer); overload;
    destructor Destroy; override;
    procedure Close; override;
    property TcpClient: TCustomIpClient read FClient;
    property Host : string read FHost;
    property Port: Integer read FPort;
  end;

  TFramedTransportImpl = class( TTransportImpl)
  private const
    FHeaderSize : Integer = 4;
  private class var
    FHeader_Dummy : array of Byte;
  protected
    FTransport : ITransport;
    FWriteBuffer : TMemoryStream;
    FReadBuffer : TMemoryStream;

    procedure InitWriteBuffer;
    procedure ReadFrame;
  public
    type
      TFactory = class( TTransportFactoryImpl )
      public
        function GetTransport( ATrans: ITransport): ITransport; override;
      end;

{$IF CompilerVersion >= 21.0}
    class constructor Create;
{$IFEND}
    constructor Create; overload;
    constructor Create( ATrans: ITransport); overload;
    destructor Destroy; override;

    procedure Open(); override;
    function GetIsOpen: Boolean; override;

    procedure Close(); override;
    function Read(var buf: TBytes; off: Integer; len: Integer): Integer; override;
    procedure Write( const buf: TBytes; off: Integer; len: Integer); override;
    procedure Flush; override;
  end;

{$IF CompilerVersion < 21.0}
procedure TFramedTransportImpl_Initialize;
{$IFEND}

implementation

{ TTransportImpl }

procedure TTransportImpl.Flush;
begin

end;

function TTransportImpl.Peek: Boolean;
begin
  Result := IsOpen;
end;

function TTransportImpl.ReadAll( var buf: TBytes; off, len: Integer): Integer;
var
  got : Integer;
  ret : Integer;
begin
  got := 0;
  while ( got < len) do
  begin
    ret := Read( buf, off + got, len - got);
    if ( ret <= 0 ) then
    begin
      raise TTransportException.Create( 'Cannot read, Remote side has closed' );
    end;
    got := got + ret;
  end;
  Result := got;
end;

procedure TTransportImpl.Write( const buf: TBytes);
begin
  Self.Write( buf, 0, Length(buf) );
end;

{ THTTPClientImpl }

procedure THTTPClientImpl.Close;
begin
  FInputStream := nil;
  FOutputStream := nil;
end;

constructor THTTPClientImpl.Create(const AUri: string);
begin
  inherited Create;
  FUri := AUri;
  FCustomHeaders := TThriftDictionaryImpl<string,string>.Create;
  FOutputStream := TThriftStreamAdapterDelphi.Create( TMemoryStream.Create, True);
end;

function THTTPClientImpl.CreateRequest: IXMLHTTPRequest;
var
  pair : TPair<string,string>;
begin
{$IF CompilerVersion >= 21.0}
  Result := CoXMLHTTP.Create;
{$ELSE}
  Result := CoXMLHTTPRequest.Create;
{$IFEND}

  Result.open('POST', FUri, False, '', '');
  Result.setRequestHeader( 'Content-Type', 'application/x-thrift');
  Result.setRequestHeader( 'Accept', 'application/x-thrift');
  Result.setRequestHeader( 'User-Agent', 'Delphi/IHTTPClient');

  for pair in FCustomHeaders do
  begin
    Result.setRequestHeader( pair.Key, pair.Value );
  end;
end;

destructor THTTPClientImpl.Destroy;
begin
  Close;
  inherited;
end;

procedure THTTPClientImpl.Flush;
begin
  try
    SendRequest;
  finally
    FOutputStream := nil;
    FOutputStream := TThriftStreamAdapterDelphi.Create( TMemoryStream.Create, True);
  end;
end;

function THTTPClientImpl.GetConnectionTimeout: Integer;
begin
  Result := FConnectionTimeout;
end;

function THTTPClientImpl.GetCustomHeaders: IThriftDictionary<string,string>;
begin
  Result := FCustomHeaders;
end;

function THTTPClientImpl.GetIsOpen: Boolean;
begin
  Result := True;
end;

function THTTPClientImpl.GetReadTimeout: Integer;
begin
  Result := FReadTimeout;
end;

procedure THTTPClientImpl.Open;
begin

end;

function THTTPClientImpl.Read( var buf: TBytes; off, len: Integer): Integer;
begin
  if FInputStream = nil then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen,
      'No request has been sent');
  end;
  try
    Result := FInputStream.Read( buf, off, len )
  except
    on E: Exception do
    begin
      raise TTransportException.Create( TTransportException.TExceptionType.Unknown,
        E.Message);
    end;
  end;
end;

procedure THTTPClientImpl.SendRequest;
var
  xmlhttp : IXMLHTTPRequest;
  ms : TMemoryStream;
  a : TBytes;
  len : Integer;
begin
  xmlhttp := CreateRequest;

  ms := TMemoryStream.Create;
  try
    a := FOutputStream.ToArray;
    len := Length(a);
    if len > 0 then
    begin
      ms.WriteBuffer( Pointer(@a[0])^, len);
    end;
    ms.Position := 0;
    xmlhttp.send( IUnknown( TStreamAdapter.Create( ms, soReference )));
    FInputStream := nil;
    FInputStream := TThriftStreamAdapterCOM.Create( IUnknown( xmlhttp.responseStream) as IStream);
  finally
    ms.Free;
  end;
end;

procedure THTTPClientImpl.SetConnectionTimeout(const Value: Integer);
begin
  FConnectionTimeout := Value;
end;

procedure THTTPClientImpl.SetReadTimeout(const Value: Integer);
begin
  FReadTimeout := Value
end;

procedure THTTPClientImpl.Write( const buf: TBytes; off, len: Integer);
begin
  FOutputStream.Write( buf, off, len);
end;

{ TTransportException }

constructor TTransportException.Create(AType: TExceptionType);
begin
  Create( AType, '' )
end;

constructor TTransportException.Create(AType: TExceptionType;
  const msg: string);
begin
  inherited Create(msg);
  FType := AType;
end;

constructor TTransportException.Create(const msg: string);
begin
  inherited Create(msg);
end;

{ TServerTransportImpl }

function TServerTransportImpl.Accept: ITransport;
begin
  Result := AcceptImpl;
  if Result = nil then
  begin
    raise TTransportException.Create( 'accept() may not return NULL' );
  end;
end;

{ TTransportFactoryImpl }

function TTransportFactoryImpl.GetTransport(ATrans: ITransport): ITransport;
begin
  Result := ATrans;
end;

{ TServerSocket }

constructor TServerSocketImpl.Create(AServer: TTcpServer; AClientTimeout: Integer);
begin
  FServer := AServer;
  FClientTimeout := AClientTimeout;
end;

constructor TServerSocketImpl.Create(AServer: TTcpServer);
begin
  Create( AServer, 0 );
end;

constructor TServerSocketImpl.Create(APort: Integer);
begin
  Create( APort, 0 );
end;

function TServerSocketImpl.AcceptImpl: ITransport;
var
  ret : TCustomIpClient;
  ret2 : IStreamTransport;
  ret3 : ITransport;
begin
  if FServer = nil then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen,
      'No underlying server socket.');
  end;

  try
    ret := TCustomIpClient.Create(nil);
    if ( not FServer.Accept( ret )) then
    begin
      ret.Free;
      Result := nil;
      Exit;
    end;

    if ret = nil then
    begin
      Result := nil;
      Exit;
    end;

    ret2 := TSocketImpl.Create( ret );
    if FUseBufferedSocket then
    begin
      ret3 := TBufferedTransportImpl.Create(ret2);
      Result := ret3;
    end else
    begin
      Result := ret2;
    end;

  except
    on E: Exception do
    begin
      raise TTransportException.Create( E.ToString );
    end;
  end;
end;

procedure TServerSocketImpl.Close;
begin
  if FServer <> nil then
  begin
    try
      FServer.Active := False;
    except
      on E: Exception do
      begin
        raise TTransportException.Create('Error on closing socket : ' + E.Message);
      end;
    end;
  end;
end;

constructor TServerSocketImpl.Create(APort, AClientTimeout: Integer;
  AUseBufferedSockets: Boolean);
begin
  FPort := APort;
  FClientTimeout := AClientTimeout;
  FUseBufferedSocket := AUseBufferedSockets;
  FOwnsServer := True;
  FServer := TTcpServer.Create( nil );
  FServer.BlockMode := bmBlocking;
{$IF CompilerVersion >= 21.0}
  FServer.LocalPort := AnsiString( IntToStr( FPort));
{$ELSE}
  FServer.LocalPort := IntToStr( FPort);
{$IFEND}
end;

destructor TServerSocketImpl.Destroy;
begin
  if FOwnsServer then
  begin
    FServer.Free;
  end;
  inherited;
end;

procedure TServerSocketImpl.Listen;
begin
  if FServer <> nil then
  begin
    try
      FServer.Active := True;
    except
      on E: Exception do
      begin
        raise TTransportException.Create('Could not accept on listening socket: ' + E.Message);
      end;
    end;
  end;
end;

constructor TServerSocketImpl.Create(APort, AClientTimeout: Integer);
begin
  Create( APort, AClientTimeout, False );
end;

{ TSocket }

constructor TSocketImpl.Create(AClient : TCustomIpClient);
var
  stream : IThriftStream;
begin
  FClient := AClient;
  stream := TTcpSocketStreamImpl.Create( FClient);
  FInputStream := stream;
  FOutputStream := stream;
end;

constructor TSocketImpl.Create(const AHost: string; APort: Integer);
begin
  Create( AHost, APort, 0);
end;

procedure TSocketImpl.Close;
begin
  inherited Close;
  if FClient <> nil then
  begin
    FClient.Free;
    FClient := nil;
  end;
end;

constructor TSocketImpl.Create(const AHost: string; APort, ATimeout: Integer);
begin
  FHost := AHost;
  FPort := APort;
  FTimeout := ATimeout;
  InitSocket;
end;

destructor TSocketImpl.Destroy;
begin
  if FOwnsClient then
  begin
    FClient.Free;
  end;
  inherited;
end;

function TSocketImpl.GetIsOpen: Boolean;
begin
  Result := False;
  if FClient <> nil then
  begin
    Result := FClient.Connected;
  end;
end;

procedure TSocketImpl.InitSocket;
var
  stream : IThriftStream;
begin
  if FClient <> nil then
  begin
    if FOwnsClient then
    begin
      FClient.Free;
      FClient := nil;
    end;
  end;
  FClient := TTcpClient.Create( nil );
  FOwnsClient := True;

  stream := TTcpSocketStreamImpl.Create( FClient);
  FInputStream := stream;
  FOutputStream := stream;

end;

procedure TSocketImpl.Open;
begin
  if IsOpen then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.AlreadyOpen,
      'Socket already connected');
  end;

  if FHost =  '' then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen,
      'Cannot open null host');
  end;

  if Port <= 0 then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen,
      'Cannot open without port');
  end;

  if FClient = nil then
  begin
    InitSocket;
  end;

  FClient.RemoteHost := TSocketHost( Host);
  FClient.RemotePort := TSocketPort( IntToStr( Port));
  FClient.Connect;

  FInputStream := TTcpSocketStreamImpl.Create( FClient);
  FOutputStream := FInputStream;
end;

{ TBufferedStream }

procedure TBufferedStreamImpl.Close;
begin
  Flush;
  FStream := nil;
  FBuffer.Free;
  FBuffer := nil;
end;

constructor TBufferedStreamImpl.Create(AStream: IThriftStream; ABufSize: Integer);
begin
  FStream := AStream;
  FBufSize := ABufSize;
  FBuffer := TMemoryStream.Create;
end;

destructor TBufferedStreamImpl.Destroy;
begin
  Close;
  inherited;
end;

procedure TBufferedStreamImpl.Flush;
var
  buf : TBytes;
  len : Integer;
begin
  if IsOpen then
  begin
    len := FBuffer.Size;
    if len > 0 then
    begin
      SetLength( buf, len );
      FBuffer.Position := 0;
      FBuffer.Read( Pointer(@buf[0])^, len );
      FStream.Write( buf, 0, len );
    end;
    FBuffer.Clear;
  end;
end;

function TBufferedStreamImpl.IsOpen: Boolean;
begin
  Result := (FBuffer <> nil) and ( FStream <> nil);
end;

procedure TBufferedStreamImpl.Open;
begin

end;

function TBufferedStreamImpl.Read( var buffer: TBytes; offset: Integer; count: Integer): Integer;
var
  nRead : Integer;
  tempbuf : TBytes;
begin
  inherited;
  Result := 0;
  if count > 0 then
  begin
    if IsOpen then
    begin
      if FBuffer.Position >= FBuffer.Size then
      begin
        FBuffer.Clear;
        SetLength( tempbuf, FBufSize);
        nRead := FStream.Read( tempbuf, 0, FBufSize );
        if nRead > 0 then
        begin
          FBuffer.WriteBuffer( Pointer(@tempbuf[0])^, nRead );
          FBuffer.Position := 0;
        end;
      end;

      if FBuffer.Position < FBuffer.Size then
      begin
        Result := FBuffer.Read( Pointer(@buffer[offset])^, count );
      end;
    end;
  end;
end;

function TBufferedStreamImpl.ToArray: TBytes;
var
  len : Integer;
begin
  len := 0;

  if IsOpen then
  begin
    len := FBuffer.Size;
  end;

  SetLength( Result, len);

  if len > 0 then
  begin
    FBuffer.Position := 0;
    FBuffer.Read( Pointer(@Result[0])^, len );
  end;
end;

procedure TBufferedStreamImpl.Write( const buffer: TBytes; offset: Integer; count: Integer);
begin
  inherited;
  if count > 0 then
  begin
    if IsOpen then
    begin
      FBuffer.Write( Pointer(@buffer[offset])^, count );
      if FBuffer.Size > FBufSize then
      begin
        Flush;
      end;
    end;
  end;
end;

{ TStreamTransportImpl }

procedure TStreamTransportImpl.Close;
begin
  if FInputStream <> FOutputStream then
  begin
    if FInputStream <> nil then
    begin
      FInputStream := nil;
    end;
    if FOutputStream <> nil then
    begin
      FOutputStream := nil;
    end;
  end else
  begin
    FInputStream := nil;
    FOutputStream := nil;
  end;
end;

constructor TStreamTransportImpl.Create( AInputStream : IThriftStream; AOutputStream : IThriftStream);
begin
  FInputStream := AInputStream;
  FOutputStream := AOutputStream;
end;

destructor TStreamTransportImpl.Destroy;
begin
  FInputStream := nil;
  FOutputStream := nil;
  inherited;
end;

procedure TStreamTransportImpl.Flush;
begin
  if FOutputStream = nil then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen, 'Cannot flush null outputstream' );
  end;

  FOutputStream.Flush;
end;

function TStreamTransportImpl.GetInputStream: IThriftStream;
begin
  Result := FInputStream;
end;

function TStreamTransportImpl.GetIsOpen: Boolean;
begin
  Result := True;
end;

function TStreamTransportImpl.GetOutputStream: IThriftStream;
begin
  Result := FInputStream;
end;

procedure TStreamTransportImpl.Open;
begin

end;

function TStreamTransportImpl.Read(var buf: TBytes; off, len: Integer): Integer;
begin
  if FInputStream = nil then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen, 'Cannot read from null inputstream' );
  end;
  Result := FInputStream.Read( buf, off, len );
end;

procedure TStreamTransportImpl.Write(const buf: TBytes; off, len: Integer);
begin
  if FOutputStream = nil then
  begin
    raise TTransportException.Create( TTransportException.TExceptionType.NotOpen, 'Cannot write to null outputstream' );
  end;

  FOutputStream.Write( buf, off, len );
end;

{ TBufferedTransportImpl }

constructor TBufferedTransportImpl.Create(ATransport: IStreamTransport);
begin
  Create( ATransport, 1024 );
end;

procedure TBufferedTransportImpl.Close;
begin
  FTransport.Close;
end;

constructor TBufferedTransportImpl.Create(ATransport: IStreamTransport;
  ABufSize: Integer);
begin
  FTransport := ATransport;
  FBufSize := ABufSize;
  InitBuffers;
end;

procedure TBufferedTransportImpl.Flush;
begin
  if FOutputBuffer <> nil then
  begin
    FOutputBuffer.Flush;
  end;
end;

function TBufferedTransportImpl.GetIsOpen: Boolean;
begin
  Result := FTransport.IsOpen;
end;

function TBufferedTransportImpl.GetUnderlyingTransport: ITransport;
begin
  Result := FTransport;
end;

procedure TBufferedTransportImpl.InitBuffers;
begin
  if FTransport.InputStream <> nil then
  begin
    FInputBuffer := TBufferedStreamImpl.Create( FTransport.InputStream, FBufSize );
  end;
  if FTransport.OutputStream <> nil then
  begin
    FOutputBuffer := TBufferedStreamImpl.Create( FTransport.OutputStream, FBufSize );
  end;
end;

procedure TBufferedTransportImpl.Open;
begin
  FTransport.Open
end;

function TBufferedTransportImpl.Read(var buf: TBytes; off, len: Integer): Integer;
begin
  Result := 0;
  if FInputBuffer <> nil then
  begin
    Result := FInputBuffer.Read( buf, off, len );
  end;
end;

procedure TBufferedTransportImpl.Write(const buf: TBytes; off, len: Integer);
begin
  if FOutputBuffer <> nil then
  begin
    FOutputBuffer.Write( buf, off, len );
  end;
end;

{ TFramedTransportImpl }

{$IF CompilerVersion < 21.0}
procedure TFramedTransportImpl_Initialize;
begin
  SetLength( TFramedTransportImpl.FHeader_Dummy, TFramedTransportImpl.FHeaderSize);
  FillChar( TFramedTransportImpl.FHeader_Dummy[0],
    Length( TFramedTransportImpl.FHeader_Dummy) * SizeOf( Byte ), 0);
end;
{$ELSE}
class constructor TFramedTransportImpl.Create;
begin
  SetLength( FHeader_Dummy, FHeaderSize);
  FillChar( FHeader_Dummy[0], Length( FHeader_Dummy) * SizeOf( Byte ), 0);
end;
{$IFEND}

constructor TFramedTransportImpl.Create;
begin
  InitWriteBuffer;
end;

procedure TFramedTransportImpl.Close;
begin
  FTransport.Close;
end;

constructor TFramedTransportImpl.Create(ATrans: ITransport);
begin
  InitWriteBuffer;
  FTransport := ATrans;
end;

destructor TFramedTransportImpl.Destroy;
begin
  FWriteBuffer.Free;
  FReadBuffer.Free;
  inherited;
end;

procedure TFramedTransportImpl.Flush;
var
  buf : TBytes;
  len : Integer;
  data_len : Integer;

begin
  len := FWriteBuffer.Size;
  SetLength( buf, len);
  if len > 0 then
  begin
    System.Move( FWriteBuffer.Memory^, buf[0], len );
  end;

  data_len := len - FHeaderSize;
  if (data_len < 0) then
  begin
    raise Exception.Create( 'TFramedTransport.Flush: data_len < 0' );
  end;

  InitWriteBuffer;

  buf[0] := Byte($FF and (data_len shr 24));
  buf[1] := Byte($FF and (data_len shr 16));
  buf[2] := Byte($FF and (data_len shr 8));
  buf[3] := Byte($FF and data_len);

  FTransport.Write( buf, 0, len );
  FTransport.Flush;
end;

function TFramedTransportImpl.GetIsOpen: Boolean;
begin
  Result := FTransport.IsOpen;
end;

type
  TAccessMemoryStream = class(TMemoryStream)
  end;

procedure TFramedTransportImpl.InitWriteBuffer;
begin
  FWriteBuffer.Free;
  FWriteBuffer := TMemoryStream.Create;
  TAccessMemoryStream(FWriteBuffer).Capacity := 1024;
  FWriteBuffer.Write( Pointer(@FHeader_Dummy[0])^, FHeaderSize);
end;

procedure TFramedTransportImpl.Open;
begin
  FTransport.Open;
end;

function TFramedTransportImpl.Read(var buf: TBytes; off, len: Integer): Integer;
var
  got : Integer;
begin
  if FReadBuffer <> nil then
  begin
    got := FReadBuffer.Read( Pointer(@buf[0])^, len );
    if got > 0 then
    begin
      Result := got;
      Exit;
    end;
  end;

  ReadFrame;
  Result := FReadBuffer.Read( Pointer(@buf[0])^, len );
end;

procedure TFramedTransportImpl.ReadFrame;
var
  i32rd : TBytes;
  size : Integer;
  buff : TBytes;
begin
  SetLength( i32rd, FHeaderSize );
  FTransport.ReadAll( i32rd, 0, FHeaderSize);
  size :=
    ((i32rd[0] and $FF) shl 24) or
    ((i32rd[1] and $FF) shl 16) or
    ((i32rd[2] and $FF) shl 8) or
     (i32rd[3] and $FF);
  SetLength( buff, size );
  FTransport.ReadAll( buff, 0, size );
  FReadBuffer.Free;
  FReadBuffer := TMemoryStream.Create;
  FReadBuffer.Write( Pointer(@buff[0])^, size );
  FReadBuffer.Position := 0;
end;

procedure TFramedTransportImpl.Write(const buf: TBytes; off, len: Integer);
begin
  FWriteBuffer.Write( Pointer(@buf[0])^, len );
end;

{ TFramedTransport.TFactory }

function TFramedTransportImpl.TFactory.GetTransport(ATrans: ITransport): ITransport;
begin
  Result := TFramedTransportImpl.Create( ATrans );
end;

{ TTcpSocketStreamImpl }

procedure TTcpSocketStreamImpl.Close;
begin
  FTcpClient.Close;
end;

constructor TTcpSocketStreamImpl.Create(ATcpClient: TCustomIpClient);
begin
  FTcpClient := ATcpClient;
end;

procedure TTcpSocketStreamImpl.Flush;
begin

end;

function TTcpSocketStreamImpl.IsOpen: Boolean;
begin
  Result := FTcpClient.Active;
end;

procedure TTcpSocketStreamImpl.Open;
begin
  FTcpClient.Open;
end;

function TTcpSocketStreamImpl.Read(var buffer: TBytes; offset,
  count: Integer): Integer;
begin
  inherited;
  Result := FTcpClient.ReceiveBuf( Pointer(@buffer[offset])^, count);
end;

function TTcpSocketStreamImpl.ToArray: TBytes;
var
  len : Integer;
begin
  len := 0;
  if IsOpen then
  begin
    len := FTcpClient.BytesReceived;
  end;

  SetLength( Result, len );

  if len > 0 then
  begin
    FTcpClient.ReceiveBuf( Pointer(@Result[0])^, len);
  end;
end;

procedure TTcpSocketStreamImpl.Write(const buffer: TBytes; offset, count: Integer);
begin
  inherited;
  FTcpClient.SendBuf( Pointer(@buffer[offset])^, count);
end;

{$IF CompilerVersion < 21.0}
initialization
begin
  TFramedTransportImpl_Initialize;
end;
{$IFEND}


end.
