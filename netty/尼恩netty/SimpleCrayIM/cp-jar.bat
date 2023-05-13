@echo off

set src_file=D:\dev\SimpleCrayIM\chat-server\target\chat-server-1.0-SNAPSHOT.jar

set dest_path=D:\virtual\centos-8.2\

XCOPY  %src_file%  %dest_path%   /Y


set src_file=D:\dev\SimpleCrayIM\chat-server\src\main\assembly\bin\IoUring_server.sh

set dest_path=D:\virtual\centos-8.2\

XCOPY  %src_file%  %dest_path%   /Y

