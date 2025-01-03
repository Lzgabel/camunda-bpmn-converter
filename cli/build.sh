#!/bin/bash

# Exit on error
set -e

# Build the native executable
mvn clean package -DskipTest -Pnative
