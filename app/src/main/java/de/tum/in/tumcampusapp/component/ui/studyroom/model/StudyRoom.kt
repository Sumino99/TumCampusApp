package de.tum.`in`.tumcampusapp.component.ui.studyroom.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.joda.time.DateTime

/**
 * Representation of a study room.
 */
@Entity(tableName = "study_rooms")
data class StudyRoom(@field:PrimaryKey
                     var id: Int = -1,
                     var code: String = "",
                     var name: String = "",
                     var location: String = "",
                     @ColumnInfo(name = "group_id")
                     var studyRoomGroup: Int = -1,
                     @ColumnInfo(name = "occupied_till")
                     var occupiedTill: DateTime = DateTime()) {
    override fun toString() = code
}
