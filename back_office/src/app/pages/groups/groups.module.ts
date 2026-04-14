import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddGroupComponent } from './add-group/add-group.component';
import { ListGroupComponent } from './list-group/list-group.component';
import { ViewGroupComponent } from './view-group/view-group.component';
import { GroupsRoutingModule } from './groups-routing.module';
import { CustomButtonModule, ModalModule } from 'src/app/theme/shared/components';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/app/theme/shared/shared.module';


@NgModule({
  declarations: [
    AddGroupComponent,
    ListGroupComponent,
    ViewGroupComponent
  ],
  imports: [
    CommonModule,
    GroupsRoutingModule,
    CustomButtonModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    SharedModule
  ]
})
export class GroupsModule { }
