#!/bin/sh

nxjc src/Main.java -cp src -d out

cd out

nxjlink -o Main.nxj Main
nxjupload -r Main.nxj
