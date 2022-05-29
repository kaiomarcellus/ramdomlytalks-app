package br.com.kmdev.randomlytalksapp.app

import br.com.kmdev.randomlytalksapp.BuildConfig
import br.com.kmdev.randomlytalksapp.data.repository.*
import br.com.kmdev.randomlytalksapp.ui.conversation.ConversationViewModel
import br.com.kmdev.randomlytalksapp.ui.rooms.RoomListViewModel
import br.com.kmdev.randomlytalksapp.ui.settings.SettingsForChatViewModel
import br.com.kmdev.randomlytalksapp.ui.splash.SplashViewModel
import br.com.kmdev.ui.components.FontProvider
import com.google.gson.Gson
import io.socket.client.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val appModules by lazy { listOf(servicesModule, repositoryModule, viewModelsModule) }

private val servicesModule = module {
    single { IO.socket(BuildConfig.SOCKET_CONNECTION_URL) }
    single<FontProvider> { MainFontProviderService(androidContext()) }
}

private val repositoryModule = module(createdAtStart = false) {
    single { Gson() }
    single { DataStoreManager(androidContext(), "RandomlyPrefs").getDataStore() }
    single<ChatRepository> { ChatRemoteRepository(get(), get()) }
    single<SettingsRepository> { SettingsLocalRepository(get(), get()) }
}

private val viewModelsModule = module(createdAtStart = false) {
    viewModel { SplashViewModel(get()) }
    viewModel { SettingsForChatViewModel(get()) }
    viewModel { RoomListViewModel(get(), get()) }
    viewModel { ConversationViewModel(get(), get()) }
}