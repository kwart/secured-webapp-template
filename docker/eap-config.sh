#!/bin/bash

JBOSS_HOME=/opt/eap
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}
DIRNAME=`dirname "$0"`
CLI_FILE=${3:-"$DIRNAME/commands.cli"}

function wait_for_server() {
  i=0;
  until test $i -lt 30 && `$JBOSS_CLI -c "ls /deployment" &> /dev/null`; do
    let i+=1
    sleep 1
  done
}

echo "=> Starting EAP server"
$JBOSS_HOME/bin/$JBOSS_MODE.sh -c $JBOSS_CONFIG >/dev/null &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> Executing the commands"
$JBOSS_CLI -c --file=${CLI_FILE}

echo "=> Shutting down EAP"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi
