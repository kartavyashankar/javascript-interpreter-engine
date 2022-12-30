#!/usr/bin/sh
APP_PATH="$(pwd)"
sh -c "docker compose -f ${APP_PATH}/project-descriptor.yml down"
if [ $? -ne 0 ]
then
  exit 1
fi

sh -c "rm -r ${APP_PATH}/build"
if [ $? -ne 0 ]
then
  exit 2
fi

sh -c "docker image rm javascript-interpreter-engine-application:latest"
if [ $? -ne 0 ]
then
  exit 3
fi

exit 0