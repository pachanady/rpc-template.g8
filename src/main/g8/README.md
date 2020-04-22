# Catalog
**Catalog** is a scala service that allows to query product catalog related data.

## Building

#### Requirements
- Setup [bintray](#bintray)
- JDK 1.8+
- Scala 2.12+ (`brew install scala`)
- SBT (`brew install sbt`)

#### Bintray

Scala core jar files are published to a private Maven repository hosted with [Bintray](http://bintray.com). You'll have to create an account to use this service.

Once you have an account and have been added to the BigCommerce organization by an existing admin, follow these steps in your [onboarding](https://intranet.bigcommerce.com/pages/viewpage.action?pageId=74194874) docs to get `sbt` to use your credentials.

* Create `~/.bintray/.credentials`:

```
realm = Bintray
host = dl.bintray.com
user = [[your bintray user]]
password = [[your bintray api key]]
```

* Create a global `sbt` script that will append these credentials to all of your projects in `~/.sbt/0.13/credentials.sbt`:

```scala
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")
```

* Verify that your credentials are properly loaded by running the `bintrayEnsureCredentials` command (your `sbt` project must have the bintray plugin installed).

#### Installation
Make sure JDK 1.8 installed.
```
$ java -version
```

#### Running application
```
> cp .env-example .env
> sbt
> ~reStart # this command will run the service and watch your changes after recompiling
```

#### Running tests

```
> sbt
> catalog-impl/test # unit tests
> catalog-impl/fun:test # functional
```

### Setting up database for testing
Catalog runs against a store database. You will need to create and access this database.
In order to run functional suite `fun:test`, you'll need to get access to a database.

 1. 
    ```
    catalog> FUN_TESTS_STORE_ID=999999
    
    You may also need to update:
    DATABASE_URL=mysql://mysql.store.bcdev:3306
    DATABASE_HOST=mysql.store.bcdev
    DATABASE_POOL_PROVIDER=config-service
    
    ```

 2. Re-start sbt

At this point, you should be able to run `fun:test` successfully.
