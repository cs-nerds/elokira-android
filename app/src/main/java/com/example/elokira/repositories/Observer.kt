import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

sealed class ResultObserver {
    object IdNoMissing : ResultObserver()


    object NameMissing : ResultObserver()

    object PhoneNoMissing: ResultObserver()

    object CodeMissing: ResultObserver()

    class NetworkError(val userMessage: String) : ResultObserver()

    object NetworkFailure : ResultObserver()

    object Success : ResultObserver()
}

val resultEmitter = EventEmitter<ResultObserver>()
val loginResult: EventSource<ResultObserver> = resultEmitter