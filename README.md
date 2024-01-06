# Song Finder powered by VocaDB

A song finder GUI app powered by [VocaDB Dump](https://github.com/CXwudi/hoshizora.sql).
Scan each title and try to find matching VocaDB records.

## Requirements

- Java 21

## Usage

### Build

```shell
./gradlew createDistributable
```

The built executable package can be found in `songfinder-app\build\compose\binaries\main\app`

### Run

First you need a running VocaDB Dump database, see [VocaDB Dump repo](https://github.com/CXwudi/hoshizora.sql)

Then create a yaml file to access the database:

```yaml
spring:
  datasource:
    url:
    username:
    password: 
```

For example,
a yml file with the default configuration from the
sample [.env file](https://github.com/CXwudi/hoshizora.sql/blob/main/docker/.env)
will be:

```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/vocadb_site
    username: vocadb
    password: my_password
```

Place this yaml file beside the executable file in `songfinder-app\build\compose\binaries\main\app`,
open a terminal in that folder and run:

```shell
./songfinder-app --spring.config.import=file:./<your yaml file>
```

Then follow the GUI to use this app.
