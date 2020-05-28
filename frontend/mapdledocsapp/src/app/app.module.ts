import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {SearchComponent} from './components/search/search.component';
import {RegisterComponent} from './components/register/register.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {httpInterceptorProviders} from './interceptors';
import {HttpClientModule} from '@angular/common/http';
import { HeaderComponent } from './components/header-component/header.component';
import { MadmpdetailsComponent } from './components/madmpdetails/madmpdetails.component';
import { UploadComponent } from './components/upload/upload.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButton, MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSidenavModule} from "@angular/material/sidenav";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SearchComponent,
    RegisterComponent,
    HeaderComponent,
    MadmpdetailsComponent,
    UploadComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSidenavModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}

