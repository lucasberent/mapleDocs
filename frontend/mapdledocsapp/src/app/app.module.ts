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
import {HeaderComponent} from './components/header-component/header.component';
import {MadmpdetailsComponent} from './components/madmpdetails/madmpdetails.component';
import {UploadComponent} from './components/upload/upload.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatCommonModule} from '@angular/material/core';
import {MatIconModule} from '@angular/material/icon';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatExpansionModule} from '@angular/material/expansion';
import {NgxJsonViewerModule} from 'ngx-json-viewer';
import {ToastrModule} from 'ngx-toastr';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatSelectModule} from '@angular/material/select';
import {TreeviewModule} from 'ngx-treeview';
import {MatRadioModule} from '@angular/material/radio';
import {MatNativeDateModule} from '@angular/material/core';
import {MatPaginatorModule} from '@angular/material/paginator';
import {AssignNewDoiDialogComponentComponent} from './components/assign-new-dio-dialog-component/assign-new-doi-dialog-component.component';
import {MatDialogModule} from '@angular/material/dialog';
import {MatChipsModule} from "@angular/material/chips";
import {MatAutocompleteModule} from "@angular/material/autocomplete";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SearchComponent,
    RegisterComponent,
    HeaderComponent,
    MadmpdetailsComponent,
    UploadComponent,
    SidebarComponent,
    AssignNewDoiDialogComponentComponent
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
    MatSidenavModule,
    MatCommonModule,
    MatIconModule,
    MatListModule,
    MatTabsModule,
    MatSidenavModule,
    MatToolbarModule,
    NgxJsonViewerModule,
    ToastrModule.forRoot(),
    MatRadioModule,
    MatExpansionModule,
    MatDatepickerModule,
    MatCheckboxModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    MatGridListModule,
    MatSelectModule,
    TreeviewModule.forRoot(),
    MatPaginatorModule,
    MatDialogModule,
    MatChipsModule,
    MatAutocompleteModule
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}

