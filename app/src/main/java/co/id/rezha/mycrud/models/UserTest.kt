package co.id.rezha.mycrud.models

class UserTest {
    var name: String = ""
    var email: String = ""

    constructor()
    constructor(name: String, email: String) {
        this.name = name
        this.email = email
    }

    fun getUserInfo(): String {
        return "$name - $email"
    }
}