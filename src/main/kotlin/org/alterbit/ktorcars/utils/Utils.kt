package org.alterbit.ktorcars.utils

import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.MDC

suspend fun withMdc(fields: Map<String, String>, block: suspend () -> Unit) {
    fields.forEach { (k, v) -> MDC.put(k, v) }
    try {
        withContext(MDCContext()) {
            block()
        }
    } finally {
        fields.forEach { (k, _) -> MDC.remove(k) }
    }
}
