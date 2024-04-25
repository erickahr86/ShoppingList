package com.erick.herrera.shoppinglist.other


fun String.validateURL(): Boolean {
    val regex = Regex("""^(http(s)?:\/\/)?([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?$""")
    return regex.matches(this)
}
