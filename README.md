# crawl-4clojure

This is a small utility project to get all my 4clojure solutions nicely formatted
to a file, including problem description.

Get the "ring-session-id" cookie contents from network tools after login in to
4clojure, and set under resources/config.edn. See resources/config.edn.sample
for an example file with needed edn key.

## Usage

You can either do `boot run` to get a file with exercises 1-150 inspected (will only saveto file the ones with solutions)

Or, you can start a boot repl and play around with it incrementally.

Run the project directly:

    $ boot run

Run the project's tests (they'll fail until you edit them):

    $ boot test

Build an uberjar from the project:

    $ boot build

Run the uberjar:

    $ java -jar target/crawl-4clojure-0.1.0-SNAPSHOT-standalone.jar [args]


## License

Copyright © 2017 Joel Quiles

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
