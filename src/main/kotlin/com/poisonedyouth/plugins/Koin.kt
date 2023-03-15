import com.poisonedyouth.user.ApplicationService
import com.poisonedyouth.user.ApplicationServiceImpl
import com.poisonedyouth.user.BookRepository
import com.poisonedyouth.user.BookRepositoryImpl
import com.poisonedyouth.user.UserRepository
import com.poisonedyouth.user.UserRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

val module = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<BookRepository> { BookRepositoryImpl() }
    single<ApplicationService> { ApplicationServiceImpl(get(), get()) }
}

fun Application.installKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(module)
    }
}
