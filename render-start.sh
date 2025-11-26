#!/usr/bin/env bash
export JAVA_TOOL_OPTIONS="-Dcom.sun.management.jmxremote=false"
java -Dspring.profiles.active=prod -Dserver.port=$PORT -jar target/*.jar
