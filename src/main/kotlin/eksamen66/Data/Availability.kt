package eksamen66.Data

import javax.persistence.*
import javax.validation.constraints.NotBlank

//Availability entity. Note the boolean, which is what we use to identify if a room is booked by a user or not. It's false by default.
@Entity
@Table(name = "availability")
data class Availability
(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long? = null,

        @get: NotBlank
        var date: String? = "",

        @get: Column(name="booked")
        var booked: Boolean? = false,

        @get: Column(name="u_name")
        var u_name: String? = "",

        @get: Column(name="comment")
        var comment: String? = "",

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "room_id")
        var room: Room? = null
)