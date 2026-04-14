import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProcessListComponent} from './process-list/process-list.component';
import {ProcessViewComponent} from './process-view/process-view.component';
import {ProcessHistoryComponent} from './process-history/process-history.component';
import {DmnModelerComponent} from "./dmn-modeler/dmn-modeler.component";
import {BpmnModelerComponent} from "./bpmn-modeler/bpmn-modeler.component";
import {DmnListComponent} from './dmn-list/dmn-list.component';
import { ProcessInstanceHistoryListComponent } from './process-instance-history-list/process-instance-history-list.component';
import { ViewHistoricTaskComponent } from '../task/view-historic-task/view-historic-task.component';
import { AiBpmnChatComponent } from './ai-bpmn-chat/ai-bpmn-chat.component';


const routes: Routes = [
  {
    path: 'view-bpmn-modeler/:id',
    component: ProcessViewComponent
  },
  {
    path: 'view-bpmn-modeler/:id/view-history/:id',
    component: ProcessHistoryComponent
  },
  {
    path: 'process-management-bpmn',
    component: ProcessListComponent
  },
  {
    path: 'process-management-dmn',
    component: DmnListComponent
  },
  {
    path: 'process-management-bpmn/bpmn-modeler/add',
    component: BpmnModelerComponent
  },
  {
    path: 'update-bpmn-modeler/:id',
    component: BpmnModelerComponent
  },
  // {
  //   path: ':id',
  //   component: BpmnModelerComponent
  // },
  {
    path: 'process-management-dmn/dmn-modeler/add',
    component: DmnModelerComponent
  },
  {
    path: 'update-dmn-modeler/:id',
    component: DmnModelerComponent
  },
  {
    path: 'view-history',
    component: ProcessHistoryComponent
  },
  {
    path: 'process-instance-history',
    component: ProcessInstanceHistoryListComponent
  },
  {
    path: 'ai-bpmn-chat',
    component: AiBpmnChatComponent,
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProcessManagementRoutingModule { }
