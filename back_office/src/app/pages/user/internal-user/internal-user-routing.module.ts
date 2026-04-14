import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {InternalUserListComponent} from "./internal-user-list/internal-user-list.component";
import {InternalUserAddComponent} from "./internal-user-add/internal-user-add.component";
import {InternalUserViewComponent} from "./internal-user-view/internal-user-view.component";
import {InternalUserUpdateComponent} from "./internal-user-update/internal-user-update.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'users',
    pathMatch: 'full'
  },
  {
    path: '',
    component: InternalUserListComponent
  },
  {
    path: 'add',
    component: InternalUserAddComponent
  },
  {
    path: 'view-user/:id',
    component: InternalUserViewComponent
  },
  {
    path: 'update-user/:id',
    component: InternalUserUpdateComponent
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InternalUserRoutingModule { }
