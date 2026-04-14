import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListOfValueRoutingModule } from './list-of-value-routing.module';
import { ListOfValueComponent } from './list-of-value.component';
import { CustomTableModule } from '../../theme/shared/components/custom-table/custom-table.module';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ListOfValueAddComponent } from './list-of-value-add/list-of-value-add.component';
import { ListOfValueViewComponent } from './list-of-value-view/list-of-value-view.component';
import { SelectComponent } from 'src/app/theme/shared/components/select/select.component';
import { CustomButtonModule } from '../../theme/shared/components';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { ListOfValueUpdateComponent } from './list-of-value-update/list-of-value-update.component';

@NgModule({
  declarations: [
    ListOfValueComponent,
    ListOfValueAddComponent,
    ListOfValueViewComponent,
    ListOfValueUpdateComponent
  ],
  imports: [
    CommonModule,
    ListOfValueRoutingModule,
    TranslateModule,
    CustomTableModule,
    ReactiveFormsModule,
    CustomButtonModule,
    NgSelectModule
  ]
})
export class ListOfValueModule { }
