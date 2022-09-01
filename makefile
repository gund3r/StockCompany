run: test
	mvn mn:run

test:
    mvn test

build: clean
	mvn verify

clean:
	mvn clean

update:
	mvn versions:update-properties versions:display-plugin-updates

.DEFAULT_GOAL := run
build-run: run