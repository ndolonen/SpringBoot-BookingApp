package eksamen66.Data

import javax.persistence.*
import javax.validation.constraints.NotBlank

//Room entity.
@Entity
@Table(name = "room")
data class Room (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long? = null,

        @get: NotBlank
        var name: String? = "",

        @get: NotBlank
        var description: String? = "",

        @get: NotBlank
        var imagename: String? = "",

        @OneToMany(mappedBy = "room", cascade = arrayOf(CascadeType.ALL), fetch = FetchType.LAZY)
        var availabilityList: List<Availability>? = emptyList()
)