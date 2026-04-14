import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EctdRoutingModule } from './ectd-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'src/app/theme/shared/shared.module';
import { NgbDropdownModule, NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { NgScrollbar } from 'ngx-scrollbar';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { EctdComponent} from "./ectd.component";

@NgModule({
  declarations: [
    EctdComponent,
  ],
  imports: [
    CommonModule,
    EctdRoutingModule,
    NgbNavModule,
    TranslateModule,
    SharedModule,
    NgScrollbar,
    MatOptionModule,
    MatSelectModule,
    MatProgressBarModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    NgbDropdownModule,
  ],
})
export class EctdModule {}
