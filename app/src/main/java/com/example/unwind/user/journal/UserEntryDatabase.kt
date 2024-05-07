import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.unwind.model.UserEntry
import com.example.unwind.user.journal.UserEntryDao
import com.example.unwind.utils.DateConverter
@Database(entities = [UserEntry::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class UserEntryDatabase : RoomDatabase() {
    abstract fun userEntryDao(): UserEntryDao
    companion object {
        @Volatile
        private var INSTANCE: UserEntryDatabase? = null

        fun getDatabase(context: Context): UserEntryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserEntryDatabase::class.java,
                    "user_entry_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
