package bylt

import scala.language.implicitConversions

package object core {

    implicit def symbolToName (sym : Symbol) : Name =
        Name ((sym.name.split ("\\_") map {_.toLowerCase}).toVector)

//    implicit def symbolToQName (sym : Symbol) : QName =
//        QName (Vector.empty, symbolToName (sym))

}
