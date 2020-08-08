package aws.lambda.runtime.mock.service

import aws.lambda.runtime.mock.service.AwsLambdaEnvironment.DEADLINE_HEADER_NAME
import aws.lambda.runtime.mock.service.AwsLambdaEnvironment.INVOKED_FUNCTION_ARN
import aws.lambda.runtime.mock.service.AwsLambdaEnvironment.REQUEST_HEADER_NAME
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger


@RestController
class AwsLambdaRuntimeApiRestController {

    private val log: Logger = Logger.getLogger(AwsLambdaRuntimeApiRestController::class.java.simpleName)

    @GetMapping("/invocation/next")
    fun initEndpoint() = ResponseEntity.ok()
        .header(REQUEST_HEADER_NAME, "mock")
        .header(INVOKED_FUNCTION_ARN, "mock")
        .header(DEADLINE_HEADER_NAME, "mock")
        .body(AwsLambdaRuntimeInitResponse())

    @PostMapping("/init/error")
    @ResponseStatus(code = HttpStatus.OK)
    fun initErrorEndpoint(@RequestBody body: String) {
        log.info("Init error. Body:\n $body")
    }

    @PostMapping("/invocation/{requestId}/error")
    @ResponseStatus(code = HttpStatus.OK)
    fun responseErrorEndpoint(@PathVariable requestId: String) {
        log.info("Invocation error. RequestId: $requestId")
    }

    @PostMapping("/invocation/{requestId}/response")
    fun responseInvocationEndpoint(@PathVariable requestId: String, @RequestBody body: String) {
        log.info("Invocation response. RequestId: $requestId. Body: \n $body")
    }
}
