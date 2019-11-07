package eksamen66.Controllers

//Imports
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import eksamen66.Data.*
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Paths
import java.io.IOException
import org.springframework.web.multipart.MultipartFile


//We chose to have all controller functionality in a single class - makes it easier.
@Controller
class HtmlController
{
    //Repository for handling the database connection
    @Autowired
    private val roomRepository : RoomRepository? = null

    @Autowired
    private val availabilityRepository : AvailabilityRepository? = null

    //directory of images
    val uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/image"

    //gets all the rooms
    @GetMapping("/")
    fun getIndex(model: Model) : String {
        model["title"] = "Room"
        model["room"] = roomRepository?.findAll()!!
        return "index"
    }

    //gets all the rooms, same as /
    @GetMapping("/room")
    fun getRoom(model: Model) : String {
        getIndex(model)
        return "index"
    }

    //gets the availability list for a specific room
    @GetMapping("/room/{id}")
    fun getAvailability(model: Model, @PathVariable id : Long) : String {
        model["title"] = "Room " + id.toString()
        model["roomid"] = id.toString()
        model["availability"] = roomRepository?.findById(id)?.get()?.availabilityList!!
        model["newBooking"] = Availability()
        return "room"
    }

    //Room management
    @GetMapping("/admin")
    fun getAdmin(model: Model) : String {
        model["title"] = "Admin"
        model["newRoom"] = Room()
        model["room"] = roomRepository?.findAll()!!

        return "admin"
    }

    //Availability management for a specific room
    @GetMapping("/admin/{id}")
    fun getAdminRoom(model: Model, @PathVariable id: Long) : String {
        model["title"] = "Admin room: " + id.toString()
        model["roomid"] = id.toString()
        model["newAvailability"] = Availability()
        model["availability"] = roomRepository?.findById(id)?.get()?.availabilityList!!
        return "adminroom"
    }

    //for a new room
    //Also handles saving images locally
    @PostMapping("/admin")
    fun postRoom(model: Model, room: Room,  @RequestParam("files") files: Array<MultipartFile>) : String {
        var filenameholder: String? = ""
        val fileNames = StringBuilder()
        for (file in files) {
            val fileNameAndPath = Paths.get(uploadDirectory, file.originalFilename)
            fileNames.append(file.originalFilename!! + " ")
            try {
                Files.write(fileNameAndPath, file.bytes)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            filenameholder = file.originalFilename
        }
        room.imagename = filenameholder
        roomRepository?.save(room)

        getAdmin(model)
        return "admin"
    }

    //adds a new availabilty to a a room
    @PostMapping("/admin/{id}")
    fun postAvailability(model: Model, availability: Availability, @PathVariable id: Long) : String {
        availability.room = roomRepository?.findById(id)?.get()
        availabilityRepository?.save(availability)
        getAdminRoom(model, id)
        return "adminroom"
    }

    //books an available date in a room
    @PostMapping("/room/{id}")
    fun postBooking(model: Model, availability: Availability, @PathVariable id: Long) : String {
        val avail: Availability = availabilityRepository?.findById(availability.id!!)?.get()!!
        avail.booked = true
        avail.comment = availability.comment
        avail.u_name = availability.u_name
        availabilityRepository.save(avail)
        getAvailability(model, id)
        return "room"
    }

    //deletes a room
    @DeleteMapping("/delete/{id}")
    fun deleteRoom(@PathVariable id: Long)
    {
        roomRepository?.deleteById(id)
    }
}