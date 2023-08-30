package com.example.restarea_order

import java.io.Serializable

abstract class ParentData(var category: String): Serializable {

    companion object{
        var TYPE_HEADER = 1
        var TYPE_CONTENT = 2
    }

    abstract fun getType(): Int
    abstract fun getDData(): String

}