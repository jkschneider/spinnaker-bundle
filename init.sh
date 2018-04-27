#!/usr/bin/env bash

declare -a projects=("rosco" "fiat" "front50" "clouddriver" "gate" "echo" "igor" "deck")

for project in "${projects[@]}"
do
   if [ ! -d "$(pwd)/../$project" ]; then
     echo "git clone 'git@github.com:spinnaker/$project.git' '$(pwd)/../$project'"
     git clone "git@github.com:spinnaker/$project.git" "$(pwd)/../$project"
   fi
done
