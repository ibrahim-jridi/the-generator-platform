import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskListComponent } from './task-list/task-list.component';
import { TaskValidateComponent } from './task-validate/task-validate.component';
import { ViewHistoricTaskComponent } from './view-historic-task/view-historic-task.component';
import { HistoricTaskListComponent } from './historic-task-list/historic-task-list.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'task-management',
    pathMatch: 'full'
  },
  {
    path: 'task-list',
    component: TaskListComponent
  },
  {
    path: 'task-list/validate-task/:id',
    component: TaskValidateComponent
  },
  {
    path: 'view-historic-task/:processInstanceId',
    component: ViewHistoricTaskComponent
  },
  {
    path: 'historic-task-list',
    component: HistoricTaskListComponent
  },
  {
    path: 'historic-task-list/view-historic-task/:id',
    component: ViewHistoricTaskComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TaskRoutingModule { }
