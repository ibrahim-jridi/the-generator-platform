import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConnectorRoutingModule } from './connector-routing.module';
import {ConnectorListComponent} from "./connector-list/connector-list.component";
import {AddConnectorComponent} from "./add-connector/add-connector.component";
import {UpdateConnectorComponent} from "./update-connector/update-connector.component";
import {ViewConnectorComponent} from "./view-connector/view-connector.component";
import {SharedModule} from "../../theme/shared/shared.module";


@NgModule({
  declarations: [
    ConnectorListComponent,
    AddConnectorComponent,
    UpdateConnectorComponent,
    ViewConnectorComponent
  ],
  imports: [
    CommonModule,
    ConnectorRoutingModule,
    SharedModule
  ]
})
export class ConnectorModule { }
