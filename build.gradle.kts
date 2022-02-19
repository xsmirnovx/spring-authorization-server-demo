import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

allprojects {
	group = "io.github.xsmirnovx"
	version = "0.0.1-SNAPSHOT"
	//java.sourceCompatibility = JavaVersion.VERSION_11

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

	repositories {
		mavenCentral()
	}
}

subprojects {

	repositories {
		mavenCentral()
	}

	apply {
		plugin("io.spring.dependency-management")
	}
}
