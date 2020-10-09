import model.Course
import model.Person
import model.Task
import model.Type
import repo.ListRepo

val persons = ListRepo<Person>()
val courses = ListRepo<Course>()
val taskTypes = ListRepo<Type>()