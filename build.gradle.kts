import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.21"
	java
    application
}

group = "com.ks"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

application.mainClass.set("com.ks.kafka.KafkaTests")
tasks.withType<Jar> {
	manifest {
		attributes["Main-Class"] = "com.ks.kafka.KafkaTests"
	}
	archiveClassifier.set("bundle")
	from(sourceSets.main.get().output)
	dependsOn(configurations.runtimeClasspath)
	from({
		configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
	})
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.apache.kafka:kafka-clients:2.8.0")
	implementation("org.apache.kafka:kafka-streams:2.8.0")

	testImplementation(platform("org.junit:junit-bom:5.7.2"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.apache.kafka:kafka-streams-test-utils:2.8.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}
