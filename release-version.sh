tag=$1

git checkout master

./mvnw -B versions:set -DnewVersion=$tag -DgenerateBackupPoms=false

git add .
git commit -m "Release version $tag"
git push

git tag v$tag
git push origin v$tag