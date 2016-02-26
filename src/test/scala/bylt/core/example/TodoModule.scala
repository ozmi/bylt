package bylt.core
package example

import bylt.core.lib._

object TodoModule {

    val Todo = RecordType (Vector (
        'id -> Text.CharSeq,
        'summary -> TemporalType (Text.CharSeq),
        'completed -> TemporalType (Boolean.Boolean)
    ))

    val Todos = ManyType (Todo, unique = true)

}
