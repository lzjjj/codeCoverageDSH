@echo off
@call mvn clean package
del /f D:\share\Dashboard\server-0.0.1.jar
copy .\target\server-0.0.1.jar D:\share\Dashboard\