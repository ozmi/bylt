package bylt

import scala.language.implicitConversions

package object core {

    implicit def symbolToName (sym : Symbol) : Name =
        Name ((sym.name.split ("\\_") map {_.toLowerCase}).toVector)

}
