package az.pashabank.starter.di

import az.pashabank.starter.appinitializers.di.initModule
import az.pashabank.data.di.dataModule
import az.pashabank.starter.domain.di.domainModule
import az.pashabank.starter.data.di.localModule
import az.pashabank.presentation.di.presentationModule
import az.pashabank.starter.data.di.remoteModule

val appComponent = listOf(
    initModule,
    dataModule,
    domainModule,
    presentationModule,
    localModule,
    remoteModule
)