package org.woowatechcamp.githubapplication.util.ext

import kotlinx.coroutines.Job

fun Job?.cancelWhenActive() {
   this?.let {
       if (it.isActive) {
           it.cancel()
       }
   }
}