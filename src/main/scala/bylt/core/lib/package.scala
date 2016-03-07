package bylt.core

package object lib {

    def module : Module = {
        val decls = Seq (Boolean, Core, Text)
        decls map {_.asModule} reduceLeft {Module.mergeModules (Module.root, _, _)}
    }

}
