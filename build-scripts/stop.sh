#!/usr/bin/sh
APP_PATH="$(pwd)"
sh -c "docker compose -f ${APP_PATH}/project-descriptor.yml down"
if [ $? -ne 0 ]
then
  exit 1
fi
exit 0