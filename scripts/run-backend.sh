#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PORTABLE_JDK="$HOME/.local/jdk/jdk-21.0.11+10"

if [[ -z "${JAVA_HOME:-}" && -x "$PORTABLE_JDK/bin/javac" ]]; then
  export JAVA_HOME="$PORTABLE_JDK"
fi

cd "$ROOT_DIR/backend"
exec ./mvnw spring-boot:run
