package com.example.restarea_order

class Header(category:String):ParentData(category) {
    override fun getType(): Int {
        return TYPE_HEADER
    }

    override fun getDData(): String {
        return category
    }
}