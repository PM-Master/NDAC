import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {LoginComponent} from './auth/login/login.component';
import {SignupComponent} from './auth/signup/signup.component';
import {PatientDetailComponent} from './patient-detail/patient-detail.component';
import {StartVisitComponent} from './visits/start-visit/start-visit.component';
import {MyRecordComponent} from './my-record/my-record.component';
import {ActiveVisitsComponent} from './visits/active-visits/active-visits.component';
import {MyPatientsComponent} from './patients/patients.component';
import {MedicinesComponent} from './medicines/medicines.component';



const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'patients', component: MyPatientsComponent },
  { path: 'patient-detail/:patientId', component: PatientDetailComponent},
  { path: 'start-visit', component: StartVisitComponent},
  { path: 'my-record', component: MyRecordComponent},
  { path: 'active-visits', component: ActiveVisitsComponent},
  { path: 'medicines', component: MedicinesComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
