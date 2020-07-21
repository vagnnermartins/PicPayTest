package com.picpay.desafio.android.common.cache

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Single
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.ConcurrentHashMap

/**
 * Cache that allows us to reuse the same requests, preventing us from doing multiple network requests
 * to get the same data.
 * After a successful request the returned data will be saved into the [cache].
 * This cache also allows us to update all the current subscribers to a key meaning that all subscribers
 * will have the most updated data once the data is updated using the the [put] method.
 *
 * @param cache used to save the results after a successful request
 */
class RxCache<T>(private val cache: Cache<T>) {

    // Map used to identify there is a request in flight
    private val requestsMap = ConcurrentHashMap<String, Flowable<Result<T>>>()

    // Map containing the BehaviorProcessors associated with each key
    private val behaviorsMap = ConcurrentHashMap<String, BehaviorProcessor<Result<T>>>()
    private val errorsMap = ConcurrentHashMap<String, PublishProcessor<Result<T>>>()

    /**
     * Gets the [Flowable] associated with the specified [key].
     * If there is a cached value the [Flowable] will emit immediately that value.
     * If there is already a request for the same [key] running, the same request wil be reused.
     * If there is no cached value and no request running for the [key], we will use the [source]
     * to retrieve the value and saved to the cache notifying all previous subscribers that are
     * interested in the same [key]
     *
     * @param key used for identifying the value requested
     * @param source [Single] used to get the value in case there is no cached value
     * @param forceLoad [Boolean] used to force the use of the [source] when there is a cached value
     * @param skipErrors [Boolean] used to prevent errors from propagating
     * @param errorFallback [Boolean] when true the cached version can be used if [forceLoad] is true.
     * If there is no cached value the error is emitted if [skipErrors] is false.
     */
    fun get(key: String, source: Single<T>, forceLoad: Boolean = false, skipErrors: Boolean = true, errorFallback: Boolean = false): Flowable<T> {
        return if (skipErrors) {
            Flowable.defer {
                getFlowable(key, source, forceLoad, errorFallback)
                        .compose(mapToSuccess())
            }
        } else {
            Flowable.defer {
                getFlowable(key, source, forceLoad, errorFallback)
                        .mergeWith(getErrorFlowable(key))
                        .compose(mapToSuccess())
            }
        }
    }

    @Synchronized
    private fun getFlowable(key: String, source: Single<T>, forceLoad: Boolean, errorFallback: Boolean): Flowable<Result<T>> {
        val processor = behaviorsMap.getOrPut(key, { BehaviorProcessor.create() })

        val cachedValue = cache.get(key)
        if (!forceLoad && cachedValue != null) {
            if (!processor.hasValue()) {
                processor.onNext(Result.Success(cachedValue))
            }
            return processor
        }

        if (isRequestInFlight(key)) {
            return requestsMap.getValue(key)
                    .switchMap { processor }
        }

        // We need to call share() to be sure if the subscriber that initiates de request
        // is cancelled and there are more subscribers, the request doesn't get cancelled
        // and the remaining subscribers will receive the value
        val request = source.toFlowable()
                .map<Result<T>> {
                    Result.Success(
                        it
                    )
                }
                .onErrorReturn {
                    if (errorFallback && cachedValue != null) {
                        Result.Success(cachedValue)
                    } else {
                        Result.Error(it)
                    }
                }
                .compose(saveResult(key))
                .compose(cleanUpRequest(key))
                .share()

        setRequestInFlight(key, request)
        return request.switchMap { processor }
    }

    /**
     * Gets the [Flowable] associated with the specified [key].
     * The [Flowable] will have the last cached value or it will return a [Flowable]
     * that will emit when someone requests a value for the same [key].
     * In either case the subscriber of this method will receive any updates to provided [key]
     * @param key used for identifying the value requested
     */
    @Synchronized
    fun get(key: String): Flowable<T> = behaviorsMap.getOrPut(key, { BehaviorProcessor.create() })
            .compose(mapToSuccess())

    /**
     * Synchronously gets the cached value for the specified [key].
     * In case the cache doesn't contain a value for that [key] it will return null
     * @param key used for identifying the value requested
     * */
    @Synchronized
    fun getCachedValue(key: String): T? = cache.get(key)

    /**
     * Saves the [value] to the cache using the [key] and notifies all current observers of the new value
     */
    @Synchronized
    fun put(key: String, value: T) {
        cache.save(key, value)
        behaviorsMap[key]?.onNext(Result.Success(value))
    }

    @Synchronized
    fun clear(key: String) {
        cache.clear(key)
        behaviorsMap.remove(key)?.onComplete()
    }

    @Synchronized
    fun clear() {
        cache.clear()
    }

    @Synchronized
    private fun setRequestInFlight(key: String, request: Flowable<Result<T>>) {
        requestsMap[key] = request
    }

    @Synchronized
    private fun isRequestInFlight(key: String) = requestsMap.containsKey(key)

    @Synchronized
    private fun cleanUpRequest(key: String) = FlowableTransformer<Result<T>, Result<T>> { upstream ->
        upstream.doOnTerminate {
            requestsMap.remove(key)
        }.doOnCancel {
            requestsMap.remove(key)
        }
    }

    @Synchronized
    private fun saveResult(key: String) = FlowableTransformer<Result<T>, Result<T>> { upstream ->
        upstream.doOnNext {
            if (it is Result.Success) {
                put(key, it.item)
            } else if (it is Result.Error) {
                errorsMap[key]?.onError(it.error)
            }
        }
    }

    private fun mapToSuccess() = FlowableTransformer<Result<T>, T> { upstream ->
        upstream.filter { it is Result.Success<T> }
                .map { (it as Result.Success<T>).item }
    }

    private fun getErrorFlowable(key: String): Flowable<Result<T>> {
        // Emmit and clear error, otherwise any new subscription would receive
        // the error immediately
        return errorsMap.getOrPut(key, { PublishProcessor.create() })
                .doOnError { errorsMap.remove(key) }
    }

    sealed class Result<T> {
        data class Success<T>(val item: T) : Result<T>()
        data class Error<T>(val error: Throwable) : Result<T>()
    }
}
