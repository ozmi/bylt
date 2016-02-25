package bylt.core


case class Name (parts : Vector [String]) {
    override def toString =
        parts mkString "_"
}
object Name {

    def fromString (name : String) : Name =
        Name (name.split ("\\_").toVector)

    import scala.language.implicitConversions

    implicit def fromSymbol (sym : Symbol) : Name =
        Name (sym.name.split ("\\_") map {_.toLowerCase} toVector)

}


case class QName (ns : Vector [Name], name : Name) {
    override def toString =
        (ns :+ name) mkString "/"
}
object QName {

    def fromString (qname : String) : QName = {
        val path = qname.split ("\\/") map {Name.fromString}
        QName (path.init.toVector, path.last)
    }

}


object Case {

    def TitleCase (name : Name) : String =
        name.parts map {_.capitalize} mkString ""

    def camelCase (name : Name) : String =
        name.parts.head + (name.parts.tail map {_.capitalize} mkString "")

    def snake_case (name : Name) : String =
        name.parts mkString "_"

    def UPPER_SNAKE_CASE (name : Name) : String =
        snake_case (name).toUpperCase

    def Title_Snake_Case (name : Name) : String =
        name.parts map {_.capitalize} mkString "_"

}