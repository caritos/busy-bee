import com.caritos.db.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabase() {
    DatabaseSingleton.init()
}