tag=$1

./mvnw -B versions:set -DnewVersion=$tag -DgenerateBackupPoms=false

git add .
git commit -m "Release version $tag"
git push origin master

git tag v$tag
git push origin v$tag