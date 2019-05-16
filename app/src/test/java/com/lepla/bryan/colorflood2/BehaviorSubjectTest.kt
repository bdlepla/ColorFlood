package com.lepla.bryan.colorflood2

import io.kotlintest.fail
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.reactivex.subjects.BehaviorSubject

class BehaviorSubjectTest : StringSpec({
    "ShouldHaveDefaultValue" {
        val subject = BehaviorSubject.createDefault(1)
        fun defaultBehavior() = subject
        var wow2 = defaultBehavior().subscribe({
                it.shouldBe(1)
            }, {
            fail(it.message!!)
        })
    }
})