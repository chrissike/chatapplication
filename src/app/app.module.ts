import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './app.component';
import {ChatClientListComponent} from './chat-client-list/chat-client-list.component';
import {StatisticsComponent} from './statistics/statistics.component';
import {MaterialModule} from "@angular/material";
import {CommonModule} from "@angular/common";

@NgModule({
    declarations: [
        AppComponent,
        ChatClientListComponent,
        StatisticsComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        MaterialModule,
        CommonModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
