package bylt.core
package lib

object Boolean extends ModuleDecl ('lib / 'boolean) {

    val True = unitType ('true)
    val False = unitType ('false)
    val Boolean = typeDecl ('boolean) {
        TaggedUnionType.fromRefs (True, False)
    }

    val Not = typeDecl ('not) {
        Boolean -> Boolean
    }

}
