plugins {
	kotlin("jvm") version "1.7.10"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	testImplementation(kotlin("test-junit5"))
}

dependencies{
	implementation("org.jgrapht:jgrapht-core:1.5.1")
	implementation("com.google.guava:guava:31.1-jre")
}

// we need to specify following sourceSets because we store main and test not in default
//  location (which is module_path/src/main and module_path/src/test)
sourceSets.main {
	java.srcDirs("src/main")
}

sourceSets.test {
	java.srcDirs("src/test")
}

tasks.test {
	useJUnitPlatform()
	maxParallelForks = 8
}
