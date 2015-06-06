#!/bin/bash

# Fix "stdin: is not a tty"
sed -i "s/^mesg n$/tty -s \&\& mesg n/" ~/.profile
