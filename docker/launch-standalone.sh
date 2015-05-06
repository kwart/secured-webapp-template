#!/bin/bash

JBOSS_HOME=/opt/eap
JBOSS_CONFIG=${1:-"standalone-ha.xml"}

IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')

$JBOSS_HOME/bin/standalone.sh -Djboss.bind.address=$IPADDR -Djboss.bind.address.management=$IPADDR -Djboss.node.name=server-$IPADDR "$@"
