import com.caritos.dao.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabase() {
    DatabaseSingleton.init()
}