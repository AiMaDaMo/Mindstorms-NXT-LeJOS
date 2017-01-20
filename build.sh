#!/bin/sh

nxjc src/Main.java -cp src -d .
nxjlink -o Main.nxj Main
nxjupload -r Main.nxj
