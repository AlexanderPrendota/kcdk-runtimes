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

    @GetMapping("/2018-06-01/runtime/invocation/next")
    fun initEndpoint() = ResponseEntity.ok()
        .header(REQUEST_HEADER_NAME, "3551dcfa-ea7d-4a91-872c-b8e2996030cf")
        .header(INVOKED_FUNCTION_ARN, "arn:aws:lambda:eu-west-1:290147673817:function:mock-mock-merged-0")
        .header(DEADLINE_HEADER_NAME, "1596748655122")
        .body(MOCK)

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
