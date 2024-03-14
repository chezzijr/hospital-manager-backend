.PHONY: deps run

# update gradle dependencies
deps:
	./gradlew dependencies --refresh-dependencies

run:
	./gradlew run

build:
	./gradlew clean build
