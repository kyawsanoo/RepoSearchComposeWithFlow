package kso.repo.search.dataSource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kso.repo.search.model.Owner

@Dao
interface OwnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertOwner(owner: Owner)

    @Query("DELETE FROM Owner")
    abstract fun deleteAllOwners()

}