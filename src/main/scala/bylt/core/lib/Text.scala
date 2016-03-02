package bylt.core
package lib

object Text extends ModuleDecl ('lib / 'text) {

    val Char = typeRef ('char)
    val CharSeq = typeDecl ('char_seq) {
        ManyType (Char, sequential = true)
    }

}
