@echo off

SET tag=%~1
git "checkout" "master"
./mvnw.cmd "-B" "versions:set" "-DnewVersion=%tag%" "-DgenerateBackupPoms=false"
git "add" "."
git "commit" "-m" "Release version %tag%"
git "push"
git "tag" "v%tag%"
git "push" "origin" "v%tag%"