package aws.lambda.runtime.mock.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AwsLambdaRuntimeMockService

fun main(args: Array<String>) {
    runApplication<AwsLambdaRuntimeMockService>(*args)
}
