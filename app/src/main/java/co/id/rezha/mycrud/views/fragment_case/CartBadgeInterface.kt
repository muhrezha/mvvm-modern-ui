package co.id.rezha.mycrud.views.fragment_case

interface CartBadgeInterface {
    fun onCartAddItem(increment: Int = 1)
    fun onCartClear()
}