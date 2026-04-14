import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ListGroupComponent } from './list-group/list-group.component';
import { AddGroupComponent } from './add-group/add-group.component';
import { ViewGroupComponent } from './view-group/view-group.component';
import {UserGroupListComponent} from "./user-group-list/user-group-list.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full'
  },
  {
    path: '',
    component: ListGroupComponent
  },
  {
    path: 'add',
    component: AddGroupComponent
  },
  {
    path: 'view-group/:id',
    component: ViewGroupComponent
  },
  {
    path: 'list-users-group/:id',
    component: UserGroupListComponent
  },
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class GroupsRoutingModule { }
