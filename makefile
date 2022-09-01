test:
    mvn test

build: clean
    mvn verify

clean:
    mvn clean

update:
    mvn versions:update-properties versions:display-plugin-updates

run:
    mvn mn:run

.DEFAULT_GOAL := test-run
test-run: test run