package bylt.core
package lib

object Text {

    val Char = TypeRef ('text / 'char)
    val CharSeq = ManyType (Char, sequential = true)

}
