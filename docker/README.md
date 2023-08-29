# Docker Setup for this project

The `compose.base.yml` file and `compose.test.yml` file together can provide the proper testing environment for CI, 
and the database can also be used for local development.

However, the test data set is too large to be uploaded to the repository, so you need to download it manually.

## Where to download

download link: https://mega.nz/file/W4swmATB#n6PzU7gAI2aLz0W625XucCAWV7Ym9C_Dy5KsozRZFis

## How to use

Extract the `import-script` folder and `import-test-data` into the `./docker` folder

To run the database only, run `docker-compose -f compose.base.yml -f compose.test.yml up mariadb -d`

To run tests, run `docker-compose -f compose.base.yml -f compose.test.yml up`

When rerunning the test, make sure to delete the `song-finder-vocadb-test-runner` image so that newest code can be picked up.