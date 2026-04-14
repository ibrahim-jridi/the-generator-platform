import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UnsubscribeWaitingListComponent } from './unsubscribe-waiting-list.component';

const routes: Routes = [{ path: '', component: UnsubscribeWaitingListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UnsubscribeWaitingListRoutingModule { }
