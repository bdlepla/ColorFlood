package com.lepla.bryan.colorflood2

import org.junit.Test
import org.junit.Assert.fail
import io.reactivex.subjects.BehaviorSubject

class BehaviorSubjectTests {
    @Test
    fun shouldHaveDefaultValue() {
        val subject = BehaviorSubject.createDefault(1)
        fun defaultBehavior() = subject
        var wow2 = defaultBehavior().subscribe({
            assert(it == 1)
        }, {
            fail(it.message!!)
        })
    }
}
