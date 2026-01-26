package co.id.rezha.myroomdb.interfaces

import co.id.rezha.core.data.tables.TableUser

interface UserCallback {
    fun onEditClicked(user: TableUser)
}
