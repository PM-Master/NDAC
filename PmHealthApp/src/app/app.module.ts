import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import {AppRoutingModule} from './app-routing.module';
import { SignupComponent } from './auth/signup/signup.component';
import {LoginComponent} from './auth/login/login.component';
import {FormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {PmHealthService} from './services/pmhealth.service';
import { PatientDetailComponent } from './patient-detail/patient-detail.component';
import { StartVisitComponent } from './visits/start-visit/start-visit.component';
import {AlertService} from './services/alert.service';
import {AlertComponent} from './directives/alert.component';
import { MyRecordComponent } from './my-record/my-record.component';
import { ActiveVisitsComponent } from './visits/active-visits/active-visits.component';
import { VisitComponent } from './visits/visit/visit.component';
import {MyPatientsComponent} from './patients/patients.component';
import { MedicinesComponent } from './medicines/medicines.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    MyPatientsComponent,
    LoginComponent,
    SignupComponent,
    PatientDetailComponent,
    StartVisitComponent,
    AlertComponent,
    MyRecordComponent,
    ActiveVisitsComponent,
    VisitComponent,
    MedicinesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [PmHealthService, AlertService],
  bootstrap: [AppComponent]
})
export class AppModule { }
