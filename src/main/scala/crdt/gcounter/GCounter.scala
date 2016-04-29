package crdt.gcounter

import scalaz.Tags.Max
import scalaz.{@@, Monoid}

trait GCounter[Id, Elt] {

  def inc(id: Id, amt: Elt)(implicit m: Monoid[Elt])

  def total(implicit m: Monoid[Elt]): Elt

  def merge(c: GCounter[Id, Elt])(implicit m: Monoid[Elt @@ Max]): GCounter[Id, Elt]

}
