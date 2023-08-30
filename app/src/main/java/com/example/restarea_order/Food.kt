package com.example.restarea_order

class Food (category:String,val name: String? = null, val price:String? = null, val data: String? = null):ParentData(category){
    override fun getType(): Int {
        return TYPE_CONTENT
    }

    override fun getDData(): String {
        return category
    }
}