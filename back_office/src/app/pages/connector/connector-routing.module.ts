import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ConnectorListComponent} from "./connector-list/connector-list.component";
import {AddConnectorComponent} from "./add-connector/add-connector.component";
import {UpdateConnectorComponent} from "./update-connector/update-connector.component";
import {ViewConnectorComponent} from "./view-connector/view-connector.component";

const routes: Routes = [
   {
     path: '',
     redirectTo: 'connector-management',
     pathMatch: 'full'
   },
   {
     path: '',
     component: ConnectorListComponent
   },
   {
     path: 'add',
     component: AddConnectorComponent
   },
   {
      path: 'update-connector/:id',
      component: UpdateConnectorComponent
   },
   {
      path: 'view-connector/:id',
      component: ViewConnectorComponent
   },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConnectorRoutingModule { }
