import model.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

fun Course.addTutorByName(name: String) {
    persons[name]?.let {
        if (it is Tutor)
            tutors.add(it)
    }
}

fun Course.addStudentByName(name: String) {
    persons[name]?.let {
        if (it is Student)
            students.add(it)
    }
}

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    @BeforeAll
    fun init() {
        mapOf(
            "Lecture" to "Lec",
            "Laboratory" to "Lab",
            "Test" to "Tst"
        ).forEach {
            taskTypes.add(Type(it.key, it.value))
        }
        mapOf(
            "Sheldon" to "Professor",
            "Leonard" to "Professor"
        ).forEach {
            persons.add(Tutor(it.key, it.value))
        }
        mapOf(
            "Howard" to "Footprint on the Moon",
            "Raj" to "Footprint on the Moon",
            "Penny" to "Waitress"
        ).forEach {
            persons.add(Student(it.key, it.value))
        }
        listOf(
            "Math",
            "Phis",
            "History"
        ).forEach {
            courses.add(Course(it))
        }
        courses["Math"]?.run {
            addTutorByName("Sheldon")
            addStudentByName("Howard")
            addStudentByName("Penny")
            tasks.add(Task("Intro", taskTypes["Lecture"]!!))
            tasks.add(Task("UML", taskTypes["Lecture"]!!))
            tasks.add(Task("Uml lab", taskTypes["Laboratory"]!!, maxValue = 5))
        }
    }

    @Test
    fun initTest() {
        assertEquals(2, persons.all().filterIsInstance<Tutor>().size)
        assertEquals(3, persons.all().filterIsInstance<Student>().size)
        assertEquals(2, persons.all().filter {
            when (it) {
                is Student -> it.group == "Footprint on the Moon"
                else -> false
            }
        }.size)
        assertEquals(2, courses["Math"]?.students?.size)
        assertEquals(1, courses["Math"]?.tutors?.size)
    }

    @Test
    fun setGradeTest() {
        val math = courses["Math"] ?: fail()
        math.setGrade("UML", "Howard", 1)
        assertEquals(
            1,
            math.tasks.find { it.name == "UML" }?.grades?.size
        )
    }

    @Test
    fun studentGradesTest() {
        val math = courses["Math"] ?: fail()
        math.setGrade("Intro", "Penny", 1)
        math.setGrade("Uml lab", "Penny", 3)
        math.setGrade("Uml lab", "Penny", 4)
        val grades = math.studentGrades("Penny")
        assertEquals(1, grades["Intro"])
        assertEquals(4, grades["Uml lab"])
    }
}