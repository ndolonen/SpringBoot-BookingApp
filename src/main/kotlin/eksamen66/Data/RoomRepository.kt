package eksamen66.Data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//repository for Room
@Repository
interface RoomRepository : JpaRepository<Room, Long>