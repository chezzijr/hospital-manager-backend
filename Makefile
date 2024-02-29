.PHONY: deps run

# update gradle dependencies
deps:
	./gradlew build --refresh-dependencies
	./gradlew clean
	./gradlew eclipse

run:
	./gradlew run
