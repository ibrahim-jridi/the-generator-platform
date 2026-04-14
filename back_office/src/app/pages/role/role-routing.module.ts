import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleListComponent } from './role-list/role-list.component';
import { RoleAddComponent } from './role-add/role-add.component';
import { RoleViewComponent } from './role-view/role-view.component';
import { RoleUpdateComponent } from './role-update/role-update.component';
import { RoleUserListComponent } from './role-user-list/role-user-list.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'role-management',
    pathMatch: 'full'
  },
  {
    path: '',
    component: RoleListComponent
  },
  {
    path: 'add',
    component: RoleAddComponent
  },
  {
    path: 'view-role/:id',
    component: RoleViewComponent
  },
  {
    path: 'update-role/:id',
    component: RoleUpdateComponent
  },
  {
    path: 'list-users-role/:id',
    component: RoleUserListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoleRoutingModule {}
