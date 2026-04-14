import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MembreAssigneeRoutingModule } from './membre-assignee-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { ModalModule } from '../../theme/shared/components';
import { SharedModule } from '../../theme/shared/shared.module';
import { NgxIntlTelInputModule } from 'ngx-intl-tel-input';
import { DateformatPipe } from '../../shared/pipes/date-transform.pipe';
import { MembreAssigneeComponent } from './membre-assignee.component';


@NgModule({
  declarations: [MembreAssigneeComponent],
  imports: [
    CommonModule,
    MembreAssigneeRoutingModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    SharedModule,
    NgxIntlTelInputModule
  ],
  providers: [DateformatPipe],
})
export class MembreAssigneeModule { }
