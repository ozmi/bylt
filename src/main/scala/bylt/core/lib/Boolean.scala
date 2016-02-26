package bylt.core
package lib

object Boolean {

    val True = UnitType ('boolean / 'true)
    val False = UnitType ('boolean / 'false)
    val Boolean = TaggedUnionType.enum (True, False)

}
