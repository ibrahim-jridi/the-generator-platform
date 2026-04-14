import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ExternalUserListComponent } from './external-user-list/external-user-list.component';
import { ExternalUserAddComponent } from './external-user-add/external-user-add.component';
import { ExternalUserUpdateComponent } from './external-user-update/external-user-update.component';
import { ExternalUserViewComponent } from './external-user-view/external-user-view.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'users',
    pathMatch: 'full'
  },
  {
    path: '',
    component: ExternalUserListComponent
  },
  {
    path: 'add',
    component: ExternalUserAddComponent
  },
  {
    path: 'update-user-external/:id',
    component: ExternalUserUpdateComponent
  },
  {
    path: 'view-user-external/:id',
    component: ExternalUserViewComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExternalUserRoutingModule { }
