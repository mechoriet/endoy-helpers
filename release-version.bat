@echo off

SET "tag=%~1"
git "checkout" "master"
git "add" "."
git "commit" "-m" "Release version v%tag%"
git "push"
git "tag" "v%tag%"
git "push" "origin" "v%tag%"