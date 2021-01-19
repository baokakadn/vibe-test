#!/bin/bash -e
BUILD_NO="$BUILD_NUMBER"

function export_binary() {

    cd $WORKSPACE/iosApp/fastlane
    set -euo pipefail
    fastlane beta_release build_number:"$BUILD_NO"
}

# 🚀 Main

export_binary

# end
