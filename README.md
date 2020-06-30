# Ao1 task

[![Build Status](https://cloud.drone.io/api/badges/rmuhamedgaliev/ao1-task/status.svg)](https://cloud.drone.io/rmuhamedgaliev/ao1-task)

## Task

### Initial Data:

Several CSV files. The number of files can be quite large (up to 100,000).
The number of rows within each file can reach up to several million.
Each file contains 5 columns: product ID (integer), Name (string), Condition (string), State (string), Price (float).
The same product IDs may occur more than once in different CSV files and in the same CSV file.

### Task:

Write a console utility using Java programming language that allows getting a selection of the cheapest 1000 products from the input CSV files, but no more than 20 products with the same ID. Use parallel processing to increase performance.

### Utility Result:

Output CSV file that meets the following criteria:
- no more than 1000 products sorted by Price from all files;
- no more than 20 products with the same ID.

## Requirements

- JDK 14
- Gradle 6.5

Project have git hooks from gradle script.

- pre-commit - start linters
- pre-push - start linters and tests

## Development

Project has [Gradle](https://gradle.org/) build system. For start development, just import project into your IDE. 
For complete build application. Use `./gradlew assembleDist` for build complete application.

Follow **gradle** tasks help to work with project:

- ./gradlew check - run linters
- ./gradlew test - run test. After run check folder **./build/reports/buildDashboard**
- ./gradlew assembleDist - for create executable application. It placed at **./build/distributions**

## Run application

For run application run `./gradlew assembleDist` and extract archive to your folder. In config directory change `application.properties` file for your purpose. Example configuration:

```properties
# Count of duplicates products in list with same ID
app.products.count-of-duplicates=20

# Count of list products
app.products.count-of-products=1000

# Count of number per file
app.csv.number-per-line=10000

# Count of generated files
app.csv.generate.count-of-files=100

# Path for generated files
app.csv.file-path=/tmp/test

# Count of threads for process files
app.csv.file.parallel-threads=10

# Separator symbol for parse CSV
app.csv.separator=,

# System timeout in minutes while script stuck
app.csv.timeout-in-minutes=15

# Type of result printer, possible value console or file
app.printer.type=file

# Name of file for file type printer for store result
app.printer.file.out=out.csv
```  

After you should placed in directory in one layer with `bin`, `config` and `lib`. Then in terminal run command `./bin/ao1-task`.

App have follow arguments:

- **./bin/ao1-task generate** - for create csv files
- **./bin/ao1-task parse** - for parse and sort files with products


