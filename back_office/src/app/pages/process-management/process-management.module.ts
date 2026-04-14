import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { ProcessManagementRoutingModule } from './process-management-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomButtonModule, ModalModule } from 'src/app/theme/shared/components';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { ProcessListComponent } from './process-list/process-list.component';
import { ProcessViewComponent } from './process-view/process-view.component';
import { ProcessHistoryComponent } from './process-history/process-history.component';
import { TaskRoutingModule } from '../task/task-routing.module';
import { NgbDropdownModule, NgbPopoverModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { RouterModule } from '@angular/router';
import { NgBpmnComponent, NgCmmnComponent, NgDmnComponent } from '@denysvuika/ng-bpmn';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDividerModule } from '@angular/material/divider';
import { AppToolbarActionComponent } from './dmn-modeler/layout/toolbar/toolbar-action.component';
import { DmnModelerComponent } from './dmn-modeler/dmn-modeler.component';
import { AppToolbarComponent } from './dmn-modeler/layout/toolbar/toolbar.component';
import { BpmnModelerComponent } from './bpmn-modeler/bpmn-modeler.component';
import { DmnListComponent } from './dmn-list/dmn-list.component';
import { DateformatPipe } from '../../shared/pipes/date-transform.pipe';
import { ProcessInstanceHistoryListComponent } from './process-instance-history-list/process-instance-history-list.component';
import { AiBpmnChatComponent } from './ai-bpmn-chat/ai-bpmn-chat.component';
import { MarkdownSimplePipe } from './ai-bpmn-chat/markdown-simple.pipe';


@NgModule({
  declarations: [
    ProcessListComponent,
    ProcessViewComponent,
    ProcessHistoryComponent,
    BpmnModelerComponent,
    AppToolbarComponent,
    DmnModelerComponent,
    DmnListComponent,
    ProcessInstanceHistoryListComponent,
    AiBpmnChatComponent, MarkdownSimplePipe
  ],
  imports: [
    CommonModule,
    CustomButtonModule,
    ProcessManagementRoutingModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    ReactiveFormsModule,
    TaskRoutingModule,
    CustomButtonModule,
    NgbDropdownModule,
    SharedModule,

    NgbTooltipModule,
    NgbPopoverModule,
    NgIf,
    RouterModule,
    NgBpmnComponent,
    NgDmnComponent,
    NgCmmnComponent,
    MatTabsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule,
    MatDividerModule,
    AppToolbarActionComponent
  ],
  providers: [DateformatPipe]
})
export class ProcessManagementModule {}
