@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup script, version 3.2.0
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------
@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'
@echo off
@REM set title of command window
title %0
@REM determine maven-home
set MAVEN_HOME=%~dp0\.mvn
@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")
@REM set mvn command
set MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd
@REM check if Maven is installed
if not exist "%MVN_CMD%" (
    @REM download Maven
    if not "%MVNW_REPOURL%" == "" (
        set REPO_URL=%MVNW_REPOURL%
    ) else (
        set REPO_URL=https://repo.maven.apache.org/maven2
    )
    set WRAPPER_JAR=%MAVEN_HOME%\wrapper\maven-wrapper.jar
    if not exist "%WRAPPER_JAR%" (
        echo Missing %WRAPPER_JAR% >&2
        goto :eof
    )
    set WRAPPER_URL=%REPO_URL%/org/apache/maven/wrapper/3.2.0/maven-wrapper-3.2.0.jar
    echo Downloading Maven Wrapper... >&2
    call :download "%WRAPPER_URL%" "%WRAPPER_JAR%"
    if %ERRORLEVEL% neq 0 (
        echo Failed to download Maven Wrapper >&2
        goto :eof
    )
    set DISTRIBUTION_URL=%REPO_URL%/org/apache/maven/apache-maven/3.9.4/apache-maven-3.9.4-bin.zip
    echo Downloading Maven... >&2
    call :download "%DISTRIBUTION_URL%" "%MAVEN_HOME%\maven.zip"
    if %ERRORLEVEL% neq 0 (
        echo Failed to download Maven >&2
        goto :eof
    )
    echo Extracting Maven... >&2
    call :unzip "%MAVEN_HOME%\maven.zip" "%MAVEN_HOME%"
    if %ERRORLEVEL% neq 0 (
        echo Failed to extract Maven >&2
        goto :eof
    )
    del "%MAVEN_HOME%\maven.zip"
)
@REM check if Java is installed
if not "%JAVA_HOME%" == "" goto :java_home_set
@REM try to find Java
for %%i in (java.exe) do set "JAVA_EXE=%%~$PATH:i"
if not "%JAVA_EXE%" == "" goto :java_found
echo No Java installation found >&2
goto :eof
:java_home_set
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo JAVA_HOME is not set correctly >&2
    goto :eof
)
set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
:java_found
@REM execute mvn
"%JAVA_EXE%" ^
  %JVM_CONFIG_MAVEN_PROPS% ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath "%MAVEN_HOME%\wrapper\maven-wrapper.jar" ^
  "-Dmaven.home=%MAVEN_HOME%" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% org.apache.maven.wrapper.MavenWrapperMain %*
goto :eof
@REM download function
:download
powershell -Command "& { $webclient = New-Object System.Net.WebClient; $webclient.DownloadFile('%1', '%2') }"
goto :eof
@REM unzip function
:unzip
powershell -Command "& { Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::ExtractToDirectory('%1', '%2') }"
goto :eof