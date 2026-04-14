import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RapportRoutingModule } from './report-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { ReportListComponent } from './report-list/report-list.component';
import { ReportAddComponent } from './report-add/report-add.component';
import { CustomButtonModule } from 'src/app/theme/shared/components';
import { ReactiveFormsModule } from '@angular/forms';
import { ReportUpdateComponent } from './report-update/report-update.component';
import { ReportViewComponent } from './report-view/report-view.component';
import { FileNamePipe } from 'src/app/shared/pipes/file-name.pipe';
import { SharedModule } from 'src/app/theme/shared/shared.module';


@NgModule({
  declarations: [
    ReportListComponent,
    ReportAddComponent,
    ReportUpdateComponent,
    ReportViewComponent
  ],
  imports: [
    CommonModule,
    RapportRoutingModule,
    TranslateModule,
    CustomTableModule,
    CustomButtonModule,
    ReactiveFormsModule,
    SharedModule
  ]
})
export class RapportModule { }
