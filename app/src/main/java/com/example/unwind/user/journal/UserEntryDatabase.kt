import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.unwind.model.UserEntry
import com.example.unwind.user.User
import com.example.unwind.user.UserDao
import com.example.unwind.user.UserDatabase
import com.example.unwind.user.journal.UserEntryDao
import com.example.unwind.utils.DateConverter
import com.example.unwind.utils.MoodConverter

@Database(entities = [UserEntry::class], version = 1)
@TypeConverters(DateConverter::class, MoodConverter::class)
abstract class UserEntryDatabase : RoomDatabase() {
    abstract fun userEntryDao(): UserEntryDao

    companion object {
        @Volatile
        private var INSTANCE: UserEntryDatabase? = null

        fun getDatabase(context: Context): UserEntryDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserEntryDatabase::class.java,
                    "user_entry_database"
                ).fallbackToDestructiveMigration() // Optional, to handle migrations
                    .build()
                return instance
            }
        }
    }
}









