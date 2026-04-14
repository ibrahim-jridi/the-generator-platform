import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RenewalCategoryWaitingListComponent } from './renewal-category-waiting-list.component';

const routes: Routes = [{ path: '', component: RenewalCategoryWaitingListComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RenewalCategoryWaitingListRoutingModule { }
