import sys
import re

# Wait, `UIViewLayoutRegion` is an iOS 17 framework thing from `SwiftUI` or `UIKit` probably.
# If I add `-framework SwiftUI` to `linkerOpts`?
with open('composeApp/build.gradle.kts', 'r') as f:
    content = f.read()

if "linkerOpts" not in content:
    content = content.replace('isStatic = true', 'isStatic = true\n            linkerOpts("-lsqlite3", "-framework", "SwiftUI", "-framework", "UIKit")')

with open('composeApp/build.gradle.kts', 'w') as f:
    f.write(content)
