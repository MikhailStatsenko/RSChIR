plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.app.marketplace"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_20
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.code.gson:gson:2.8.9")
	implementation("org.springframework.data:spring-data-jpa:3.1.4")
	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.flywaydb:flyway-core")
	implementation("org.projectlombok:lombok:1.18.30")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly("org.projectlombok:lombok:1.18.30")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
	archiveFileName.set("marketplace.jar")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

