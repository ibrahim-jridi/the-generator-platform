import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskRoutingModule } from './task-routing.module';
import { CustomButtonModule } from 'src/app/theme/shared/components';
import { TranslateModule } from '@ngx-translate/core';
import { TaskListComponent } from './task-list/task-list.component';
import {NgbDropdownModule, NgbPopoverModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import { TaskValidateComponent } from './task-validate/task-validate.component';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { ViewHistoricTaskComponent } from './view-historic-task/view-historic-task.component';
import {HistoricTaskListComponent} from "./historic-task-list/historic-task-list.component";
import {DateformatPipe} from "../../shared/pipes/date-transform.pipe";

@NgModule({
  declarations: [
    TaskListComponent,
    TaskValidateComponent,
    ViewHistoricTaskComponent,
    HistoricTaskListComponent],
  imports: [
    CommonModule,
    TaskRoutingModule,
    CustomButtonModule,
    TranslateModule,
    NgbDropdownModule,
    SharedModule,
  ],
  providers: [DateformatPipe],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TaskModule { }
