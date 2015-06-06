#!/bin/bash

# Fix "stdin: is not a tty"
sed -i "s/^mesg n$/tty -s \&\& mesg n/" ~/.profile

# https://blog.docker.com/2013/07/how-to-use-your-own-registry/

