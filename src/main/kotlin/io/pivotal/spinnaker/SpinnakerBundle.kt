package io.pivotal.spinnaker

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpinnakerBundleApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpinnakerBundleApplication::class.java, *args)
}

