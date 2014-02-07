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

 unit Thrift.Server;

interface

uses
  SysUtils,
  Thrift,
  Thrift.Protocol,
  Thrift.Transport;

type
  IServer = interface
    ['{CF9F56C6-BB39-4C7D-877B-43B416572CE6}']
    procedure Serve;
    procedure Stop;
  end;

  TServerImpl = class abstract( TInterfacedObject, IServer )
  public
    type
      TLogDelegate = reference to procedure( str: string);
  protected
    FProcessor : IProcessor;
    FServerTransport : IServerTransport;
    FInputTransportFactory : ITransportFactory;
    FOutputTransportFactory : ITransportFactory;
    FInputProtocolFactory : IProtocolFactory;
    FOutputProtocolFactory : IProtocolFactory;
    FLogDelegate : TLogDelegate;

    class procedure DefaultLogDelegate( str: string);

    procedure Serve; virtual; abstract;
    procedure Stop; virtual; abstract;
  public
    constructor Create(
      AProcessor :IProcessor;
      AServerTransport: IServerTransport;
      AInputTransportFactory : ITransportFactory;
      AOutputTransportFactory : ITransportFactory;
      AInputProtocolFactory : IProtocolFactory;
      AOutputProtocolFactory : IProtocolFactory;
      ALogDelegate : TLogDelegate
      ); overload;

    constructor Create( AProcessor :IProcessor;
      AServerTransport: IServerTransport); overload;

    constructor Create(
      AProcessor :IProcessor;
      AServerTransport: IServerTransport;
      ALogDelegate: TLogDelegate
      ); overload;

    constructor Create(
      AProcessor :IProcessor;
      AServerTransport: IServerTransport;
      ATransportFactory : ITransportFactory
      ); overload;

    constructor Create(
      AProcessor :IProcessor;
      AServerTransport: IServerTransport;
      ATransportFactory : ITransportFactory;
      AProtocolFactory : IProtocolFactory
      ); overload;
  end;

  TSimpleServer = class( TServerImpl)
  private
    FStop : Boolean;
  public
    constructor Create( AProcessor: IProcessor; AServerTransport: IServerTransport); overload;
    constructor Create( AProcessor: IProcessor; AServerTransport: IServerTransport;
      ALogDel: TServerImpl.TLogDelegate); overload;
    constructor Create( AProcessor: IProcessor; AServerTransport: IServerTransport;
      ATransportFactory: ITransportFactory); overload;
    constructor Create( AProcessor: IProcessor; AServerTransport: IServerTransport;
      ATransportFactory: ITransportFactory; AProtocolFactory: IProtocolFactory); overload;

    procedure Serve; override;
    procedure Stop; override;
  end;


implementation

{ TServerImpl }

constructor TServerImpl.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ALogDelegate: TLogDelegate);
var
  InputFactory, OutputFactory : IProtocolFactory;
  InputTransFactory, OutputTransFactory : ITransportFactory;

begin
  InputFactory := TBinaryProtocolImpl.TFactory.Create;
  OutputFactory := TBinaryProtocolImpl.TFactory.Create;
  InputTransFactory := TTransportFactoryImpl.Create;
  OutputTransFactory := TTransportFactoryImpl.Create;

  Create(
    AProcessor,
    AServerTransport,
    InputTransFactory,
    OutputTransFactory,
    InputFactory,
    OutputFactory,
    ALogDelegate
  );
end;

constructor TServerImpl.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport);
var
  InputFactory, OutputFactory : IProtocolFactory;
  InputTransFactory, OutputTransFactory : ITransportFactory;

begin
  InputFactory := TBinaryProtocolImpl.TFactory.Create;
  OutputFactory := TBinaryProtocolImpl.TFactory.Create;
  InputTransFactory := TTransportFactoryImpl.Create;
  OutputTransFactory := TTransportFactoryImpl.Create;

  Create(
    AProcessor,
    AServerTransport,
    InputTransFactory,
    OutputTransFactory,
    InputFactory,
    OutputFactory,
    DefaultLogDelegate
  );
end;

constructor TServerImpl.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ATransportFactory: ITransportFactory);
var
  InputProtocolFactory : IProtocolFactory;
  OutputProtocolFactory : IProtocolFactory;
begin
  InputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;
  OutputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;

  Create( AProcessor, AServerTransport, ATransportFactory, ATransportFactory,
    InputProtocolFactory, OutputProtocolFactory, DefaultLogDelegate);
end;

constructor TServerImpl.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; AInputTransportFactory,
  AOutputTransportFactory: ITransportFactory; AInputProtocolFactory,
  AOutputProtocolFactory: IProtocolFactory;
  ALogDelegate : TLogDelegate);
begin
  FProcessor := AProcessor;
  FServerTransport := AServerTransport;
  FInputTransportFactory := AInputTransportFactory;
  FOutputTransportFactory := AOutputTransportFactory;
  FInputProtocolFactory := AInputProtocolFactory;
  FOutputProtocolFactory := AOutputProtocolFactory;
  FLogDelegate := ALogDelegate;
end;

class procedure TServerImpl.DefaultLogDelegate( str: string);
begin
  Writeln( str );
end;

constructor TServerImpl.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ATransportFactory: ITransportFactory;
  AProtocolFactory: IProtocolFactory);
begin
  Create( AProcessor, AServerTransport,
          ATransportFactory, ATransportFactory,
          AProtocolFactory, AProtocolFactory,
          DefaultLogDelegate);
end;

{ TSimpleServer }

constructor TSimpleServer.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport);
var
  InputProtocolFactory : IProtocolFactory;
  OutputProtocolFactory : IProtocolFactory;
  InputTransportFactory : ITransportFactory;
  OutputTransportFactory : ITransportFactory;
begin
  InputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;
  OutputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;
  InputTransportFactory := TTransportFactoryImpl.Create;
  OutputTransportFactory := TTransportFactoryImpl.Create;

  inherited Create( AProcessor, AServerTransport, InputTransportFactory,
    OutputTransportFactory, InputProtocolFactory, OutputProtocolFactory, DefaultLogDelegate);
end;

constructor TSimpleServer.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ALogDel: TServerImpl.TLogDelegate);
var
  InputProtocolFactory : IProtocolFactory;
  OutputProtocolFactory : IProtocolFactory;
  InputTransportFactory : ITransportFactory;
  OutputTransportFactory : ITransportFactory;
begin
  InputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;
  OutputProtocolFactory := TBinaryProtocolImpl.TFactory.Create;
  InputTransportFactory := TTransportFactoryImpl.Create;
  OutputTransportFactory := TTransportFactoryImpl.Create;

  inherited Create( AProcessor, AServerTransport, InputTransportFactory,
    OutputTransportFactory, InputProtocolFactory, OutputProtocolFactory, ALogDel);
end;

constructor TSimpleServer.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ATransportFactory: ITransportFactory);
begin
  inherited Create( AProcessor, AServerTransport, ATransportFactory,
    ATransportFactory, TBinaryProtocolImpl.TFactory.Create, TBinaryProtocolImpl.TFactory.Create, DefaultLogDelegate);
end;

constructor TSimpleServer.Create(AProcessor: IProcessor;
  AServerTransport: IServerTransport; ATransportFactory: ITransportFactory;
  AProtocolFactory: IProtocolFactory);
begin
  inherited Create( AProcessor, AServerTransport, ATransportFactory,
    ATransportFactory, AProtocolFactory, AProtocolFactory, DefaultLogDelegate);
end;

procedure TSimpleServer.Serve;
var
  client : ITransport;
  InputTransport : ITransport;
  OutputTransport : ITransport;
  InputProtocol : IProtocol;
  OutputProtocol : IProtocol;
begin
  try
    FServerTransport.Listen;
  except
    on E: Exception do
    begin
      FLogDelegate( E.ToString);
    end;
  end;

  client := nil;
  InputTransport := nil;
  OutputTransport := nil;
  InputProtocol := nil;
  OutputProtocol := nil;

  while (not FStop) do
  begin
    try
      client := FServerTransport.Accept;
      FLogDelegate( 'Client Connected!');
      InputTransport := FInputTransportFactory.GetTransport( client );
      OutputTransport := FOutputTransportFactory.GetTransport( client );
      InputProtocol := FInputProtocolFactory.GetProtocol( InputTransport );
      OutputProtocol := FOutputProtocolFactory.GetProtocol( OutputTransport );
      while ( FProcessor.Process( InputProtocol, OutputProtocol )) do
      begin
        if FStop then Break;
      end;
    except
      on E: TTransportException do
      begin
        if FStop then
        begin
          FLogDelegate('TSimpleServer was shutting down, caught ' + E.ClassName);
        end;
      end;
      on E: Exception do
      begin
        FLogDelegate( E.ToString );
      end;
    end;
    if InputTransport <> nil then
    begin
      InputTransport.Close;
    end;
    if OutputTransport <> nil then
    begin
      OutputTransport.Close;
    end;
  end;

  if FStop then
  begin
    try
      FServerTransport.Close;
    except
      on E: TTransportException do
      begin
        FLogDelegate('TServerTranport failed on close: ' + E.Message);
      end;
    end;
    FStop := False;
  end;
end;

procedure TSimpleServer.Stop;
begin
  FStop := True;
  FServerTransport.Close;
end;

end.
