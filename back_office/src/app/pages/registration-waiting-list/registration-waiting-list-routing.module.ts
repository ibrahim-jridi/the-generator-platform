import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationWaitingListComponent } from './registration-waiting-list.component';

const routes: Routes = [{ path: '', component: RegistrationWaitingListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegistrationWaitingListRoutingModule { }
