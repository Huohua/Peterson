target=`android list target | grep android-17 | cut -d " " -f 2`
android update project --path . --target $target --subprojects
ant release
