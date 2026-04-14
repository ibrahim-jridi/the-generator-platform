import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MembreDesigneRoutingModule} from './membre-designe-routing.module';
import {MembreDesigneComponent} from "./membre-designe.component";
import {SharedModule} from "../../theme/shared/shared.module";
import {DateformatPipe} from "../../shared/pipes/date-transform.pipe";
import {TranslateModule} from "@ngx-translate/core";
import {CustomTableModule} from "../../theme/shared/components/custom-table/custom-table.module";
import {ModalModule} from "../../theme/shared/components";
import {NgxIntlTelInputModule} from "ngx-intl-tel-input";


@NgModule({
  declarations: [
    MembreDesigneComponent
  ],
  imports: [
    CommonModule,
    MembreDesigneRoutingModule,
    SharedModule,
    TranslateModule,
    CustomTableModule,
    ModalModule,
    NgxIntlTelInputModule

  ],
  providers: [DateformatPipe],
})
export class MembreDesigneModule { }
