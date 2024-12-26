package neoflex.statement.annotation;
import feign.FeignException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Retryable(retryFor = FeignException.FeignServerException.class, backoff = @Backoff(delay = 2500))
public @interface FeignRetryable {}
