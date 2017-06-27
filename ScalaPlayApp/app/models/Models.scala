package models

case class User(id: Option[Long], name: String, pass: String)

case class Item(id: Option[Long], name: String)

case class Path(id: Option[Long], name: String)

case class DropLog(id: Option[Long], kc: Int, userID: Long, pathID: Long)

case class DropLogHasItem(dropLogID: Long, ItemID: Long)

// forms
case class LogData(log: DropLog, itemIDs: Seq[Long])

// view
case class DropLogData(log: DropLog, path: Path, items: Seq[Option[Item]])