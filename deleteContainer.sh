#!/bin/bash

if [ "$1" == "" ]; then
  echo "Parameter cannot be empty."
else
  sudo docker stop "$1" && sudo docker rm "$1"
fi