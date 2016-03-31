package bylt.core
package lib

object Boolean extends ModuleDecl ('lib / 'boolean) {

    val True = unitType ('true)
    val False = unitType ('false)
    val Boolean = typeDecl ('boolean) {
        SumType.fromRefs (True, False)
    }

    val Not = typeDecl ('not) {
        Boolean -> Boolean
    }

    val And = typeDecl ('and) {
        Boolean -> Boolean -> Boolean
    }

    val Or = typeDecl ('or) {
        Boolean -> Boolean -> Boolean
    }

}
