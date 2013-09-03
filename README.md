[![Build Status](https://travis-ci.org/umatrangolo/gilt-api.png)](https://travis-ci.org/umatrangolo/gilt-api)
Gilt-API
========

Java/Scala friendly implementation of the GILT API.

This is a Scala 2.10.x implementation of the publicly available API
of Gilt (www.gilt.com). This implementation can be used both from a
Java client or Scala one.

Non blocking IO is built on top of Ning Async Client and each API call
returns a composable deferred result allowing to write very efficient
clients (e.g. they will fit nicely in a Play 2.1+ environment).

Download
--------

The current version is 0.0.3 and it is available from Maven Central
repo using the following coordinates:

#### Maven:

    <dependency>
        <groupId>com.umatrangolo</groupId>
        <artifactId>gilt-api_2.10</artifactId>
        <version>0.0.3</version>
    </dependency>

#### Ivy:

    <dependency org="com.umatrangolo" name="gilt-api_2.10" rev="0.0.3" />

#### SBT:

    libraryDependencies += "com.umatrangolo" % "gilt-api_2.10" % "0.0.3"

For more detailed information please refer to the Maven Central [repository](http://search.maven.org/#artifactdetails%7Ccom.umatrangolo%7Cgilt-api_2.10%7C0.0.3%7Cjar).

Configuration
-------------

The configuration could be provided using the giltapi.properties file
that should be present in the classpath. The only mandatory
information is the Gilt API key.

#### The API Key

Each Gilt client must provide a personal API key. This key can be
obtained registering on the [Gilt API dev center](http://api.gilt.com).
Once you got the API key it must be configured in the library by
either setting the 'giltapi.client.apikey' system property or by
appending this line to the giltapi.properties file.

    giltapi.client.apikey=b744156f9efa7ef32abf78d030d99ba3

Note that the above key is not going to work :)

#### Ning configuration

Ning could be also fine tuned using the following properties:

    giltapi.client.ning.is_compression_enabled=true
    giltapi.client.ning.is_pooling_enabled=true
    giltapi.client.ning.request_timeout_in_ms=30000

Refer to the Ning Async client javadocs for more info.

Usage
-----

The main interfaces are:

* *Sales*
  Gives access to all active and upcoming sales for a given store.

* *Products*
  Allows to retrieve details about a particular product in a sale.

#### Creating the client

A Factory has been provided to create a client that implements one of
the provided interfaces.

The following code will create a factory for the `Sales` type using a
given API key:

    import com.umatrangolo.giltapi.client.GiltClientFactory

    val salesClient: Sales = GiltClientFactory.newSalesClientInstance("b744156f9efa7ef32abf78d030d99ba3")

A client can also be created without specifying the API key if it is
passed as a sys property or if it is provided in the
`giltapi.properties` file in the classpath. The following code builds
a client for the `Products` type using no API key.

    import com.umatrangolo.giltapi.client.GiltClientFactory

    val productClient: Products = GiltClientFactory.newProductsClientInstance()

Please note that the underlying async provider (Ning) will be shared
by all the instances.

#### For Java users

The Java client is heavily based on the Google Guava library. Each API
call will return back a `ListenableFuture` object that could be freely
composed in a non blocking fashion. The content of these futures are
often `Optional` obj to avoid to pass back `null` to signal no
elements.

The following code excerpt looks for all active sales in the *Women*
store:

    import com.umatrangolo.giltapi.Sales;
    import com.umatrangolo.giltapi.model.Sale;
    import com.umatrangolo.giltapi.model.Store;
    import com.google.common.util.concurrent.ListenableFuture;

    Sales salesClient = newSalesClientInstance()
    ListenableFuture<List<Sale>> salesForWomen = salesClient.getActiveSales(Store.Women).get();

#### For Scala users

Scala users have access to the same API but they need to import some
implicit conversions to get rid of all the Java friendly
annoyances. Each Scala client will return back proper freely
composable `scala.concurrent.Futures` objects and `scala.Option` instead
of Guava `Optional`.

The following excerpt fetches all the active sales in the *Women* store
to retrieve the first one and print its details:

    // needed to get rid of Optional and ListenableFuture
    import com.umatrangolo.giltapi.utils.FutureConversions._
    import com.umatrangolo.giltapi.utils.GuavaConversions._

    ...

    val salesClient: Sales = GiltClientFactory.newSalesClientInstance()

    salesClient.getActiveSales(Store.Women).map { salesForWomen =>
      salesForWomen.headOption.foreach { sale =>
        salesClient.getSale(sale.key, Store.Women).map { saleDetails => println(">> [%s] is \n%s".format(sale.key, saleDetails)) }
      }
    }

#### Provided examples

Both Java and Scala examples are provided as a starting point on how
to use this library. Please refer to them for more example code.

Coming Soon
-----------

+ Support Search API endpoints.

    ugo.matrangolo@gilt.com
    02-Sep-2013
