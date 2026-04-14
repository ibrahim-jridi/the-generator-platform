import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RenewalRegionWaitingListComponent } from './renewal-region-waiting-list.component';

const routes: Routes = [
  { 
    path: '',
    component: RenewalRegionWaitingListComponent 
  }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RenewalRegionWaitingListRoutingModule { }
